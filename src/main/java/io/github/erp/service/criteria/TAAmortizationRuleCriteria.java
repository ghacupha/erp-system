package io.github.erp.service.criteria;

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
 * Criteria class for the {@link io.github.erp.domain.TAAmortizationRule} entity. This class is used
 * in {@link io.github.erp.web.rest.TAAmortizationRuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ta-amortization-rules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TAAmortizationRuleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private UUIDFilter identifier;

    private LongFilter leaseContractId;

    private LongFilter debitId;

    private LongFilter creditId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public TAAmortizationRuleCriteria() {}

    public TAAmortizationRuleCriteria(TAAmortizationRuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.identifier = other.identifier == null ? null : other.identifier.copy();
        this.leaseContractId = other.leaseContractId == null ? null : other.leaseContractId.copy();
        this.debitId = other.debitId == null ? null : other.debitId.copy();
        this.creditId = other.creditId == null ? null : other.creditId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TAAmortizationRuleCriteria copy() {
        return new TAAmortizationRuleCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public LongFilter getDebitId() {
        return debitId;
    }

    public LongFilter debitId() {
        if (debitId == null) {
            debitId = new LongFilter();
        }
        return debitId;
    }

    public void setDebitId(LongFilter debitId) {
        this.debitId = debitId;
    }

    public LongFilter getCreditId() {
        return creditId;
    }

    public LongFilter creditId() {
        if (creditId == null) {
            creditId = new LongFilter();
        }
        return creditId;
    }

    public void setCreditId(LongFilter creditId) {
        this.creditId = creditId;
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
        final TAAmortizationRuleCriteria that = (TAAmortizationRuleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(identifier, that.identifier) &&
            Objects.equals(leaseContractId, that.leaseContractId) &&
            Objects.equals(debitId, that.debitId) &&
            Objects.equals(creditId, that.creditId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, identifier, leaseContractId, debitId, creditId, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TAAmortizationRuleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (identifier != null ? "identifier=" + identifier + ", " : "") +
            (leaseContractId != null ? "leaseContractId=" + leaseContractId + ", " : "") +
            (debitId != null ? "debitId=" + debitId + ", " : "") +
            (creditId != null ? "creditId=" + creditId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
