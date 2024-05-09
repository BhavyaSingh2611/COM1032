package taskB;

/**
 * This class main purpose is to be a linked list
 * for the current blocks of memory that are placed or free
 * for the simulation of First Fit, Best Fit, and Worst Fit
 * memory allocation methods.
 */
public class MainMemory {
    private BlockNode start;
    private BlockNode end;
    private int size;

    /**
     * Constructor, initialize linked list
     */
    public MainMemory() {
        start = null;
        end = null;
        size = 0;
    }

    /**
     * Checks if linked list is empty
     *
     * @return True if empty,  false if not
     */
    public boolean isEmpty() {
        return start == null;
    }

    /**
     * Gets the size of linked list
     *
     * @return size of linked list
     */
    public int getSize() {
        return size;
    }

    /**
     * Inserts Block at start of linked list, best to be used to initialize first node.
     *
     * @param block Block of memory to insert.
     */
    public void insertAtStart(Block block) {
        BlockNode nptr = new BlockNode(block, null);
        size++;
        if (start == null) {
            start = nptr;
            end = start;
        } else {
            nptr.setNext(start);
            start = nptr;
        }
    }

    /**
     * First fit insert, this method goes through the linked list finding the
     * first place it can insert the block into memory.
     *
     * @param proc to insert into memory
     * @return True if successfully inserted block of memory, False if failed.
     */
    public boolean firstFitInsert(Process proc) {
        Block block = new Block(proc);
        BlockNode nptr = new BlockNode(block, null);

        if (start == null) {
            start = nptr;
            end = start;
            return true;
        } else {

            BlockNode curr = start;

            //look at all available slots/holes in memory
            //select the first available position of suitable size for block
            while (curr != null) {

                //enough available space in memory identified
                if (curr.getBlock().canPlace(block.getProcess())) {

                    //get the end memory location for available block curr
                    int end = curr.getBlock().getHole().getEnd();

                    //add the process in memory
                    curr.getBlock().setProcess(block.getProcess());

                    //take only what we need from memory
                    int block_start = curr.getBlock().getHole().getStart();
                    int memory_needs = block.getProcess().getArgument();
                    curr.getBlock().getHole().setRange(block_start, block_start + memory_needs - 1);

                    //create a new block with the rest of memory we don't need
                    //notice curr.getBlock().getHole().getEnd() was changed by line 155
                    if (curr.getBlock().getHole().getEnd() < end) {
                        BlockNode newBlock = new BlockNode(
                                new Block(null, new Hole(curr.getBlock().getHole().getEnd() + 1, end)), curr.getNext());

                        curr.setNext(newBlock);
                    }
                    size++;
                    return true;
                }
                curr = curr.getNext();
            }
            return false;
        }
    }

    /**
     * Best fit insert, this method goes through the linked list finding the
     * best place it can insert the block into memory.
     *
     * @param proc to insert into memory
     * @return True if successfully placed, false if it failed.
     */
    public boolean bestFitInsert(Process proc) {
        Block block = new Block(proc);
        BlockNode ptr = new BlockNode(block, null);

        if (start == null) {
            start = ptr;
            end = start;
            return true;
        } else {
            BlockNode curr = start;
            int index = -1;
            int min = 5000;
            int i = 0;

            //look at all available slots/holes in memory
            //select the position for the smallest available block of memory that is suitable
            while (curr != null) {

                if (curr.getBlock().getSize() >= proc.getArgument()) {
                    if (curr.getBlock().canPlace(proc)) {
                        if (curr.getBlock().getSize() < min) {
                            index = i;
                            min = curr.getBlock().getSize();
                        }

                    }
                }
                curr = curr.getNext();
                i++;
            }

            //if no position was found return false
            if (index == -1) {
                return false;
            }

            //look at all blocks/holes in memory until you get to the index position
            //select the first available position of suitable size for block
            i = 0;
            curr = start;
            while (curr != null) {

                if (i == index) {

                    //enough available space in memory identified
                    if (curr.getBlock().canPlace(block.getProcess())) {

                        //get the end memory location for available block curr
                        int end = curr.getBlock().getHole().getEnd();

                        //add the process in memory
                        curr.getBlock().setProcess(block.getProcess());

                        //take only what we need from memory
                        int block_start = curr.getBlock().getHole().getStart();
                        int memory_needs = block.getProcess().getArgument();
                        curr.getBlock().getHole().setRange(block_start, block_start + memory_needs - 1);

                        //create a new block with the rest of memory we don't need
                        //notice curr.getBlock().getHole().getEnd() was changed
                        if (curr.getBlock().getHole().getEnd() < end) {
                            BlockNode newBlock = new BlockNode(
                                    new Block(null, new Hole(curr.getBlock().getHole().getEnd() + 1, end)),
                                    curr.getNext());

                            curr.setNext(newBlock);
                        }
                        size++;
                        return true;
                    }
                }

                i++;
                curr = curr.getNext();
            }
            return false;
        }
    }

