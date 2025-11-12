package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import java.util.Objects;

/**
 * DTO representing the aggregated lease liability maturity bucket.
 */
public class LeaseLiabilityMaturitySummaryDTO implements Serializable {

    private String maturityLabel;

    private BigDecimal leasePrincipal;

    private BigDecimal interestPayable;

    private BigDecimal total;

    public String getMaturityLabel() {
        return maturityLabel;
    }

    public void setMaturityLabel(String maturityLabel) {
        this.maturityLabel = maturityLabel;
    }

    public BigDecimal getLeasePrincipal() {
        return leasePrincipal;
    }

    public void setLeasePrincipal(BigDecimal leasePrincipal) {
        this.leasePrincipal = leasePrincipal;
    }

    public BigDecimal getInterestPayable() {
        return interestPayable;
    }

    public void setInterestPayable(BigDecimal interestPayable) {
        this.interestPayable = interestPayable;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityMaturitySummaryDTO)) {
            return false;
        }
        LeaseLiabilityMaturitySummaryDTO that = (LeaseLiabilityMaturitySummaryDTO) o;
        return Objects.equals(maturityLabel, that.maturityLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maturityLabel);
    }

    @Override
    public String toString() {
        return "LeaseLiabilityMaturitySummaryDTO{" +
            "maturityLabel='" + getMaturityLabel() + '\'' +
            ", leasePrincipal=" + getLeasePrincipal() +
            ", interestPayable=" + getInterestPayable() +
            ", total=" + getTotal() +
            '}';
    }
}
