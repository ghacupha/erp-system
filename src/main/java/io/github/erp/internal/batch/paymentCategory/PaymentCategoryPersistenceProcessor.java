package io.github.erp.internal.batch.paymentCategory;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.batch.ListProcessor;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.PaymentCategoryEVM;
import io.github.erp.service.dto.PaymentCategoryDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * List processor implementation that also adds additional data to the output, i.e. the file-processing-token
 */
public class PaymentCategoryPersistenceProcessor implements ListProcessor<PaymentCategoryEVM, PaymentCategoryDTO> {


    private final Mapping<PaymentCategoryEVM, PaymentCategoryDTO> mapping;
    private final String uploadToken;

    @org.jetbrains.annotations.Contract(pure = true)
    public PaymentCategoryPersistenceProcessor(Mapping<PaymentCategoryEVM, PaymentCategoryDTO> mapping, String uploadToken) {
        this.mapping = mapping;
        this.uploadToken = uploadToken;
    }

    @Override
    public List<PaymentCategoryDTO> process(@NonNull List<PaymentCategoryEVM> evms) throws Exception {
        return evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(uploadToken)).collect(ImmutableList.toImmutableList());
    }
}
