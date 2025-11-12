package io.github.erp.domain;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaseRepaymentPeriod.
 */
@Entity
@Table(name = "lease_repayment_period")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leaserepaymentperiod")
public class LeaseRepaymentPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sequence_number", nullable = false, unique = true)
    private Long sequenceNumber;

    @NotNull
    @Column(name = "start_date", nullable = false, unique = true)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false, unique = true)
    private LocalDate endDate;

    @NotNull
    @Column(name = "period_code", nullable = false, unique = true)
    private String periodCode;

    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private FiscalMonth fiscalMonth;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseRepaymentPeriod id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public LeaseRepaymentPeriod sequenceNumber(Long sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LeaseRepaymentPeriod startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public LeaseRepaymentPeriod endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPeriodCode() {
        return this.periodCode;
    }

    public LeaseRepaymentPeriod periodCode(String periodCode) {
        this.setPeriodCode(periodCode);
        return this;
    }

    public void setPeriodCode(String periodCode) {
        this.periodCode = periodCode;
    }

    public FiscalMonth getFiscalMonth() {
        return this.fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonth fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public LeaseRepaymentPeriod fiscalMonth(FiscalMonth fiscalMonth) {
        this.setFiscalMonth(fiscalMonth);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseRepaymentPeriod)) {
            return false;
        }
        return id != null && id.equals(((LeaseRepaymentPeriod) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseRepaymentPeriod{" +
            "id=" + getId() +
            ", sequenceNumber=" + getSequenceNumber() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", periodCode='" + getPeriodCode() + "'" +
            "}";
    }
}
