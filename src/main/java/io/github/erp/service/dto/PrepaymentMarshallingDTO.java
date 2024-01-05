package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PrepaymentMarshalling} entity.
 */
public class PrepaymentMarshallingDTO implements Serializable {

    private Long id;

    private Boolean inactive;

    private Integer amortizationPeriods;

    private Boolean processed;

    private UUID compilationToken;

    private PrepaymentAccountDTO prepaymentAccount;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private FiscalMonthDTO firstFiscalMonth;

    private FiscalMonthDTO lastFiscalMonth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Integer getAmortizationPeriods() {
        return amortizationPeriods;
    }

    public void setAmortizationPeriods(Integer amortizationPeriods) {
        this.amortizationPeriods = amortizationPeriods;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public UUID getCompilationToken() {
        return compilationToken;
    }

    public void setCompilationToken(UUID compilationToken) {
        this.compilationToken = compilationToken;
    }

    public PrepaymentAccountDTO getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public void setPrepaymentAccount(PrepaymentAccountDTO prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public FiscalMonthDTO getFirstFiscalMonth() {
        return firstFiscalMonth;
    }

    public void setFirstFiscalMonth(FiscalMonthDTO firstFiscalMonth) {
        this.firstFiscalMonth = firstFiscalMonth;
    }

    public FiscalMonthDTO getLastFiscalMonth() {
        return lastFiscalMonth;
    }

    public void setLastFiscalMonth(FiscalMonthDTO lastFiscalMonth) {
        this.lastFiscalMonth = lastFiscalMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentMarshallingDTO)) {
            return false;
        }

        PrepaymentMarshallingDTO prepaymentMarshallingDTO = (PrepaymentMarshallingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prepaymentMarshallingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentMarshallingDTO{" +
            "id=" + getId() +
            ", inactive='" + getInactive() + "'" +
            ", amortizationPeriods=" + getAmortizationPeriods() +
            ", processed='" + getProcessed() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", prepaymentAccount=" + getPrepaymentAccount() +
            ", placeholders=" + getPlaceholders() +
            ", firstFiscalMonth=" + getFirstFiscalMonth() +
            ", lastFiscalMonth=" + getLastFiscalMonth() +
            "}";
    }
}
