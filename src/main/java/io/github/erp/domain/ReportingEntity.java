package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportingEntity.
 */
@Entity
@Table(name = "reporting_entity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reportingentity")
public class ReportingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "entity_name", nullable = false, unique = true)
    private String entityName;

    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private SettlementCurrency reportingCurrency;

    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private TransactionAccount retainedEarningsAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportingEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public ReportingEntity entityName(String entityName) {
        this.setEntityName(entityName);
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public SettlementCurrency getReportingCurrency() {
        return this.reportingCurrency;
    }

    public void setReportingCurrency(SettlementCurrency settlementCurrency) {
        this.reportingCurrency = settlementCurrency;
    }

    public ReportingEntity reportingCurrency(SettlementCurrency settlementCurrency) {
        this.setReportingCurrency(settlementCurrency);
        return this;
    }

    public TransactionAccount getRetainedEarningsAccount() {
        return this.retainedEarningsAccount;
    }

    public void setRetainedEarningsAccount(TransactionAccount transactionAccount) {
        this.retainedEarningsAccount = transactionAccount;
    }

    public ReportingEntity retainedEarningsAccount(TransactionAccount transactionAccount) {
        this.setRetainedEarningsAccount(transactionAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportingEntity)) {
            return false;
        }
        return id != null && id.equals(((ReportingEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingEntity{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            "}";
    }
}
