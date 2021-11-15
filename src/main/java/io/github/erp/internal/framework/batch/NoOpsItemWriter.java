package io.github.erp.internal.framework.batch;

import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;

import java.util.List;

public class NoOpsItemWriter<T> implements ItemWriter<List<T>> {

    @Override
    public void write(@NonNull List<? extends List<T>> items) throws Exception {
    }
}
