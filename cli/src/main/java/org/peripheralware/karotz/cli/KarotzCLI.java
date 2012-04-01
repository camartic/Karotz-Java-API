package org.peripheralware.karotz.cli;

import jline.ConsoleReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.peripheralware.karotz.KarotzException;
import org.peripheralware.karotz.action.led.LedLightAction;
import org.peripheralware.karotz.action.led.LedOffAction;
import org.peripheralware.karotz.action.multimedia.PlayMultimediaAction;
import org.peripheralware.karotz.action.tts.SpeakAction;
import org.peripheralware.karotz.client.KarotzClient;
import org.peripheralware.karotz.publisher.KarotzActionPublisher;

import java.io.IOException;

public class KarotzCLI {

    private static final String H = "h";
    private static final String HELP_SHORT_CODE = H;
    private static final String APIKEY_SHORT_CODE = "a";
    private static final String SECRET_KEY_SHORT_CODE = "s";
    private static final String INSTALL_ID_SHORT_CODE = "i";
    private final KarotzClient client;
    private final KarotzActionPublisher karotzActionPublisher;
    private static final String API_DEFAULT = "a8e0ad3e-7a24-4028-92ac-d376c7168bdf";
    private static final String SECRET_KEY_DEFAULT = "c53b533c-a8a9-4d0c-a181-9e6e8709d3f1";

    public KarotzCLI(final KarotzClient client) {
        this.client = client;
        karotzActionPublisher = new KarotzActionPublisher(client);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    client.stopInteractiveMode();
                } catch (KarotzException e) {
                    log("Error stopping client:" + e.getMessage());
                }
            }
        });
    }

    private void run() {

        log("Starting Client.");
        try {
            client.startInteractiveMode();
            log("Ready.");
        } catch (KarotzException e) {
            exit(e.getMessage());
        }

        runCLILoop();

    }

    private void runCLILoop() {
        try {
            String input = "";
            ConsoleReader consoleReader = new ConsoleReader();
            consoleReader.setDefaultPrompt("karotz > ");
            do {
                input = consoleReader.readLine();

                processInput(consoleReader, input);
            } while (!input.equals("exit"));


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void processInput(ConsoleReader consoleReader, String input) throws IOException {
        input = input.trim();
        String[] split = input.split(" ");

        String command = split[0];

        if (command.equals("play")) {
            try {
                karotzActionPublisher.performAction(new PlayMultimediaAction(input.substring(command.length())));
                completeCommand(consoleReader);
            } catch (KarotzException e) {
                consoleReader.printString(e.getMessage());
            }
        } else if (command.equals("speak")) {
            try {
                karotzActionPublisher.performAction(new SpeakAction(input.substring(command.length())));
                completeCommand(consoleReader);
            } catch (KarotzException e) {
                consoleReader.printString(e.getMessage());
            }
        } else if (command.equals("led")) {
            try {
                if (split[1].equals("off")) {
                    karotzActionPublisher.performAction(new LedOffAction());
                } else {
                    karotzActionPublisher.performAction(new LedLightAction(input.substring(command.length())));
                }
                completeCommand(consoleReader);
            } catch (KarotzException e) {
                consoleReader.printString(e.getMessage());
            }
        } else if (command.equals("stop")) {
            try {
                client.stopInteractiveMode();
                completeCommand(consoleReader);
                exit("Exiting");
            } catch (KarotzException e) {
                consoleReader.printString(e.getMessage());
            }
        } else {
            consoleReader.printString("Unknown command:" + input);
        }

    }

    private void completeCommand(ConsoleReader consoleReader) throws IOException {
        consoleReader.printString("Complete");
        consoleReader.printNewline();
    }

    private void log(String message) {
        System.out.println(message);
    }


    private static Options getOptions() {
        Options options = new Options();

        options.addOption(APIKEY_SHORT_CODE, "apiKey", true, "Karotz API Key");
        options.addOption("h", "help", true, "Show this help");
        options.addOption(INSTALL_ID_SHORT_CODE, "installId", true, "Karotz Install Id");
        options.addOption(SECRET_KEY_SHORT_CODE, "secretKey", true, "Karotz Secret Key");

        return options;
    }

    public static void main(String... args) {


        CommandLineParser parser = new PosixParser();
        try {
            CommandLine line = parser.parse(getOptions(), args);

            if (line.hasOption(HELP_SHORT_CODE)) {
                printHelpAndExit();
            }

            String apiKey = API_DEFAULT;
            if (line.hasOption(APIKEY_SHORT_CODE)) {
                apiKey = line.getOptionValue(APIKEY_SHORT_CODE);
            }
            String secretkey = SECRET_KEY_DEFAULT;
            if (line.hasOption(SECRET_KEY_SHORT_CODE)) {
                secretkey = line.getOptionValue(SECRET_KEY_SHORT_CODE);
            }
            String installId = null;
            if (line.hasOption(INSTALL_ID_SHORT_CODE)) {
                installId = line.getOptionValue(INSTALL_ID_SHORT_CODE);
            }

            if (installId == null) {
                System.err.println("Install Id is required.");
                printHelpAndExit();
            }

            new KarotzCLI(new KarotzClient(apiKey, secretkey, installId)).run();

        } catch (ParseException e) {
            exit(e.getMessage());
        }


    }

    private static void printHelpAndExit() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java KarotzCLI", getOptions());
        System.exit(0);
    }

    private static void exit(String message) {
        System.err.println(message);
        System.exit(1);
    }
}
