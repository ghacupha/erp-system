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
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ExchangeRate} entity.
 */
public class ExchangeRateDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate businessReportingDay;

    @NotNull
    private Double buyingRate;

    @NotNull
    private Double sellingRate;

    @NotNull
    private Double meanRate;

    @NotNull
    private Double closingBidRate;

    @NotNull
    private Double closingOfferRate;

    @NotNull
    private Double usdCrossRate;

    private InstitutionCodeDTO institutionCode;

    private IsoCurrencyCodeDTO currencyCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBusinessReportingDay() {
        return businessReportingDay;
    }

    public void setBusinessReportingDay(LocalDate businessReportingDay) {
        this.businessReportingDay = businessReportingDay;
    }

    public Double getBuyingRate() {
        return buyingRate;
    }

    public void setBuyingRate(Double buyingRate) {
        this.buyingRate = buyingRate;
    }

    public Double getSellingRate() {
        return sellingRate;
    }

    public void setSellingRate(Double sellingRate) {
        this.sellingRate = sellingRate;
    }

    public Double getMeanRate() {
        return meanRate;
    }

    public void setMeanRate(Double meanRate) {
        this.meanRate = meanRate;
    }

    public Double getClosingBidRate() {
        return closingBidRate;
    }

    public void setClosingBidRate(Double closingBidRate) {
        this.closingBidRate = closingBidRate;
    }

    public Double getClosingOfferRate() {
        return closingOfferRate;
    }

    public void setClosingOfferRate(Double closingOfferRate) {
        this.closingOfferRate = closingOfferRate;
    }

    public Double getUsdCrossRate() {
        return usdCrossRate;
    }

    public void setUsdCrossRate(Double usdCrossRate) {
        this.usdCrossRate = usdCrossRate;
    }

    public InstitutionCodeDTO getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(InstitutionCodeDTO institutionCode) {
        this.institutionCode = institutionCode;
    }

    public IsoCurrencyCodeDTO getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(IsoCurrencyCodeDTO currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExchangeRateDTO)) {
            return false;
        }

        ExchangeRateDTO exchangeRateDTO = (ExchangeRateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, exchangeRateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExchangeRateDTO{" +
            "id=" + getId() +
            ", businessReportingDay='" + getBusinessReportingDay() + "'" +
            ", buyingRate=" + getBuyingRate() +
            ", sellingRate=" + getSellingRate() +
            ", meanRate=" + getMeanRate() +
            ", closingBidRate=" + getClosingBidRate() +
            ", closingOfferRate=" + getClosingOfferRate() +
            ", usdCrossRate=" + getUsdCrossRate() +
            ", institutionCode=" + getInstitutionCode() +
            ", currencyCode=" + getCurrencyCode() +
            "}";
    }
}
