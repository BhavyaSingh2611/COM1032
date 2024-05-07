package taskA.commands;

import java.util.List;
import java.util.Queue;

public interface Command {
    void setArguments(List<String> arguments, Queue<String> errorStream);

    void execute(Queue<String> outputStream, Queue<String> errorStream);
}
