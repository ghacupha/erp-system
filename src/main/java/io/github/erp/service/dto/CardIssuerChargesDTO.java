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
