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
 * A RouMonthlyDepreciationReportItem.
 */
@Entity
@Table(name = "rou_monthly_depreciation_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "roumonthlydepreciationreportitem")
public class RouMonthlyDepreciationReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fiscal_month_start_date")
    private LocalDate fiscalMonthStartDate;

    @Column(name = "fiscal_month_end_date")
    private LocalDate fiscalMonthEndDate;

    @Column(name = "total_depreciation_amount", precision = 21, scale = 2)
    private BigDecimal totalDepreciationAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouMonthlyDepreciationReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFiscalMonthStartDate() {
        return this.fiscalMonthStartDate;
    }

    public RouMonthlyDepreciationReportItem fiscalMonthStartDate(LocalDate fiscalMonthStartDate) {
        this.setFiscalMonthStartDate(fiscalMonthStartDate);
        return this;
    }

    public void setFiscalMonthStartDate(LocalDate fiscalMonthStartDate) {
        this.fiscalMonthStartDate = fiscalMonthStartDate;
    }

    public LocalDate getFiscalMonthEndDate() {
        return this.fiscalMonthEndDate;
    }

    public RouMonthlyDepreciationReportItem fiscalMonthEndDate(LocalDate fiscalMonthEndDate) {
        this.setFiscalMonthEndDate(fiscalMonthEndDate);
        return this;
    }

    public void setFiscalMonthEndDate(LocalDate fiscalMonthEndDate) {
        this.fiscalMonthEndDate = fiscalMonthEndDate;
    }

    public BigDecimal getTotalDepreciationAmount() {
        return this.totalDepreciationAmount;
    }

    public RouMonthlyDepreciationReportItem totalDepreciationAmount(BigDecimal totalDepreciationAmount) {
        this.setTotalDepreciationAmount(totalDepreciationAmount);
        return this;
    }

    public void setTotalDepreciationAmount(BigDecimal totalDepreciationAmount) {
        this.totalDepreciationAmount = totalDepreciationAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouMonthlyDepreciationReportItem)) {
            return false;
        }
        return id != null && id.equals(((RouMonthlyDepreciationReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouMonthlyDepreciationReportItem{" +
            "id=" + getId() +
            ", fiscalMonthStartDate='" + getFiscalMonthStartDate() + "'" +
            ", fiscalMonthEndDate='" + getFiscalMonthEndDate() + "'" +
            ", totalDepreciationAmount=" + getTotalDepreciationAmount() +
            "}";
    }
}
