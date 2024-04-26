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
 * A DTO for the {@link io.github.erp.domain.InstitutionStatusType} entity.
 */
public class InstitutionStatusTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String institutionStatusCode;

    private String institutionStatusType;

    @Lob
    private String insitutionStatusTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstitutionStatusCode() {
        return institutionStatusCode;
    }

    public void setInstitutionStatusCode(String institutionStatusCode) {
        this.institutionStatusCode = institutionStatusCode;
    }

    public String getInstitutionStatusType() {
        return institutionStatusType;
    }

    public void setInstitutionStatusType(String institutionStatusType) {
        this.institutionStatusType = institutionStatusType;
    }

    public String getInsitutionStatusTypeDescription() {
        return insitutionStatusTypeDescription;
    }

    public void setInsitutionStatusTypeDescription(String insitutionStatusTypeDescription) {
        this.insitutionStatusTypeDescription = insitutionStatusTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstitutionStatusTypeDTO)) {
            return false;
        }

        InstitutionStatusTypeDTO institutionStatusTypeDTO = (InstitutionStatusTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, institutionStatusTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstitutionStatusTypeDTO{" +
            "id=" + getId() +
            ", institutionStatusCode='" + getInstitutionStatusCode() + "'" +
            ", institutionStatusType='" + getInstitutionStatusType() + "'" +
            ", insitutionStatusTypeDescription='" + getInsitutionStatusTypeDescription() + "'" +
            "}";
    }
}
