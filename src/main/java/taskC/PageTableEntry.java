package taskC;

public class PageTableEntry {
    public int valid;
    public String physicalPage;

    public PageTableEntry(int valid, String physicalPage) {
        this.valid = valid;
        this.physicalPage = physicalPage;
    }

    @Override
    public String toString() {
        return valid + "," + physicalPage;
    }
}