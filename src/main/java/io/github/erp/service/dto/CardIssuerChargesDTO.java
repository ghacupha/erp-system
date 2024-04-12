package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CardIssuerCharges} entity.
 */
public class CardIssuerChargesDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal cardFeeChargeInLCY;

    private InstitutionCodeDTO bankCode;

    private CardCategoryTypeDTO cardCategory;

    private CardTypesDTO cardType;

    private CardBrandTypeDTO cardBrand;

    private CardClassTypeDTO cardClass;

    private CardChargesDTO cardChargeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public BigDecimal getCardFeeChargeInLCY() {
        return cardFeeChargeInLCY;
    }

    public void setCardFeeChargeInLCY(BigDecimal cardFeeChargeInLCY) {
        this.cardFeeChargeInLCY = cardFeeChargeInLCY;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public CardCategoryTypeDTO getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(CardCategoryTypeDTO cardCategory) {
        this.cardCategory = cardCategory;
    }

    public CardTypesDTO getCardType() {
        return cardType;
    }

    public void setCardType(CardTypesDTO cardType) {
        this.cardType = cardType;
    }

    public CardBrandTypeDTO getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(CardBrandTypeDTO cardBrand) {
        this.cardBrand = cardBrand;
    }

    public CardClassTypeDTO getCardClass() {
        return cardClass;
    }

    public void setCardClass(CardClassTypeDTO cardClass) {
        this.cardClass = cardClass;
    }

    public CardChargesDTO getCardChargeType() {
        return cardChargeType;
    }

    public void setCardChargeType(CardChargesDTO cardChargeType) {
        this.cardChargeType = cardChargeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardIssuerChargesDTO)) {
            return false;
        }

        CardIssuerChargesDTO cardIssuerChargesDTO = (CardIssuerChargesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardIssuerChargesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardIssuerChargesDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", cardFeeChargeInLCY=" + getCardFeeChargeInLCY() +
            ", bankCode=" + getBankCode() +
            ", cardCategory=" + getCardCategory() +
            ", cardType=" + getCardType() +
            ", cardBrand=" + getCardBrand() +
            ", cardClass=" + getCardClass() +
            ", cardChargeType=" + getCardChargeType() +
            "}";
    }
}
