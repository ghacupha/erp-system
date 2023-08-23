package io.github.erp.erp.depreciation;

public class DepreciationBatchSequenceNotFound extends IllegalStateException {

    /**
     * Constructs an IllegalStateException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * @param s the String that contains a detailed message
     */
    public DepreciationBatchSequenceNotFound(String s) {
        super("Depreciation batch id: " + s + " not found");
    }
}
