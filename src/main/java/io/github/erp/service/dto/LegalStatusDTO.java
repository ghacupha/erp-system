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
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LegalStatus} entity.
 */
public class LegalStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String legalStatusCode;

    @NotNull
    private String legalStatusType;

    @Lob
    private String legalStatusDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLegalStatusCode() {
        return legalStatusCode;
    }

    public void setLegalStatusCode(String legalStatusCode) {
        this.legalStatusCode = legalStatusCode;
    }

    public String getLegalStatusType() {
        return legalStatusType;
    }

    public void setLegalStatusType(String legalStatusType) {
        this.legalStatusType = legalStatusType;
    }

    public String getLegalStatusDescription() {
        return legalStatusDescription;
    }

    public void setLegalStatusDescription(String legalStatusDescription) {
        this.legalStatusDescription = legalStatusDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LegalStatusDTO)) {
            return false;
        }

        LegalStatusDTO legalStatusDTO = (LegalStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, legalStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LegalStatusDTO{" +
            "id=" + getId() +
            ", legalStatusCode='" + getLegalStatusCode() + "'" +
            ", legalStatusType='" + getLegalStatusType() + "'" +
            ", legalStatusDescription='" + getLegalStatusDescription() + "'" +
            "}";
    }
}
