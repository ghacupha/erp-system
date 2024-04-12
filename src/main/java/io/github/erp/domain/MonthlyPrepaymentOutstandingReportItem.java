package io.github.erp.domain;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MonthlyPrepaymentOutstandingReportItem.
 */
@Entity
@Table(name = "monthly_prepayment_outstanding_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "monthlyprepaymentoutstandingreportitem")
public class MonthlyPrepaymentOutstandingReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fiscal_month_end_date")
    private LocalDate fiscalMonthEndDate;

    @Column(name = "total_prepayment_amount", precision = 21, scale = 2)
    private BigDecimal totalPrepaymentAmount;

    @Column(name = "total_amortised_amount", precision = 21, scale = 2)
    private BigDecimal totalAmortisedAmount;

    @Column(name = "total_outstanding_amount", precision = 21, scale = 2)
    private BigDecimal totalOutstandingAmount;

    @Column(name = "number_of_prepayment_accounts")
    private Integer numberOfPrepaymentAccounts;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MonthlyPrepaymentOutstandingReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFiscalMonthEndDate() {
        return this.fiscalMonthEndDate;
    }

    public MonthlyPrepaymentOutstandingReportItem fiscalMonthEndDate(LocalDate fiscalMonthEndDate) {
        this.setFiscalMonthEndDate(fiscalMonthEndDate);
        return this;
    }

    public void setFiscalMonthEndDate(LocalDate fiscalMonthEndDate) {
        this.fiscalMonthEndDate = fiscalMonthEndDate;
    }

    public BigDecimal getTotalPrepaymentAmount() {
        return this.totalPrepaymentAmount;
    }

    public MonthlyPrepaymentOutstandingReportItem totalPrepaymentAmount(BigDecimal totalPrepaymentAmount) {
        this.setTotalPrepaymentAmount(totalPrepaymentAmount);
        return this;
    }

    public void setTotalPrepaymentAmount(BigDecimal totalPrepaymentAmount) {
        this.totalPrepaymentAmount = totalPrepaymentAmount;
    }

    public BigDecimal getTotalAmortisedAmount() {
        return this.totalAmortisedAmount;
    }

    public MonthlyPrepaymentOutstandingReportItem totalAmortisedAmount(BigDecimal totalAmortisedAmount) {
        this.setTotalAmortisedAmount(totalAmortisedAmount);
        return this;
    }

    public void setTotalAmortisedAmount(BigDecimal totalAmortisedAmount) {
        this.totalAmortisedAmount = totalAmortisedAmount;
    }

    public BigDecimal getTotalOutstandingAmount() {
        return this.totalOutstandingAmount;
    }

    public MonthlyPrepaymentOutstandingReportItem totalOutstandingAmount(BigDecimal totalOutstandingAmount) {
        this.setTotalOutstandingAmount(totalOutstandingAmount);
        return this;
    }

    public void setTotalOutstandingAmount(BigDecimal totalOutstandingAmount) {
        this.totalOutstandingAmount = totalOutstandingAmount;
    }

    public Integer getNumberOfPrepaymentAccounts() {
        return this.numberOfPrepaymentAccounts;
    }

    public MonthlyPrepaymentOutstandingReportItem numberOfPrepaymentAccounts(Integer numberOfPrepaymentAccounts) {
        this.setNumberOfPrepaymentAccounts(numberOfPrepaymentAccounts);
        return this;
    }

    public void setNumberOfPrepaymentAccounts(Integer numberOfPrepaymentAccounts) {
        this.numberOfPrepaymentAccounts = numberOfPrepaymentAccounts;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonthlyPrepaymentOutstandingReportItem)) {
            return false;
        }
        return id != null && id.equals(((MonthlyPrepaymentOutstandingReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonthlyPrepaymentOutstandingReportItem{" +
            "id=" + getId() +
            ", fiscalMonthEndDate='" + getFiscalMonthEndDate() + "'" +
            ", totalPrepaymentAmount=" + getTotalPrepaymentAmount() +
            ", totalAmortisedAmount=" + getTotalAmortisedAmount() +
            ", totalOutstandingAmount=" + getTotalOutstandingAmount() +
            ", numberOfPrepaymentAccounts=" + getNumberOfPrepaymentAccounts() +
            "}";
    }
}
