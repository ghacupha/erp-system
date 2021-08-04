package io.github.erp.service.dto;

import io.github.erp.domain.enumeration.CurrencyTypes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.Payment} entity.
 */
public class PaymentDTO implements Serializable {

    private Long id;

    private String paymentNumber;

    private LocalDate paymentDate;

    private BigDecimal paymentAmount;

    private String description;

    @NotNull
    private CurrencyTypes currency;

    @NotNull
    @DecimalMin(value = "1.00")
    private Double conversionRate;

    private TaxRuleDTO taxRule;

    private PaymentCategoryDTO paymentCategory;

    private PaymentCalculationDTO paymentCalculation;

    private PaymentRequisitionDTO paymentRequisition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CurrencyTypes getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTypes currency) {
        this.currency = currency;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public TaxRuleDTO getTaxRule() {
        return taxRule;
    }

    public void setTaxRule(TaxRuleDTO taxRule) {
        this.taxRule = taxRule;
    }

    public PaymentCategoryDTO getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(PaymentCategoryDTO paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public PaymentCalculationDTO getPaymentCalculation() {
        return paymentCalculation;
    }

    public void setPaymentCalculation(PaymentCalculationDTO paymentCalculation) {
        this.paymentCalculation = paymentCalculation;
    }

    public PaymentRequisitionDTO getPaymentRequisition() {
        return paymentRequisition;
    }

    public void setPaymentRequisition(PaymentRequisitionDTO paymentRequisition) {
        this.paymentRequisition = paymentRequisition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", description='" + getDescription() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", conversionRate=" + getConversionRate() +
            ", taxRule=" + getTaxRule() +
            ", paymentCategory=" + getPaymentCategory() +
            ", paymentCalculation=" + getPaymentCalculation() +
            ", paymentRequisition=" + getPaymentRequisition() +
            "}";
    }
}
