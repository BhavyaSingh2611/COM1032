package taskC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageTable {
    public int nextPageIndex = 13;
    public Map<Integer, PageTableEntry> pageTable = new HashMap<>();

    public void initialize(List<String> entries) {
        for (String entry : entries) {
            String[] chunks = entry.split(",");
            this.pageTable.put(Integer.parseInt(chunks[0]), new PageTableEntry(Integer.parseInt(chunks[1]), chunks[2].trim()));
        }
    }
}
