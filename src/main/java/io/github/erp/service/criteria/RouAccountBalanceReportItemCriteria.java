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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.RouAccountBalanceReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.RouAccountBalanceReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-account-balance-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouAccountBalanceReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assetAccountName;

    private StringFilter assetAccountNumber;

    private StringFilter depreciationAccountNumber;

    private BigDecimalFilter netBookValue;

    private LocalDateFilter fiscalMonthEndDate;

    private Boolean distinct;

    public RouAccountBalanceReportItemCriteria() {}

    public RouAccountBalanceReportItemCriteria(RouAccountBalanceReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetAccountName = other.assetAccountName == null ? null : other.assetAccountName.copy();
        this.assetAccountNumber = other.assetAccountNumber == null ? null : other.assetAccountNumber.copy();
        this.depreciationAccountNumber = other.depreciationAccountNumber == null ? null : other.depreciationAccountNumber.copy();
        this.netBookValue = other.netBookValue == null ? null : other.netBookValue.copy();
        this.fiscalMonthEndDate = other.fiscalMonthEndDate == null ? null : other.fiscalMonthEndDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouAccountBalanceReportItemCriteria copy() {
        return new RouAccountBalanceReportItemCriteria(this);
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

    public StringFilter getAssetAccountName() {
        return assetAccountName;
    }

    public StringFilter assetAccountName() {
        if (assetAccountName == null) {
            assetAccountName = new StringFilter();
        }
        return assetAccountName;
    }

    public void setAssetAccountName(StringFilter assetAccountName) {
        this.assetAccountName = assetAccountName;
    }

    public StringFilter getAssetAccountNumber() {
        return assetAccountNumber;
    }

    public StringFilter assetAccountNumber() {
        if (assetAccountNumber == null) {
            assetAccountNumber = new StringFilter();
        }
        return assetAccountNumber;
    }

    public void setAssetAccountNumber(StringFilter assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public StringFilter getDepreciationAccountNumber() {
        return depreciationAccountNumber;
    }

    public StringFilter depreciationAccountNumber() {
        if (depreciationAccountNumber == null) {
            depreciationAccountNumber = new StringFilter();
        }
        return depreciationAccountNumber;
    }

    public void setDepreciationAccountNumber(StringFilter depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public BigDecimalFilter getNetBookValue() {
        return netBookValue;
    }

    public BigDecimalFilter netBookValue() {
        if (netBookValue == null) {
            netBookValue = new BigDecimalFilter();
        }
        return netBookValue;
    }

    public void setNetBookValue(BigDecimalFilter netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDateFilter getFiscalMonthEndDate() {
        return fiscalMonthEndDate;
    }

    public LocalDateFilter fiscalMonthEndDate() {
        if (fiscalMonthEndDate == null) {
            fiscalMonthEndDate = new LocalDateFilter();
        }
        return fiscalMonthEndDate;
    }

    public void setFiscalMonthEndDate(LocalDateFilter fiscalMonthEndDate) {
        this.fiscalMonthEndDate = fiscalMonthEndDate;
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
        final RouAccountBalanceReportItemCriteria that = (RouAccountBalanceReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetAccountName, that.assetAccountName) &&
            Objects.equals(assetAccountNumber, that.assetAccountNumber) &&
            Objects.equals(depreciationAccountNumber, that.depreciationAccountNumber) &&
            Objects.equals(netBookValue, that.netBookValue) &&
            Objects.equals(fiscalMonthEndDate, that.fiscalMonthEndDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetAccountName,
            assetAccountNumber,
            depreciationAccountNumber,
            netBookValue,
            fiscalMonthEndDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouAccountBalanceReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetAccountName != null ? "assetAccountName=" + assetAccountName + ", " : "") +
            (assetAccountNumber != null ? "assetAccountNumber=" + assetAccountNumber + ", " : "") +
            (depreciationAccountNumber != null ? "depreciationAccountNumber=" + depreciationAccountNumber + ", " : "") +
            (netBookValue != null ? "netBookValue=" + netBookValue + ", " : "") +
            (fiscalMonthEndDate != null ? "fiscalMonthEndDate=" + fiscalMonthEndDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
