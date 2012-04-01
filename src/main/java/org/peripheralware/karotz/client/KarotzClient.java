package org.peripheralware.karotz.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.peripheralware.karotz.KarotzException;
import org.peripheralware.karotz.evil.KarotzUtil;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.slf4j.Logger;

/**
 * KarotzClient class.
 *
 * @author William Durand <william.durand1@gmail.com>
 */
public class KarotzClient {

    /**
     * Base URL for the START method (auth)
     */
    private static final String KAROTZ_URL_START = "http://api.karotz.com/api/karotz/start";

    /**
     * Base URL for the Interactive mode method
     */
    private static final String KAROTZ_URL_INTERACTIVE_MODE = "http://api.karotz.com/api/karotz/interactivemode";

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KarotzClient.class);

    /**
     * Interactive Id
     */
    private static String interactiveId;

    /**
     * API Key
     */
    private String apiKey;

    /**
     * API Secret
     */
    private String secretKey;

    /**
     * Install Id
     */
    private String installId;

    /**
     * Default constructor.
     *
     * @param apiKey application APIKey
     * @param secretKey application SecretKey
     * @param installId application Install ID
     */
    public KarotzClient(String apiKey, String secretKey, String installId) {
        this.installId = installId;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    public String getInteractiveId() {
        return interactiveId;
    }

    public boolean isInteractive() {
        return interactiveId != null;
    }

    public synchronized void startInteractiveMode() throws KarotzException {
        if (isInteractive()) {
            return;
        }
        Random random = new Random(System.currentTimeMillis());
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("apikey", apiKey);
        parameters.put("installid", installId);
        parameters.put("once", String.valueOf(random.nextInt(99999999)));
        // See: http://stackoverflow.com/questions/732034/getting-unixtime-in-java
        parameters.put("timestamp", String.valueOf((int) (System.currentTimeMillis() / 1000L)));
        String url = getSignedUrl(parameters, secretKey);

        String result = doRequest(url);
        LOGGER.info("Got: {}", result);

        interactiveId = parseResponse(result, "interactiveId");
        if (interactiveId == null) {
            String code = parseResponse(result, "code");
            System.err.println(result);
            throw new KarotzException("[code] " + code);
        }
    }

    public synchronized void stopInteractiveMode() throws KarotzException {
        if (!isInteractive()) {
            return;
        }
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("action", "stop");
        parameters.put("interactiveid", interactiveId);

        String url = KAROTZ_URL_INTERACTIVE_MODE + '?' + KarotzUtil.buildQuery(parameters);

        String result = doRequest(url);
        String code = parseResponse(result, "code");
        if (!"OK".equalsIgnoreCase(code) && !"NOT_CONNECTED".equalsIgnoreCase(code)) {
            throw new KarotzException("[code] " + code);
        }

        interactiveId = null;
    }

    /**
     * Sends cmd to Karotz using ReST.
     *
     * @param url Karotz webAPI URL
     * @return response
     * @throws UnableToPerformRequestException Network trouble.
     */
    public String doRequest(String url) throws UnableToPerformRequestException {
        if (url == null) {
            throw new IllegalArgumentException("url is null");
        }

        LOGGER.info("Request: {}", url);
        String result;
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            result = IOUtils.toString(inputStream);
            LOGGER.info("result is {}", result);
        } catch (IOException e) {
            throw new UnableToPerformRequestException(e);
        }

        return result;
    }

    /**
     * Parses response from karotz.
     *
     * @param response response from karotz
     * @param tagName to extract
     * @return tag value
     * @throws UnableToPerformRequestException problem with input source.
     * @throws UnableToProcessResponseException problem decoding response.
     */
    public String parseResponse(String response, String tagName) throws UnableToProcessResponseException, UnableToPerformRequestException {
        if (response == null || tagName == null) {
            throw new IllegalArgumentException("params should not be null.");
        }

        String value;
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = parser.parse(new InputSource(new StringReader(response)));
            Element elt = (Element) document.getElementsByTagName(tagName).item(0);
            if (elt == null) {
                return null;
            }
            value = elt.getTextContent();
        } catch (SAXException e) {
            throw new UnableToProcessResponseException(e);
        } catch (ParserConfigurationException e) {
            throw new UnableToProcessResponseException(e);
        } catch (IOException e) {
            throw new UnableToPerformRequestException(e);
        }

        return value;
    }

    private String getSignedUrl(Map<String, String> params, String secretKey) throws KarotzException {
        String q = KarotzUtil.buildQuery(params);
        String signedQuery = KarotzUtil.doHmacSha1(secretKey, q);
        LOGGER.info("signedQuery: [{}]", signedQuery);
        try {
            return String.format("%s?%s&signature=%s", KAROTZ_URL_START, q, URLEncoder.encode(signedQuery,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new KarotzException(e);
        }
    }
}
