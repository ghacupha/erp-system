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
import io.github.erp.domain.enumeration.BranchStatusType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.OutletStatus} entity.
 */
public class OutletStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String branchStatusTypeCode;

    @NotNull
    private BranchStatusType branchStatusType;

    private String branchStatusTypeDescription;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchStatusTypeCode() {
        return branchStatusTypeCode;
    }

    public void setBranchStatusTypeCode(String branchStatusTypeCode) {
        this.branchStatusTypeCode = branchStatusTypeCode;
    }

    public BranchStatusType getBranchStatusType() {
        return branchStatusType;
    }

    public void setBranchStatusType(BranchStatusType branchStatusType) {
        this.branchStatusType = branchStatusType;
    }

    public String getBranchStatusTypeDescription() {
        return branchStatusTypeDescription;
    }

    public void setBranchStatusTypeDescription(String branchStatusTypeDescription) {
        this.branchStatusTypeDescription = branchStatusTypeDescription;
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
        if (!(o instanceof OutletStatusDTO)) {
            return false;
        }

        OutletStatusDTO outletStatusDTO = (OutletStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, outletStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutletStatusDTO{" +
            "id=" + getId() +
            ", branchStatusTypeCode='" + getBranchStatusTypeCode() + "'" +
            ", branchStatusType='" + getBranchStatusType() + "'" +
            ", branchStatusTypeDescription='" + getBranchStatusTypeDescription() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
