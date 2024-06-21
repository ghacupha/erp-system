package io.github.erp.domain;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaseAmortizationSchedule.
 */
@Entity
@Table(name = "lease_amortization_schedule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leaseamortizationschedule")
public class LeaseAmortizationSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "identifier", nullable = false, unique = true)
    private UUID identifier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "leaseAmortizationCalculation", "leasePayments" }, allowSetters = true)
    private LeaseLiability leaseLiability;

    @OneToMany(mappedBy = "leaseAmortizationSchedule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "placeholders", "universallyUniqueMappings", "leasePeriod", "leaseAmortizationSchedule", "leaseContract", "leaseLiability",
        },
        allowSetters = true
    )
    private Set<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItems = new HashSet<>();

    @JsonIgnoreProperties(
        value = {
            "superintendentServiceOutlet",
            "mainDealer",
            "firstReportingPeriod",
            "lastReportingPeriod",
            "leaseContractDocument",
            "leaseContractCalculations",
        },
        allowSetters = true
    )
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private IFRS16LeaseContract leaseContract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseAmortizationSchedule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getIdentifier() {
        return this.identifier;
    }

    public LeaseAmortizationSchedule identifier(UUID identifier) {
        this.setIdentifier(identifier);
        return this;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public LeaseLiability getLeaseLiability() {
        return this.leaseLiability;
    }

    public void setLeaseLiability(LeaseLiability leaseLiability) {
        this.leaseLiability = leaseLiability;
    }

    public LeaseAmortizationSchedule leaseLiability(LeaseLiability leaseLiability) {
        this.setLeaseLiability(leaseLiability);
        return this;
    }

    public Set<LeaseLiabilityScheduleItem> getLeaseLiabilityScheduleItems() {
        return this.leaseLiabilityScheduleItems;
    }

    public void setLeaseLiabilityScheduleItems(Set<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItems) {
        if (this.leaseLiabilityScheduleItems != null) {
            this.leaseLiabilityScheduleItems.forEach(i -> i.setLeaseAmortizationSchedule(null));
        }
        if (leaseLiabilityScheduleItems != null) {
            leaseLiabilityScheduleItems.forEach(i -> i.setLeaseAmortizationSchedule(this));
        }
        this.leaseLiabilityScheduleItems = leaseLiabilityScheduleItems;
    }

    public LeaseAmortizationSchedule leaseLiabilityScheduleItems(Set<LeaseLiabilityScheduleItem> leaseLiabilityScheduleItems) {
        this.setLeaseLiabilityScheduleItems(leaseLiabilityScheduleItems);
        return this;
    }

    public LeaseAmortizationSchedule addLeaseLiabilityScheduleItem(LeaseLiabilityScheduleItem leaseLiabilityScheduleItem) {
        this.leaseLiabilityScheduleItems.add(leaseLiabilityScheduleItem);
        leaseLiabilityScheduleItem.setLeaseAmortizationSchedule(this);
        return this;
    }

    public LeaseAmortizationSchedule removeLeaseLiabilityScheduleItem(LeaseLiabilityScheduleItem leaseLiabilityScheduleItem) {
        this.leaseLiabilityScheduleItems.remove(leaseLiabilityScheduleItem);
        leaseLiabilityScheduleItem.setLeaseAmortizationSchedule(null);
        return this;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return this.leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContract = iFRS16LeaseContract;
    }

    public LeaseAmortizationSchedule leaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.setLeaseContract(iFRS16LeaseContract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseAmortizationSchedule)) {
            return false;
        }
        return id != null && id.equals(((LeaseAmortizationSchedule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseAmortizationSchedule{" +
            "id=" + getId() +
            ", identifier='" + getIdentifier() + "'" +
            "}";
    }
}
