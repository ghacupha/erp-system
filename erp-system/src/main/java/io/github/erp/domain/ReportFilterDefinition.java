package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Value object describing a filter that can be applied when requesting a report.
 */
@Embeddable
public class ReportFilterDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Column(name = "filter_label", nullable = false)
    @Field(type = FieldType.Keyword)
    private String label;

    @NotBlank
    @Column(name = "query_parameter_key", nullable = false)
    @Field(type = FieldType.Keyword)
    private String queryParameterKey;

    @NotBlank
    @Column(name = "value_source", nullable = false)
    @Field(type = FieldType.Keyword)
    private String valueSource;

    @Column(name = "ui_hint", length = 1000)
    @Field(type = FieldType.Text)
    private String uiHint;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ReportFilterDefinition label(String label) {
        this.setLabel(label);
        return this;
    }

    public String getQueryParameterKey() {
        return queryParameterKey;
    }

    public void setQueryParameterKey(String queryParameterKey) {
        this.queryParameterKey = queryParameterKey;
    }

    public ReportFilterDefinition queryParameterKey(String queryParameterKey) {
        this.setQueryParameterKey(queryParameterKey);
        return this;
    }

    public String getValueSource() {
        return valueSource;
    }

    public void setValueSource(String valueSource) {
        this.valueSource = valueSource;
    }

    public ReportFilterDefinition valueSource(String valueSource) {
        this.setValueSource(valueSource);
        return this;
    }

    public String getUiHint() {
        return uiHint;
    }

    public void setUiHint(String uiHint) {
        this.uiHint = uiHint;
    }

    public ReportFilterDefinition uiHint(String uiHint) {
        this.setUiHint(uiHint);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportFilterDefinition)) {
            return false;
        }
        ReportFilterDefinition that = (ReportFilterDefinition) o;
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
        return "ReportFilterDefinition{" +
        "label='" + label + '\'' +
        ", queryParameterKey='" + queryParameterKey + '\'' +
        ", valueSource='" + valueSource + '\'' +
        ", uiHint='" + uiHint + '\'' +
        "}";
    }
}
