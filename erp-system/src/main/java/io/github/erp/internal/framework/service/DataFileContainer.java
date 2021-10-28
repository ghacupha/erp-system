package io.github.erp.internal.framework.service;

import io.github.erp.internal.framework.batch.HasDataFile;

import java.util.Optional;

public interface DataFileContainer<T> {

    /**
     * Returns an instance of the file-upload that contains the same file-id
     * @param fileId
     * @return
     */
    Optional<? extends T> findOne(long fileId);
}
