package io.github.erp.service.dto;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.time.LocalDate;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link io.github.erp.domain.PaymentCalculation} entity.
 */
public class PaymentCalculationDTO implements Serializable {
    
    private Long id;

    private String paymentNumber;

    private LocalDate paymentDate;

    private String paymentCategory;

    private BigDecimal paymentExpense;

    private BigDecimal withholdingVAT;

    private BigDecimal withholdingTax;

    private BigDecimal paymentAmount;

    
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

    public String getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public BigDecimal getPaymentExpense() {
        return paymentExpense;
    }

    public void setPaymentExpense(BigDecimal paymentExpense) {
        this.paymentExpense = paymentExpense;
    }

    public BigDecimal getWithholdingVAT() {
        return withholdingVAT;
    }

    public void setWithholdingVAT(BigDecimal withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentCalculationDTO)) {
            return false;
        }

        return id != null && id.equals(((PaymentCalculationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCalculationDTO{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentCategory='" + getPaymentCategory() + "'" +
            ", paymentExpense=" + getPaymentExpense() +
            ", withholdingVAT=" + getWithholdingVAT() +
            ", withholdingTax=" + getWithholdingTax() +
            ", paymentAmount=" + getPaymentAmount() +
            "}";
    }
}
