package taskB;

/**
 * This class is used as a node for the linked list
 * That is, we start linking blocks together
 */
public class BlockNode {
    private Block block;
    private BlockNode next;

    /**
     * Default constructor
     */
    public BlockNode() {
        next = null;
        block = null;
    }

    /**
     * Constructor of BlockNode
     *
     * @param block Block of memory at this node
     * @param next next node linked to current node
     */
    public BlockNode(Block block, BlockNode next) {
        this.block = block;
        this.next = next;
    }

    /**
     * Gets the next BlockNode that's linked.
     *
     * @return next BlockNode linked to current.
     */
    public BlockNode getNext() {
        return this.next;
    }

    /**
     * Setter for the next BlockNode link
     *
     * @param next next BlockNode to be linked to
     */
    public void setNext(BlockNode next) {
        this.next = next;
    }

    /**
     * Getter for the block stored at this BlockNode if one.
     *
     * @return block to get
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Sets the block of memory for current node.
     *
     * @param block Block to be set.
     */
    public void setBlock(Block block) {
        this.block = block;
    }
}
