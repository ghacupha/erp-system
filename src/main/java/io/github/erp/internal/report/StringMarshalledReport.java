package io.github.erp.internal.report;

/**
 * Generates a report whose parameter is a string and the result is a string. The result
 * being a string can be used to represent the path of the report, and the parameter can
 * be anything given a string. In some cases I imagine that might be a query in others it
 * could be the format of a report.
 */
public interface StringMarshalledReport extends Report<String, String> {
}
