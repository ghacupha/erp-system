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
import io.github.erp.domain.enumeration.RemittanceType;
import io.github.erp.domain.enumeration.RemittanceTypeFlag;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.RemittanceFlag} entity.
 */
public class RemittanceFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private RemittanceTypeFlag remittanceTypeFlag;

    @NotNull
    private RemittanceType remittanceType;

    @Lob
    private String remittanceTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RemittanceTypeFlag getRemittanceTypeFlag() {
        return remittanceTypeFlag;
    }

    public void setRemittanceTypeFlag(RemittanceTypeFlag remittanceTypeFlag) {
        this.remittanceTypeFlag = remittanceTypeFlag;
    }

    public RemittanceType getRemittanceType() {
        return remittanceType;
    }

    public void setRemittanceType(RemittanceType remittanceType) {
        this.remittanceType = remittanceType;
    }

    public String getRemittanceTypeDetails() {
        return remittanceTypeDetails;
    }

    public void setRemittanceTypeDetails(String remittanceTypeDetails) {
        this.remittanceTypeDetails = remittanceTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RemittanceFlagDTO)) {
            return false;
        }

        RemittanceFlagDTO remittanceFlagDTO = (RemittanceFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, remittanceFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RemittanceFlagDTO{" +
            "id=" + getId() +
            ", remittanceTypeFlag='" + getRemittanceTypeFlag() + "'" +
            ", remittanceType='" + getRemittanceType() + "'" +
            ", remittanceTypeDetails='" + getRemittanceTypeDetails() + "'" +
            "}";
    }
}
