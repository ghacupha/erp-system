package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.EmploymentTerms} entity.
 */
public class EmploymentTermsDTO implements Serializable {

    private Long id;

    @NotNull
    private String employmentTermsCode;

    @NotNull
    private String employmentTermsStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmploymentTermsCode() {
        return employmentTermsCode;
    }

    public void setEmploymentTermsCode(String employmentTermsCode) {
        this.employmentTermsCode = employmentTermsCode;
    }

    public String getEmploymentTermsStatus() {
        return employmentTermsStatus;
    }

    public void setEmploymentTermsStatus(String employmentTermsStatus) {
        this.employmentTermsStatus = employmentTermsStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmploymentTermsDTO)) {
            return false;
        }

        EmploymentTermsDTO employmentTermsDTO = (EmploymentTermsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employmentTermsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmploymentTermsDTO{" +
            "id=" + getId() +
            ", employmentTermsCode='" + getEmploymentTermsCode() + "'" +
            ", employmentTermsStatus='" + getEmploymentTermsStatus() + "'" +
            "}";
    }
}
