package taskA;

import taskA.commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class TaskA {

    public static void main(String[] args) throws IOException {
        System.out.println("Operating Systems Coursework");
        System.out.println("Name: Bhavya Singh");
        System.out.println("Please enter your commands - cat, cut, sort, uniq, wc or |");

        String commandLine;
        String filePath = "taskA.txt";

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print(">>");
            commandLine = console.readLine();
            if (commandLine.isEmpty()) continue;
            if (commandLine.equals("exit")) break;

            List<String> commands = Parsers.parsePipes(commandLine);
            Queue<String> outputPipe = new ArrayDeque<>();
            Queue<String> errorPipe = new ArrayDeque<>();

            for (String c : commands) {
                List<String> command = Parsers.parseCommand(c.trim());
                String commandName = command.remove(0);
                Command executable = switch (commandName) {
                    case "cat" -> new CatCommand();
                    case "cut" -> new CutCommand();
                    case "sort" -> new SortCommand();
                    case "uniq" -> new UniqCommand();
                    case "wc" -> new WcCommand();
                    default -> null;
                };

                if (executable == null) {
                    System.out.println("Not Implemented: " + commandName);
                    break;
                }

                executable.setArguments(command, errorPipe);
                executable.execute(outputPipe, errorPipe);
            }

            while (!outputPipe.isEmpty()) {
                System.out.println(outputPipe.poll());
            }

            while (!errorPipe.isEmpty()) {
                System.out.println(errorPipe.poll());
            }
        }
    }
}
