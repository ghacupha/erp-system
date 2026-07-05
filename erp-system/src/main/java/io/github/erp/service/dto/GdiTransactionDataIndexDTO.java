package io.github.erp.service.dto;

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
