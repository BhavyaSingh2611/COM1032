package taskC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TLB {
    public List<TLBEntry> tlb = new ArrayList<>();

    public void initialize(List<String> tlbEntries) {
        for (String entry : tlbEntries) {
            int[] chunks = Arrays.stream(entry.split(",")).mapToInt(Integer::parseInt).toArray();
            this.tlb.add(new TLBEntry(chunks[0], chunks[1], chunks[2], chunks[3]));
        }
    }

    public void updateTLB(int tag, int physicalPageIndex) {
        TLBEntry lru = null;
        for (TLBEntry entry : this.tlb) {
            if (entry.valid == 0) {
                lru = entry;
                break;
            }
        }

        if (lru == null) {
            lru = this.tlb.get(0);
            for (TLBEntry entry : this.tlb) {
                if (entry.lru < lru.lru) {
                    lru = entry;
                }
            }
        }

        lru.valid = 1;
        lru.tag = tag;
        lru.physicalPage = physicalPageIndex;
        updateLRUOrder(lru);
    }

    public void updateLRUOrder(TLBEntry currentEntry) {
        int currentLRU = currentEntry.lru;
        currentEntry.lru = 4;

        for (TLBEntry entry : this.tlb) {
            if (entry != currentEntry && entry.lru > currentLRU) {
                entry.lru--;
            }
        }
    }
}
