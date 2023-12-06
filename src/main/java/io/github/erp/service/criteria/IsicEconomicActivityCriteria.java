package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
 * Criteria class for the {@link io.github.erp.domain.IsicEconomicActivity} entity. This class is used
 * in {@link io.github.erp.web.rest.IsicEconomicActivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /isic-economic-activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IsicEconomicActivityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter businessEconomicActivityCode;

    private StringFilter section;

    private StringFilter sectionLabel;

    private StringFilter division;

    private StringFilter divisionLabel;

    private StringFilter groupCode;

    private StringFilter groupLabel;

    private StringFilter classCode;

    private StringFilter businessEconomicActivityType;

    private Boolean distinct;

    public IsicEconomicActivityCriteria() {}

    public IsicEconomicActivityCriteria(IsicEconomicActivityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.businessEconomicActivityCode = other.businessEconomicActivityCode == null ? null : other.businessEconomicActivityCode.copy();
        this.section = other.section == null ? null : other.section.copy();
        this.sectionLabel = other.sectionLabel == null ? null : other.sectionLabel.copy();
        this.division = other.division == null ? null : other.division.copy();
        this.divisionLabel = other.divisionLabel == null ? null : other.divisionLabel.copy();
        this.groupCode = other.groupCode == null ? null : other.groupCode.copy();
        this.groupLabel = other.groupLabel == null ? null : other.groupLabel.copy();
        this.classCode = other.classCode == null ? null : other.classCode.copy();
        this.businessEconomicActivityType = other.businessEconomicActivityType == null ? null : other.businessEconomicActivityType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public IsicEconomicActivityCriteria copy() {
        return new IsicEconomicActivityCriteria(this);
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

    public StringFilter getBusinessEconomicActivityCode() {
        return businessEconomicActivityCode;
    }

    public StringFilter businessEconomicActivityCode() {
        if (businessEconomicActivityCode == null) {
            businessEconomicActivityCode = new StringFilter();
        }
        return businessEconomicActivityCode;
    }

    public void setBusinessEconomicActivityCode(StringFilter businessEconomicActivityCode) {
        this.businessEconomicActivityCode = businessEconomicActivityCode;
    }

    public StringFilter getSection() {
        return section;
    }

    public StringFilter section() {
        if (section == null) {
            section = new StringFilter();
        }
        return section;
    }

    public void setSection(StringFilter section) {
        this.section = section;
    }

    public StringFilter getSectionLabel() {
        return sectionLabel;
    }

    public StringFilter sectionLabel() {
        if (sectionLabel == null) {
            sectionLabel = new StringFilter();
        }
        return sectionLabel;
    }

    public void setSectionLabel(StringFilter sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public StringFilter getDivision() {
        return division;
    }

    public StringFilter division() {
        if (division == null) {
            division = new StringFilter();
        }
        return division;
    }

    public void setDivision(StringFilter division) {
        this.division = division;
    }

    public StringFilter getDivisionLabel() {
        return divisionLabel;
    }

    public StringFilter divisionLabel() {
        if (divisionLabel == null) {
            divisionLabel = new StringFilter();
        }
        return divisionLabel;
    }

    public void setDivisionLabel(StringFilter divisionLabel) {
        this.divisionLabel = divisionLabel;
    }

    public StringFilter getGroupCode() {
        return groupCode;
    }

    public StringFilter groupCode() {
        if (groupCode == null) {
            groupCode = new StringFilter();
        }
        return groupCode;
    }

    public void setGroupCode(StringFilter groupCode) {
        this.groupCode = groupCode;
    }

    public StringFilter getGroupLabel() {
        return groupLabel;
    }

    public StringFilter groupLabel() {
        if (groupLabel == null) {
            groupLabel = new StringFilter();
        }
        return groupLabel;
    }

    public void setGroupLabel(StringFilter groupLabel) {
        this.groupLabel = groupLabel;
    }

    public StringFilter getClassCode() {
        return classCode;
    }

    public StringFilter classCode() {
        if (classCode == null) {
            classCode = new StringFilter();
        }
        return classCode;
    }

    public void setClassCode(StringFilter classCode) {
        this.classCode = classCode;
    }

    public StringFilter getBusinessEconomicActivityType() {
        return businessEconomicActivityType;
    }

    public StringFilter businessEconomicActivityType() {
        if (businessEconomicActivityType == null) {
            businessEconomicActivityType = new StringFilter();
        }
        return businessEconomicActivityType;
    }

    public void setBusinessEconomicActivityType(StringFilter businessEconomicActivityType) {
        this.businessEconomicActivityType = businessEconomicActivityType;
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
        final IsicEconomicActivityCriteria that = (IsicEconomicActivityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(businessEconomicActivityCode, that.businessEconomicActivityCode) &&
            Objects.equals(section, that.section) &&
            Objects.equals(sectionLabel, that.sectionLabel) &&
            Objects.equals(division, that.division) &&
            Objects.equals(divisionLabel, that.divisionLabel) &&
            Objects.equals(groupCode, that.groupCode) &&
            Objects.equals(groupLabel, that.groupLabel) &&
            Objects.equals(classCode, that.classCode) &&
            Objects.equals(businessEconomicActivityType, that.businessEconomicActivityType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            businessEconomicActivityCode,
            section,
            sectionLabel,
            division,
            divisionLabel,
            groupCode,
            groupLabel,
            classCode,
            businessEconomicActivityType,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsicEconomicActivityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (businessEconomicActivityCode != null ? "businessEconomicActivityCode=" + businessEconomicActivityCode + ", " : "") +
            (section != null ? "section=" + section + ", " : "") +
            (sectionLabel != null ? "sectionLabel=" + sectionLabel + ", " : "") +
            (division != null ? "division=" + division + ", " : "") +
            (divisionLabel != null ? "divisionLabel=" + divisionLabel + ", " : "") +
            (groupCode != null ? "groupCode=" + groupCode + ", " : "") +
            (groupLabel != null ? "groupLabel=" + groupLabel + ", " : "") +
            (classCode != null ? "classCode=" + classCode + ", " : "") +
            (businessEconomicActivityType != null ? "businessEconomicActivityType=" + businessEconomicActivityType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
