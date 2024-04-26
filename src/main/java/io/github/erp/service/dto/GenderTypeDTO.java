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
import io.github.erp.domain.enumeration.genderTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.GenderType} entity.
 */
public class GenderTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String genderCode;

    @NotNull
    private genderTypes genderType;

    @Lob
    private String genderDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public genderTypes getGenderType() {
        return genderType;
    }

    public void setGenderType(genderTypes genderType) {
        this.genderType = genderType;
    }

    public String getGenderDescription() {
        return genderDescription;
    }

    public void setGenderDescription(String genderDescription) {
        this.genderDescription = genderDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenderTypeDTO)) {
            return false;
        }

        GenderTypeDTO genderTypeDTO = (GenderTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, genderTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenderTypeDTO{" +
            "id=" + getId() +
            ", genderCode='" + getGenderCode() + "'" +
            ", genderType='" + getGenderType() + "'" +
            ", genderDescription='" + getGenderDescription() + "'" +
            "}";
    }
}
