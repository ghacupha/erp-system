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
