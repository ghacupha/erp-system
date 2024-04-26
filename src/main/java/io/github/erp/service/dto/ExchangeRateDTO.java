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
