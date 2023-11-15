package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.AssetCategory} entity. This class is used
 * in {@link io.github.erp.web.rest.AssetCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assetCategoryName;

    private StringFilter description;

    private StringFilter notes;

    private BigDecimalFilter depreciationRateYearly;

    private LongFilter depreciationMethodId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public AssetCategoryCriteria() {}

    public AssetCategoryCriteria(AssetCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetCategoryName = other.assetCategoryName == null ? null : other.assetCategoryName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.depreciationRateYearly = other.depreciationRateYearly == null ? null : other.depreciationRateYearly.copy();
        this.depreciationMethodId = other.depreciationMethodId == null ? null : other.depreciationMethodId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetCategoryCriteria copy() {
        return new AssetCategoryCriteria(this);
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

    public StringFilter getAssetCategoryName() {
        return assetCategoryName;
    }

    public StringFilter assetCategoryName() {
        if (assetCategoryName == null) {
            assetCategoryName = new StringFilter();
        }
        return assetCategoryName;
    }

    public void setAssetCategoryName(StringFilter assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public BigDecimalFilter getDepreciationRateYearly() {
        return depreciationRateYearly;
    }

    public BigDecimalFilter depreciationRateYearly() {
        if (depreciationRateYearly == null) {
            depreciationRateYearly = new BigDecimalFilter();
        }
        return depreciationRateYearly;
    }

    public void setDepreciationRateYearly(BigDecimalFilter depreciationRateYearly) {
        this.depreciationRateYearly = depreciationRateYearly;
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
        final AssetCategoryCriteria that = (AssetCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetCategoryName, that.assetCategoryName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(depreciationRateYearly, that.depreciationRateYearly) &&
            Objects.equals(depreciationMethodId, that.depreciationMethodId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetCategoryName,
            description,
            notes,
            depreciationRateYearly,
            depreciationMethodId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetCategoryName != null ? "assetCategoryName=" + assetCategoryName + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (depreciationRateYearly != null ? "depreciationRateYearly=" + depreciationRateYearly + ", " : "") +
            (depreciationMethodId != null ? "depreciationMethodId=" + depreciationMethodId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
