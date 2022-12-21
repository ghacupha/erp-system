package io.github.erp.internal.report.attachment;

import io.github.erp.internal.model.HasChecksum;

import java.util.UUID;

/**
 * Representation of the business-document-fso on retrieval
 */
public interface RetrievedDocument<FSO, A> extends HasChecksum, ContainsChecksumAlgorithm<A>, RecordsTamper {

    void setDocumentFile(byte[] reportResource);

    String getDocumentTitle();

    UUID getDocumentSerial();

    String getFileContentType();

    @Override
    default void setChecksum(String fileChecksum) {
        // We dare not reset any serials
    }

    @Override
    default String getFileChecksum() {
        return this.getDocumentSerial().toString();
    }

    A getChecksumAlgorithm();

    void isTampered(boolean isTempered);
}
