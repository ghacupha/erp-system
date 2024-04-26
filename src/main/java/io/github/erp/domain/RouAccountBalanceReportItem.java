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
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouAccountBalanceReportItem.
 */
@Entity
@Table(name = "rou_account_balance_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rouaccountbalancereportitem")
public class RouAccountBalanceReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_account_name")
    private String assetAccountName;

    @Column(name = "asset_account_number")
    private String assetAccountNumber;

    @Column(name = "depreciation_account_number")
    private String depreciationAccountNumber;

    @Column(name = "net_book_value", precision = 21, scale = 2)
    private BigDecimal netBookValue;

    @Column(name = "fiscal_month_end_date")
    private LocalDate fiscalMonthEndDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouAccountBalanceReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetAccountName() {
        return this.assetAccountName;
    }

    public RouAccountBalanceReportItem assetAccountName(String assetAccountName) {
        this.setAssetAccountName(assetAccountName);
        return this;
    }

    public void setAssetAccountName(String assetAccountName) {
        this.assetAccountName = assetAccountName;
    }

    public String getAssetAccountNumber() {
        return this.assetAccountNumber;
    }

    public RouAccountBalanceReportItem assetAccountNumber(String assetAccountNumber) {
        this.setAssetAccountNumber(assetAccountNumber);
        return this;
    }

    public void setAssetAccountNumber(String assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public String getDepreciationAccountNumber() {
        return this.depreciationAccountNumber;
    }

    public RouAccountBalanceReportItem depreciationAccountNumber(String depreciationAccountNumber) {
        this.setDepreciationAccountNumber(depreciationAccountNumber);
        return this;
    }

    public void setDepreciationAccountNumber(String depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public BigDecimal getNetBookValue() {
        return this.netBookValue;
    }

    public RouAccountBalanceReportItem netBookValue(BigDecimal netBookValue) {
        this.setNetBookValue(netBookValue);
        return this;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDate getFiscalMonthEndDate() {
        return this.fiscalMonthEndDate;
    }

    public RouAccountBalanceReportItem fiscalMonthEndDate(LocalDate fiscalMonthEndDate) {
        this.setFiscalMonthEndDate(fiscalMonthEndDate);
        return this;
    }

    public void setFiscalMonthEndDate(LocalDate fiscalMonthEndDate) {
        this.fiscalMonthEndDate = fiscalMonthEndDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouAccountBalanceReportItem)) {
            return false;
        }
        return id != null && id.equals(((RouAccountBalanceReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouAccountBalanceReportItem{" +
            "id=" + getId() +
            ", assetAccountName='" + getAssetAccountName() + "'" +
            ", assetAccountNumber='" + getAssetAccountNumber() + "'" +
            ", depreciationAccountNumber='" + getDepreciationAccountNumber() + "'" +
            ", netBookValue=" + getNetBookValue() +
            ", fiscalMonthEndDate='" + getFiscalMonthEndDate() + "'" +
            "}";
    }
}
