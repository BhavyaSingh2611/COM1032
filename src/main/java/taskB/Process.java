package taskB;

/**
 * This class defines basic aspects of a Process
 */
public class Process {
    private int referenceNumber;
    private int operation;
    private int argument;

    private Process() {
    }

    /**
     * @param referenceNumber A reference number (a unique identifier for that operation)
     * @param operation        An operation (either 1 for allocate? or 2 for de-allocate?)
     * @param argument         An argument (a size in bytes for an allocate operation;
     *                         a reference number for a de-allocate operation)
     */
    public Process(int referenceNumber, int operation, int argument) {
        this.referenceNumber = referenceNumber;
        this.operation = operation;
        this.argument = argument;
    }

    public int getReferenceNumber() {
        return this.referenceNumber;
    }

    public int getOperation() {
        return this.operation;
    }

    public int getArgument() {
        return this.argument;
    }

    public boolean isAllocating() {
        return getOperation() == 1;
    }

    public boolean isDeallocating() {
        return getOperation() == 2;
    }
}
