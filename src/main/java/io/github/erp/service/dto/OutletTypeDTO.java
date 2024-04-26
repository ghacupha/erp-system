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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.OutletType} entity.
 */
public class OutletTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String outletTypeCode;

    @NotNull
    private String outletType;

    private String outletTypeDetails;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutletTypeCode() {
        return outletTypeCode;
    }

    public void setOutletTypeCode(String outletTypeCode) {
        this.outletTypeCode = outletTypeCode;
    }

    public String getOutletType() {
        return outletType;
    }

    public void setOutletType(String outletType) {
        this.outletType = outletType;
    }

    public String getOutletTypeDetails() {
        return outletTypeDetails;
    }

    public void setOutletTypeDetails(String outletTypeDetails) {
        this.outletTypeDetails = outletTypeDetails;
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
        if (!(o instanceof OutletTypeDTO)) {
            return false;
        }

        OutletTypeDTO outletTypeDTO = (OutletTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, outletTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutletTypeDTO{" +
            "id=" + getId() +
            ", outletTypeCode='" + getOutletTypeCode() + "'" +
            ", outletType='" + getOutletType() + "'" +
            ", outletTypeDetails='" + getOutletTypeDetails() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
