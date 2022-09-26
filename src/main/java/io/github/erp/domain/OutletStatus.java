package io.github.erp.domain;

/*-
 * Erp System - Mark III No 1 (Caleb Series) Server ver 0.1.1-SNAPSHOT
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
import io.github.erp.domain.enumeration.BranchStatusType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OutletStatus.
 */
@Entity
@Table(name = "outlet_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "outletstatus")
public class OutletStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "branch_status_type_code", nullable = false, unique = true)
    private String branchStatusTypeCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "branch_status_type", nullable = false, unique = true)
    private BranchStatusType branchStatusType;

    @Column(name = "branch_status_type_description")
    private String branchStatusTypeDescription;

    @ManyToMany
    @JoinTable(
        name = "rel_outlet_status__placeholder",
        joinColumns = @JoinColumn(name = "outlet_status_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OutletStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchStatusTypeCode() {
        return this.branchStatusTypeCode;
    }

    public OutletStatus branchStatusTypeCode(String branchStatusTypeCode) {
        this.setBranchStatusTypeCode(branchStatusTypeCode);
        return this;
    }

    public void setBranchStatusTypeCode(String branchStatusTypeCode) {
        this.branchStatusTypeCode = branchStatusTypeCode;
    }

    public BranchStatusType getBranchStatusType() {
        return this.branchStatusType;
    }

    public OutletStatus branchStatusType(BranchStatusType branchStatusType) {
        this.setBranchStatusType(branchStatusType);
        return this;
    }

    public void setBranchStatusType(BranchStatusType branchStatusType) {
        this.branchStatusType = branchStatusType;
    }

    public String getBranchStatusTypeDescription() {
        return this.branchStatusTypeDescription;
    }

    public OutletStatus branchStatusTypeDescription(String branchStatusTypeDescription) {
        this.setBranchStatusTypeDescription(branchStatusTypeDescription);
        return this;
    }

    public void setBranchStatusTypeDescription(String branchStatusTypeDescription) {
        this.branchStatusTypeDescription = branchStatusTypeDescription;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public OutletStatus placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public OutletStatus addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public OutletStatus removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OutletStatus)) {
            return false;
        }
        return id != null && id.equals(((OutletStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutletStatus{" +
            "id=" + getId() +
            ", branchStatusTypeCode='" + getBranchStatusTypeCode() + "'" +
            ", branchStatusType='" + getBranchStatusType() + "'" +
            ", branchStatusTypeDescription='" + getBranchStatusTypeDescription() + "'" +
            "}";
    }
}
