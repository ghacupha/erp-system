package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

/**
 * Criteria class for the {@link io.github.erp.domain.IsoCountryCode} entity. This class is used
 * in {@link io.github.erp.web.rest.IsoCountryCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /iso-country-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IsoCountryCodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter countryCode;

    private StringFilter countryDescription;

    private StringFilter continentCode;

    private StringFilter continentName;

    private StringFilter subRegion;

    private Boolean distinct;

    public IsoCountryCodeCriteria() {}

    public IsoCountryCodeCriteria(IsoCountryCodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.countryCode = other.countryCode == null ? null : other.countryCode.copy();
        this.countryDescription = other.countryDescription == null ? null : other.countryDescription.copy();
        this.continentCode = other.continentCode == null ? null : other.continentCode.copy();
        this.continentName = other.continentName == null ? null : other.continentName.copy();
        this.subRegion = other.subRegion == null ? null : other.subRegion.copy();
        this.distinct = other.distinct;
    }

    @Override
    public IsoCountryCodeCriteria copy() {
        return new IsoCountryCodeCriteria(this);
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

    public StringFilter getCountryCode() {
        return countryCode;
    }

    public StringFilter countryCode() {
        if (countryCode == null) {
            countryCode = new StringFilter();
        }
        return countryCode;
    }

    public void setCountryCode(StringFilter countryCode) {
        this.countryCode = countryCode;
    }

    public StringFilter getCountryDescription() {
        return countryDescription;
    }

    public StringFilter countryDescription() {
        if (countryDescription == null) {
            countryDescription = new StringFilter();
        }
        return countryDescription;
    }

    public void setCountryDescription(StringFilter countryDescription) {
        this.countryDescription = countryDescription;
    }

    public StringFilter getContinentCode() {
        return continentCode;
    }

    public StringFilter continentCode() {
        if (continentCode == null) {
            continentCode = new StringFilter();
        }
        return continentCode;
    }

    public void setContinentCode(StringFilter continentCode) {
        this.continentCode = continentCode;
    }

    public StringFilter getContinentName() {
        return continentName;
    }

    public StringFilter continentName() {
        if (continentName == null) {
            continentName = new StringFilter();
        }
        return continentName;
    }

    public void setContinentName(StringFilter continentName) {
        this.continentName = continentName;
    }

    public StringFilter getSubRegion() {
        return subRegion;
    }

    public StringFilter subRegion() {
        if (subRegion == null) {
            subRegion = new StringFilter();
        }
        return subRegion;
    }

    public void setSubRegion(StringFilter subRegion) {
        this.subRegion = subRegion;
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
        final IsoCountryCodeCriteria that = (IsoCountryCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(countryCode, that.countryCode) &&
            Objects.equals(countryDescription, that.countryDescription) &&
            Objects.equals(continentCode, that.continentCode) &&
            Objects.equals(continentName, that.continentName) &&
            Objects.equals(subRegion, that.subRegion) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, countryCode, countryDescription, continentCode, continentName, subRegion, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsoCountryCodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (countryCode != null ? "countryCode=" + countryCode + ", " : "") +
            (countryDescription != null ? "countryDescription=" + countryDescription + ", " : "") +
            (continentCode != null ? "continentCode=" + continentCode + ", " : "") +
            (continentName != null ? "continentName=" + continentName + ", " : "") +
            (subRegion != null ? "subRegion=" + subRegion + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
