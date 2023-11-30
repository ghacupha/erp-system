package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
 * A DTO for the {@link io.github.erp.domain.CardCharges} entity.
 */
public class CardChargesDTO implements Serializable {

    private Long id;

    @NotNull
    private String cardChargeType;

    @NotNull
    private String cardChargeTypeName;

    @Lob
    private String cardChargeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardChargeType() {
        return cardChargeType;
    }

    public void setCardChargeType(String cardChargeType) {
        this.cardChargeType = cardChargeType;
    }

    public String getCardChargeTypeName() {
        return cardChargeTypeName;
    }

    public void setCardChargeTypeName(String cardChargeTypeName) {
        this.cardChargeTypeName = cardChargeTypeName;
    }

    public String getCardChargeDetails() {
        return cardChargeDetails;
    }

    public void setCardChargeDetails(String cardChargeDetails) {
        this.cardChargeDetails = cardChargeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardChargesDTO)) {
            return false;
        }

        CardChargesDTO cardChargesDTO = (CardChargesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardChargesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardChargesDTO{" +
            "id=" + getId() +
            ", cardChargeType='" + getCardChargeType() + "'" +
            ", cardChargeTypeName='" + getCardChargeTypeName() + "'" +
            ", cardChargeDetails='" + getCardChargeDetails() + "'" +
            "}";
    }
}
