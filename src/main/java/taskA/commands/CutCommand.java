package taskA.commands;

import taskA.Parsers;

import java.io.File;
import java.util.*;

public class CutCommand implements Command {
    private static final Set<String> SUPPORTED_PARAMS = new HashSet<>(List.of("__file", "d", "f"));
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

        String delimiter = params.get("d");
        if (delimiter == null) {
            delimiter = "\",\"";
        }

        String parsedDelimiter = delimiter.substring(1, delimiter.length() - 1);

        String field = params.get("f");
        if (field == null) {
            errorStream.add("Field not provided");
            return;
        }
        List<Integer> parsedFields = parseField(field);

        try {
            if (fileName == null) {
                List<String> temp = new ArrayList<>(outputStream.size());
                while (!outputStream.isEmpty()) {
                    String data = outputStream.poll();
                    temp.add(data);
                }
                for (String data : temp) {
                    process(data, parsedDelimiter, parsedFields, outputStream);
                }
            } else {
                File file = new File(fileName);
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    process(data, parsedDelimiter, parsedFields, outputStream);
                }
                reader.close();
            }
        } catch (Exception e) {
            errorStream.add(e.getMessage());
        }
    }

    private void process(String data, String delimiter, List<Integer> fields, Queue<String> outputStream) {
        String[] split = data.split(delimiter);
        List<String> output = new ArrayList<>();
        for (int i : fields) {
            if (i < split.length && i >= 1) {
                output.add(split[i - 1]);
            }
        }
        outputStream.add(String.join(delimiter, output));
    }

    private List<Integer> parseField(String field) {
        List<Integer> fields = new ArrayList<>();
        String[] split = field.split(",");
        for (String s : split) {
            if (s.contains("-")) {
                String[] range = s.split("-");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (int i = start; i <= end; i++) {
                    fields.add(i);
                }
            } else {
                fields.add(Integer.parseInt(s));
            }
        }
        return fields;
    }
}
