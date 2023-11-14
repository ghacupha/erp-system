package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PartyRelationType} entity. This class is used
 * in {@link io.github.erp.web.rest.PartyRelationTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /party-relation-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PartyRelationTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter partyRelationTypeCode;

    private StringFilter partyRelationType;

    private Boolean distinct;

    public PartyRelationTypeCriteria() {}

    public PartyRelationTypeCriteria(PartyRelationTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.partyRelationTypeCode = other.partyRelationTypeCode == null ? null : other.partyRelationTypeCode.copy();
        this.partyRelationType = other.partyRelationType == null ? null : other.partyRelationType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PartyRelationTypeCriteria copy() {
        return new PartyRelationTypeCriteria(this);
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

    public StringFilter getPartyRelationTypeCode() {
        return partyRelationTypeCode;
    }

    public StringFilter partyRelationTypeCode() {
        if (partyRelationTypeCode == null) {
            partyRelationTypeCode = new StringFilter();
        }
        return partyRelationTypeCode;
    }

    public void setPartyRelationTypeCode(StringFilter partyRelationTypeCode) {
        this.partyRelationTypeCode = partyRelationTypeCode;
    }

    public StringFilter getPartyRelationType() {
        return partyRelationType;
    }

    public StringFilter partyRelationType() {
        if (partyRelationType == null) {
            partyRelationType = new StringFilter();
        }
        return partyRelationType;
    }

    public void setPartyRelationType(StringFilter partyRelationType) {
        this.partyRelationType = partyRelationType;
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
        final PartyRelationTypeCriteria that = (PartyRelationTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(partyRelationTypeCode, that.partyRelationTypeCode) &&
            Objects.equals(partyRelationType, that.partyRelationType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, partyRelationTypeCode, partyRelationType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyRelationTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (partyRelationTypeCode != null ? "partyRelationTypeCode=" + partyRelationTypeCode + ", " : "") +
            (partyRelationType != null ? "partyRelationType=" + partyRelationType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
