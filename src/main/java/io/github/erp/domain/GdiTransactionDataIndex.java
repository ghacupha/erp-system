package io.github.erp.domain;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.DatasetBehaviorTypes;
import io.github.erp.domain.enumeration.UpdateFrequencyTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A GdiTransactionDataIndex.
 */
@Entity
@Table(name = "gdi_transaction_data_index")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "gditransactiondataindex")
public class GdiTransactionDataIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "dataset_name", nullable = false, unique = true)
    private String datasetName;

    @NotNull
    @Column(name = "database_name", nullable = false, unique = true)
    private String databaseName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "update_frequency", nullable = false)
    private UpdateFrequencyTypes updateFrequency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dataset_behavior", nullable = false)
    private DatasetBehaviorTypes datasetBehavior;

    @Column(name = "minimum_data_rows_per_request")
    private Integer minimumDataRowsPerRequest;

    @Column(name = "maximum_data_rows_per_request")
    private Integer maximumDataRowsPerRequest;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "dataset_description")
    private String datasetDescription;

    @Column(name = "data_path")
    private String dataPath;

    @ManyToMany
    @JoinTable(
        name = "rel_gdi_transaction_data_index__master_data_item",
        joinColumns = @JoinColumn(name = "gdi_transaction_data_index_id"),
        inverseJoinColumns = @JoinColumn(name = "master_data_item_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<GdiMasterDataIndex> masterDataItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "teamMembers" }, allowSetters = true)
    private BusinessTeam businessTeam;

    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private BusinessDocument dataSetTemplate;

    @ManyToMany
    @JoinTable(
        name = "rel_gdi_transaction_data_index__placeholder",
        joinColumns = @JoinColumn(name = "gdi_transaction_data_index_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GdiTransactionDataIndex id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatasetName() {
        return this.datasetName;
    }

    public GdiTransactionDataIndex datasetName(String datasetName) {
        this.setDatasetName(datasetName);
        return this;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public GdiTransactionDataIndex databaseName(String databaseName) {
        this.setDatabaseName(databaseName);
        return this;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public UpdateFrequencyTypes getUpdateFrequency() {
        return this.updateFrequency;
    }

    public GdiTransactionDataIndex updateFrequency(UpdateFrequencyTypes updateFrequency) {
        this.setUpdateFrequency(updateFrequency);
        return this;
    }

    public void setUpdateFrequency(UpdateFrequencyTypes updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public DatasetBehaviorTypes getDatasetBehavior() {
        return this.datasetBehavior;
    }

    public GdiTransactionDataIndex datasetBehavior(DatasetBehaviorTypes datasetBehavior) {
        this.setDatasetBehavior(datasetBehavior);
        return this;
    }

    public void setDatasetBehavior(DatasetBehaviorTypes datasetBehavior) {
        this.datasetBehavior = datasetBehavior;
    }

    public Integer getMinimumDataRowsPerRequest() {
        return this.minimumDataRowsPerRequest;
    }

    public GdiTransactionDataIndex minimumDataRowsPerRequest(Integer minimumDataRowsPerRequest) {
        this.setMinimumDataRowsPerRequest(minimumDataRowsPerRequest);
        return this;
    }

    public void setMinimumDataRowsPerRequest(Integer minimumDataRowsPerRequest) {
        this.minimumDataRowsPerRequest = minimumDataRowsPerRequest;
    }

    public Integer getMaximumDataRowsPerRequest() {
        return this.maximumDataRowsPerRequest;
    }

    public GdiTransactionDataIndex maximumDataRowsPerRequest(Integer maximumDataRowsPerRequest) {
        this.setMaximumDataRowsPerRequest(maximumDataRowsPerRequest);
        return this;
    }

    public void setMaximumDataRowsPerRequest(Integer maximumDataRowsPerRequest) {
        this.maximumDataRowsPerRequest = maximumDataRowsPerRequest;
    }

    public String getDatasetDescription() {
        return this.datasetDescription;
    }

    public GdiTransactionDataIndex datasetDescription(String datasetDescription) {
        this.setDatasetDescription(datasetDescription);
        return this;
    }

    public void setDatasetDescription(String datasetDescription) {
        this.datasetDescription = datasetDescription;
    }

    public String getDataPath() {
        return this.dataPath;
    }

    public GdiTransactionDataIndex dataPath(String dataPath) {
        this.setDataPath(dataPath);
        return this;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public Set<GdiMasterDataIndex> getMasterDataItems() {
        return this.masterDataItems;
    }

    public void setMasterDataItems(Set<GdiMasterDataIndex> gdiMasterDataIndices) {
        this.masterDataItems = gdiMasterDataIndices;
    }

    public GdiTransactionDataIndex masterDataItems(Set<GdiMasterDataIndex> gdiMasterDataIndices) {
        this.setMasterDataItems(gdiMasterDataIndices);
        return this;
    }

    public GdiTransactionDataIndex addMasterDataItem(GdiMasterDataIndex gdiMasterDataIndex) {
        this.masterDataItems.add(gdiMasterDataIndex);
        return this;
    }

    public GdiTransactionDataIndex removeMasterDataItem(GdiMasterDataIndex gdiMasterDataIndex) {
        this.masterDataItems.remove(gdiMasterDataIndex);
        return this;
    }

    public BusinessTeam getBusinessTeam() {
        return this.businessTeam;
    }

    public void setBusinessTeam(BusinessTeam businessTeam) {
        this.businessTeam = businessTeam;
    }

    public GdiTransactionDataIndex businessTeam(BusinessTeam businessTeam) {
        this.setBusinessTeam(businessTeam);
        return this;
    }

    public BusinessDocument getDataSetTemplate() {
        return this.dataSetTemplate;
    }

    public void setDataSetTemplate(BusinessDocument businessDocument) {
        this.dataSetTemplate = businessDocument;
    }

    public GdiTransactionDataIndex dataSetTemplate(BusinessDocument businessDocument) {
        this.setDataSetTemplate(businessDocument);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public GdiTransactionDataIndex placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public GdiTransactionDataIndex addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public GdiTransactionDataIndex removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GdiTransactionDataIndex)) {
            return false;
        }
        return id != null && id.equals(((GdiTransactionDataIndex) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GdiTransactionDataIndex{" +
            "id=" + getId() +
            ", datasetName='" + getDatasetName() + "'" +
            ", databaseName='" + getDatabaseName() + "'" +
            ", updateFrequency='" + getUpdateFrequency() + "'" +
            ", datasetBehavior='" + getDatasetBehavior() + "'" +
            ", minimumDataRowsPerRequest=" + getMinimumDataRowsPerRequest() +
            ", maximumDataRowsPerRequest=" + getMaximumDataRowsPerRequest() +
            ", datasetDescription='" + getDatasetDescription() + "'" +
            ", dataPath='" + getDataPath() + "'" +
            "}";
    }
}
