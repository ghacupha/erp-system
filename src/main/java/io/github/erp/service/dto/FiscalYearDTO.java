package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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

import io.github.erp.domain.enumeration.FiscalYearStatusType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.FiscalYear} entity.
 */
public class FiscalYearDTO implements Serializable {

    private Long id;

    @NotNull
    private String fiscalYearCode;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private FiscalYearStatusType fiscalYearStatus;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> universallyUniqueMappings = new HashSet<>();

    private ApplicationUserDTO createdBy;

    private ApplicationUserDTO lastUpdatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFiscalYearCode() {
        return fiscalYearCode;
    }

    public void setFiscalYearCode(String fiscalYearCode) {
        this.fiscalYearCode = fiscalYearCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public FiscalYearStatusType getFiscalYearStatus() {
        return fiscalYearStatus;
    }

    public void setFiscalYearStatus(FiscalYearStatusType fiscalYearStatus) {
        this.fiscalYearStatus = fiscalYearStatus;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<UniversallyUniqueMappingDTO> getUniversallyUniqueMappings() {
        return universallyUniqueMappings;
    }

    public void setUniversallyUniqueMappings(Set<UniversallyUniqueMappingDTO> universallyUniqueMappings) {
        this.universallyUniqueMappings = universallyUniqueMappings;
    }

    public ApplicationUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public ApplicationUserDTO getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(ApplicationUserDTO lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiscalYearDTO)) {
            return false;
        }

        FiscalYearDTO fiscalYearDTO = (FiscalYearDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fiscalYearDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalYearDTO{" +
            "id=" + getId() +
            ", fiscalYearCode='" + getFiscalYearCode() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", fiscalYearStatus='" + getFiscalYearStatus() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", universallyUniqueMappings=" + getUniversallyUniqueMappings() +
            ", createdBy=" + getCreatedBy() +
            ", lastUpdatedBy=" + getLastUpdatedBy() +
            "}";
    }
}
