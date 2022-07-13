package io.github.erp.service.criteria;

/*-
 * Erp System - Mark II No 19 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.DepreciationRegime;
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

/**
 * Criteria class for the {@link io.github.erp.domain.FixedAssetDepreciation} entity. This class is used
 * in {@link io.github.erp.web.rest.FixedAssetDepreciationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fixed-asset-depreciations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FixedAssetDepreciationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DepreciationRegime
     */
    public static class DepreciationRegimeFilter extends Filter<DepreciationRegime> {

        public DepreciationRegimeFilter() {}

        public DepreciationRegimeFilter(DepreciationRegimeFilter filter) {
            super(filter);
        }

        @Override
        public DepreciationRegimeFilter copy() {
            return new DepreciationRegimeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter assetNumber;

    private StringFilter serviceOutletCode;

    private StringFilter assetTag;

    private StringFilter assetDescription;

    private LocalDateFilter depreciationDate;

    private StringFilter assetCategory;

    private BigDecimalFilter depreciationAmount;

    private DepreciationRegimeFilter depreciationRegime;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter placeholderId;

    private Boolean distinct;

    public FixedAssetDepreciationCriteria() {}

    public FixedAssetDepreciationCriteria(FixedAssetDepreciationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetNumber = other.assetNumber == null ? null : other.assetNumber.copy();
        this.serviceOutletCode = other.serviceOutletCode == null ? null : other.serviceOutletCode.copy();
        this.assetTag = other.assetTag == null ? null : other.assetTag.copy();
        this.assetDescription = other.assetDescription == null ? null : other.assetDescription.copy();
        this.depreciationDate = other.depreciationDate == null ? null : other.depreciationDate.copy();
        this.assetCategory = other.assetCategory == null ? null : other.assetCategory.copy();
        this.depreciationAmount = other.depreciationAmount == null ? null : other.depreciationAmount.copy();
        this.depreciationRegime = other.depreciationRegime == null ? null : other.depreciationRegime.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FixedAssetDepreciationCriteria copy() {
        return new FixedAssetDepreciationCriteria(this);
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

    public LongFilter getAssetNumber() {
        return assetNumber;
    }

    public LongFilter assetNumber() {
        if (assetNumber == null) {
            assetNumber = new LongFilter();
        }
        return assetNumber;
    }

    public void setAssetNumber(LongFilter assetNumber) {
        this.assetNumber = assetNumber;
    }

    public StringFilter getServiceOutletCode() {
        return serviceOutletCode;
    }

    public StringFilter serviceOutletCode() {
        if (serviceOutletCode == null) {
            serviceOutletCode = new StringFilter();
        }
        return serviceOutletCode;
    }

    public void setServiceOutletCode(StringFilter serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public StringFilter getAssetTag() {
        return assetTag;
    }

    public StringFilter assetTag() {
        if (assetTag == null) {
            assetTag = new StringFilter();
        }
        return assetTag;
    }

    public void setAssetTag(StringFilter assetTag) {
        this.assetTag = assetTag;
    }

    public StringFilter getAssetDescription() {
        return assetDescription;
    }

    public StringFilter assetDescription() {
        if (assetDescription == null) {
            assetDescription = new StringFilter();
        }
        return assetDescription;
    }

    public void setAssetDescription(StringFilter assetDescription) {
        this.assetDescription = assetDescription;
    }

    public LocalDateFilter getDepreciationDate() {
        return depreciationDate;
    }

    public LocalDateFilter depreciationDate() {
        if (depreciationDate == null) {
            depreciationDate = new LocalDateFilter();
        }
        return depreciationDate;
    }

    public void setDepreciationDate(LocalDateFilter depreciationDate) {
        this.depreciationDate = depreciationDate;
    }

    public StringFilter getAssetCategory() {
        return assetCategory;
    }

    public StringFilter assetCategory() {
        if (assetCategory == null) {
            assetCategory = new StringFilter();
        }
        return assetCategory;
    }

    public void setAssetCategory(StringFilter assetCategory) {
        this.assetCategory = assetCategory;
    }

    public BigDecimalFilter getDepreciationAmount() {
        return depreciationAmount;
    }

    public BigDecimalFilter depreciationAmount() {
        if (depreciationAmount == null) {
            depreciationAmount = new BigDecimalFilter();
        }
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimalFilter depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public DepreciationRegimeFilter getDepreciationRegime() {
        return depreciationRegime;
    }

    public DepreciationRegimeFilter depreciationRegime() {
        if (depreciationRegime == null) {
            depreciationRegime = new DepreciationRegimeFilter();
        }
        return depreciationRegime;
    }

    public void setDepreciationRegime(DepreciationRegimeFilter depreciationRegime) {
        this.depreciationRegime = depreciationRegime;
    }

    public StringFilter getFileUploadToken() {
        return fileUploadToken;
    }

    public StringFilter fileUploadToken() {
        if (fileUploadToken == null) {
            fileUploadToken = new StringFilter();
        }
        return fileUploadToken;
    }

    public void setFileUploadToken(StringFilter fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public StringFilter getCompilationToken() {
        return compilationToken;
    }

    public StringFilter compilationToken() {
        if (compilationToken == null) {
            compilationToken = new StringFilter();
        }
        return compilationToken;
    }

    public void setCompilationToken(StringFilter compilationToken) {
        this.compilationToken = compilationToken;
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
        final FixedAssetDepreciationCriteria that = (FixedAssetDepreciationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetNumber, that.assetNumber) &&
            Objects.equals(serviceOutletCode, that.serviceOutletCode) &&
            Objects.equals(assetTag, that.assetTag) &&
            Objects.equals(assetDescription, that.assetDescription) &&
            Objects.equals(depreciationDate, that.depreciationDate) &&
            Objects.equals(assetCategory, that.assetCategory) &&
            Objects.equals(depreciationAmount, that.depreciationAmount) &&
            Objects.equals(depreciationRegime, that.depreciationRegime) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetNumber,
            serviceOutletCode,
            assetTag,
            assetDescription,
            depreciationDate,
            assetCategory,
            depreciationAmount,
            depreciationRegime,
            fileUploadToken,
            compilationToken,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FixedAssetDepreciationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetNumber != null ? "assetNumber=" + assetNumber + ", " : "") +
            (serviceOutletCode != null ? "serviceOutletCode=" + serviceOutletCode + ", " : "") +
            (assetTag != null ? "assetTag=" + assetTag + ", " : "") +
            (assetDescription != null ? "assetDescription=" + assetDescription + ", " : "") +
            (depreciationDate != null ? "depreciationDate=" + depreciationDate + ", " : "") +
            (assetCategory != null ? "assetCategory=" + assetCategory + ", " : "") +
            (depreciationAmount != null ? "depreciationAmount=" + depreciationAmount + ", " : "") +
            (depreciationRegime != null ? "depreciationRegime=" + depreciationRegime + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
