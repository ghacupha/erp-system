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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SnaSectorCode} entity.
 */
public class SnaSectorCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String sectorTypeCode;

    private String mainSectorCode;

    private String mainSectorTypeName;

    private String subSectorCode;

    private String subSectorName;

    private String subSubSectorCode;

    private String subSubSectorName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectorTypeCode() {
        return sectorTypeCode;
    }

    public void setSectorTypeCode(String sectorTypeCode) {
        this.sectorTypeCode = sectorTypeCode;
    }

    public String getMainSectorCode() {
        return mainSectorCode;
    }

    public void setMainSectorCode(String mainSectorCode) {
        this.mainSectorCode = mainSectorCode;
    }

    public String getMainSectorTypeName() {
        return mainSectorTypeName;
    }

    public void setMainSectorTypeName(String mainSectorTypeName) {
        this.mainSectorTypeName = mainSectorTypeName;
    }

    public String getSubSectorCode() {
        return subSectorCode;
    }

    public void setSubSectorCode(String subSectorCode) {
        this.subSectorCode = subSectorCode;
    }

    public String getSubSectorName() {
        return subSectorName;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    public String getSubSubSectorCode() {
        return subSubSectorCode;
    }

    public void setSubSubSectorCode(String subSubSectorCode) {
        this.subSubSectorCode = subSubSectorCode;
    }

    public String getSubSubSectorName() {
        return subSubSectorName;
    }

    public void setSubSubSectorName(String subSubSectorName) {
        this.subSubSectorName = subSubSectorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SnaSectorCodeDTO)) {
            return false;
        }

        SnaSectorCodeDTO snaSectorCodeDTO = (SnaSectorCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, snaSectorCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SnaSectorCodeDTO{" +
            "id=" + getId() +
            ", sectorTypeCode='" + getSectorTypeCode() + "'" +
            ", mainSectorCode='" + getMainSectorCode() + "'" +
            ", mainSectorTypeName='" + getMainSectorTypeName() + "'" +
            ", subSectorCode='" + getSubSectorCode() + "'" +
            ", subSectorName='" + getSubSectorName() + "'" +
            ", subSubSectorCode='" + getSubSubSectorCode() + "'" +
            ", subSubSectorName='" + getSubSubSectorName() + "'" +
            "}";
    }
}
