package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.recurrenceFrequency;
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
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.AmortizationRecurrence} entity. This class is used
 * in {@link io.github.erp.web.rest.AmortizationRecurrenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /amortization-recurrences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AmortizationRecurrenceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering recurrenceFrequency
     */
    public static class recurrenceFrequencyFilter extends Filter<recurrenceFrequency> {

        public recurrenceFrequencyFilter() {}

        public recurrenceFrequencyFilter(recurrenceFrequencyFilter filter) {
            super(filter);
        }

        @Override
        public recurrenceFrequencyFilter copy() {
            return new recurrenceFrequencyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter firstAmortizationDate;

    private recurrenceFrequencyFilter amortizationFrequency;

    private IntegerFilter numberOfRecurrences;

    private StringFilter particulars;

    private BooleanFilter isActive;

    private BooleanFilter isOverWritten;

    private ZonedDateTimeFilter timeOfInstallation;

    private UUIDFilter recurrenceGuid;

    private UUIDFilter prepaymentAccountGuid;

    private LongFilter placeholderId;

    private LongFilter parametersId;

    private LongFilter applicationParametersId;

    private LongFilter depreciationMethodId;

    private LongFilter prepaymentAccountId;

    private Boolean distinct;

    public AmortizationRecurrenceCriteria() {}

    public AmortizationRecurrenceCriteria(AmortizationRecurrenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstAmortizationDate = other.firstAmortizationDate == null ? null : other.firstAmortizationDate.copy();
        this.amortizationFrequency = other.amortizationFrequency == null ? null : other.amortizationFrequency.copy();
        this.numberOfRecurrences = other.numberOfRecurrences == null ? null : other.numberOfRecurrences.copy();
        this.particulars = other.particulars == null ? null : other.particulars.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.isOverWritten = other.isOverWritten == null ? null : other.isOverWritten.copy();
        this.timeOfInstallation = other.timeOfInstallation == null ? null : other.timeOfInstallation.copy();
        this.recurrenceGuid = other.recurrenceGuid == null ? null : other.recurrenceGuid.copy();
        this.prepaymentAccountGuid = other.prepaymentAccountGuid == null ? null : other.prepaymentAccountGuid.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.parametersId = other.parametersId == null ? null : other.parametersId.copy();
        this.applicationParametersId = other.applicationParametersId == null ? null : other.applicationParametersId.copy();
        this.depreciationMethodId = other.depreciationMethodId == null ? null : other.depreciationMethodId.copy();
        this.prepaymentAccountId = other.prepaymentAccountId == null ? null : other.prepaymentAccountId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AmortizationRecurrenceCriteria copy() {
        return new AmortizationRecurrenceCriteria(this);
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

    public LocalDateFilter getFirstAmortizationDate() {
        return firstAmortizationDate;
    }

    public LocalDateFilter firstAmortizationDate() {
        if (firstAmortizationDate == null) {
            firstAmortizationDate = new LocalDateFilter();
        }
        return firstAmortizationDate;
    }

    public void setFirstAmortizationDate(LocalDateFilter firstAmortizationDate) {
        this.firstAmortizationDate = firstAmortizationDate;
    }

    public recurrenceFrequencyFilter getAmortizationFrequency() {
        return amortizationFrequency;
    }

    public recurrenceFrequencyFilter amortizationFrequency() {
        if (amortizationFrequency == null) {
            amortizationFrequency = new recurrenceFrequencyFilter();
        }
        return amortizationFrequency;
    }

    public void setAmortizationFrequency(recurrenceFrequencyFilter amortizationFrequency) {
        this.amortizationFrequency = amortizationFrequency;
    }

    public IntegerFilter getNumberOfRecurrences() {
        return numberOfRecurrences;
    }

    public IntegerFilter numberOfRecurrences() {
        if (numberOfRecurrences == null) {
            numberOfRecurrences = new IntegerFilter();
        }
        return numberOfRecurrences;
    }

    public void setNumberOfRecurrences(IntegerFilter numberOfRecurrences) {
        this.numberOfRecurrences = numberOfRecurrences;
    }

    public StringFilter getParticulars() {
        return particulars;
    }

    public StringFilter particulars() {
        if (particulars == null) {
            particulars = new StringFilter();
        }
        return particulars;
    }

    public void setParticulars(StringFilter particulars) {
        this.particulars = particulars;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public BooleanFilter getIsOverWritten() {
        return isOverWritten;
    }

    public BooleanFilter isOverWritten() {
        if (isOverWritten == null) {
            isOverWritten = new BooleanFilter();
        }
        return isOverWritten;
    }

    public void setIsOverWritten(BooleanFilter isOverWritten) {
        this.isOverWritten = isOverWritten;
    }

    public ZonedDateTimeFilter getTimeOfInstallation() {
        return timeOfInstallation;
    }

    public ZonedDateTimeFilter timeOfInstallation() {
        if (timeOfInstallation == null) {
            timeOfInstallation = new ZonedDateTimeFilter();
        }
        return timeOfInstallation;
    }

    public void setTimeOfInstallation(ZonedDateTimeFilter timeOfInstallation) {
        this.timeOfInstallation = timeOfInstallation;
    }

    public UUIDFilter getRecurrenceGuid() {
        return recurrenceGuid;
    }

    public UUIDFilter recurrenceGuid() {
        if (recurrenceGuid == null) {
            recurrenceGuid = new UUIDFilter();
        }
        return recurrenceGuid;
    }

    public void setRecurrenceGuid(UUIDFilter recurrenceGuid) {
        this.recurrenceGuid = recurrenceGuid;
    }

    public UUIDFilter getPrepaymentAccountGuid() {
        return prepaymentAccountGuid;
    }

    public UUIDFilter prepaymentAccountGuid() {
        if (prepaymentAccountGuid == null) {
            prepaymentAccountGuid = new UUIDFilter();
        }
        return prepaymentAccountGuid;
    }

    public void setPrepaymentAccountGuid(UUIDFilter prepaymentAccountGuid) {
        this.prepaymentAccountGuid = prepaymentAccountGuid;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
    }

    public LongFilter getParametersId() {
        return parametersId;
    }

    public LongFilter parametersId() {
        if (parametersId == null) {
            parametersId = new LongFilter();
        }
        return parametersId;
    }

    public void setParametersId(LongFilter parametersId) {
        this.parametersId = parametersId;
    }

    public LongFilter getApplicationParametersId() {
        return applicationParametersId;
    }

    public LongFilter applicationParametersId() {
        if (applicationParametersId == null) {
            applicationParametersId = new LongFilter();
        }
        return applicationParametersId;
    }

    public void setApplicationParametersId(LongFilter applicationParametersId) {
        this.applicationParametersId = applicationParametersId;
    }

    public LongFilter getDepreciationMethodId() {
        return depreciationMethodId;
    }

    public LongFilter depreciationMethodId() {
        if (depreciationMethodId == null) {
            depreciationMethodId = new LongFilter();
        }
        return depreciationMethodId;
    }

    public void setDepreciationMethodId(LongFilter depreciationMethodId) {
        this.depreciationMethodId = depreciationMethodId;
    }

    public LongFilter getPrepaymentAccountId() {
        return prepaymentAccountId;
    }

    public LongFilter prepaymentAccountId() {
        if (prepaymentAccountId == null) {
            prepaymentAccountId = new LongFilter();
        }
        return prepaymentAccountId;
    }

    public void setPrepaymentAccountId(LongFilter prepaymentAccountId) {
        this.prepaymentAccountId = prepaymentAccountId;
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
        final AmortizationRecurrenceCriteria that = (AmortizationRecurrenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstAmortizationDate, that.firstAmortizationDate) &&
            Objects.equals(amortizationFrequency, that.amortizationFrequency) &&
            Objects.equals(numberOfRecurrences, that.numberOfRecurrences) &&
            Objects.equals(particulars, that.particulars) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(isOverWritten, that.isOverWritten) &&
            Objects.equals(timeOfInstallation, that.timeOfInstallation) &&
            Objects.equals(recurrenceGuid, that.recurrenceGuid) &&
            Objects.equals(prepaymentAccountGuid, that.prepaymentAccountGuid) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(parametersId, that.parametersId) &&
            Objects.equals(applicationParametersId, that.applicationParametersId) &&
            Objects.equals(depreciationMethodId, that.depreciationMethodId) &&
            Objects.equals(prepaymentAccountId, that.prepaymentAccountId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstAmortizationDate,
            amortizationFrequency,
            numberOfRecurrences,
            particulars,
            isActive,
            isOverWritten,
            timeOfInstallation,
            recurrenceGuid,
            prepaymentAccountGuid,
            placeholderId,
            parametersId,
            applicationParametersId,
            depreciationMethodId,
            prepaymentAccountId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmortizationRecurrenceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstAmortizationDate != null ? "firstAmortizationDate=" + firstAmortizationDate + ", " : "") +
            (amortizationFrequency != null ? "amortizationFrequency=" + amortizationFrequency + ", " : "") +
            (numberOfRecurrences != null ? "numberOfRecurrences=" + numberOfRecurrences + ", " : "") +
            (particulars != null ? "particulars=" + particulars + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (isOverWritten != null ? "isOverWritten=" + isOverWritten + ", " : "") +
            (timeOfInstallation != null ? "timeOfInstallation=" + timeOfInstallation + ", " : "") +
            (recurrenceGuid != null ? "recurrenceGuid=" + recurrenceGuid + ", " : "") +
            (prepaymentAccountGuid != null ? "prepaymentAccountGuid=" + prepaymentAccountGuid + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (parametersId != null ? "parametersId=" + parametersId + ", " : "") +
            (applicationParametersId != null ? "applicationParametersId=" + applicationParametersId + ", " : "") +
            (depreciationMethodId != null ? "depreciationMethodId=" + depreciationMethodId + ", " : "") +
            (prepaymentAccountId != null ? "prepaymentAccountId=" + prepaymentAccountId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
