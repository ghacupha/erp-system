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
 * A DTO for the {@link io.github.erp.domain.Payment} entity.
 */
public class PaymentDTO implements Serializable {
    
    private Long id;

    private String paymentNumber;

    private LocalDate paymentDate;

    private BigDecimal paymentAmount;

    private String dealerName;

    private String paymentCategory;


    private Long paymentCalculationId;

    private Long paymentRequisitionId;

    private Long taxRuleId;
    
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

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(String paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public Long getPaymentCalculationId() {
        return paymentCalculationId;
    }

    public void setPaymentCalculationId(Long paymentCalculationId) {
        this.paymentCalculationId = paymentCalculationId;
    }

    public Long getPaymentRequisitionId() {
        return paymentRequisitionId;
    }

    public void setPaymentRequisitionId(Long paymentRequisitionId) {
        this.paymentRequisitionId = paymentRequisitionId;
    }

    public Long getTaxRuleId() {
        return taxRuleId;
    }

    public void setTaxRuleId(Long taxRuleId) {
        this.taxRuleId = taxRuleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        return id != null && id.equals(((PaymentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", dealerName='" + getDealerName() + "'" +
            ", paymentCategory='" + getPaymentCategory() + "'" +
            ", paymentCalculationId=" + getPaymentCalculationId() +
            ", paymentRequisitionId=" + getPaymentRequisitionId() +
            ", taxRuleId=" + getTaxRuleId() +
            "}";
    }
}
