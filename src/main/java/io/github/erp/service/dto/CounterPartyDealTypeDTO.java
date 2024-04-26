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
 * A DTO for the {@link io.github.erp.domain.CounterPartyDealType} entity.
 */
public class CounterPartyDealTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String counterpartyDealCode;

    @NotNull
    private String counterpartyDealTypeDetails;

    @Lob
    private String counterpartyDealTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCounterpartyDealCode() {
        return counterpartyDealCode;
    }

    public void setCounterpartyDealCode(String counterpartyDealCode) {
        this.counterpartyDealCode = counterpartyDealCode;
    }

    public String getCounterpartyDealTypeDetails() {
        return counterpartyDealTypeDetails;
    }

    public void setCounterpartyDealTypeDetails(String counterpartyDealTypeDetails) {
        this.counterpartyDealTypeDetails = counterpartyDealTypeDetails;
    }

    public String getCounterpartyDealTypeDescription() {
        return counterpartyDealTypeDescription;
    }

    public void setCounterpartyDealTypeDescription(String counterpartyDealTypeDescription) {
        this.counterpartyDealTypeDescription = counterpartyDealTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CounterPartyDealTypeDTO)) {
            return false;
        }

        CounterPartyDealTypeDTO counterPartyDealTypeDTO = (CounterPartyDealTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, counterPartyDealTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounterPartyDealTypeDTO{" +
            "id=" + getId() +
            ", counterpartyDealCode='" + getCounterpartyDealCode() + "'" +
            ", counterpartyDealTypeDetails='" + getCounterpartyDealTypeDetails() + "'" +
            ", counterpartyDealTypeDescription='" + getCounterpartyDealTypeDescription() + "'" +
            "}";
    }
}
