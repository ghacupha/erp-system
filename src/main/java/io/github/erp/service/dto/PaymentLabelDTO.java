package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PaymentLabel} entity.
 */
public class PaymentLabelDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    private String comments;

    private PaymentLabelDTO containingPaymentLabel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public PaymentLabelDTO getContainingPaymentLabel() {
        return containingPaymentLabel;
    }

    public void setContainingPaymentLabel(PaymentLabelDTO containingPaymentLabel) {
        this.containingPaymentLabel = containingPaymentLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentLabelDTO)) {
            return false;
        }

        PaymentLabelDTO paymentLabelDTO = (PaymentLabelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentLabelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentLabelDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", comments='" + getComments() + "'" +
            ", containingPaymentLabel=" + getContainingPaymentLabel() +
            "}";
    }
}
