package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.IsicEconomicActivity} entity.
 */
public class IsicEconomicActivityDTO implements Serializable {

    private Long id;

    @NotNull
    private String businessEconomicActivityCode;

    @NotNull
    private String section;

    @NotNull
    private String sectionLabel;

    @NotNull
    private String division;

    @NotNull
    private String divisionLabel;

    private String groupCode;

    @NotNull
    private String groupLabel;

    @NotNull
    private String classCode;

    private String businessEconomicActivityType;

    @Lob
    private String businessEconomicActivityTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessEconomicActivityCode() {
        return businessEconomicActivityCode;
    }

    public void setBusinessEconomicActivityCode(String businessEconomicActivityCode) {
        this.businessEconomicActivityCode = businessEconomicActivityCode;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDivisionLabel() {
        return divisionLabel;
    }

    public void setDivisionLabel(String divisionLabel) {
        this.divisionLabel = divisionLabel;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getBusinessEconomicActivityType() {
        return businessEconomicActivityType;
    }

    public void setBusinessEconomicActivityType(String businessEconomicActivityType) {
        this.businessEconomicActivityType = businessEconomicActivityType;
    }

    public String getBusinessEconomicActivityTypeDescription() {
        return businessEconomicActivityTypeDescription;
    }

    public void setBusinessEconomicActivityTypeDescription(String businessEconomicActivityTypeDescription) {
        this.businessEconomicActivityTypeDescription = businessEconomicActivityTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IsicEconomicActivityDTO)) {
            return false;
        }

        IsicEconomicActivityDTO isicEconomicActivityDTO = (IsicEconomicActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, isicEconomicActivityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsicEconomicActivityDTO{" +
            "id=" + getId() +
            ", businessEconomicActivityCode='" + getBusinessEconomicActivityCode() + "'" +
            ", section='" + getSection() + "'" +
            ", sectionLabel='" + getSectionLabel() + "'" +
            ", division='" + getDivision() + "'" +
            ", divisionLabel='" + getDivisionLabel() + "'" +
            ", groupCode='" + getGroupCode() + "'" +
            ", groupLabel='" + getGroupLabel() + "'" +
            ", classCode='" + getClassCode() + "'" +
            ", businessEconomicActivityType='" + getBusinessEconomicActivityType() + "'" +
            ", businessEconomicActivityTypeDescription='" + getBusinessEconomicActivityTypeDescription() + "'" +
            "}";
    }
}
