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
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.GdiMasterDataIndex} entity.
 */
public class GdiMasterDataIndexDTO implements Serializable {

    private Long id;

    @NotNull
    private String entityName;

    @NotNull
    private String databaseName;

    @Lob
    private String businessDescription;

    private String dataPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GdiMasterDataIndexDTO)) {
            return false;
        }

        GdiMasterDataIndexDTO gdiMasterDataIndexDTO = (GdiMasterDataIndexDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gdiMasterDataIndexDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GdiMasterDataIndexDTO{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            ", databaseName='" + getDatabaseName() + "'" +
            ", businessDescription='" + getBusinessDescription() + "'" +
            ", dataPath='" + getDataPath() + "'" +
            "}";
    }
}
