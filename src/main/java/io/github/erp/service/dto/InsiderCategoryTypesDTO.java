package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
 * A DTO for the {@link io.github.erp.domain.InsiderCategoryTypes} entity.
 */
public class InsiderCategoryTypesDTO implements Serializable {

    private Long id;

    @NotNull
    private String insiderCategoryTypeCode;

    @NotNull
    private String insiderCategoryTypeDetail;

    @Lob
    private String insiderCategoryDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInsiderCategoryTypeCode() {
        return insiderCategoryTypeCode;
    }

    public void setInsiderCategoryTypeCode(String insiderCategoryTypeCode) {
        this.insiderCategoryTypeCode = insiderCategoryTypeCode;
    }

    public String getInsiderCategoryTypeDetail() {
        return insiderCategoryTypeDetail;
    }

    public void setInsiderCategoryTypeDetail(String insiderCategoryTypeDetail) {
        this.insiderCategoryTypeDetail = insiderCategoryTypeDetail;
    }

    public String getInsiderCategoryDescription() {
        return insiderCategoryDescription;
    }

    public void setInsiderCategoryDescription(String insiderCategoryDescription) {
        this.insiderCategoryDescription = insiderCategoryDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsiderCategoryTypesDTO)) {
            return false;
        }

        InsiderCategoryTypesDTO insiderCategoryTypesDTO = (InsiderCategoryTypesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, insiderCategoryTypesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsiderCategoryTypesDTO{" +
            "id=" + getId() +
            ", insiderCategoryTypeCode='" + getInsiderCategoryTypeCode() + "'" +
            ", insiderCategoryTypeDetail='" + getInsiderCategoryTypeDetail() + "'" +
            ", insiderCategoryDescription='" + getInsiderCategoryDescription() + "'" +
            "}";
    }
}
