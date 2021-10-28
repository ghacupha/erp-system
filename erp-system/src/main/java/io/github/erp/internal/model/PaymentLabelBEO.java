package io.github.erp.internal.model;

import io.github.erp.internal.framework.batch.BatchEntity;
import io.github.erp.service.dto.PaymentLabelDTO;
import io.github.erp.service.dto.PlaceholderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLabelBEO implements BatchEntity {

    private Long id;

    @NotNull
    private String description;

    private String comments;

    private PaymentLabelDTO containingPaymentLabel;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private String fileUploadToken;

    private String compilationToken;

    @Override
    public void setUploadToken(String uploadToken) {
        this.fileUploadToken = uploadToken;
    }

    @Override
    public String getUploadToken() {
        return this.fileUploadToken;
    }
}
