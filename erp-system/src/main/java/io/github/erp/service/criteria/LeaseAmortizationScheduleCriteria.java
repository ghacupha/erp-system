package io.github.erp.service.criteria;

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
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.LeaseAmortizationSchedule} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseAmortizationScheduleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-amortization-schedules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseAmortizationScheduleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter identifier;

    private BooleanFilter active;

    private LongFilter leaseLiabilityId;

    private LongFilter leaseLiabilityScheduleItemId;

    private LongFilter leaseContractId;

    private LongFilter leaseLiabilityCompilationId;

    private Boolean distinct;

    public LeaseAmortizationScheduleCriteria() {}

    public LeaseAmortizationScheduleCriteria(LeaseAmortizationScheduleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.identifier = other.identifier == null ? null : other.identifier.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.leaseLiabilityId = other.leaseLiabilityId == null ? null : other.leaseLiabilityId.copy();
        this.leaseLiabilityScheduleItemId = other.leaseLiabilityScheduleItemId == null ? null : other.leaseLiabilityScheduleItemId.copy();
        this.leaseContractId = other.leaseContractId == null ? null : other.leaseContractId.copy();
        this.leaseLiabilityCompilationId = other.leaseLiabilityCompilationId == null ? null : other.leaseLiabilityCompilationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseAmortizationScheduleCriteria copy() {
        return new LeaseAmortizationScheduleCriteria(this);
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

    public UUIDFilter getIdentifier() {
        return identifier;
    }

    public UUIDFilter identifier() {
        if (identifier == null) {
            identifier = new UUIDFilter();
        }
        return identifier;
    }

    public void setIdentifier(UUIDFilter identifier) {
        this.identifier = identifier;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getLeaseLiabilityId() {
        return leaseLiabilityId;
    }

    public LongFilter leaseLiabilityId() {
        if (leaseLiabilityId == null) {
            leaseLiabilityId = new LongFilter();
        }
        return leaseLiabilityId;
    }

    public void setLeaseLiabilityId(LongFilter leaseLiabilityId) {
        this.leaseLiabilityId = leaseLiabilityId;
    }

    public LongFilter getLeaseLiabilityScheduleItemId() {
        return leaseLiabilityScheduleItemId;
    }

    public LongFilter leaseLiabilityScheduleItemId() {
        if (leaseLiabilityScheduleItemId == null) {
            leaseLiabilityScheduleItemId = new LongFilter();
        }
        return leaseLiabilityScheduleItemId;
    }

    public void setLeaseLiabilityScheduleItemId(LongFilter leaseLiabilityScheduleItemId) {
        this.leaseLiabilityScheduleItemId = leaseLiabilityScheduleItemId;
    }

    public LongFilter getLeaseContractId() {
        return leaseContractId;
    }

    public LongFilter leaseContractId() {
        if (leaseContractId == null) {
            leaseContractId = new LongFilter();
        }
        return leaseContractId;
    }

    public void setLeaseContractId(LongFilter leaseContractId) {
        this.leaseContractId = leaseContractId;
    }

    public LongFilter getLeaseLiabilityCompilationId() {
        return leaseLiabilityCompilationId;
    }

    public LongFilter leaseLiabilityCompilationId() {
        if (leaseLiabilityCompilationId == null) {
            leaseLiabilityCompilationId = new LongFilter();
        }
        return leaseLiabilityCompilationId;
    }

    public void setLeaseLiabilityCompilationId(LongFilter leaseLiabilityCompilationId) {
        this.leaseLiabilityCompilationId = leaseLiabilityCompilationId;
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
        final LeaseAmortizationScheduleCriteria that = (LeaseAmortizationScheduleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(identifier, that.identifier) &&
            Objects.equals(active, that.active) &&
            Objects.equals(leaseLiabilityId, that.leaseLiabilityId) &&
            Objects.equals(leaseLiabilityScheduleItemId, that.leaseLiabilityScheduleItemId) &&
            Objects.equals(leaseContractId, that.leaseContractId) &&
            Objects.equals(leaseLiabilityCompilationId, that.leaseLiabilityCompilationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            identifier,
            active,
            leaseLiabilityId,
            leaseLiabilityScheduleItemId,
            leaseContractId,
            leaseLiabilityCompilationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseAmortizationScheduleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (identifier != null ? "identifier=" + identifier + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (leaseLiabilityId != null ? "leaseLiabilityId=" + leaseLiabilityId + ", " : "") +
            (leaseLiabilityScheduleItemId != null ? "leaseLiabilityScheduleItemId=" + leaseLiabilityScheduleItemId + ", " : "") +
            (leaseContractId != null ? "leaseContractId=" + leaseContractId + ", " : "") +
            (leaseLiabilityCompilationId != null ? "leaseLiabilityCompilationId=" + leaseLiabilityCompilationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
