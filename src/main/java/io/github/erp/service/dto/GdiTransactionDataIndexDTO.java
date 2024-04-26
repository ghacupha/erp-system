package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.enumeration.DatasetBehaviorTypes;
import io.github.erp.domain.enumeration.UpdateFrequencyTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.GdiTransactionDataIndex} entity.
 */
public class GdiTransactionDataIndexDTO implements Serializable {

    private Long id;

    @NotNull
    private String datasetName;

    @NotNull
    private String databaseName;

    @NotNull
    private UpdateFrequencyTypes updateFrequency;

    @NotNull
    private DatasetBehaviorTypes datasetBehavior;

    private Integer minimumDataRowsPerRequest;

    private Integer maximumDataRowsPerRequest;

    @Lob
    private String datasetDescription;

    private String dataPath;

    private Set<GdiMasterDataIndexDTO> masterDataItems = new HashSet<>();

    private BusinessTeamDTO businessTeam;

    private BusinessDocumentDTO dataSetTemplate;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public UpdateFrequencyTypes getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdateFrequency(UpdateFrequencyTypes updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public DatasetBehaviorTypes getDatasetBehavior() {
        return datasetBehavior;
    }

    public void setDatasetBehavior(DatasetBehaviorTypes datasetBehavior) {
        this.datasetBehavior = datasetBehavior;
    }

    public Integer getMinimumDataRowsPerRequest() {
        return minimumDataRowsPerRequest;
    }

    public void setMinimumDataRowsPerRequest(Integer minimumDataRowsPerRequest) {
        this.minimumDataRowsPerRequest = minimumDataRowsPerRequest;
    }

    public Integer getMaximumDataRowsPerRequest() {
        return maximumDataRowsPerRequest;
    }

    public void setMaximumDataRowsPerRequest(Integer maximumDataRowsPerRequest) {
        this.maximumDataRowsPerRequest = maximumDataRowsPerRequest;
    }

    public String getDatasetDescription() {
        return datasetDescription;
    }

    public void setDatasetDescription(String datasetDescription) {
        this.datasetDescription = datasetDescription;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public Set<GdiMasterDataIndexDTO> getMasterDataItems() {
        return masterDataItems;
    }

    public void setMasterDataItems(Set<GdiMasterDataIndexDTO> masterDataItems) {
        this.masterDataItems = masterDataItems;
    }

    public BusinessTeamDTO getBusinessTeam() {
        return businessTeam;
    }

    public void setBusinessTeam(BusinessTeamDTO businessTeam) {
        this.businessTeam = businessTeam;
    }

    public BusinessDocumentDTO getDataSetTemplate() {
        return dataSetTemplate;
    }

    public void setDataSetTemplate(BusinessDocumentDTO dataSetTemplate) {
        this.dataSetTemplate = dataSetTemplate;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GdiTransactionDataIndexDTO)) {
            return false;
        }

        GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO = (GdiTransactionDataIndexDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gdiTransactionDataIndexDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GdiTransactionDataIndexDTO{" +
            "id=" + getId() +
            ", datasetName='" + getDatasetName() + "'" +
            ", databaseName='" + getDatabaseName() + "'" +
            ", updateFrequency='" + getUpdateFrequency() + "'" +
            ", datasetBehavior='" + getDatasetBehavior() + "'" +
            ", minimumDataRowsPerRequest=" + getMinimumDataRowsPerRequest() +
            ", maximumDataRowsPerRequest=" + getMaximumDataRowsPerRequest() +
            ", datasetDescription='" + getDatasetDescription() + "'" +
            ", dataPath='" + getDataPath() + "'" +
            ", masterDataItems=" + getMasterDataItems() +
            ", businessTeam=" + getBusinessTeam() +
            ", dataSetTemplate=" + getDataSetTemplate() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
