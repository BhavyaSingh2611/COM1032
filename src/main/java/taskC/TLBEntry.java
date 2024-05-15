package taskC;

public class TLBEntry {
    public int valid;
    public int tag;
    public int physicalPage;
    public int lru;

    public TLBEntry(int valid, int tag, int physicalPage, int lru) {
        this.valid = valid;
        this.tag = tag;
        this.physicalPage = physicalPage;
        this.lru = lru;
    }

    @Override
    public String toString() {
        return valid + "," + tag + "," + physicalPage + "," + lru;
    }
}