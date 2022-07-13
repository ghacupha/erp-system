package io.github.erp.internal.model;

/**
 * This interface serves an object that tracks a checksum of some kind,
 * without dictating the algorithm for the calculation or the purpose. The
 * checksum is just there and is used internally used by implementation for
 * unique identification of data, files, users and verification of the same
 */
public interface HasChecksum {

    void setChecksum(String fileChecksum);

    String getFileChecksum();
}
