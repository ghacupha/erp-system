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
import io.github.erp.domain.enumeration.CardStateFlagTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CardState} entity.
 */
public class CardStateDTO implements Serializable {

    private Long id;

    @NotNull
    private CardStateFlagTypes cardStateFlag;

    @NotNull
    private String cardStateFlagDetails;

    private String cardStateFlagDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardStateFlagTypes getCardStateFlag() {
        return cardStateFlag;
    }

    public void setCardStateFlag(CardStateFlagTypes cardStateFlag) {
        this.cardStateFlag = cardStateFlag;
    }

    public String getCardStateFlagDetails() {
        return cardStateFlagDetails;
    }

    public void setCardStateFlagDetails(String cardStateFlagDetails) {
        this.cardStateFlagDetails = cardStateFlagDetails;
    }

    public String getCardStateFlagDescription() {
        return cardStateFlagDescription;
    }

    public void setCardStateFlagDescription(String cardStateFlagDescription) {
        this.cardStateFlagDescription = cardStateFlagDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardStateDTO)) {
            return false;
        }

        CardStateDTO cardStateDTO = (CardStateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardStateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardStateDTO{" +
            "id=" + getId() +
            ", cardStateFlag='" + getCardStateFlag() + "'" +
            ", cardStateFlagDetails='" + getCardStateFlagDetails() + "'" +
            ", cardStateFlagDescription='" + getCardStateFlagDescription() + "'" +
            "}";
    }
}
