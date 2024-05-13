package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.enumeration.MandatoryFieldFlagTypes;
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
 * Criteria class for the {@link io.github.erp.domain.AccountAttributeMetadata} entity. This class is used
 * in {@link io.github.erp.web.rest.AccountAttributeMetadataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /account-attribute-metadata?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AccountAttributeMetadataCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MandatoryFieldFlagTypes
     */
    public static class MandatoryFieldFlagTypesFilter extends Filter<MandatoryFieldFlagTypes> {

        public MandatoryFieldFlagTypesFilter() {}

        public MandatoryFieldFlagTypesFilter(MandatoryFieldFlagTypesFilter filter) {
            super(filter);
        }

        @Override
        public MandatoryFieldFlagTypesFilter copy() {
            return new MandatoryFieldFlagTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter precedence;

    private StringFilter columnName;

    private StringFilter shortName;

    private StringFilter dataType;

    private IntegerFilter length;

    private StringFilter columnIndex;

    private MandatoryFieldFlagTypesFilter mandatoryFieldFlag;

    private StringFilter dbColumnName;

    private IntegerFilter metadataVersion;

    private LongFilter standardInputTemplateId;

    private Boolean distinct;

    public AccountAttributeMetadataCriteria() {}

    public AccountAttributeMetadataCriteria(AccountAttributeMetadataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.precedence = other.precedence == null ? null : other.precedence.copy();
        this.columnName = other.columnName == null ? null : other.columnName.copy();
        this.shortName = other.shortName == null ? null : other.shortName.copy();
        this.dataType = other.dataType == null ? null : other.dataType.copy();
        this.length = other.length == null ? null : other.length.copy();
        this.columnIndex = other.columnIndex == null ? null : other.columnIndex.copy();
        this.mandatoryFieldFlag = other.mandatoryFieldFlag == null ? null : other.mandatoryFieldFlag.copy();
        this.dbColumnName = other.dbColumnName == null ? null : other.dbColumnName.copy();
        this.metadataVersion = other.metadataVersion == null ? null : other.metadataVersion.copy();
        this.standardInputTemplateId = other.standardInputTemplateId == null ? null : other.standardInputTemplateId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AccountAttributeMetadataCriteria copy() {
        return new AccountAttributeMetadataCriteria(this);
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

    public IntegerFilter getPrecedence() {
        return precedence;
    }

    public IntegerFilter precedence() {
        if (precedence == null) {
            precedence = new IntegerFilter();
        }
        return precedence;
    }

    public void setPrecedence(IntegerFilter precedence) {
        this.precedence = precedence;
    }

    public StringFilter getColumnName() {
        return columnName;
    }

    public StringFilter columnName() {
        if (columnName == null) {
            columnName = new StringFilter();
        }
        return columnName;
    }

    public void setColumnName(StringFilter columnName) {
        this.columnName = columnName;
    }

    public StringFilter getShortName() {
        return shortName;
    }

    public StringFilter shortName() {
        if (shortName == null) {
            shortName = new StringFilter();
        }
        return shortName;
    }

    public void setShortName(StringFilter shortName) {
        this.shortName = shortName;
    }

    public StringFilter getDataType() {
        return dataType;
    }

    public StringFilter dataType() {
        if (dataType == null) {
            dataType = new StringFilter();
        }
        return dataType;
    }

    public void setDataType(StringFilter dataType) {
        this.dataType = dataType;
    }

    public IntegerFilter getLength() {
        return length;
    }

    public IntegerFilter length() {
        if (length == null) {
            length = new IntegerFilter();
        }
        return length;
    }

    public void setLength(IntegerFilter length) {
        this.length = length;
    }

    public StringFilter getColumnIndex() {
        return columnIndex;
    }

    public StringFilter columnIndex() {
        if (columnIndex == null) {
            columnIndex = new StringFilter();
        }
        return columnIndex;
    }

    public void setColumnIndex(StringFilter columnIndex) {
        this.columnIndex = columnIndex;
    }

    public MandatoryFieldFlagTypesFilter getMandatoryFieldFlag() {
        return mandatoryFieldFlag;
    }

    public MandatoryFieldFlagTypesFilter mandatoryFieldFlag() {
        if (mandatoryFieldFlag == null) {
            mandatoryFieldFlag = new MandatoryFieldFlagTypesFilter();
        }
        return mandatoryFieldFlag;
    }

    public void setMandatoryFieldFlag(MandatoryFieldFlagTypesFilter mandatoryFieldFlag) {
        this.mandatoryFieldFlag = mandatoryFieldFlag;
    }

    public StringFilter getDbColumnName() {
        return dbColumnName;
    }

    public StringFilter dbColumnName() {
        if (dbColumnName == null) {
            dbColumnName = new StringFilter();
        }
        return dbColumnName;
    }

    public void setDbColumnName(StringFilter dbColumnName) {
        this.dbColumnName = dbColumnName;
    }

    public IntegerFilter getMetadataVersion() {
        return metadataVersion;
    }

    public IntegerFilter metadataVersion() {
        if (metadataVersion == null) {
            metadataVersion = new IntegerFilter();
        }
        return metadataVersion;
    }

    public void setMetadataVersion(IntegerFilter metadataVersion) {
        this.metadataVersion = metadataVersion;
    }

    public LongFilter getStandardInputTemplateId() {
        return standardInputTemplateId;
    }

    public LongFilter standardInputTemplateId() {
        if (standardInputTemplateId == null) {
            standardInputTemplateId = new LongFilter();
        }
        return standardInputTemplateId;
    }

    public void setStandardInputTemplateId(LongFilter standardInputTemplateId) {
        this.standardInputTemplateId = standardInputTemplateId;
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
        final AccountAttributeMetadataCriteria that = (AccountAttributeMetadataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(precedence, that.precedence) &&
            Objects.equals(columnName, that.columnName) &&
            Objects.equals(shortName, that.shortName) &&
            Objects.equals(dataType, that.dataType) &&
            Objects.equals(length, that.length) &&
            Objects.equals(columnIndex, that.columnIndex) &&
            Objects.equals(mandatoryFieldFlag, that.mandatoryFieldFlag) &&
            Objects.equals(dbColumnName, that.dbColumnName) &&
            Objects.equals(metadataVersion, that.metadataVersion) &&
            Objects.equals(standardInputTemplateId, that.standardInputTemplateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            precedence,
            columnName,
            shortName,
            dataType,
            length,
            columnIndex,
            mandatoryFieldFlag,
            dbColumnName,
            metadataVersion,
            standardInputTemplateId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountAttributeMetadataCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (precedence != null ? "precedence=" + precedence + ", " : "") +
            (columnName != null ? "columnName=" + columnName + ", " : "") +
            (shortName != null ? "shortName=" + shortName + ", " : "") +
            (dataType != null ? "dataType=" + dataType + ", " : "") +
            (length != null ? "length=" + length + ", " : "") +
            (columnIndex != null ? "columnIndex=" + columnIndex + ", " : "") +
            (mandatoryFieldFlag != null ? "mandatoryFieldFlag=" + mandatoryFieldFlag + ", " : "") +
            (dbColumnName != null ? "dbColumnName=" + dbColumnName + ", " : "") +
            (metadataVersion != null ? "metadataVersion=" + metadataVersion + ", " : "") +
            (standardInputTemplateId != null ? "standardInputTemplateId=" + standardInputTemplateId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
