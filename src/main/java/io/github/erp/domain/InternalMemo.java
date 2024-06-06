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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InternalMemo.
 */
@Entity
@Table(name = "internal_memo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "internalmemo")
public class InternalMemo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 8, max = 8)
    @Column(name = "catalogue_number", length = 8, nullable = false, unique = true)
    private String catalogueNumber;

    @NotNull
    @Column(name = "reference_number", nullable = false, unique = true)
    private String referenceNumber;

    @NotNull
    @Column(name = "memo_date", nullable = false)
    private LocalDate memoDate;

    @Column(name = "purpose_description")
    private String purposeDescription;

    @ManyToOne
    private MemoAction actionRequired;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer addressedTo;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer from;

    @ManyToMany
    @JoinTable(
        name = "rel_internal_memo__prepared_by",
        joinColumns = @JoinColumn(name = "internal_memo_id"),
        inverseJoinColumns = @JoinColumn(name = "prepared_by_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> preparedBies = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_internal_memo__reviewed_by",
        joinColumns = @JoinColumn(name = "internal_memo_id"),
        inverseJoinColumns = @JoinColumn(name = "reviewed_by_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> reviewedBies = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_internal_memo__approved_by",
        joinColumns = @JoinColumn(name = "internal_memo_id"),
        inverseJoinColumns = @JoinColumn(name = "approved_by_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> approvedBies = new HashSet<>();

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_internal_memo__memo_document",
        joinColumns = @JoinColumn(name = "internal_memo_id"),
        inverseJoinColumns = @JoinColumn(name = "memo_document_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private Set<BusinessDocument> memoDocuments = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_internal_memo__placeholder",
        joinColumns = @JoinColumn(name = "internal_memo_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InternalMemo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatalogueNumber() {
        return this.catalogueNumber;
    }

    public InternalMemo catalogueNumber(String catalogueNumber) {
        this.setCatalogueNumber(catalogueNumber);
        return this;
    }

    public void setCatalogueNumber(String catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public InternalMemo referenceNumber(String referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getMemoDate() {
        return this.memoDate;
    }

    public InternalMemo memoDate(LocalDate memoDate) {
        this.setMemoDate(memoDate);
        return this;
    }

    public void setMemoDate(LocalDate memoDate) {
        this.memoDate = memoDate;
    }

    public String getPurposeDescription() {
        return this.purposeDescription;
    }

    public InternalMemo purposeDescription(String purposeDescription) {
        this.setPurposeDescription(purposeDescription);
        return this;
    }

    public void setPurposeDescription(String purposeDescription) {
        this.purposeDescription = purposeDescription;
    }

    public MemoAction getActionRequired() {
        return this.actionRequired;
    }

    public void setActionRequired(MemoAction memoAction) {
        this.actionRequired = memoAction;
    }

    public InternalMemo actionRequired(MemoAction memoAction) {
        this.setActionRequired(memoAction);
        return this;
    }

    public Dealer getAddressedTo() {
        return this.addressedTo;
    }

    public void setAddressedTo(Dealer dealer) {
        this.addressedTo = dealer;
    }

    public InternalMemo addressedTo(Dealer dealer) {
        this.setAddressedTo(dealer);
        return this;
    }

    public Dealer getFrom() {
        return this.from;
    }

    public void setFrom(Dealer dealer) {
        this.from = dealer;
    }

    public InternalMemo from(Dealer dealer) {
        this.setFrom(dealer);
        return this;
    }

    public Set<Dealer> getPreparedBies() {
        return this.preparedBies;
    }

    public void setPreparedBies(Set<Dealer> dealers) {
        this.preparedBies = dealers;
    }

    public InternalMemo preparedBies(Set<Dealer> dealers) {
        this.setPreparedBies(dealers);
        return this;
    }

    public InternalMemo addPreparedBy(Dealer dealer) {
        this.preparedBies.add(dealer);
        return this;
    }

    public InternalMemo removePreparedBy(Dealer dealer) {
        this.preparedBies.remove(dealer);
        return this;
    }

    public Set<Dealer> getReviewedBies() {
        return this.reviewedBies;
    }

    public void setReviewedBies(Set<Dealer> dealers) {
        this.reviewedBies = dealers;
    }

    public InternalMemo reviewedBies(Set<Dealer> dealers) {
        this.setReviewedBies(dealers);
        return this;
    }

    public InternalMemo addReviewedBy(Dealer dealer) {
        this.reviewedBies.add(dealer);
        return this;
    }

    public InternalMemo removeReviewedBy(Dealer dealer) {
        this.reviewedBies.remove(dealer);
        return this;
    }

    public Set<Dealer> getApprovedBies() {
        return this.approvedBies;
    }

    public void setApprovedBies(Set<Dealer> dealers) {
        this.approvedBies = dealers;
    }

    public InternalMemo approvedBies(Set<Dealer> dealers) {
        this.setApprovedBies(dealers);
        return this;
    }

    public InternalMemo addApprovedBy(Dealer dealer) {
        this.approvedBies.add(dealer);
        return this;
    }

    public InternalMemo removeApprovedBy(Dealer dealer) {
        this.approvedBies.remove(dealer);
        return this;
    }

    public Set<BusinessDocument> getMemoDocuments() {
        return this.memoDocuments;
    }

    public void setMemoDocuments(Set<BusinessDocument> businessDocuments) {
        this.memoDocuments = businessDocuments;
    }

    public InternalMemo memoDocuments(Set<BusinessDocument> businessDocuments) {
        this.setMemoDocuments(businessDocuments);
        return this;
    }

    public InternalMemo addMemoDocument(BusinessDocument businessDocument) {
        this.memoDocuments.add(businessDocument);
        return this;
    }

    public InternalMemo removeMemoDocument(BusinessDocument businessDocument) {
        this.memoDocuments.remove(businessDocument);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public InternalMemo placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public InternalMemo addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public InternalMemo removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InternalMemo)) {
            return false;
        }
        return id != null && id.equals(((InternalMemo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InternalMemo{" +
            "id=" + getId() +
            ", catalogueNumber='" + getCatalogueNumber() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", memoDate='" + getMemoDate() + "'" +
            ", purposeDescription='" + getPurposeDescription() + "'" +
            "}";
    }
}
