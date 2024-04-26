package io.github.erp.domain;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExchangeRate.
 */
@Entity
@Table(name = "exchange_rate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "exchangerate")
public class ExchangeRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "business_reporting_day", nullable = false)
    private LocalDate businessReportingDay;

    @NotNull
    @Column(name = "buying_rate", nullable = false)
    private Double buyingRate;

    @NotNull
    @Column(name = "selling_rate", nullable = false)
    private Double sellingRate;

    @NotNull
    @Column(name = "mean_rate", nullable = false)
    private Double meanRate;

    @NotNull
    @Column(name = "closing_bid_rate", nullable = false)
    private Double closingBidRate;

    @NotNull
    @Column(name = "closing_offer_rate", nullable = false)
    private Double closingOfferRate;

    @NotNull
    @Column(name = "usd_cross_rate", nullable = false)
    private Double usdCrossRate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode institutionCode;

    @ManyToOne(optional = false)
    @NotNull
    private IsoCurrencyCode currencyCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExchangeRate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBusinessReportingDay() {
        return this.businessReportingDay;
    }

    public ExchangeRate businessReportingDay(LocalDate businessReportingDay) {
        this.setBusinessReportingDay(businessReportingDay);
        return this;
    }

    public void setBusinessReportingDay(LocalDate businessReportingDay) {
        this.businessReportingDay = businessReportingDay;
    }

    public Double getBuyingRate() {
        return this.buyingRate;
    }

    public ExchangeRate buyingRate(Double buyingRate) {
        this.setBuyingRate(buyingRate);
        return this;
    }

    public void setBuyingRate(Double buyingRate) {
        this.buyingRate = buyingRate;
    }

    public Double getSellingRate() {
        return this.sellingRate;
    }

    public ExchangeRate sellingRate(Double sellingRate) {
        this.setSellingRate(sellingRate);
        return this;
    }

    public void setSellingRate(Double sellingRate) {
        this.sellingRate = sellingRate;
    }

    public Double getMeanRate() {
        return this.meanRate;
    }

    public ExchangeRate meanRate(Double meanRate) {
        this.setMeanRate(meanRate);
        return this;
    }

    public void setMeanRate(Double meanRate) {
        this.meanRate = meanRate;
    }

    public Double getClosingBidRate() {
        return this.closingBidRate;
    }

    public ExchangeRate closingBidRate(Double closingBidRate) {
        this.setClosingBidRate(closingBidRate);
        return this;
    }

    public void setClosingBidRate(Double closingBidRate) {
        this.closingBidRate = closingBidRate;
    }

    public Double getClosingOfferRate() {
        return this.closingOfferRate;
    }

    public ExchangeRate closingOfferRate(Double closingOfferRate) {
        this.setClosingOfferRate(closingOfferRate);
        return this;
    }

    public void setClosingOfferRate(Double closingOfferRate) {
        this.closingOfferRate = closingOfferRate;
    }

    public Double getUsdCrossRate() {
        return this.usdCrossRate;
    }

    public ExchangeRate usdCrossRate(Double usdCrossRate) {
        this.setUsdCrossRate(usdCrossRate);
        return this;
    }

    public void setUsdCrossRate(Double usdCrossRate) {
        this.usdCrossRate = usdCrossRate;
    }

    public InstitutionCode getInstitutionCode() {
        return this.institutionCode;
    }

    public void setInstitutionCode(InstitutionCode institutionCode) {
        this.institutionCode = institutionCode;
    }

    public ExchangeRate institutionCode(InstitutionCode institutionCode) {
        this.setInstitutionCode(institutionCode);
        return this;
    }

    public IsoCurrencyCode getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(IsoCurrencyCode isoCurrencyCode) {
        this.currencyCode = isoCurrencyCode;
    }

    public ExchangeRate currencyCode(IsoCurrencyCode isoCurrencyCode) {
        this.setCurrencyCode(isoCurrencyCode);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExchangeRate)) {
            return false;
        }
        return id != null && id.equals(((ExchangeRate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExchangeRate{" +
            "id=" + getId() +
            ", businessReportingDay='" + getBusinessReportingDay() + "'" +
            ", buyingRate=" + getBuyingRate() +
            ", sellingRate=" + getSellingRate() +
            ", meanRate=" + getMeanRate() +
            ", closingBidRate=" + getClosingBidRate() +
            ", closingOfferRate=" + getClosingOfferRate() +
            ", usdCrossRate=" + getUsdCrossRate() +
            "}";
    }
}
