package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
