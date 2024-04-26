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
import io.github.erp.domain.enumeration.DepreciationTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.DepreciationMethod} entity.
 */
public class DepreciationMethodDTO implements Serializable {

    private Long id;

    @NotNull
    private String depreciationMethodName;

    private String description;

    @NotNull
    private DepreciationTypes depreciationType;

    @Lob
    private String remarks;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepreciationMethodName() {
        return depreciationMethodName;
    }

    public void setDepreciationMethodName(String depreciationMethodName) {
        this.depreciationMethodName = depreciationMethodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DepreciationTypes getDepreciationType() {
        return depreciationType;
    }

    public void setDepreciationType(DepreciationTypes depreciationType) {
        this.depreciationType = depreciationType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
        if (!(o instanceof DepreciationMethodDTO)) {
            return false;
        }

        DepreciationMethodDTO depreciationMethodDTO = (DepreciationMethodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depreciationMethodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationMethodDTO{" +
            "id=" + getId() +
            ", depreciationMethodName='" + getDepreciationMethodName() + "'" +
            ", description='" + getDescription() + "'" +
            ", depreciationType='" + getDepreciationType() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
