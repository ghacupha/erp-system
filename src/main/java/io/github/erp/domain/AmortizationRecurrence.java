package io.github.erp.domain;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
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
import io.github.erp.domain.enumeration.recurrenceFrequency;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AmortizationRecurrence.
 */
@Entity
@Table(name = "amortization_recurrence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "amortizationrecurrence")
public class AmortizationRecurrence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_amortization_date", nullable = false)
    private LocalDate firstAmortizationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "amortization_frequency", nullable = false)
    private recurrenceFrequency amortizationFrequency;

    @NotNull
    @Column(name = "number_of_recurrences", nullable = false)
    private Integer numberOfRecurrences;

    @Lob
    @Column(name = "notes")
    private byte[] notes;

    @Column(name = "notes_content_type")
    private String notesContentType;

    @Column(name = "particulars")
    private String particulars;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_over_written")
    private Boolean isOverWritten;

    @NotNull
    @Column(name = "time_of_installation", nullable = false)
    private ZonedDateTime timeOfInstallation;

    @NotNull
    @Column(name = "recurrence_guid", nullable = false, unique = true)
    private UUID recurrenceGuid;

    @NotNull
    @Column(name = "prepayment_account_guid", nullable = false)
    private UUID prepaymentAccountGuid;

    @ManyToMany
    @JoinTable(
        name = "rel_amortization_recurrence__placeholder",
        joinColumns = @JoinColumn(name = "amortization_recurrence_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_amortization_recurrence__parameters",
        joinColumns = @JoinColumn(name = "amortization_recurrence_id"),
        inverseJoinColumns = @JoinColumn(name = "parameters_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private Set<PrepaymentMapping> parameters = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_amortization_recurrence__application_parameters",
        joinColumns = @JoinColumn(name = "amortization_recurrence_id"),
        inverseJoinColumns = @JoinColumn(name = "application_parameters_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> applicationParameters = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private DepreciationMethod depreciationMethod;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "settlementCurrency",
            "prepaymentTransaction",
            "serviceOutlet",
            "dealer",
            "debitAccount",
            "transferAccount",
            "placeholders",
            "generalParameters",
            "prepaymentParameters",
            "businessDocuments",
        },
        allowSetters = true
    )
    private PrepaymentAccount prepaymentAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AmortizationRecurrence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFirstAmortizationDate() {
        return this.firstAmortizationDate;
    }

    public AmortizationRecurrence firstAmortizationDate(LocalDate firstAmortizationDate) {
        this.setFirstAmortizationDate(firstAmortizationDate);
        return this;
    }

    public void setFirstAmortizationDate(LocalDate firstAmortizationDate) {
        this.firstAmortizationDate = firstAmortizationDate;
    }

    public recurrenceFrequency getAmortizationFrequency() {
        return this.amortizationFrequency;
    }

    public AmortizationRecurrence amortizationFrequency(recurrenceFrequency amortizationFrequency) {
        this.setAmortizationFrequency(amortizationFrequency);
        return this;
    }

    public void setAmortizationFrequency(recurrenceFrequency amortizationFrequency) {
        this.amortizationFrequency = amortizationFrequency;
    }

    public Integer getNumberOfRecurrences() {
        return this.numberOfRecurrences;
    }

    public AmortizationRecurrence numberOfRecurrences(Integer numberOfRecurrences) {
        this.setNumberOfRecurrences(numberOfRecurrences);
        return this;
    }

    public void setNumberOfRecurrences(Integer numberOfRecurrences) {
        this.numberOfRecurrences = numberOfRecurrences;
    }

    public byte[] getNotes() {
        return this.notes;
    }

    public AmortizationRecurrence notes(byte[] notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(byte[] notes) {
        this.notes = notes;
    }

    public String getNotesContentType() {
        return this.notesContentType;
    }

    public AmortizationRecurrence notesContentType(String notesContentType) {
        this.notesContentType = notesContentType;
        return this;
    }

    public void setNotesContentType(String notesContentType) {
        this.notesContentType = notesContentType;
    }

    public String getParticulars() {
        return this.particulars;
    }

    public AmortizationRecurrence particulars(String particulars) {
        this.setParticulars(particulars);
        return this;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AmortizationRecurrence isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsOverWritten() {
        return this.isOverWritten;
    }

    public AmortizationRecurrence isOverWritten(Boolean isOverWritten) {
        this.setIsOverWritten(isOverWritten);
        return this;
    }

    public void setIsOverWritten(Boolean isOverWritten) {
        this.isOverWritten = isOverWritten;
    }

    public ZonedDateTime getTimeOfInstallation() {
        return this.timeOfInstallation;
    }

    public AmortizationRecurrence timeOfInstallation(ZonedDateTime timeOfInstallation) {
        this.setTimeOfInstallation(timeOfInstallation);
        return this;
    }

    public void setTimeOfInstallation(ZonedDateTime timeOfInstallation) {
        this.timeOfInstallation = timeOfInstallation;
    }

    public UUID getRecurrenceGuid() {
        return this.recurrenceGuid;
    }

    public AmortizationRecurrence recurrenceGuid(UUID recurrenceGuid) {
        this.setRecurrenceGuid(recurrenceGuid);
        return this;
    }

    public void setRecurrenceGuid(UUID recurrenceGuid) {
        this.recurrenceGuid = recurrenceGuid;
    }

    public UUID getPrepaymentAccountGuid() {
        return this.prepaymentAccountGuid;
    }

    public AmortizationRecurrence prepaymentAccountGuid(UUID prepaymentAccountGuid) {
        this.setPrepaymentAccountGuid(prepaymentAccountGuid);
        return this;
    }

    public void setPrepaymentAccountGuid(UUID prepaymentAccountGuid) {
        this.prepaymentAccountGuid = prepaymentAccountGuid;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public AmortizationRecurrence placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public AmortizationRecurrence addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public AmortizationRecurrence removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<PrepaymentMapping> getParameters() {
        return this.parameters;
    }

    public void setParameters(Set<PrepaymentMapping> prepaymentMappings) {
        this.parameters = prepaymentMappings;
    }

    public AmortizationRecurrence parameters(Set<PrepaymentMapping> prepaymentMappings) {
        this.setParameters(prepaymentMappings);
        return this;
    }

    public AmortizationRecurrence addParameters(PrepaymentMapping prepaymentMapping) {
        this.parameters.add(prepaymentMapping);
        return this;
    }

    public AmortizationRecurrence removeParameters(PrepaymentMapping prepaymentMapping) {
        this.parameters.remove(prepaymentMapping);
        return this;
    }

    public Set<UniversallyUniqueMapping> getApplicationParameters() {
        return this.applicationParameters;
    }

    public void setApplicationParameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.applicationParameters = universallyUniqueMappings;
    }

    public AmortizationRecurrence applicationParameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setApplicationParameters(universallyUniqueMappings);
        return this;
    }

    public AmortizationRecurrence addApplicationParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.applicationParameters.add(universallyUniqueMapping);
        return this;
    }

    public AmortizationRecurrence removeApplicationParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.applicationParameters.remove(universallyUniqueMapping);
        return this;
    }

    public DepreciationMethod getDepreciationMethod() {
        return this.depreciationMethod;
    }

    public void setDepreciationMethod(DepreciationMethod depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public AmortizationRecurrence depreciationMethod(DepreciationMethod depreciationMethod) {
        this.setDepreciationMethod(depreciationMethod);
        return this;
    }

    public PrepaymentAccount getPrepaymentAccount() {
        return this.prepaymentAccount;
    }

    public void setPrepaymentAccount(PrepaymentAccount prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public AmortizationRecurrence prepaymentAccount(PrepaymentAccount prepaymentAccount) {
        this.setPrepaymentAccount(prepaymentAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AmortizationRecurrence)) {
            return false;
        }
        return id != null && id.equals(((AmortizationRecurrence) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmortizationRecurrence{" +
            "id=" + getId() +
            ", firstAmortizationDate='" + getFirstAmortizationDate() + "'" +
            ", amortizationFrequency='" + getAmortizationFrequency() + "'" +
            ", numberOfRecurrences=" + getNumberOfRecurrences() +
            ", notes='" + getNotes() + "'" +
            ", notesContentType='" + getNotesContentType() + "'" +
            ", particulars='" + getParticulars() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isOverWritten='" + getIsOverWritten() + "'" +
            ", timeOfInstallation='" + getTimeOfInstallation() + "'" +
            ", recurrenceGuid='" + getRecurrenceGuid() + "'" +
            ", prepaymentAccountGuid='" + getPrepaymentAccountGuid() + "'" +
            "}";
    }
}
