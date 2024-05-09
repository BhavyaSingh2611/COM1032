package taskA;

import java.io.File;
import java.util.*;

public class Parsers {
    public static Map<String, String> parseArguments(List<String> arguments) {
        // Create a new HashMap to store the parsed arguments
        Map<String, String> params = new HashMap<>();

        // Try to get the last argument as a file name
        try {
            String fileName = arguments.get(arguments.size() - 1);
            File file = new File(fileName);
            // If the file exists, add it to the params map and remove it from the arguments list
            if (file.exists()) {
                params.put("__file", arguments.remove(arguments.size() - 1));
            }
        } catch (Exception ignored) {}

        // Iterate over the arguments list
        for (int i = 0; i < arguments.size(); i++) {
            // If the argument contains "=", it's a key-value pair (--key=value)
            if (arguments.get(i).contains("=")) {
                // Split the argument into key and value
                String[] split = arguments.get(i).split("=");
                params.put(removeDashes(split[0]), split[1]);
            }
            // If the argument starts with "-", it's a flag (--key value) or a boolean flag (--key)
            else if (arguments.get(i).startsWith("-")) {
                // Get the next argument, if it exists
                int nextIndex = Math.min(i + 1, arguments.size() - 1);
                // Check if the next argument is also a flag
                boolean nextIsFlag = arguments.get(nextIndex).startsWith("-");
                String key = removeDashes(arguments.get(i));

                // If the next argument is a flag, the current flag's value is "true" (--key)
                if (nextIsFlag) {
                    params.put(key, "true");
                }
                // Otherwise, the next argument is the current flag's value
                else {
                    params.put(key, arguments.get(nextIndex));
                    // Skip the next argument since we've already processed it
                    i++;
                }
            }
        }
        return params;
    }

    public static List<String> parseCommand(String commandLine) {
        String[] split = commandLine.split("\\s+");
        return new ArrayList<>(Arrays.asList(split));
    }

    public static List<String> parsePipes(String commandLine) {
        String[] split = commandLine.split("\\|");
        return Arrays.asList(split);
    }

    private static String removeDashes(String str) {
        // Remove leading 1 to 2 dashes from the key
        return str.replaceFirst("^-{1,2}", "");
    }
}
