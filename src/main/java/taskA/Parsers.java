package taskA;

import java.io.File;
import java.util.*;

public class Parsers {
    public static Map<String, String> parseArguments(List<String> arguments) {
        Map<String, String> params = new HashMap<>();
        try {
            String fileName = arguments.get(arguments.size() - 1);
            File file = new File(fileName);
            if (file.exists()) {
                params.put("__file", arguments.remove(arguments.size() - 1));
            }
        } catch (Exception ignored) {
        }

        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i).contains("=")) {
                String[] split = arguments.get(i).split("=");

                params.put(removeDashes(split[0]), split[1]);

            } else if (arguments.get(i).startsWith("-")) {
                int nextIndex = Math.min(i + 1, arguments.size() - 1);

                boolean nextIsFlag = arguments.get(nextIndex).startsWith("-");

                String key = removeDashes(arguments.get(i));

                if (nextIsFlag) {
                    params.put(key, "true");
                } else {
                    params.put(key, arguments.get(nextIndex));
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
        return str.replaceFirst("^-{1,2}", "");
    }
}
