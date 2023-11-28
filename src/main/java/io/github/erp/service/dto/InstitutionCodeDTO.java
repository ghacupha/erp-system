package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
 * A DTO for the {@link io.github.erp.domain.InstitutionCode} entity.
 */
public class InstitutionCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String institutionCode;

    @NotNull
    private String institutionName;

    private String shortName;

    private String category;

    private String institutionCategory;

    private String institutionOwnership;

    private LocalDate dateLicensed;

    private String institutionStatus;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstitutionCategory() {
        return institutionCategory;
    }

    public void setInstitutionCategory(String institutionCategory) {
        this.institutionCategory = institutionCategory;
    }

    public String getInstitutionOwnership() {
        return institutionOwnership;
    }

    public void setInstitutionOwnership(String institutionOwnership) {
        this.institutionOwnership = institutionOwnership;
    }

    public LocalDate getDateLicensed() {
        return dateLicensed;
    }

    public void setDateLicensed(LocalDate dateLicensed) {
        this.dateLicensed = dateLicensed;
    }

    public String getInstitutionStatus() {
        return institutionStatus;
    }

    public void setInstitutionStatus(String institutionStatus) {
        this.institutionStatus = institutionStatus;
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
        if (!(o instanceof InstitutionCodeDTO)) {
            return false;
        }

        InstitutionCodeDTO institutionCodeDTO = (InstitutionCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, institutionCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstitutionCodeDTO{" +
            "id=" + getId() +
            ", institutionCode='" + getInstitutionCode() + "'" +
            ", institutionName='" + getInstitutionName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", category='" + getCategory() + "'" +
            ", institutionCategory='" + getInstitutionCategory() + "'" +
            ", institutionOwnership='" + getInstitutionOwnership() + "'" +
            ", dateLicensed='" + getDateLicensed() + "'" +
            ", institutionStatus='" + getInstitutionStatus() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
