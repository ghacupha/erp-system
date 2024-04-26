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
 * A DTO for the {@link io.github.erp.domain.FxTransactionRateType} entity.
 */
public class FxTransactionRateTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String fxTransactionRateTypeCode;

    @NotNull
    private String fxTransactionRateType;

    @Lob
    private String fxTransactionRateTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFxTransactionRateTypeCode() {
        return fxTransactionRateTypeCode;
    }

    public void setFxTransactionRateTypeCode(String fxTransactionRateTypeCode) {
        this.fxTransactionRateTypeCode = fxTransactionRateTypeCode;
    }

    public String getFxTransactionRateType() {
        return fxTransactionRateType;
    }

    public void setFxTransactionRateType(String fxTransactionRateType) {
        this.fxTransactionRateType = fxTransactionRateType;
    }

    public String getFxTransactionRateTypeDetails() {
        return fxTransactionRateTypeDetails;
    }

    public void setFxTransactionRateTypeDetails(String fxTransactionRateTypeDetails) {
        this.fxTransactionRateTypeDetails = fxTransactionRateTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxTransactionRateTypeDTO)) {
            return false;
        }

        FxTransactionRateTypeDTO fxTransactionRateTypeDTO = (FxTransactionRateTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fxTransactionRateTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxTransactionRateTypeDTO{" +
            "id=" + getId() +
            ", fxTransactionRateTypeCode='" + getFxTransactionRateTypeCode() + "'" +
            ", fxTransactionRateType='" + getFxTransactionRateType() + "'" +
            ", fxTransactionRateTypeDetails='" + getFxTransactionRateTypeDetails() + "'" +
            "}";
    }
}
