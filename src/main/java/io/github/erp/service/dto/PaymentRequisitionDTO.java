package io.github.erp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.github.erp.domain.PaymentRequisition} entity.
 */
public class PaymentRequisitionDTO implements Serializable {

    private Long id;

    private BigDecimal invoicedAmount;

    private BigDecimal disbursementCost;

    private BigDecimal vatableAmount;

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private DealerDTO dealer;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<PaymentLabelDTO> getPaymentLabels() {
        return paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabelDTO> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public DealerDTO getDealer() {
        return dealer;
    }

    public void setDealer(DealerDTO dealer) {
        this.dealer = dealer;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
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
            ", invoicedAmount=" + getInvoicedAmount() +
            ", disbursementCost=" + getDisbursementCost() +
            ", vatableAmount=" + getVatableAmount() +
            ", paymentLabels=" + getPaymentLabels() +
            ", dealer=" + getDealer() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
