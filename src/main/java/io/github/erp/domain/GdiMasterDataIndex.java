package io.github.erp.domain;

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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A GdiMasterDataIndex.
 */
@Entity
@Table(name = "gdi_master_data_index")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "gdimasterdataindex")
public class GdiMasterDataIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "entity_name", nullable = false, unique = true)
    private String entityName;

    @NotNull
    @Column(name = "database_name", nullable = false, unique = true)
    private String databaseName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "business_description")
    private String businessDescription;

    @Column(name = "data_path")
    private String dataPath;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GdiMasterDataIndex id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public GdiMasterDataIndex entityName(String entityName) {
        this.setEntityName(entityName);
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public GdiMasterDataIndex databaseName(String databaseName) {
        this.setDatabaseName(databaseName);
        return this;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getBusinessDescription() {
        return this.businessDescription;
    }

    public GdiMasterDataIndex businessDescription(String businessDescription) {
        this.setBusinessDescription(businessDescription);
        return this;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public String getDataPath() {
        return this.dataPath;
    }

    public GdiMasterDataIndex dataPath(String dataPath) {
        this.setDataPath(dataPath);
        return this;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GdiMasterDataIndex)) {
            return false;
        }
        return id != null && id.equals(((GdiMasterDataIndex) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GdiMasterDataIndex{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            ", databaseName='" + getDatabaseName() + "'" +
            ", businessDescription='" + getBusinessDescription() + "'" +
            ", dataPath='" + getDataPath() + "'" +
            "}";
    }
}
