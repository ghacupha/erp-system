package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.ExchangeRate} entity. This class is used
 * in {@link io.github.erp.web.rest.ExchangeRateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /exchange-rates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExchangeRateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter businessReportingDay;

    private DoubleFilter buyingRate;

    private DoubleFilter sellingRate;

    private DoubleFilter meanRate;

    private DoubleFilter closingBidRate;

    private DoubleFilter closingOfferRate;

    private DoubleFilter usdCrossRate;

    private LongFilter institutionCodeId;

    private LongFilter currencyCodeId;

    private Boolean distinct;

    public ExchangeRateCriteria() {}

    public ExchangeRateCriteria(ExchangeRateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.businessReportingDay = other.businessReportingDay == null ? null : other.businessReportingDay.copy();
        this.buyingRate = other.buyingRate == null ? null : other.buyingRate.copy();
        this.sellingRate = other.sellingRate == null ? null : other.sellingRate.copy();
        this.meanRate = other.meanRate == null ? null : other.meanRate.copy();
        this.closingBidRate = other.closingBidRate == null ? null : other.closingBidRate.copy();
        this.closingOfferRate = other.closingOfferRate == null ? null : other.closingOfferRate.copy();
        this.usdCrossRate = other.usdCrossRate == null ? null : other.usdCrossRate.copy();
        this.institutionCodeId = other.institutionCodeId == null ? null : other.institutionCodeId.copy();
        this.currencyCodeId = other.currencyCodeId == null ? null : other.currencyCodeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExchangeRateCriteria copy() {
        return new ExchangeRateCriteria(this);
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

    public LocalDateFilter getBusinessReportingDay() {
        return businessReportingDay;
    }

    public LocalDateFilter businessReportingDay() {
        if (businessReportingDay == null) {
            businessReportingDay = new LocalDateFilter();
        }
        return businessReportingDay;
    }

    public void setBusinessReportingDay(LocalDateFilter businessReportingDay) {
        this.businessReportingDay = businessReportingDay;
    }

    public DoubleFilter getBuyingRate() {
        return buyingRate;
    }

    public DoubleFilter buyingRate() {
        if (buyingRate == null) {
            buyingRate = new DoubleFilter();
        }
        return buyingRate;
    }

    public void setBuyingRate(DoubleFilter buyingRate) {
        this.buyingRate = buyingRate;
    }

    public DoubleFilter getSellingRate() {
        return sellingRate;
    }

    public DoubleFilter sellingRate() {
        if (sellingRate == null) {
            sellingRate = new DoubleFilter();
        }
        return sellingRate;
    }

    public void setSellingRate(DoubleFilter sellingRate) {
        this.sellingRate = sellingRate;
    }

    public DoubleFilter getMeanRate() {
        return meanRate;
    }

    public DoubleFilter meanRate() {
        if (meanRate == null) {
            meanRate = new DoubleFilter();
        }
        return meanRate;
    }

    public void setMeanRate(DoubleFilter meanRate) {
        this.meanRate = meanRate;
    }

    public DoubleFilter getClosingBidRate() {
        return closingBidRate;
    }

    public DoubleFilter closingBidRate() {
        if (closingBidRate == null) {
            closingBidRate = new DoubleFilter();
        }
        return closingBidRate;
    }

    public void setClosingBidRate(DoubleFilter closingBidRate) {
        this.closingBidRate = closingBidRate;
    }

    public DoubleFilter getClosingOfferRate() {
        return closingOfferRate;
    }

    public DoubleFilter closingOfferRate() {
        if (closingOfferRate == null) {
            closingOfferRate = new DoubleFilter();
        }
        return closingOfferRate;
    }

    public void setClosingOfferRate(DoubleFilter closingOfferRate) {
        this.closingOfferRate = closingOfferRate;
    }

    public DoubleFilter getUsdCrossRate() {
        return usdCrossRate;
    }

    public DoubleFilter usdCrossRate() {
        if (usdCrossRate == null) {
            usdCrossRate = new DoubleFilter();
        }
        return usdCrossRate;
    }

    public void setUsdCrossRate(DoubleFilter usdCrossRate) {
        this.usdCrossRate = usdCrossRate;
    }

    public LongFilter getInstitutionCodeId() {
        return institutionCodeId;
    }

    public LongFilter institutionCodeId() {
        if (institutionCodeId == null) {
            institutionCodeId = new LongFilter();
        }
        return institutionCodeId;
    }

    public void setInstitutionCodeId(LongFilter institutionCodeId) {
        this.institutionCodeId = institutionCodeId;
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
        final ExchangeRateCriteria that = (ExchangeRateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(businessReportingDay, that.businessReportingDay) &&
            Objects.equals(buyingRate, that.buyingRate) &&
            Objects.equals(sellingRate, that.sellingRate) &&
            Objects.equals(meanRate, that.meanRate) &&
            Objects.equals(closingBidRate, that.closingBidRate) &&
            Objects.equals(closingOfferRate, that.closingOfferRate) &&
            Objects.equals(usdCrossRate, that.usdCrossRate) &&
            Objects.equals(institutionCodeId, that.institutionCodeId) &&
            Objects.equals(currencyCodeId, that.currencyCodeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            businessReportingDay,
            buyingRate,
            sellingRate,
            meanRate,
            closingBidRate,
            closingOfferRate,
            usdCrossRate,
            institutionCodeId,
            currencyCodeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExchangeRateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (businessReportingDay != null ? "businessReportingDay=" + businessReportingDay + ", " : "") +
            (buyingRate != null ? "buyingRate=" + buyingRate + ", " : "") +
            (sellingRate != null ? "sellingRate=" + sellingRate + ", " : "") +
            (meanRate != null ? "meanRate=" + meanRate + ", " : "") +
            (closingBidRate != null ? "closingBidRate=" + closingBidRate + ", " : "") +
            (closingOfferRate != null ? "closingOfferRate=" + closingOfferRate + ", " : "") +
            (usdCrossRate != null ? "usdCrossRate=" + usdCrossRate + ", " : "") +
            (institutionCodeId != null ? "institutionCodeId=" + institutionCodeId + ", " : "") +
            (currencyCodeId != null ? "currencyCodeId=" + currencyCodeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
