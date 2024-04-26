package io.github.erp.service.criteria;

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
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.AmortizationPostingReport} entity. This class is used
 * in {@link io.github.erp.web.rest.AmortizationPostingReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /amortization-posting-reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AmortizationPostingReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter catalogueNumber;

    private StringFilter debitAccount;

    private StringFilter creditAccount;

    private StringFilter description;

    private BigDecimalFilter amortizationAmount;

    private Boolean distinct;

    public AmortizationPostingReportCriteria() {}

    public AmortizationPostingReportCriteria(AmortizationPostingReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.catalogueNumber = other.catalogueNumber == null ? null : other.catalogueNumber.copy();
        this.debitAccount = other.debitAccount == null ? null : other.debitAccount.copy();
        this.creditAccount = other.creditAccount == null ? null : other.creditAccount.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.amortizationAmount = other.amortizationAmount == null ? null : other.amortizationAmount.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AmortizationPostingReportCriteria copy() {
        return new AmortizationPostingReportCriteria(this);
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

    public StringFilter getCatalogueNumber() {
        return catalogueNumber;
    }

    public StringFilter catalogueNumber() {
        if (catalogueNumber == null) {
            catalogueNumber = new StringFilter();
        }
        return catalogueNumber;
    }

    public void setCatalogueNumber(StringFilter catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public StringFilter getDebitAccount() {
        return debitAccount;
    }

    public StringFilter debitAccount() {
        if (debitAccount == null) {
            debitAccount = new StringFilter();
        }
        return debitAccount;
    }

    public void setDebitAccount(StringFilter debitAccount) {
        this.debitAccount = debitAccount;
    }

    public StringFilter getCreditAccount() {
        return creditAccount;
    }

    public StringFilter creditAccount() {
        if (creditAccount == null) {
            creditAccount = new StringFilter();
        }
        return creditAccount;
    }

    public void setCreditAccount(StringFilter creditAccount) {
        this.creditAccount = creditAccount;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getAmortizationAmount() {
        return amortizationAmount;
    }

    public BigDecimalFilter amortizationAmount() {
        if (amortizationAmount == null) {
            amortizationAmount = new BigDecimalFilter();
        }
        return amortizationAmount;
    }

    public void setAmortizationAmount(BigDecimalFilter amortizationAmount) {
        this.amortizationAmount = amortizationAmount;
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
        final AmortizationPostingReportCriteria that = (AmortizationPostingReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(catalogueNumber, that.catalogueNumber) &&
            Objects.equals(debitAccount, that.debitAccount) &&
            Objects.equals(creditAccount, that.creditAccount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(amortizationAmount, that.amortizationAmount) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, catalogueNumber, debitAccount, creditAccount, description, amortizationAmount, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmortizationPostingReportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (catalogueNumber != null ? "catalogueNumber=" + catalogueNumber + ", " : "") +
            (debitAccount != null ? "debitAccount=" + debitAccount + ", " : "") +
            (creditAccount != null ? "creditAccount=" + creditAccount + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (amortizationAmount != null ? "amortizationAmount=" + amortizationAmount + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
