package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.domain.enumeration.DatasetBehaviorTypes;
import io.github.erp.domain.enumeration.UpdateFrequencyTypes;
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
 * Criteria class for the {@link io.github.erp.domain.GdiTransactionDataIndex} entity. This class is used
 * in {@link io.github.erp.web.rest.GdiTransactionDataIndexResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gdi-transaction-data-indices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GdiTransactionDataIndexCriteria implements Serializable, Criteria {

    /**
     * Class for filtering UpdateFrequencyTypes
     */
    public static class UpdateFrequencyTypesFilter extends Filter<UpdateFrequencyTypes> {

        public UpdateFrequencyTypesFilter() {}

        public UpdateFrequencyTypesFilter(UpdateFrequencyTypesFilter filter) {
            super(filter);
        }

        @Override
        public UpdateFrequencyTypesFilter copy() {
            return new UpdateFrequencyTypesFilter(this);
        }
    }

    /**
     * Class for filtering DatasetBehaviorTypes
     */
    public static class DatasetBehaviorTypesFilter extends Filter<DatasetBehaviorTypes> {

        public DatasetBehaviorTypesFilter() {}

        public DatasetBehaviorTypesFilter(DatasetBehaviorTypesFilter filter) {
            super(filter);
        }

        @Override
        public DatasetBehaviorTypesFilter copy() {
            return new DatasetBehaviorTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter datasetName;

    private StringFilter databaseName;

    private UpdateFrequencyTypesFilter updateFrequency;

    private DatasetBehaviorTypesFilter datasetBehavior;

    private IntegerFilter minimumDataRowsPerRequest;

    private IntegerFilter maximumDataRowsPerRequest;

    private StringFilter dataPath;

    private LongFilter masterDataItemId;

    private LongFilter businessTeamId;

    private LongFilter dataSetTemplateId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public GdiTransactionDataIndexCriteria() {}

    public GdiTransactionDataIndexCriteria(GdiTransactionDataIndexCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.datasetName = other.datasetName == null ? null : other.datasetName.copy();
        this.databaseName = other.databaseName == null ? null : other.databaseName.copy();
        this.updateFrequency = other.updateFrequency == null ? null : other.updateFrequency.copy();
        this.datasetBehavior = other.datasetBehavior == null ? null : other.datasetBehavior.copy();
        this.minimumDataRowsPerRequest = other.minimumDataRowsPerRequest == null ? null : other.minimumDataRowsPerRequest.copy();
        this.maximumDataRowsPerRequest = other.maximumDataRowsPerRequest == null ? null : other.maximumDataRowsPerRequest.copy();
        this.dataPath = other.dataPath == null ? null : other.dataPath.copy();
        this.masterDataItemId = other.masterDataItemId == null ? null : other.masterDataItemId.copy();
        this.businessTeamId = other.businessTeamId == null ? null : other.businessTeamId.copy();
        this.dataSetTemplateId = other.dataSetTemplateId == null ? null : other.dataSetTemplateId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GdiTransactionDataIndexCriteria copy() {
        return new GdiTransactionDataIndexCriteria(this);
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

    public StringFilter getDatasetName() {
        return datasetName;
    }

    public StringFilter datasetName() {
        if (datasetName == null) {
            datasetName = new StringFilter();
        }
        return datasetName;
    }

    public void setDatasetName(StringFilter datasetName) {
        this.datasetName = datasetName;
    }

    public StringFilter getDatabaseName() {
        return databaseName;
    }

    public StringFilter databaseName() {
        if (databaseName == null) {
            databaseName = new StringFilter();
        }
        return databaseName;
    }

    public void setDatabaseName(StringFilter databaseName) {
        this.databaseName = databaseName;
    }

    public UpdateFrequencyTypesFilter getUpdateFrequency() {
        return updateFrequency;
    }

    public UpdateFrequencyTypesFilter updateFrequency() {
        if (updateFrequency == null) {
            updateFrequency = new UpdateFrequencyTypesFilter();
        }
        return updateFrequency;
    }

    public void setUpdateFrequency(UpdateFrequencyTypesFilter updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public DatasetBehaviorTypesFilter getDatasetBehavior() {
        return datasetBehavior;
    }

    public DatasetBehaviorTypesFilter datasetBehavior() {
        if (datasetBehavior == null) {
            datasetBehavior = new DatasetBehaviorTypesFilter();
        }
        return datasetBehavior;
    }

    public void setDatasetBehavior(DatasetBehaviorTypesFilter datasetBehavior) {
        this.datasetBehavior = datasetBehavior;
    }

    public IntegerFilter getMinimumDataRowsPerRequest() {
        return minimumDataRowsPerRequest;
    }

    public IntegerFilter minimumDataRowsPerRequest() {
        if (minimumDataRowsPerRequest == null) {
            minimumDataRowsPerRequest = new IntegerFilter();
        }
        return minimumDataRowsPerRequest;
    }

    public void setMinimumDataRowsPerRequest(IntegerFilter minimumDataRowsPerRequest) {
        this.minimumDataRowsPerRequest = minimumDataRowsPerRequest;
    }

    public IntegerFilter getMaximumDataRowsPerRequest() {
        return maximumDataRowsPerRequest;
    }

    public IntegerFilter maximumDataRowsPerRequest() {
        if (maximumDataRowsPerRequest == null) {
            maximumDataRowsPerRequest = new IntegerFilter();
        }
        return maximumDataRowsPerRequest;
    }

    public void setMaximumDataRowsPerRequest(IntegerFilter maximumDataRowsPerRequest) {
        this.maximumDataRowsPerRequest = maximumDataRowsPerRequest;
    }

    public StringFilter getDataPath() {
        return dataPath;
    }

    public StringFilter dataPath() {
        if (dataPath == null) {
            dataPath = new StringFilter();
        }
        return dataPath;
    }

    public void setDataPath(StringFilter dataPath) {
        this.dataPath = dataPath;
    }

    public LongFilter getMasterDataItemId() {
        return masterDataItemId;
    }

    public LongFilter masterDataItemId() {
        if (masterDataItemId == null) {
            masterDataItemId = new LongFilter();
        }
        return masterDataItemId;
    }

    public void setMasterDataItemId(LongFilter masterDataItemId) {
        this.masterDataItemId = masterDataItemId;
    }

    public LongFilter getBusinessTeamId() {
        return businessTeamId;
    }

    public LongFilter businessTeamId() {
        if (businessTeamId == null) {
            businessTeamId = new LongFilter();
        }
        return businessTeamId;
    }

    public void setBusinessTeamId(LongFilter businessTeamId) {
        this.businessTeamId = businessTeamId;
    }

    public LongFilter getDataSetTemplateId() {
        return dataSetTemplateId;
    }

    public LongFilter dataSetTemplateId() {
        if (dataSetTemplateId == null) {
            dataSetTemplateId = new LongFilter();
        }
        return dataSetTemplateId;
    }

    public void setDataSetTemplateId(LongFilter dataSetTemplateId) {
        this.dataSetTemplateId = dataSetTemplateId;
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
        final GdiTransactionDataIndexCriteria that = (GdiTransactionDataIndexCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(datasetName, that.datasetName) &&
            Objects.equals(databaseName, that.databaseName) &&
            Objects.equals(updateFrequency, that.updateFrequency) &&
            Objects.equals(datasetBehavior, that.datasetBehavior) &&
            Objects.equals(minimumDataRowsPerRequest, that.minimumDataRowsPerRequest) &&
            Objects.equals(maximumDataRowsPerRequest, that.maximumDataRowsPerRequest) &&
            Objects.equals(dataPath, that.dataPath) &&
            Objects.equals(masterDataItemId, that.masterDataItemId) &&
            Objects.equals(businessTeamId, that.businessTeamId) &&
            Objects.equals(dataSetTemplateId, that.dataSetTemplateId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            datasetName,
            databaseName,
            updateFrequency,
            datasetBehavior,
            minimumDataRowsPerRequest,
            maximumDataRowsPerRequest,
            dataPath,
            masterDataItemId,
            businessTeamId,
            dataSetTemplateId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GdiTransactionDataIndexCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (datasetName != null ? "datasetName=" + datasetName + ", " : "") +
            (databaseName != null ? "databaseName=" + databaseName + ", " : "") +
            (updateFrequency != null ? "updateFrequency=" + updateFrequency + ", " : "") +
            (datasetBehavior != null ? "datasetBehavior=" + datasetBehavior + ", " : "") +
            (minimumDataRowsPerRequest != null ? "minimumDataRowsPerRequest=" + minimumDataRowsPerRequest + ", " : "") +
            (maximumDataRowsPerRequest != null ? "maximumDataRowsPerRequest=" + maximumDataRowsPerRequest + ", " : "") +
            (dataPath != null ? "dataPath=" + dataPath + ", " : "") +
            (masterDataItemId != null ? "masterDataItemId=" + masterDataItemId + ", " : "") +
            (businessTeamId != null ? "businessTeamId=" + businessTeamId + ", " : "") +
            (dataSetTemplateId != null ? "dataSetTemplateId=" + dataSetTemplateId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
