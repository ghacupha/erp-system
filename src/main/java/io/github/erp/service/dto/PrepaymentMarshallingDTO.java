package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.5-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PrepaymentMarshalling} entity.
 */
public class PrepaymentMarshallingDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean inactive;

    private LocalDate amortizationCommencementDate;

    private Integer amortizationPeriods;

    private PrepaymentAccountDTO prepaymentAccount;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

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

    public LocalDate getAmortizationCommencementDate() {
        return amortizationCommencementDate;
    }

    public void setAmortizationCommencementDate(LocalDate amortizationCommencementDate) {
        this.amortizationCommencementDate = amortizationCommencementDate;
    }

    public Integer getAmortizationPeriods() {
        return amortizationPeriods;
    }

    public void setAmortizationPeriods(Integer amortizationPeriods) {
        this.amortizationPeriods = amortizationPeriods;
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
            ", amortizationCommencementDate='" + getAmortizationCommencementDate() + "'" +
            ", amortizationPeriods=" + getAmortizationPeriods() +
            ", prepaymentAccount=" + getPrepaymentAccount() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
