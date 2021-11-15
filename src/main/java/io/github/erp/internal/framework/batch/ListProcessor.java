package io.github.erp.internal.framework.batch;

import org.springframework.batch.item.ItemProcessor;

import java.util.List;

/**
 * This is a processor mostly applicable in batch processes that takes a list of one form of data
 * and responds with another
 *
 * @param <E> Initial form of data
 * @param <D> Output form of data
 */
public interface ListProcessor<E, D> extends ItemProcessor<List<E>, List<D>> {
}
