package taskA.commands;

import taskA.Parsers;

import java.io.File;
import java.util.*;

public class WcCommand implements Command {
    private static final Set<String> SUPPORTED_PARAMS = new HashSet<>(List.of("__file", "l"));
    private Map<String, String> params;

    @Override
    public void setArguments(List<String> arguments, Queue<String> errorStream) {
        this.params = Parsers.parseArguments(arguments);
        Set<String> keys = this.params.keySet();
        for (String key : keys) {
            if (!SUPPORTED_PARAMS.contains(key)) {
                errorStream.add("Unsupported parameter: " + key);
            }
        }
    }

    @Override
    public void execute(Queue<String> outputStream, Queue<String> errorStream) {
        String fileName = params.get("__file");
        if (outputStream.isEmpty() && fileName == null) {
            errorStream.add("File name not provided");
            return;
        }

        int lines = 0;
        int words = 0;
        int bytes = 0;
        try {
            if (fileName == null) {
                while (!outputStream.isEmpty()) {
                    String data = outputStream.poll();
                    lines++;
                    words += data.split("\\s+").length;
                    bytes += data.getBytes().length;
                }
            } else {
                File file = new File(fileName);
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    lines++;
                    words += data.split("\\s+").length;
                    bytes += data.getBytes().length;
                }
                reader.close();
            }
            if (params.containsKey("l")) {
                outputStream.add(String.valueOf(lines));
            } else {
                outputStream.add(lines + " " + words + " " + bytes);
            }
        } catch (Exception e) {
            errorStream.add(e.getMessage());
        }
    }
}