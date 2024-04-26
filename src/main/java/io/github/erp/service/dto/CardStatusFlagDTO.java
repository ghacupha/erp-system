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
import io.github.erp.domain.enumeration.FlagCodes;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CardStatusFlag} entity.
 */
public class CardStatusFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private FlagCodes cardStatusFlag;

    @NotNull
    private String cardStatusFlagDescription;

    private String cardStatusFlagDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlagCodes getCardStatusFlag() {
        return cardStatusFlag;
    }

    public void setCardStatusFlag(FlagCodes cardStatusFlag) {
        this.cardStatusFlag = cardStatusFlag;
    }

    public String getCardStatusFlagDescription() {
        return cardStatusFlagDescription;
    }

    public void setCardStatusFlagDescription(String cardStatusFlagDescription) {
        this.cardStatusFlagDescription = cardStatusFlagDescription;
    }

    public String getCardStatusFlagDetails() {
        return cardStatusFlagDetails;
    }

    public void setCardStatusFlagDetails(String cardStatusFlagDetails) {
        this.cardStatusFlagDetails = cardStatusFlagDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardStatusFlagDTO)) {
            return false;
        }

        CardStatusFlagDTO cardStatusFlagDTO = (CardStatusFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardStatusFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardStatusFlagDTO{" +
            "id=" + getId() +
            ", cardStatusFlag='" + getCardStatusFlag() + "'" +
            ", cardStatusFlagDescription='" + getCardStatusFlagDescription() + "'" +
            ", cardStatusFlagDetails='" + getCardStatusFlagDetails() + "'" +
            "}";
    }
}
