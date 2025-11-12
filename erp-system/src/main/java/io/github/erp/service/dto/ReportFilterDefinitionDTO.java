package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import javax.validation.constraints.NotBlank;

/**
 * DTO representation of {@link io.github.erp.domain.ReportFilterDefinition} for REST payloads.
 */
public class ReportFilterDefinitionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String label;

    @NotBlank
    private String queryParameterKey;

    @NotBlank
    private String valueSource;

    private String uiHint;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getQueryParameterKey() {
        return queryParameterKey;
    }

    public void setQueryParameterKey(String queryParameterKey) {
        this.queryParameterKey = queryParameterKey;
    }

    public String getValueSource() {
        return valueSource;
    }

    public void setValueSource(String valueSource) {
        this.valueSource = valueSource;
    }

    public String getUiHint() {
        return uiHint;
    }

    public void setUiHint(String uiHint) {
        this.uiHint = uiHint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportFilterDefinitionDTO)) {
            return false;
        }
        ReportFilterDefinitionDTO that = (ReportFilterDefinitionDTO) o;
        return (
            Objects.equals(label, that.label) &&
            Objects.equals(queryParameterKey, that.queryParameterKey) &&
            Objects.equals(valueSource, that.valueSource) &&
            Objects.equals(uiHint, that.uiHint)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, queryParameterKey, valueSource, uiHint);
    }

    @Override
    public String toString() {
        return "ReportFilterDefinitionDTO{" +
        "label='" + label + '\'' +
        ", queryParameterKey='" + queryParameterKey + '\'' +
        ", valueSource='" + valueSource + '\'' +
        ", uiHint='" + uiHint + '\'' +
        "}";
    }
}
