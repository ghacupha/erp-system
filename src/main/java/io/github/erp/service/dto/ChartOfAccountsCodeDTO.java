package io.github.erp.service.dto;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
 * A DTO for the {@link io.github.erp.domain.ChartOfAccountsCode} entity.
 */
public class ChartOfAccountsCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String chartOfAccountsCode;

    @NotNull
    private String chartOfAccountsClass;

    @Lob
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }

    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }

    public String getChartOfAccountsClass() {
        return chartOfAccountsClass;
    }

    public void setChartOfAccountsClass(String chartOfAccountsClass) {
        this.chartOfAccountsClass = chartOfAccountsClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChartOfAccountsCodeDTO)) {
            return false;
        }

        ChartOfAccountsCodeDTO chartOfAccountsCodeDTO = (ChartOfAccountsCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chartOfAccountsCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChartOfAccountsCodeDTO{" +
            "id=" + getId() +
            ", chartOfAccountsCode='" + getChartOfAccountsCode() + "'" +
            ", chartOfAccountsClass='" + getChartOfAccountsClass() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
