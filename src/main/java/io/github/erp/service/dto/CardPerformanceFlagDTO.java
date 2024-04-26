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
import io.github.erp.domain.enumeration.CardPerformanceFlags;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CardPerformanceFlag} entity.
 */
public class CardPerformanceFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private CardPerformanceFlags cardPerformanceFlag;

    @NotNull
    private String cardPerformanceFlagDescription;

    @Lob
    private String cardPerformanceFlagDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardPerformanceFlags getCardPerformanceFlag() {
        return cardPerformanceFlag;
    }

    public void setCardPerformanceFlag(CardPerformanceFlags cardPerformanceFlag) {
        this.cardPerformanceFlag = cardPerformanceFlag;
    }

    public String getCardPerformanceFlagDescription() {
        return cardPerformanceFlagDescription;
    }

    public void setCardPerformanceFlagDescription(String cardPerformanceFlagDescription) {
        this.cardPerformanceFlagDescription = cardPerformanceFlagDescription;
    }

    public String getCardPerformanceFlagDetails() {
        return cardPerformanceFlagDetails;
    }

    public void setCardPerformanceFlagDetails(String cardPerformanceFlagDetails) {
        this.cardPerformanceFlagDetails = cardPerformanceFlagDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardPerformanceFlagDTO)) {
            return false;
        }

        CardPerformanceFlagDTO cardPerformanceFlagDTO = (CardPerformanceFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardPerformanceFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardPerformanceFlagDTO{" +
            "id=" + getId() +
            ", cardPerformanceFlag='" + getCardPerformanceFlag() + "'" +
            ", cardPerformanceFlagDescription='" + getCardPerformanceFlagDescription() + "'" +
            ", cardPerformanceFlagDetails='" + getCardPerformanceFlagDetails() + "'" +
            "}";
    }
}
