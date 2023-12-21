package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CardTypes} entity.
 */
public class CardTypesDTO implements Serializable {

    private Long id;

    @NotNull
    private String cardTypeCode;

    @NotNull
    private String cardType;

    @Lob
    private String cardTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardTypeDetails() {
        return cardTypeDetails;
    }

    public void setCardTypeDetails(String cardTypeDetails) {
        this.cardTypeDetails = cardTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardTypesDTO)) {
            return false;
        }

        CardTypesDTO cardTypesDTO = (CardTypesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardTypesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardTypesDTO{" +
            "id=" + getId() +
            ", cardTypeCode='" + getCardTypeCode() + "'" +
            ", cardType='" + getCardType() + "'" +
            ", cardTypeDetails='" + getCardTypeDetails() + "'" +
            "}";
    }
}
