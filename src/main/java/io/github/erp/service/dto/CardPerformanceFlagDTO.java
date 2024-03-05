package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
