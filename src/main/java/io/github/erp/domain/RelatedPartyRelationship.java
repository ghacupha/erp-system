package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
 * A RelatedPartyRelationship.
 */
@Entity
@Table(name = "related_party_relationship")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "relatedpartyrelationship")
public class RelatedPartyRelationship implements Serializable {

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
    @Column(name = "customer_id", nullable = false, unique = true)
    private String customerId;

    @NotNull
    @Column(name = "related_party_id", nullable = false, unique = true)
    private String relatedPartyId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private BankBranchCode branchId;

    @ManyToOne(optional = false)
    @NotNull
    private PartyRelationType relationshipType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RelatedPartyRelationship id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public RelatedPartyRelationship reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public RelatedPartyRelationship customerId(String customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRelatedPartyId() {
        return this.relatedPartyId;
    }

    public RelatedPartyRelationship relatedPartyId(String relatedPartyId) {
        this.setRelatedPartyId(relatedPartyId);
        return this;
    }

    public void setRelatedPartyId(String relatedPartyId) {
        this.relatedPartyId = relatedPartyId;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public RelatedPartyRelationship bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public BankBranchCode getBranchId() {
        return this.branchId;
    }

    public void setBranchId(BankBranchCode bankBranchCode) {
        this.branchId = bankBranchCode;
    }

    public RelatedPartyRelationship branchId(BankBranchCode bankBranchCode) {
        this.setBranchId(bankBranchCode);
        return this;
    }

    public PartyRelationType getRelationshipType() {
        return this.relationshipType;
    }

    public void setRelationshipType(PartyRelationType partyRelationType) {
        this.relationshipType = partyRelationType;
    }

    public RelatedPartyRelationship relationshipType(PartyRelationType partyRelationType) {
        this.setRelationshipType(partyRelationType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RelatedPartyRelationship)) {
            return false;
        }
        return id != null && id.equals(((RelatedPartyRelationship) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RelatedPartyRelationship{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", relatedPartyId='" + getRelatedPartyId() + "'" +
            "}";
    }
}
