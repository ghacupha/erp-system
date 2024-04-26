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
import io.github.erp.domain.enumeration.CurrencyAuthenticityFlags;
import io.github.erp.domain.enumeration.CurrencyAuthenticityTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CurrencyAuthenticityFlag} entity.
 */
public class CurrencyAuthenticityFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private CurrencyAuthenticityFlags currencyAuthenticityFlag;

    @NotNull
    private CurrencyAuthenticityTypes currencyAuthenticityType;

    @Lob
    private String currencyAuthenticityTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyAuthenticityFlags getCurrencyAuthenticityFlag() {
        return currencyAuthenticityFlag;
    }

    public void setCurrencyAuthenticityFlag(CurrencyAuthenticityFlags currencyAuthenticityFlag) {
        this.currencyAuthenticityFlag = currencyAuthenticityFlag;
    }

    public CurrencyAuthenticityTypes getCurrencyAuthenticityType() {
        return currencyAuthenticityType;
    }

    public void setCurrencyAuthenticityType(CurrencyAuthenticityTypes currencyAuthenticityType) {
        this.currencyAuthenticityType = currencyAuthenticityType;
    }

    public String getCurrencyAuthenticityTypeDetails() {
        return currencyAuthenticityTypeDetails;
    }

    public void setCurrencyAuthenticityTypeDetails(String currencyAuthenticityTypeDetails) {
        this.currencyAuthenticityTypeDetails = currencyAuthenticityTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyAuthenticityFlagDTO)) {
            return false;
        }

        CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO = (CurrencyAuthenticityFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, currencyAuthenticityFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyAuthenticityFlagDTO{" +
            "id=" + getId() +
            ", currencyAuthenticityFlag='" + getCurrencyAuthenticityFlag() + "'" +
            ", currencyAuthenticityType='" + getCurrencyAuthenticityType() + "'" +
            ", currencyAuthenticityTypeDetails='" + getCurrencyAuthenticityTypeDetails() + "'" +
            "}";
    }
}
