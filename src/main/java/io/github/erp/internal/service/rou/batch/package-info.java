/**
 * The batch implementation of ROU depreciation starts with an initial acquisition of
 * rou-model-metadata instances and after filtering and sorting the process then processes each individually
 * by determining the depreciation horizon for each and extracting from the repository the
 * relevant lease-period instances. Then for each lease-period the system will then
 * calculate depreciation amount and persist that data as depreciation-entry
 */
package io.github.erp.internal.service.rou.batch;
