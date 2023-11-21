package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.CardFraudInformation} entity. This class is used
 * in {@link io.github.erp.web.rest.CardFraudInformationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /card-fraud-informations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CardFraudInformationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private IntegerFilter totalNumberOfFraudIncidents;

    private IntegerFilter valueOfFraudIncedentsInLCY;

    private Boolean distinct;

    public CardFraudInformationCriteria() {}

    public CardFraudInformationCriteria(CardFraudInformationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.totalNumberOfFraudIncidents = other.totalNumberOfFraudIncidents == null ? null : other.totalNumberOfFraudIncidents.copy();
        this.valueOfFraudIncedentsInLCY = other.valueOfFraudIncedentsInLCY == null ? null : other.valueOfFraudIncedentsInLCY.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CardFraudInformationCriteria copy() {
        return new CardFraudInformationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getReportingDate() {
        return reportingDate;
    }

    public LocalDateFilter reportingDate() {
        if (reportingDate == null) {
            reportingDate = new LocalDateFilter();
        }
        return reportingDate;
    }

    public void setReportingDate(LocalDateFilter reportingDate) {
        this.reportingDate = reportingDate;
    }

    public IntegerFilter getTotalNumberOfFraudIncidents() {
        return totalNumberOfFraudIncidents;
    }

    public IntegerFilter totalNumberOfFraudIncidents() {
        if (totalNumberOfFraudIncidents == null) {
            totalNumberOfFraudIncidents = new IntegerFilter();
        }
        return totalNumberOfFraudIncidents;
    }

    public void setTotalNumberOfFraudIncidents(IntegerFilter totalNumberOfFraudIncidents) {
        this.totalNumberOfFraudIncidents = totalNumberOfFraudIncidents;
    }

    public IntegerFilter getValueOfFraudIncedentsInLCY() {
        return valueOfFraudIncedentsInLCY;
    }

    public IntegerFilter valueOfFraudIncedentsInLCY() {
        if (valueOfFraudIncedentsInLCY == null) {
            valueOfFraudIncedentsInLCY = new IntegerFilter();
        }
        return valueOfFraudIncedentsInLCY;
    }

    public void setValueOfFraudIncedentsInLCY(IntegerFilter valueOfFraudIncedentsInLCY) {
        this.valueOfFraudIncedentsInLCY = valueOfFraudIncedentsInLCY;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CardFraudInformationCriteria that = (CardFraudInformationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(totalNumberOfFraudIncidents, that.totalNumberOfFraudIncidents) &&
            Objects.equals(valueOfFraudIncedentsInLCY, that.valueOfFraudIncedentsInLCY) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportingDate, totalNumberOfFraudIncidents, valueOfFraudIncedentsInLCY, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardFraudInformationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (totalNumberOfFraudIncidents != null ? "totalNumberOfFraudIncidents=" + totalNumberOfFraudIncidents + ", " : "") +
            (valueOfFraudIncedentsInLCY != null ? "valueOfFraudIncedentsInLCY=" + valueOfFraudIncedentsInLCY + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
