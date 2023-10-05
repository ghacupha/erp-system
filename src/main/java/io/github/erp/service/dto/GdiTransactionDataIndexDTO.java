package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

    private Integer minimumDatarowsPerRequest;

    private Integer maximumDataRowsPerRequest;

    @Lob
    private String datasetDescription;

    @Lob
    private byte[] dataTemplate;

    private String dataTemplateContentType;
    private Set<GdiMasterDataIndexDTO> masterDataItems = new HashSet<>();

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

    public Integer getMinimumDatarowsPerRequest() {
        return minimumDatarowsPerRequest;
    }

    public void setMinimumDatarowsPerRequest(Integer minimumDatarowsPerRequest) {
        this.minimumDatarowsPerRequest = minimumDatarowsPerRequest;
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

    public byte[] getDataTemplate() {
        return dataTemplate;
    }

    public void setDataTemplate(byte[] dataTemplate) {
        this.dataTemplate = dataTemplate;
    }

    public String getDataTemplateContentType() {
        return dataTemplateContentType;
    }

    public void setDataTemplateContentType(String dataTemplateContentType) {
        this.dataTemplateContentType = dataTemplateContentType;
    }

    public Set<GdiMasterDataIndexDTO> getMasterDataItems() {
        return masterDataItems;
    }

    public void setMasterDataItems(Set<GdiMasterDataIndexDTO> masterDataItems) {
        this.masterDataItems = masterDataItems;
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
            ", minimumDatarowsPerRequest=" + getMinimumDatarowsPerRequest() +
            ", maximumDataRowsPerRequest=" + getMaximumDataRowsPerRequest() +
            ", datasetDescription='" + getDatasetDescription() + "'" +
            ", dataTemplate='" + getDataTemplate() + "'" +
            ", masterDataItems=" + getMasterDataItems() +
            "}";
    }
}
