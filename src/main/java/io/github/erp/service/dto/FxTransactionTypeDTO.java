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
 * A DTO for the {@link io.github.erp.domain.FxTransactionType} entity.
 */
public class FxTransactionTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String fxTransactionTypeCode;

    @NotNull
    private String fxTransactionType;

    @Lob
    private String fxTransactionTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFxTransactionTypeCode() {
        return fxTransactionTypeCode;
    }

    public void setFxTransactionTypeCode(String fxTransactionTypeCode) {
        this.fxTransactionTypeCode = fxTransactionTypeCode;
    }

    public String getFxTransactionType() {
        return fxTransactionType;
    }

    public void setFxTransactionType(String fxTransactionType) {
        this.fxTransactionType = fxTransactionType;
    }

    public String getFxTransactionTypeDescription() {
        return fxTransactionTypeDescription;
    }

    public void setFxTransactionTypeDescription(String fxTransactionTypeDescription) {
        this.fxTransactionTypeDescription = fxTransactionTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxTransactionTypeDTO)) {
            return false;
        }

        FxTransactionTypeDTO fxTransactionTypeDTO = (FxTransactionTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fxTransactionTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxTransactionTypeDTO{" +
            "id=" + getId() +
            ", fxTransactionTypeCode='" + getFxTransactionTypeCode() + "'" +
            ", fxTransactionType='" + getFxTransactionType() + "'" +
            ", fxTransactionTypeDescription='" + getFxTransactionTypeDescription() + "'" +
            "}";
    }
}
