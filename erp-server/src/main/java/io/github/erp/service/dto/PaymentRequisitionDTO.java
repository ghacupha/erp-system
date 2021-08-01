package io.github.erp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.PaymentRequisition} entity.
 */
public class PaymentRequisitionDTO implements Serializable {

    private Long id;

    private String dealerName;

    private BigDecimal invoicedAmount;

    private BigDecimal disbursementCost;

    private BigDecimal vatableAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public BigDecimal getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimal getDisbursementCost() {
        return disbursementCost;
    }

    public void setDisbursementCost(BigDecimal disbursementCost) {
        this.disbursementCost = disbursementCost;
    }

    public BigDecimal getVatableAmount() {
        return vatableAmount;
    }

    public void setVatableAmount(BigDecimal vatableAmount) {
        this.vatableAmount = vatableAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentRequisitionDTO)) {
            return false;
        }

        PaymentRequisitionDTO paymentRequisitionDTO = (PaymentRequisitionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentRequisitionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentRequisitionDTO{" +
            "id=" + getId() +
            ", dealerName='" + getDealerName() + "'" +
            ", invoicedAmount=" + getInvoicedAmount() +
            ", disbursementCost=" + getDisbursementCost() +
            ", vatableAmount=" + getVatableAmount() +
            "}";
    }
}
