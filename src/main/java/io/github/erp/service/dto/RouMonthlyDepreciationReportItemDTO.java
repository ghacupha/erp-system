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
 * A DTO for the {@link io.github.erp.domain.RouMonthlyDepreciationReportItem} entity.
 */
public class RouMonthlyDepreciationReportItemDTO implements Serializable {

    private Long id;

    private LocalDate fiscalMonthStartDate;

    private LocalDate fiscalMonthEndDate;

    private BigDecimal totalDepreciationAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFiscalMonthStartDate() {
        return fiscalMonthStartDate;
    }

    public void setFiscalMonthStartDate(LocalDate fiscalMonthStartDate) {
        this.fiscalMonthStartDate = fiscalMonthStartDate;
    }

    public LocalDate getFiscalMonthEndDate() {
        return fiscalMonthEndDate;
    }

    public void setFiscalMonthEndDate(LocalDate fiscalMonthEndDate) {
        this.fiscalMonthEndDate = fiscalMonthEndDate;
    }

    public BigDecimal getTotalDepreciationAmount() {
        return totalDepreciationAmount;
    }

    public void setTotalDepreciationAmount(BigDecimal totalDepreciationAmount) {
        this.totalDepreciationAmount = totalDepreciationAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouMonthlyDepreciationReportItemDTO)) {
            return false;
        }

        RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO = (RouMonthlyDepreciationReportItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rouMonthlyDepreciationReportItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouMonthlyDepreciationReportItemDTO{" +
            "id=" + getId() +
            ", fiscalMonthStartDate='" + getFiscalMonthStartDate() + "'" +
            ", fiscalMonthEndDate='" + getFiscalMonthEndDate() + "'" +
            ", totalDepreciationAmount=" + getTotalDepreciationAmount() +
            "}";
    }
}
