package io.github.erp.internal.batch.invoice;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.batch.ListProcessor;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.InvoiceEVM;
import io.github.erp.service.dto.InvoiceDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * List processor implementation that also adds additional data to the output, i.e. the file-processing-token
 */
public class InvoicePersistenceProcessor implements ListProcessor<InvoiceEVM, InvoiceDTO> {


    private final Mapping<InvoiceEVM, InvoiceDTO> mapping;
    private final String uploadToken;

    @org.jetbrains.annotations.Contract(pure = true)
    public InvoicePersistenceProcessor(Mapping<InvoiceEVM, InvoiceDTO> mapping, String uploadToken) {
        this.mapping = mapping;
        this.uploadToken = uploadToken;
    }

    @Override
    public List<InvoiceDTO> process(@NonNull List<InvoiceEVM> evms) throws Exception {
        return evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(uploadToken)).collect(ImmutableList.toImmutableList());
    }
}
