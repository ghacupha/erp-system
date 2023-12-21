package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A WeeklyCounterfeitHolding.
 */
@Entity
@Table(name = "weekly_counterfeit_holding")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "weeklycounterfeitholding")
public class WeeklyCounterfeitHolding implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reporting_date", nullable = false)
    private LocalDate reportingDate;

    @NotNull
    @Column(name = "date_confiscated", nullable = false)
    private LocalDate dateConfiscated;

    @NotNull
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @NotNull
    @Column(name = "depositors_names", nullable = false)
    private String depositorsNames;

    @NotNull
    @Column(name = "tellers_names", nullable = false)
    private String tellersNames;

    @NotNull
    @Column(name = "date_submitted_to_cbk", nullable = false)
    private LocalDate dateSubmittedToCBK;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remarks")
    private String remarks;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WeeklyCounterfeitHolding id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public WeeklyCounterfeitHolding reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public LocalDate getDateConfiscated() {
        return this.dateConfiscated;
    }

    public WeeklyCounterfeitHolding dateConfiscated(LocalDate dateConfiscated) {
        this.setDateConfiscated(dateConfiscated);
        return this;
    }

    public void setDateConfiscated(LocalDate dateConfiscated) {
        this.dateConfiscated = dateConfiscated;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public WeeklyCounterfeitHolding serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDepositorsNames() {
        return this.depositorsNames;
    }

    public WeeklyCounterfeitHolding depositorsNames(String depositorsNames) {
        this.setDepositorsNames(depositorsNames);
        return this;
    }

    public void setDepositorsNames(String depositorsNames) {
        this.depositorsNames = depositorsNames;
    }

    public String getTellersNames() {
        return this.tellersNames;
    }

    public WeeklyCounterfeitHolding tellersNames(String tellersNames) {
        this.setTellersNames(tellersNames);
        return this;
    }

    public void setTellersNames(String tellersNames) {
        this.tellersNames = tellersNames;
    }

    public LocalDate getDateSubmittedToCBK() {
        return this.dateSubmittedToCBK;
    }

    public WeeklyCounterfeitHolding dateSubmittedToCBK(LocalDate dateSubmittedToCBK) {
        this.setDateSubmittedToCBK(dateSubmittedToCBK);
        return this;
    }

    public void setDateSubmittedToCBK(LocalDate dateSubmittedToCBK) {
        this.dateSubmittedToCBK = dateSubmittedToCBK;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public WeeklyCounterfeitHolding remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeeklyCounterfeitHolding)) {
            return false;
        }
        return id != null && id.equals(((WeeklyCounterfeitHolding) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeeklyCounterfeitHolding{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", dateConfiscated='" + getDateConfiscated() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", depositorsNames='" + getDepositorsNames() + "'" +
            ", tellersNames='" + getTellersNames() + "'" +
            ", dateSubmittedToCBK='" + getDateSubmittedToCBK() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
