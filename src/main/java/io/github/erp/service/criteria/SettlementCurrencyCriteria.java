package io.github.erp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.SettlementCurrency} entity. This class is used
 * in {@link io.github.erp.web.rest.SettlementCurrencyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /settlement-currencies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SettlementCurrencyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter iso4217CurrencyCode;

    private StringFilter currencyName;

    private StringFilter country;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter placeholderId;

    private Boolean distinct;

    public SettlementCurrencyCriteria() {}

    public SettlementCurrencyCriteria(SettlementCurrencyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.iso4217CurrencyCode = other.iso4217CurrencyCode == null ? null : other.iso4217CurrencyCode.copy();
        this.currencyName = other.currencyName == null ? null : other.currencyName.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SettlementCurrencyCriteria copy() {
        return new SettlementCurrencyCriteria(this);
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

    public StringFilter getIso4217CurrencyCode() {
        return iso4217CurrencyCode;
    }

    public StringFilter iso4217CurrencyCode() {
        if (iso4217CurrencyCode == null) {
            iso4217CurrencyCode = new StringFilter();
        }
        return iso4217CurrencyCode;
    }

    public void setIso4217CurrencyCode(StringFilter iso4217CurrencyCode) {
        this.iso4217CurrencyCode = iso4217CurrencyCode;
    }

    public StringFilter getCurrencyName() {
        return currencyName;
    }

    public StringFilter currencyName() {
        if (currencyName == null) {
            currencyName = new StringFilter();
        }
        return currencyName;
    }

    public void setCurrencyName(StringFilter currencyName) {
        this.currencyName = currencyName;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getFileUploadToken() {
        return fileUploadToken;
    }

    public StringFilter fileUploadToken() {
        if (fileUploadToken == null) {
            fileUploadToken = new StringFilter();
        }
        return fileUploadToken;
    }

    public void setFileUploadToken(StringFilter fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public StringFilter getCompilationToken() {
        return compilationToken;
    }

    public StringFilter compilationToken() {
        if (compilationToken == null) {
            compilationToken = new StringFilter();
        }
        return compilationToken;
    }

    public void setCompilationToken(StringFilter compilationToken) {
        this.compilationToken = compilationToken;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
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
        final SettlementCurrencyCriteria that = (SettlementCurrencyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(iso4217CurrencyCode, that.iso4217CurrencyCode) &&
            Objects.equals(currencyName, that.currencyName) &&
            Objects.equals(country, that.country) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, iso4217CurrencyCode, currencyName, country, fileUploadToken, compilationToken, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettlementCurrencyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (iso4217CurrencyCode != null ? "iso4217CurrencyCode=" + iso4217CurrencyCode + ", " : "") +
            (currencyName != null ? "currencyName=" + currencyName + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
