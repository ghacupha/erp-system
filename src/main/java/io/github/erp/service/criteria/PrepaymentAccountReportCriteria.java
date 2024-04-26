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
 * Criteria class for the {@link io.github.erp.domain.PrepaymentAccountReport} entity. This class is used
 * in {@link io.github.erp.web.rest.PrepaymentAccountReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prepayment-account-reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrepaymentAccountReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter prepaymentAccount;

    private BigDecimalFilter prepaymentAmount;

    private BigDecimalFilter amortisedAmount;

    private BigDecimalFilter outstandingAmount;

    private IntegerFilter numberOfPrepaymentAccounts;

    private IntegerFilter numberOfAmortisedItems;

    private Boolean distinct;

    public PrepaymentAccountReportCriteria() {}

    public PrepaymentAccountReportCriteria(PrepaymentAccountReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.prepaymentAccount = other.prepaymentAccount == null ? null : other.prepaymentAccount.copy();
        this.prepaymentAmount = other.prepaymentAmount == null ? null : other.prepaymentAmount.copy();
        this.amortisedAmount = other.amortisedAmount == null ? null : other.amortisedAmount.copy();
        this.outstandingAmount = other.outstandingAmount == null ? null : other.outstandingAmount.copy();
        this.numberOfPrepaymentAccounts = other.numberOfPrepaymentAccounts == null ? null : other.numberOfPrepaymentAccounts.copy();
        this.numberOfAmortisedItems = other.numberOfAmortisedItems == null ? null : other.numberOfAmortisedItems.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PrepaymentAccountReportCriteria copy() {
        return new PrepaymentAccountReportCriteria(this);
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

    public StringFilter getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public StringFilter prepaymentAccount() {
        if (prepaymentAccount == null) {
            prepaymentAccount = new StringFilter();
        }
        return prepaymentAccount;
    }

    public void setPrepaymentAccount(StringFilter prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public BigDecimalFilter getPrepaymentAmount() {
        return prepaymentAmount;
    }

    public BigDecimalFilter prepaymentAmount() {
        if (prepaymentAmount == null) {
            prepaymentAmount = new BigDecimalFilter();
        }
        return prepaymentAmount;
    }

    public void setPrepaymentAmount(BigDecimalFilter prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
    }

    public BigDecimalFilter getAmortisedAmount() {
        return amortisedAmount;
    }

    public BigDecimalFilter amortisedAmount() {
        if (amortisedAmount == null) {
            amortisedAmount = new BigDecimalFilter();
        }
        return amortisedAmount;
    }

    public void setAmortisedAmount(BigDecimalFilter amortisedAmount) {
        this.amortisedAmount = amortisedAmount;
    }

    public BigDecimalFilter getOutstandingAmount() {
        return outstandingAmount;
    }

    public BigDecimalFilter outstandingAmount() {
        if (outstandingAmount == null) {
            outstandingAmount = new BigDecimalFilter();
        }
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimalFilter outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public IntegerFilter getNumberOfPrepaymentAccounts() {
        return numberOfPrepaymentAccounts;
    }

    public IntegerFilter numberOfPrepaymentAccounts() {
        if (numberOfPrepaymentAccounts == null) {
            numberOfPrepaymentAccounts = new IntegerFilter();
        }
        return numberOfPrepaymentAccounts;
    }

    public void setNumberOfPrepaymentAccounts(IntegerFilter numberOfPrepaymentAccounts) {
        this.numberOfPrepaymentAccounts = numberOfPrepaymentAccounts;
    }

    public IntegerFilter getNumberOfAmortisedItems() {
        return numberOfAmortisedItems;
    }

    public IntegerFilter numberOfAmortisedItems() {
        if (numberOfAmortisedItems == null) {
            numberOfAmortisedItems = new IntegerFilter();
        }
        return numberOfAmortisedItems;
    }

    public void setNumberOfAmortisedItems(IntegerFilter numberOfAmortisedItems) {
        this.numberOfAmortisedItems = numberOfAmortisedItems;
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
        final PrepaymentAccountReportCriteria that = (PrepaymentAccountReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(prepaymentAccount, that.prepaymentAccount) &&
            Objects.equals(prepaymentAmount, that.prepaymentAmount) &&
            Objects.equals(amortisedAmount, that.amortisedAmount) &&
            Objects.equals(outstandingAmount, that.outstandingAmount) &&
            Objects.equals(numberOfPrepaymentAccounts, that.numberOfPrepaymentAccounts) &&
            Objects.equals(numberOfAmortisedItems, that.numberOfAmortisedItems) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            prepaymentAccount,
            prepaymentAmount,
            amortisedAmount,
            outstandingAmount,
            numberOfPrepaymentAccounts,
            numberOfAmortisedItems,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentAccountReportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (prepaymentAccount != null ? "prepaymentAccount=" + prepaymentAccount + ", " : "") +
            (prepaymentAmount != null ? "prepaymentAmount=" + prepaymentAmount + ", " : "") +
            (amortisedAmount != null ? "amortisedAmount=" + amortisedAmount + ", " : "") +
            (outstandingAmount != null ? "outstandingAmount=" + outstandingAmount + ", " : "") +
            (numberOfPrepaymentAccounts != null ? "numberOfPrepaymentAccounts=" + numberOfPrepaymentAccounts + ", " : "") +
            (numberOfAmortisedItems != null ? "numberOfAmortisedItems=" + numberOfAmortisedItems + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
