package io.github.erp.internal.report.attachment;

/**
 * Records whether or not information has been tampered, by setting the argument
 * parameter as true or false
 *
 * @param <T>
 */
public interface RecordsTamper {

    /**
     * Records whether or not information has been tampered
     *
     * @param dataIsTempered true if the information is tampered
     */
    void isTampered(boolean dataIsTempered);
}
