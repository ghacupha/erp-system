package io.github.erp.erp.leases.payments.upload.batch;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LeasePaymentCsvRow {
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;
    private Boolean active;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
