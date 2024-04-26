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
 * A DTO for the {@link io.github.erp.domain.FxRateType} entity.
 */
public class FxRateTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String fxRateCode;

    @NotNull
    private String fxRateType;

    @Lob
    private String fxRateDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFxRateCode() {
        return fxRateCode;
    }

    public void setFxRateCode(String fxRateCode) {
        this.fxRateCode = fxRateCode;
    }

    public String getFxRateType() {
        return fxRateType;
    }

    public void setFxRateType(String fxRateType) {
        this.fxRateType = fxRateType;
    }

    public String getFxRateDetails() {
        return fxRateDetails;
    }

    public void setFxRateDetails(String fxRateDetails) {
        this.fxRateDetails = fxRateDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxRateTypeDTO)) {
            return false;
        }

        FxRateTypeDTO fxRateTypeDTO = (FxRateTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fxRateTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxRateTypeDTO{" +
            "id=" + getId() +
            ", fxRateCode='" + getFxRateCode() + "'" +
            ", fxRateType='" + getFxRateType() + "'" +
            ", fxRateDetails='" + getFxRateDetails() + "'" +
            "}";
    }
}
