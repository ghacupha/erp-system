package io.github.erp.internal.model;

import io.github.erp.internal.framework.batch.BatchEntity;
import io.github.erp.service.dto.DealerDTO;
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
public class DealerBEO implements BatchEntity {

    private Long id;

    @NotNull
    private String dealerName;

    private String taxNumber;

    private String postalAddress;

    private String physicalAddress;

    private String accountName;

    private String accountNumber;

    private String bankersName;

    private String bankersBranch;

    private String bankersSwiftCode;

    private String fileUploadToken;

    private String compilationToken;

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private DealerDTO dealerGroup;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    @Override
    public void setUploadToken(String uploadToken) {
        this.fileUploadToken = uploadToken;
    }

    @Override
    public String getUploadToken() {
        return this.fileUploadToken;
    }
}
