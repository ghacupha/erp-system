package io.github.erp.internal.batch.payment;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.batch.ListProcessor;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.PaymentEVM;
import io.github.erp.service.dto.PaymentDTO;
import org.springframework.lang.NonNull;

import java.util.List;

public class PaymentPersistenceProcessor implements ListProcessor<PaymentEVM, PaymentDTO> {


    private final Mapping<PaymentEVM, PaymentDTO> mapping;
    private final String uploadToken;

    @org.jetbrains.annotations.Contract(pure = true)
    public PaymentPersistenceProcessor(Mapping<PaymentEVM, PaymentDTO> mapping, String uploadToken) {
        this.mapping = mapping;
        this.uploadToken = uploadToken;
    }

    @Override
    public List<PaymentDTO> process(@NonNull List<PaymentEVM> evms) throws Exception {
        return evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(uploadToken)).collect(ImmutableList.toImmutableList());
    }
}
