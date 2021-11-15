package io.github.erp.internal.batch.signedPayment;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.batch.ListProcessor;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.SignedPaymentEVM;
import io.github.erp.service.dto.SignedPaymentDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * List processor implementation that also adds additional data to the output, i.e. the file-processing-token
 */
public class SignedPaymentPersistenceProcessor implements ListProcessor<SignedPaymentEVM, SignedPaymentDTO> {


    private final Mapping<SignedPaymentEVM, SignedPaymentDTO> mapping;
    private final String uploadToken;

    @org.jetbrains.annotations.Contract(pure = true)
    public SignedPaymentPersistenceProcessor(Mapping<SignedPaymentEVM, SignedPaymentDTO> mapping, String uploadToken) {
        this.mapping = mapping;
        this.uploadToken = uploadToken;
    }

    @Override
    public List<SignedPaymentDTO> process(@NonNull List<SignedPaymentEVM> evms) throws Exception {
        return evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(uploadToken)).collect(ImmutableList.toImmutableList());
    }
}
