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
 * A DTO for the {@link io.github.erp.domain.KenyanCurrencyDenomination} entity.
 */
public class KenyanCurrencyDenominationDTO implements Serializable {

    private Long id;

    @NotNull
    private String currencyDenominationCode;

    @NotNull
    private String currencyDenominationType;

    private String currencyDenominationTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyDenominationCode() {
        return currencyDenominationCode;
    }

    public void setCurrencyDenominationCode(String currencyDenominationCode) {
        this.currencyDenominationCode = currencyDenominationCode;
    }

    public String getCurrencyDenominationType() {
        return currencyDenominationType;
    }

    public void setCurrencyDenominationType(String currencyDenominationType) {
        this.currencyDenominationType = currencyDenominationType;
    }

    public String getCurrencyDenominationTypeDetails() {
        return currencyDenominationTypeDetails;
    }

    public void setCurrencyDenominationTypeDetails(String currencyDenominationTypeDetails) {
        this.currencyDenominationTypeDetails = currencyDenominationTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KenyanCurrencyDenominationDTO)) {
            return false;
        }

        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = (KenyanCurrencyDenominationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, kenyanCurrencyDenominationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KenyanCurrencyDenominationDTO{" +
            "id=" + getId() +
            ", currencyDenominationCode='" + getCurrencyDenominationCode() + "'" +
            ", currencyDenominationType='" + getCurrencyDenominationType() + "'" +
            ", currencyDenominationTypeDetails='" + getCurrencyDenominationTypeDetails() + "'" +
            "}";
    }
}
