package io.github.erp.service.dto;

import io.github.erp.domain.enumeration.CurrencyTypes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SignedPayment} entity.
 */
public class SignedPaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private String transactionNumber;

    @NotNull
    private LocalDate transactionDate;

    @NotNull
    private CurrencyTypes transactionCurrency;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal transactionAmount;

    private String fileUploadToken;

    private String compilationToken;

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private DealerDTO dealer;

    private PaymentCategoryDTO paymentCategory;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private SignedPaymentDTO signedPaymentGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public CurrencyTypes getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(CurrencyTypes transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getFileUploadToken() {
        return fileUploadToken;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return compilationToken;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
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

    public PaymentCategoryDTO getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(PaymentCategoryDTO paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public SignedPaymentDTO getSignedPaymentGroup() {
        return signedPaymentGroup;
    }

    public void setSignedPaymentGroup(SignedPaymentDTO signedPaymentGroup) {
        this.signedPaymentGroup = signedPaymentGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignedPaymentDTO)) {
            return false;
        }

        SignedPaymentDTO signedPaymentDTO = (SignedPaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, signedPaymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignedPaymentDTO{" +
            "id=" + getId() +
            ", transactionNumber='" + getTransactionNumber() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionCurrency='" + getTransactionCurrency() + "'" +
            ", transactionAmount=" + getTransactionAmount() +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", paymentLabels=" + getPaymentLabels() +
            ", dealer=" + getDealer() +
            ", paymentCategory=" + getPaymentCategory() +
            ", placeholders=" + getPlaceholders() +
            ", signedPaymentGroup=" + getSignedPaymentGroup() +
            "}";
    }
}
