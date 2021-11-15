package io.github.erp.internal.batch.paymentLabel;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.batch.ListProcessor;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.PaymentLabelEVM;
import io.github.erp.service.dto.PaymentLabelDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * List processor implementation that also adds additional data to the output, i.e. the file-processing-token
 */
public class PaymentLabelPersistenceProcessor implements ListProcessor<PaymentLabelEVM, PaymentLabelDTO> {


    private final Mapping<PaymentLabelEVM, PaymentLabelDTO> mapping;
    private final String uploadToken;

    @org.jetbrains.annotations.Contract(pure = true)
    public PaymentLabelPersistenceProcessor(Mapping<PaymentLabelEVM, PaymentLabelDTO> mapping, String uploadToken) {
        this.mapping = mapping;
        this.uploadToken = uploadToken;
    }

    @Override
    public List<PaymentLabelDTO> process(@NonNull List<PaymentLabelEVM> evms) throws Exception {
        return evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(uploadToken)).collect(ImmutableList.toImmutableList());
    }
}
