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
import io.github.erp.domain.enumeration.CurrencyServiceability;
import io.github.erp.domain.enumeration.CurrencyServiceabilityFlagTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CurrencyServiceabilityFlag} entity.
 */
public class CurrencyServiceabilityFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private CurrencyServiceabilityFlagTypes currencyServiceabilityFlag;

    @NotNull
    private CurrencyServiceability currencyServiceability;

    @Lob
    private String currencyServiceabilityFlagDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyServiceabilityFlagTypes getCurrencyServiceabilityFlag() {
        return currencyServiceabilityFlag;
    }

    public void setCurrencyServiceabilityFlag(CurrencyServiceabilityFlagTypes currencyServiceabilityFlag) {
        this.currencyServiceabilityFlag = currencyServiceabilityFlag;
    }

    public CurrencyServiceability getCurrencyServiceability() {
        return currencyServiceability;
    }

    public void setCurrencyServiceability(CurrencyServiceability currencyServiceability) {
        this.currencyServiceability = currencyServiceability;
    }

    public String getCurrencyServiceabilityFlagDetails() {
        return currencyServiceabilityFlagDetails;
    }

    public void setCurrencyServiceabilityFlagDetails(String currencyServiceabilityFlagDetails) {
        this.currencyServiceabilityFlagDetails = currencyServiceabilityFlagDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyServiceabilityFlagDTO)) {
            return false;
        }

        CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO = (CurrencyServiceabilityFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, currencyServiceabilityFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyServiceabilityFlagDTO{" +
            "id=" + getId() +
            ", currencyServiceabilityFlag='" + getCurrencyServiceabilityFlag() + "'" +
            ", currencyServiceability='" + getCurrencyServiceability() + "'" +
            ", currencyServiceabilityFlagDetails='" + getCurrencyServiceabilityFlagDetails() + "'" +
            "}";
    }
}
