package io.github.erp.domain;

/*-
 * Erp System - Mark III No 3 (Caleb Series) Server ver 0.1.3-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkInProgressTransfer.
 */
@Entity
@Table(name = "work_in_progress_transfer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workinprogresstransfer")
public class WorkInProgressTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "target_asset_number")
    private String targetAssetNumber;

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_transfer__work_in_progress_registration",
        joinColumns = @JoinColumn(name = "work_in_progress_transfer_id"),
        inverseJoinColumns = @JoinColumn(name = "work_in_progress_registration_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "paymentInvoices",
            "serviceOutlets",
            "settlements",
            "purchaseOrders",
            "deliveryNotes",
            "jobSheets",
            "dealer",
            "workInProgressGroup",
            "settlementCurrency",
            "workProjectRegister",
        },
        allowSetters = true
    )
    private Set<WorkInProgressRegistration> workInProgressRegistrations = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_transfer__placeholder",
        joinColumns = @JoinColumn(name = "work_in_progress_transfer_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkInProgressTransfer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public WorkInProgressTransfer description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetAssetNumber() {
        return this.targetAssetNumber;
    }

    public WorkInProgressTransfer targetAssetNumber(String targetAssetNumber) {
        this.setTargetAssetNumber(targetAssetNumber);
        return this;
    }

    public void setTargetAssetNumber(String targetAssetNumber) {
        this.targetAssetNumber = targetAssetNumber;
    }

    public Set<WorkInProgressRegistration> getWorkInProgressRegistrations() {
        return this.workInProgressRegistrations;
    }

    public void setWorkInProgressRegistrations(Set<WorkInProgressRegistration> workInProgressRegistrations) {
        this.workInProgressRegistrations = workInProgressRegistrations;
    }

    public WorkInProgressTransfer workInProgressRegistrations(Set<WorkInProgressRegistration> workInProgressRegistrations) {
        this.setWorkInProgressRegistrations(workInProgressRegistrations);
        return this;
    }

    public WorkInProgressTransfer addWorkInProgressRegistration(WorkInProgressRegistration workInProgressRegistration) {
        this.workInProgressRegistrations.add(workInProgressRegistration);
        return this;
    }

    public WorkInProgressTransfer removeWorkInProgressRegistration(WorkInProgressRegistration workInProgressRegistration) {
        this.workInProgressRegistrations.remove(workInProgressRegistration);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public WorkInProgressTransfer placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public WorkInProgressTransfer addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public WorkInProgressTransfer removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkInProgressTransfer)) {
            return false;
        }
        return id != null && id.equals(((WorkInProgressTransfer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressTransfer{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", targetAssetNumber='" + getTargetAssetNumber() + "'" +
            "}";
    }
}
