package taskA.commands;

import taskA.Parsers;

import java.io.File;
import java.util.*;

public class UniqCommand implements Command {
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
        String fileName = params.get("__file");
        if (outputStream.isEmpty() && fileName == null) {
            errorStream.add("File name not provided");
            return;
        }

        List<String> temp = new ArrayList<>(outputStream.size());
        try {
            if (fileName == null) {
                while (!outputStream.isEmpty()) {
                    String data = outputStream.poll();
                    temp.add(data);
                }
            } else {
                File file = new File(fileName);
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    temp.add(data);
                }
                reader.close();
            }
            String current = "";
            for (String data : temp) {
                if (!current.equals(data)) {
                    outputStream.add(data);
                    current = data;
                }
            }
        } catch (Exception e) {
            errorStream.add(e.getMessage());
        }
    }
}