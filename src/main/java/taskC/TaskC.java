package taskC;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TaskC {
    private static final int PAGE_SIZE = 4096;

    private static final TLB TLB = new TLB();
    private static final List<TLBEntry> tlb = TLB.tlb;

    private static final PageTable PageTable = new PageTable();
    private static final Map<Integer, PageTableEntry> pageTable = PageTable.pageTable;

    private static final List<Integer> virtualAddresses = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        List<String> fileContents = new ArrayList<>();
        try {
            File taskFile = new File("taskC.txt");
            Scanner fileReader = new Scanner(taskFile);
            while (fileReader.hasNextLine()) {
                fileContents.add(fileReader.nextLine());
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("taskC.txt not found.");
        }

        int initialAddressIndex = fileContents.indexOf("#Address");
        int initialTLBIndex = fileContents.indexOf("#Initial TLB") + 1;
        int initialPageTableIndex = fileContents.indexOf("#Initial Page table") + 1;

        List<String> tlbEntries = new ArrayList<>();
        List<String> pageTableEntries = new ArrayList<>();
        for (int i = initialAddressIndex + 1; i < initialTLBIndex - 1; i++) {
            virtualAddresses.add(Integer.parseInt(fileContents.get(i).split("x")[1], 16));
        }

        for (int i = initialTLBIndex + 1; i < initialPageTableIndex - 1; i++) {
            tlbEntries.add(fileContents.get(i));
        }

        for (int i = initialPageTableIndex + 1; i < fileContents.size(); i++) {
            pageTableEntries.add(fileContents.get(i));
        }

        TLB.initialize(tlbEntries);
        PageTable.initialize(pageTableEntries);

        process();
    }

    private static void process() throws IOException {
        List<String> output = new ArrayList<>();
        for (int virtualAddress : virtualAddresses) {
            String result = processAddress(virtualAddress);

            output.add(String.format("# After the memory access 0x%04X", virtualAddress));
            output.add("#Address, Result (Hit, Miss, PageFault)");

            output.add(String.format("0x%04X,%s", virtualAddress, result));

            output.add("#updated TLB\n" + "#Valid, Tag, Physical Page #, LRU");
            for (TLBEntry entry : tlb) {
                output.add(entry.toString());
            }

            output.add("#updated Page table\n" + "#Index,Valid,Physical Page or On Disk");
            for (Map.Entry<Integer, PageTableEntry> entry : pageTable.entrySet()) {
                output.add(entry.getKey() + "," + entry.getValue().toString());
            }
        }


        writeResults(output);
    }

    private static void writeResults(List<String> output) throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("taskc-sampleoutput.txt"))) {
            for (String result : output) {
                fileWriter.write(result);
                fileWriter.newLine();
            }
        }
    }

    private static String processAddress(int virtualAddress) {
        int pageNumber = virtualAddress / PAGE_SIZE;

        for (TLBEntry entry : tlb) {
            if (entry.valid == 1 && entry.tag == pageNumber) {
                TLB.updateLRUOrder(entry);
                return "Hit";
            }
        }

        if (pageTable.get(pageNumber).valid == 0 || pageNumber >= pageTable.size()) {
            pageTable.put(pageNumber, new PageTableEntry(1, String.valueOf(PageTable.nextPageIndex++)));
            TLB.updateTLB(pageNumber, PageTable.nextPageIndex - 1);
            return "Page fault";
        } else {
            TLB.updateTLB(pageNumber, Integer.parseInt(pageTable.get(pageNumber).physicalPage));
            return "Miss";
        }
    }
}
