package better.smartcard.gp;

import better.smartcard.util.AID;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a CAP file prepared for loading
 *
 * This contains CAP file components in load order,
 * split into blocks no bigger than indicated.
 *
 * Once loaded each of these will become an ELF on the card.
 *
 * These objects can be produced using methods in
 * {@link better.smartcard.cap.CapPackage}.
 */
public class GPLoadFile {

    /** AID of the package */
    AID mPackageAID;

    /** Maximum size among all blocks */
    int mBlockSize = 0;
    /** Total size of the load file */
    int mTotalSize = 0;

    /** List of the actual blocks */
    List<byte[]> mBlocks = new ArrayList<>();

    /** Construct a new load file */
    public GPLoadFile(AID packageAID) {
        mPackageAID = packageAID;
    }

    /** @return the AID of the package contained in this load file */
    public AID getPackageAID() {
        return mPackageAID;
    }

    /** @return the maximum block size (in bytes) of this load file */
    public int getBlockSize() {
        return mBlockSize;
    }

    /** @return the total size (in bytes) of this load file */
    public int getTotalSize() {
        return mTotalSize;
    }

    /** @return the number of blocks in this load file */
    public int getNumBlocks() {
        return mBlocks.size();
    }

    /** @return the blocks comprising this load file */
    public List<byte[]> getBlocks() {
        return mBlocks;
    }

    /**
     * Add a block to the load file
     *
     * Use with care! Maximum block size will be adjusted!
     *
     * @param block to add
     */
    public void addBlock(byte[] block) {
        int length = block.length;
        // keep total size
        mTotalSize += length;
        // adjust block size
        if(length > mBlockSize) {
            mBlockSize = length;
        }
        // add the block
        mBlocks.add(block);
    }

}
