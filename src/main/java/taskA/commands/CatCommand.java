package taskA.commands;

import taskA.Parsers;

import java.io.File;
import java.util.*;

public class CatCommand implements Command {
    private static final Set<String> SUPPORTED_PARAMS = new HashSet<>(List.of("__file"));
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
        try {
            String fileName = params.get("__file");
            if (fileName == null) {
                errorStream.add("File name not provided");
                return;
            }

            File file = new File(fileName);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                outputStream.add(data);
            }
            reader.close();
        } catch (Exception e) {
            errorStream.add(e.getMessage());
        }
    }
}
