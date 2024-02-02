package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import io.github.erp.domain.enumeration.CardCategoryFlag;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CardCategoryType} entity.
 */
public class CardCategoryTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private CardCategoryFlag cardCategoryFlag;

    @NotNull
    private String cardCategoryDescription;

    @Lob
    private String cardCategoryDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardCategoryFlag getCardCategoryFlag() {
        return cardCategoryFlag;
    }

    public void setCardCategoryFlag(CardCategoryFlag cardCategoryFlag) {
        this.cardCategoryFlag = cardCategoryFlag;
    }

    public String getCardCategoryDescription() {
        return cardCategoryDescription;
    }

    public void setCardCategoryDescription(String cardCategoryDescription) {
        this.cardCategoryDescription = cardCategoryDescription;
    }

    public String getCardCategoryDetails() {
        return cardCategoryDetails;
    }

    public void setCardCategoryDetails(String cardCategoryDetails) {
        this.cardCategoryDetails = cardCategoryDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardCategoryTypeDTO)) {
            return false;
        }

        CardCategoryTypeDTO cardCategoryTypeDTO = (CardCategoryTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardCategoryTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardCategoryTypeDTO{" +
            "id=" + getId() +
            ", cardCategoryFlag='" + getCardCategoryFlag() + "'" +
            ", cardCategoryDescription='" + getCardCategoryDescription() + "'" +
            ", cardCategoryDetails='" + getCardCategoryDetails() + "'" +
            "}";
    }
}
