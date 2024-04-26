package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.MonthlyPrepaymentOutstandingReportItem} entity.
 */
public class MonthlyPrepaymentOutstandingReportItemDTO implements Serializable {

    private Long id;

    private LocalDate fiscalMonthEndDate;

    private BigDecimal totalPrepaymentAmount;

    private BigDecimal totalAmortisedAmount;

    private BigDecimal totalOutstandingAmount;

    private Integer numberOfPrepaymentAccounts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFiscalMonthEndDate() {
        return fiscalMonthEndDate;
    }

    public void setFiscalMonthEndDate(LocalDate fiscalMonthEndDate) {
        this.fiscalMonthEndDate = fiscalMonthEndDate;
    }

    public BigDecimal getTotalPrepaymentAmount() {
        return totalPrepaymentAmount;
    }

    public void setTotalPrepaymentAmount(BigDecimal totalPrepaymentAmount) {
        this.totalPrepaymentAmount = totalPrepaymentAmount;
    }

    public BigDecimal getTotalAmortisedAmount() {
        return totalAmortisedAmount;
    }

    public void setTotalAmortisedAmount(BigDecimal totalAmortisedAmount) {
        this.totalAmortisedAmount = totalAmortisedAmount;
    }

    public BigDecimal getTotalOutstandingAmount() {
        return totalOutstandingAmount;
    }

    public void setTotalOutstandingAmount(BigDecimal totalOutstandingAmount) {
        this.totalOutstandingAmount = totalOutstandingAmount;
    }

    public Integer getNumberOfPrepaymentAccounts() {
        return numberOfPrepaymentAccounts;
    }

    public void setNumberOfPrepaymentAccounts(Integer numberOfPrepaymentAccounts) {
        this.numberOfPrepaymentAccounts = numberOfPrepaymentAccounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonthlyPrepaymentOutstandingReportItemDTO)) {
            return false;
        }

        MonthlyPrepaymentOutstandingReportItemDTO monthlyPrepaymentOutstandingReportItemDTO = (MonthlyPrepaymentOutstandingReportItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monthlyPrepaymentOutstandingReportItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonthlyPrepaymentOutstandingReportItemDTO{" +
            "id=" + getId() +
            ", fiscalMonthEndDate='" + getFiscalMonthEndDate() + "'" +
            ", totalPrepaymentAmount=" + getTotalPrepaymentAmount() +
            ", totalAmortisedAmount=" + getTotalAmortisedAmount() +
            ", totalOutstandingAmount=" + getTotalOutstandingAmount() +
            ", numberOfPrepaymentAccounts=" + getNumberOfPrepaymentAccounts() +
            "}";
    }
}
