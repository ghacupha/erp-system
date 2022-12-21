package io.github.erp.internal.report.attachment;

/**
 * This interface checks if the checksum on the file and the checksum calculated for the same
 * file at present are the same
 */
public interface MatchesChecksum<Algo> {

    /**
     * This method calculates internally the checksum of a file comparing the checksum in store
     * and returns true if they match, and false otherwise
     *
     * @param documentChecksum Existing checksum of the file from records (DB)
     * @param fileName Name of the file whose checksum we seek
     * @param checksumAlgorithm Algorithm used to calculate the checksum
     * @return true is the checksum is matching
     */
    boolean checksumIsMatching(String documentChecksum, String fileName, Algo checksumAlgorithm);
}
