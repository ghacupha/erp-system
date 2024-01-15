package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.CreditCardFacility} entity. This class is used
 * in {@link io.github.erp.web.rest.CreditCardFacilityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /credit-card-facilities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CreditCardFacilityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private IntegerFilter totalNumberOfActiveCreditCards;

    private BigDecimalFilter totalCreditCardLimitsInCCY;

    private BigDecimalFilter totalCreditCardLimitsInLCY;

    private BigDecimalFilter totalCreditCardAmountUtilisedInCCY;

    private BigDecimalFilter totalCreditCardAmountUtilisedInLcy;

    private BigDecimalFilter totalNPACreditCardAmountInFCY;

    private BigDecimalFilter totalNPACreditCardAmountInLCY;

    private LongFilter bankCodeId;

    private LongFilter customerCategoryId;

    private LongFilter currencyCodeId;

    private Boolean distinct;

    public CreditCardFacilityCriteria() {}

    public CreditCardFacilityCriteria(CreditCardFacilityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.totalNumberOfActiveCreditCards =
            other.totalNumberOfActiveCreditCards == null ? null : other.totalNumberOfActiveCreditCards.copy();
        this.totalCreditCardLimitsInCCY = other.totalCreditCardLimitsInCCY == null ? null : other.totalCreditCardLimitsInCCY.copy();
        this.totalCreditCardLimitsInLCY = other.totalCreditCardLimitsInLCY == null ? null : other.totalCreditCardLimitsInLCY.copy();
        this.totalCreditCardAmountUtilisedInCCY =
            other.totalCreditCardAmountUtilisedInCCY == null ? null : other.totalCreditCardAmountUtilisedInCCY.copy();
        this.totalCreditCardAmountUtilisedInLcy =
            other.totalCreditCardAmountUtilisedInLcy == null ? null : other.totalCreditCardAmountUtilisedInLcy.copy();
        this.totalNPACreditCardAmountInFCY =
            other.totalNPACreditCardAmountInFCY == null ? null : other.totalNPACreditCardAmountInFCY.copy();
        this.totalNPACreditCardAmountInLCY =
            other.totalNPACreditCardAmountInLCY == null ? null : other.totalNPACreditCardAmountInLCY.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.customerCategoryId = other.customerCategoryId == null ? null : other.customerCategoryId.copy();
        this.currencyCodeId = other.currencyCodeId == null ? null : other.currencyCodeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CreditCardFacilityCriteria copy() {
        return new CreditCardFacilityCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getReportingDate() {
        return reportingDate;
    }

    public LocalDateFilter reportingDate() {
        if (reportingDate == null) {
            reportingDate = new LocalDateFilter();
        }
        return reportingDate;
    }

    public void setReportingDate(LocalDateFilter reportingDate) {
        this.reportingDate = reportingDate;
    }

    public IntegerFilter getTotalNumberOfActiveCreditCards() {
        return totalNumberOfActiveCreditCards;
    }

    public IntegerFilter totalNumberOfActiveCreditCards() {
        if (totalNumberOfActiveCreditCards == null) {
            totalNumberOfActiveCreditCards = new IntegerFilter();
        }
        return totalNumberOfActiveCreditCards;
    }

    public void setTotalNumberOfActiveCreditCards(IntegerFilter totalNumberOfActiveCreditCards) {
        this.totalNumberOfActiveCreditCards = totalNumberOfActiveCreditCards;
    }

    public BigDecimalFilter getTotalCreditCardLimitsInCCY() {
        return totalCreditCardLimitsInCCY;
    }

    public BigDecimalFilter totalCreditCardLimitsInCCY() {
        if (totalCreditCardLimitsInCCY == null) {
            totalCreditCardLimitsInCCY = new BigDecimalFilter();
        }
        return totalCreditCardLimitsInCCY;
    }

    public void setTotalCreditCardLimitsInCCY(BigDecimalFilter totalCreditCardLimitsInCCY) {
        this.totalCreditCardLimitsInCCY = totalCreditCardLimitsInCCY;
    }

    public BigDecimalFilter getTotalCreditCardLimitsInLCY() {
        return totalCreditCardLimitsInLCY;
    }

    public BigDecimalFilter totalCreditCardLimitsInLCY() {
        if (totalCreditCardLimitsInLCY == null) {
            totalCreditCardLimitsInLCY = new BigDecimalFilter();
        }
        return totalCreditCardLimitsInLCY;
    }

    public void setTotalCreditCardLimitsInLCY(BigDecimalFilter totalCreditCardLimitsInLCY) {
        this.totalCreditCardLimitsInLCY = totalCreditCardLimitsInLCY;
    }

    public BigDecimalFilter getTotalCreditCardAmountUtilisedInCCY() {
        return totalCreditCardAmountUtilisedInCCY;
    }

    public BigDecimalFilter totalCreditCardAmountUtilisedInCCY() {
        if (totalCreditCardAmountUtilisedInCCY == null) {
            totalCreditCardAmountUtilisedInCCY = new BigDecimalFilter();
        }
        return totalCreditCardAmountUtilisedInCCY;
    }

    public void setTotalCreditCardAmountUtilisedInCCY(BigDecimalFilter totalCreditCardAmountUtilisedInCCY) {
        this.totalCreditCardAmountUtilisedInCCY = totalCreditCardAmountUtilisedInCCY;
    }

    public BigDecimalFilter getTotalCreditCardAmountUtilisedInLcy() {
        return totalCreditCardAmountUtilisedInLcy;
    }

    public BigDecimalFilter totalCreditCardAmountUtilisedInLcy() {
        if (totalCreditCardAmountUtilisedInLcy == null) {
            totalCreditCardAmountUtilisedInLcy = new BigDecimalFilter();
        }
        return totalCreditCardAmountUtilisedInLcy;
    }

    public void setTotalCreditCardAmountUtilisedInLcy(BigDecimalFilter totalCreditCardAmountUtilisedInLcy) {
        this.totalCreditCardAmountUtilisedInLcy = totalCreditCardAmountUtilisedInLcy;
    }

    public BigDecimalFilter getTotalNPACreditCardAmountInFCY() {
        return totalNPACreditCardAmountInFCY;
    }

    public BigDecimalFilter totalNPACreditCardAmountInFCY() {
        if (totalNPACreditCardAmountInFCY == null) {
            totalNPACreditCardAmountInFCY = new BigDecimalFilter();
        }
        return totalNPACreditCardAmountInFCY;
    }

    public void setTotalNPACreditCardAmountInFCY(BigDecimalFilter totalNPACreditCardAmountInFCY) {
        this.totalNPACreditCardAmountInFCY = totalNPACreditCardAmountInFCY;
    }

    public BigDecimalFilter getTotalNPACreditCardAmountInLCY() {
        return totalNPACreditCardAmountInLCY;
    }

    public BigDecimalFilter totalNPACreditCardAmountInLCY() {
        if (totalNPACreditCardAmountInLCY == null) {
            totalNPACreditCardAmountInLCY = new BigDecimalFilter();
        }
        return totalNPACreditCardAmountInLCY;
    }

    public void setTotalNPACreditCardAmountInLCY(BigDecimalFilter totalNPACreditCardAmountInLCY) {
        this.totalNPACreditCardAmountInLCY = totalNPACreditCardAmountInLCY;
    }

    public LongFilter getBankCodeId() {
        return bankCodeId;
    }

    public LongFilter bankCodeId() {
        if (bankCodeId == null) {
            bankCodeId = new LongFilter();
        }
        return bankCodeId;
    }

    public void setBankCodeId(LongFilter bankCodeId) {
        this.bankCodeId = bankCodeId;
    }

    public LongFilter getCustomerCategoryId() {
        return customerCategoryId;
    }

    public LongFilter customerCategoryId() {
        if (customerCategoryId == null) {
            customerCategoryId = new LongFilter();
        }
        return customerCategoryId;
    }

    public void setCustomerCategoryId(LongFilter customerCategoryId) {
        this.customerCategoryId = customerCategoryId;
    }

    public LongFilter getCurrencyCodeId() {
        return currencyCodeId;
    }

    public LongFilter currencyCodeId() {
        if (currencyCodeId == null) {
            currencyCodeId = new LongFilter();
        }
        return currencyCodeId;
    }

    public void setCurrencyCodeId(LongFilter currencyCodeId) {
        this.currencyCodeId = currencyCodeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CreditCardFacilityCriteria that = (CreditCardFacilityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(totalNumberOfActiveCreditCards, that.totalNumberOfActiveCreditCards) &&
            Objects.equals(totalCreditCardLimitsInCCY, that.totalCreditCardLimitsInCCY) &&
            Objects.equals(totalCreditCardLimitsInLCY, that.totalCreditCardLimitsInLCY) &&
            Objects.equals(totalCreditCardAmountUtilisedInCCY, that.totalCreditCardAmountUtilisedInCCY) &&
            Objects.equals(totalCreditCardAmountUtilisedInLcy, that.totalCreditCardAmountUtilisedInLcy) &&
            Objects.equals(totalNPACreditCardAmountInFCY, that.totalNPACreditCardAmountInFCY) &&
            Objects.equals(totalNPACreditCardAmountInLCY, that.totalNPACreditCardAmountInLCY) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(customerCategoryId, that.customerCategoryId) &&
            Objects.equals(currencyCodeId, that.currencyCodeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportingDate,
            totalNumberOfActiveCreditCards,
            totalCreditCardLimitsInCCY,
            totalCreditCardLimitsInLCY,
            totalCreditCardAmountUtilisedInCCY,
            totalCreditCardAmountUtilisedInLcy,
            totalNPACreditCardAmountInFCY,
            totalNPACreditCardAmountInLCY,
            bankCodeId,
            customerCategoryId,
            currencyCodeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditCardFacilityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (totalNumberOfActiveCreditCards != null ? "totalNumberOfActiveCreditCards=" + totalNumberOfActiveCreditCards + ", " : "") +
            (totalCreditCardLimitsInCCY != null ? "totalCreditCardLimitsInCCY=" + totalCreditCardLimitsInCCY + ", " : "") +
            (totalCreditCardLimitsInLCY != null ? "totalCreditCardLimitsInLCY=" + totalCreditCardLimitsInLCY + ", " : "") +
            (totalCreditCardAmountUtilisedInCCY != null ? "totalCreditCardAmountUtilisedInCCY=" + totalCreditCardAmountUtilisedInCCY + ", " : "") +
            (totalCreditCardAmountUtilisedInLcy != null ? "totalCreditCardAmountUtilisedInLcy=" + totalCreditCardAmountUtilisedInLcy + ", " : "") +
            (totalNPACreditCardAmountInFCY != null ? "totalNPACreditCardAmountInFCY=" + totalNPACreditCardAmountInFCY + ", " : "") +
            (totalNPACreditCardAmountInLCY != null ? "totalNPACreditCardAmountInLCY=" + totalNPACreditCardAmountInLCY + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (customerCategoryId != null ? "customerCategoryId=" + customerCategoryId + ", " : "") +
            (currencyCodeId != null ? "currencyCodeId=" + currencyCodeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