    /**
     * This method goes through current memory blocks. If blocks are next to each other
     * and free it will join the blocks together making a larger block.
     */
    public void joinBlocks() {
        BlockNode ptr = start;

        while (ptr.getNext() != null) {

            BlockNode next = ptr.getNext();

            if (ptr.getBlock().getProcess() == null && next.getBlock().getProcess() == null) {
                int start = ptr.getBlock().getHole().getStart();
                int end = next.getBlock().getHole().getEnd();
                ptr.getBlock().getHole().setRange(start, end);
                ptr.setNext(next.getNext());
                size--;
                continue;
            }
            ptr = ptr.getNext();
        }
    }


    /**
     * This method gets the external fragmentation of the current memory blocks
     * if a block of memory failed to placed.
     *
     * @return external fragmentation of memory.
     */
    public int externalFragmentation() {
        BlockNode ptr = start;
        int externalFragmentation = 0;

        while (ptr != null) {
            if (ptr.getBlock().getProcess() == null) {
                externalFragmentation += ptr.getBlock().getSize();
            }
            ptr = ptr.getNext();
        }

        return externalFragmentation;
    }

    /**
     * compact memory, this method goes through the current memory blocks
     * and moves all the blocks to the start of memory.
     */
//    public void compactMemory() {
//        BlockNode newStart = null;
//        BlockNode newEnd = null;
//        int totalMemoryUsed = 0;
//
//        // Iterate through the linked list of memory blocks
//        for (BlockNode curr = start; curr != null; curr = curr.getNext()) {
//            // If a block has a process, create a new block with the process
//            if (curr.getBlock().getProcess() != null) {
//                // Create a new Hole object for the Block
//                Hole newHole = new Hole(totalMemoryUsed, totalMemoryUsed + curr.getBlock().getSize() - 1);
//                BlockNode newNode = new BlockNode(new Block(curr.getBlock().getProcess(), newHole), null);
//                totalMemoryUsed += curr.getBlock().getSize();
//
//                // Add the new block to the new start pointer
//                if (newStart == null) {
//                    newStart = newNode;
//                    newEnd = newStart;
//                } else {
//                    newEnd.setNext(newNode);
//                    newEnd = newNode;
//                }
//            }
//        }
//
//        // Create a new block with the remaining free memory
//        BlockNode freeMemoryNode = new BlockNode(new Block(null, new Hole(totalMemoryUsed, TaskB.TOTAL_BYTES - 1)), null);
//
//        // Add the free memory block to the new start pointer
//        if (newStart == null) {
//            newStart = freeMemoryNode;
//        } else {
//            newEnd.setNext(freeMemoryNode);
//        }
//
//        // Replace the old start pointer with the new one
//        start = newStart;
//    }
    public void compactMemory() {
        int totalMemoryUsed = 0;
        BlockNode prev = null;
        BlockNode curr = start;

        // Iterate through the linked list of memory blocks
        while (curr != null) {
            // If a block has a process, update its hole and increment totalMemoryUsed
            if (curr.getBlock().getProcess() != null) {
                curr.getBlock().getHole().setRange(totalMemoryUsed, totalMemoryUsed + curr.getBlock().getSize() - 1);
                totalMemoryUsed += curr.getBlock().getSize();
                prev = curr;
                curr = curr.getNext();
            } else {
                // If a block doesn't have a process, remove it from the linked list
                if (prev != null) {
                    prev.setNext(curr.getNext());
                } else {
                    start = curr.getNext();
                }
                curr = curr.getNext();
            }
        }

        // Create a new block with the remaining free memory
        Block freeMemoryBlock = new Block(null, new Hole(totalMemoryUsed, TaskB.TOTAL_BYTES - 1));

        // If the linked list is empty, set the start to the free memory block
        if (start == null) {
            start = new BlockNode(freeMemoryBlock, null);
        } else {
            // Otherwise, add the free memory block to the end of the linked list
            if (prev != null) {
                prev.setNext(new BlockNode(freeMemoryBlock, null));
            }
        }
    }

    /**
     * This method goes through the blocks of memory and de-allocates the block
     * for the provided process_number
     *
     * @param process_number Process to be de-allocated.
     */
    public void deallocateBlock(int process_number) {

        BlockNode ptr = start;
        while (ptr != null) {

            if (ptr.getBlock().getProcess() != null) {
                if (ptr.getBlock().getProcess().getReference_number() == process_number) {
                    ptr.getBlock().setProcess(null);
                    joinBlocks();
                    return;
                }
            }
            ptr = ptr.getNext();
        }
    }

    /**
     * This method prints the whole list of current memory.
     */
    public void printBlocks() {
        System.out.println("Current memory display");
        BlockNode ptr = start;
        while (ptr != null) {
            ptr.getBlock().displayBlock();
            ptr = ptr.getNext();
        }
    }
}
