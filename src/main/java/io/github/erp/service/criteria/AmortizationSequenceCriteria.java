package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.AmortizationSequence} entity. This class is used
 * in {@link io.github.erp.web.rest.AmortizationSequenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /amortization-sequences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AmortizationSequenceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter prepaymentAccountGuid;

    private UUIDFilter recurrenceGuid;

    private IntegerFilter sequenceNumber;

    private StringFilter particulars;

    private LocalDateFilter currentAmortizationDate;

    private LocalDateFilter previousAmortizationDate;

    private LocalDateFilter nextAmortizationDate;

    private BooleanFilter isCommencementSequence;

    private BooleanFilter isTerminalSequence;

    private BigDecimalFilter amortizationAmount;

    private UUIDFilter sequenceGuid;

    private LongFilter prepaymentAccountId;

    private LongFilter amortizationRecurrenceId;

    private LongFilter placeholderId;

    private LongFilter prepaymentMappingId;

    private LongFilter applicationParametersId;

    private Boolean distinct;

    public AmortizationSequenceCriteria() {}

    public AmortizationSequenceCriteria(AmortizationSequenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.prepaymentAccountGuid = other.prepaymentAccountGuid == null ? null : other.prepaymentAccountGuid.copy();
        this.recurrenceGuid = other.recurrenceGuid == null ? null : other.recurrenceGuid.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.particulars = other.particulars == null ? null : other.particulars.copy();
        this.currentAmortizationDate = other.currentAmortizationDate == null ? null : other.currentAmortizationDate.copy();
        this.previousAmortizationDate = other.previousAmortizationDate == null ? null : other.previousAmortizationDate.copy();
        this.nextAmortizationDate = other.nextAmortizationDate == null ? null : other.nextAmortizationDate.copy();
        this.isCommencementSequence = other.isCommencementSequence == null ? null : other.isCommencementSequence.copy();
        this.isTerminalSequence = other.isTerminalSequence == null ? null : other.isTerminalSequence.copy();
        this.amortizationAmount = other.amortizationAmount == null ? null : other.amortizationAmount.copy();
        this.sequenceGuid = other.sequenceGuid == null ? null : other.sequenceGuid.copy();
        this.prepaymentAccountId = other.prepaymentAccountId == null ? null : other.prepaymentAccountId.copy();
        this.amortizationRecurrenceId = other.amortizationRecurrenceId == null ? null : other.amortizationRecurrenceId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.prepaymentMappingId = other.prepaymentMappingId == null ? null : other.prepaymentMappingId.copy();
        this.applicationParametersId = other.applicationParametersId == null ? null : other.applicationParametersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AmortizationSequenceCriteria copy() {
        return new AmortizationSequenceCriteria(this);
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

    public IntegerFilter getSequenceNumber() {
        return sequenceNumber;
    }

    public IntegerFilter sequenceNumber() {
        if (sequenceNumber == null) {
            sequenceNumber = new IntegerFilter();
        }
        return sequenceNumber;
    }

    public void setSequenceNumber(IntegerFilter sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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

    public LocalDateFilter getCurrentAmortizationDate() {
        return currentAmortizationDate;
    }

    public LocalDateFilter currentAmortizationDate() {
        if (currentAmortizationDate == null) {
            currentAmortizationDate = new LocalDateFilter();
        }
        return currentAmortizationDate;
    }

    public void setCurrentAmortizationDate(LocalDateFilter currentAmortizationDate) {
        this.currentAmortizationDate = currentAmortizationDate;
    }

    public LocalDateFilter getPreviousAmortizationDate() {
        return previousAmortizationDate;
    }

    public LocalDateFilter previousAmortizationDate() {
        if (previousAmortizationDate == null) {
            previousAmortizationDate = new LocalDateFilter();
        }
        return previousAmortizationDate;
    }

    public void setPreviousAmortizationDate(LocalDateFilter previousAmortizationDate) {
        this.previousAmortizationDate = previousAmortizationDate;
    }

    public LocalDateFilter getNextAmortizationDate() {
        return nextAmortizationDate;
    }

    public LocalDateFilter nextAmortizationDate() {
        if (nextAmortizationDate == null) {
            nextAmortizationDate = new LocalDateFilter();
        }
        return nextAmortizationDate;
    }

    public void setNextAmortizationDate(LocalDateFilter nextAmortizationDate) {
        this.nextAmortizationDate = nextAmortizationDate;
    }

    public BooleanFilter getIsCommencementSequence() {
        return isCommencementSequence;
    }

    public BooleanFilter isCommencementSequence() {
        if (isCommencementSequence == null) {
            isCommencementSequence = new BooleanFilter();
        }
        return isCommencementSequence;
    }

    public void setIsCommencementSequence(BooleanFilter isCommencementSequence) {
        this.isCommencementSequence = isCommencementSequence;
    }

    public BooleanFilter getIsTerminalSequence() {
        return isTerminalSequence;
    }

    public BooleanFilter isTerminalSequence() {
        if (isTerminalSequence == null) {
            isTerminalSequence = new BooleanFilter();
        }
        return isTerminalSequence;
    }

    public void setIsTerminalSequence(BooleanFilter isTerminalSequence) {
        this.isTerminalSequence = isTerminalSequence;
    }

    public BigDecimalFilter getAmortizationAmount() {
        return amortizationAmount;
    }

    public BigDecimalFilter amortizationAmount() {
        if (amortizationAmount == null) {
            amortizationAmount = new BigDecimalFilter();
        }
        return amortizationAmount;
    }

    public void setAmortizationAmount(BigDecimalFilter amortizationAmount) {
        this.amortizationAmount = amortizationAmount;
    }

    public UUIDFilter getSequenceGuid() {
        return sequenceGuid;
    }

    public UUIDFilter sequenceGuid() {
        if (sequenceGuid == null) {
            sequenceGuid = new UUIDFilter();
        }
        return sequenceGuid;
    }

    public void setSequenceGuid(UUIDFilter sequenceGuid) {
        this.sequenceGuid = sequenceGuid;
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

    public LongFilter getAmortizationRecurrenceId() {
        return amortizationRecurrenceId;
    }

    public LongFilter amortizationRecurrenceId() {
        if (amortizationRecurrenceId == null) {
            amortizationRecurrenceId = new LongFilter();
        }
        return amortizationRecurrenceId;
    }

    public void setAmortizationRecurrenceId(LongFilter amortizationRecurrenceId) {
        this.amortizationRecurrenceId = amortizationRecurrenceId;
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

    public LongFilter getPrepaymentMappingId() {
        return prepaymentMappingId;
    }

    public LongFilter prepaymentMappingId() {
        if (prepaymentMappingId == null) {
            prepaymentMappingId = new LongFilter();
        }
        return prepaymentMappingId;
    }

    public void setPrepaymentMappingId(LongFilter prepaymentMappingId) {
        this.prepaymentMappingId = prepaymentMappingId;
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
        final AmortizationSequenceCriteria that = (AmortizationSequenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(prepaymentAccountGuid, that.prepaymentAccountGuid) &&
            Objects.equals(recurrenceGuid, that.recurrenceGuid) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(particulars, that.particulars) &&
            Objects.equals(currentAmortizationDate, that.currentAmortizationDate) &&
            Objects.equals(previousAmortizationDate, that.previousAmortizationDate) &&
            Objects.equals(nextAmortizationDate, that.nextAmortizationDate) &&
            Objects.equals(isCommencementSequence, that.isCommencementSequence) &&
            Objects.equals(isTerminalSequence, that.isTerminalSequence) &&
            Objects.equals(amortizationAmount, that.amortizationAmount) &&
            Objects.equals(sequenceGuid, that.sequenceGuid) &&
            Objects.equals(prepaymentAccountId, that.prepaymentAccountId) &&
            Objects.equals(amortizationRecurrenceId, that.amortizationRecurrenceId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(prepaymentMappingId, that.prepaymentMappingId) &&
            Objects.equals(applicationParametersId, that.applicationParametersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            prepaymentAccountGuid,
            recurrenceGuid,
            sequenceNumber,
            particulars,
            currentAmortizationDate,
            previousAmortizationDate,
            nextAmortizationDate,
            isCommencementSequence,
            isTerminalSequence,
            amortizationAmount,
            sequenceGuid,
            prepaymentAccountId,
            amortizationRecurrenceId,
            placeholderId,
            prepaymentMappingId,
            applicationParametersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmortizationSequenceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (prepaymentAccountGuid != null ? "prepaymentAccountGuid=" + prepaymentAccountGuid + ", " : "") +
            (recurrenceGuid != null ? "recurrenceGuid=" + recurrenceGuid + ", " : "") +
            (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber + ", " : "") +
            (particulars != null ? "particulars=" + particulars + ", " : "") +
            (currentAmortizationDate != null ? "currentAmortizationDate=" + currentAmortizationDate + ", " : "") +
            (previousAmortizationDate != null ? "previousAmortizationDate=" + previousAmortizationDate + ", " : "") +
            (nextAmortizationDate != null ? "nextAmortizationDate=" + nextAmortizationDate + ", " : "") +
            (isCommencementSequence != null ? "isCommencementSequence=" + isCommencementSequence + ", " : "") +
            (isTerminalSequence != null ? "isTerminalSequence=" + isTerminalSequence + ", " : "") +
            (amortizationAmount != null ? "amortizationAmount=" + amortizationAmount + ", " : "") +
            (sequenceGuid != null ? "sequenceGuid=" + sequenceGuid + ", " : "") +
            (prepaymentAccountId != null ? "prepaymentAccountId=" + prepaymentAccountId + ", " : "") +
            (amortizationRecurrenceId != null ? "amortizationRecurrenceId=" + amortizationRecurrenceId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (prepaymentMappingId != null ? "prepaymentMappingId=" + prepaymentMappingId + ", " : "") +
            (applicationParametersId != null ? "applicationParametersId=" + applicationParametersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
