package org.peripheralware.karotz.publisher;

import org.peripheralware.karotz.client.KarotzActionFailedException;
import org.peripheralware.karotz.client.KarotzClient;
import org.peripheralware.karotz.evil.KarotzUtil;
import org.peripheralware.karotz.evil.UnableToBuildKarotzWebAPIURLException;
import org.peripheralware.karotz.client.UnableToPerformRequestException;
import org.peripheralware.karotz.action.KarotzAction;

import java.util.Map;
import java.util.logging.Logger;


/**
 * KarotzActionPublisher class.
 *
 * @author Martin Ritchie
 */
public class KarotzActionPublisher {

    protected static final Logger LOGGER = Logger.getLogger(KarotzActionPublisher.class.getName());

    private KarotzClient client;

    public KarotzActionPublisher(KarotzClient client) {
        this.client = client;
    }

    /**
     *
     * @param action to perform
     * @return if action was successful
     * @throws ClientMustBeInInteractiveModeException
     * @throws UnableToBuildKarotzWebAPIURLException
     * @throws UnableToPerformRequestException
     */
    public boolean performAction(KarotzAction action) throws ClientMustBeInInteractiveModeException, UnableToBuildKarotzWebAPIURLException, UnableToPerformRequestException {

        if (!client.isInteractive()) {
            throw new ClientMustBeInInteractiveModeException();
        }

        Map<String, String> params = action.getParameters();
        params.put("interactiveid", client.getInteractiveId());
        String url = action.getURL() + '?' + KarotzUtil.buildQuery(params);

        String result = client.doRequest(url);
        String code = client.parseResponse(result, "code");
        if (!"OK".equalsIgnoreCase(code)) {
            throw new KarotzActionFailedException("failed to do action: " + code, code);
        }
        return  true;
    }

}
