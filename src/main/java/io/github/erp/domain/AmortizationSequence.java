package io.github.erp.domain;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.0
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AmortizationSequence.
 */
@Entity
@Table(name = "amortization_sequence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "amortizationsequence")
public class AmortizationSequence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "prepayment_account_guid", nullable = false)
    private UUID prepaymentAccountGuid;

    @NotNull
    @Column(name = "recurrence_guid", nullable = false)
    private UUID recurrenceGuid;

    @NotNull
    @Column(name = "sequence_number", nullable = false)
    private Integer sequenceNumber;

    @Column(name = "particulars")
    private String particulars;

    @NotNull
    @Column(name = "current_amortization_date", nullable = false)
    private LocalDate currentAmortizationDate;

    @Column(name = "previous_amortization_date")
    private LocalDate previousAmortizationDate;

    @Column(name = "next_amortization_date")
    private LocalDate nextAmortizationDate;

    @NotNull
    @Column(name = "is_commencement_sequence", nullable = false)
    private Boolean isCommencementSequence;

    @NotNull
    @Column(name = "is_terminal_sequence", nullable = false)
    private Boolean isTerminalSequence;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amortization_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amortizationAmount;

    @NotNull
    @Column(name = "sequence_guid", nullable = false, unique = true)
    private UUID sequenceGuid;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "placeholders", "parameters", "applicationParameters", "depreciationMethod", "prepaymentAccount" },
        allowSetters = true
    )
    private AmortizationRecurrence amortizationRecurrence;

    @ManyToMany
    @JoinTable(
        name = "rel_amortization_sequence__placeholder",
        joinColumns = @JoinColumn(name = "amortization_sequence_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_amortization_sequence__prepayment_mapping",
        joinColumns = @JoinColumn(name = "amortization_sequence_id"),
        inverseJoinColumns = @JoinColumn(name = "prepayment_mapping_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private Set<PrepaymentMapping> prepaymentMappings = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_amortization_sequence__application_parameters",
        joinColumns = @JoinColumn(name = "amortization_sequence_id"),
        inverseJoinColumns = @JoinColumn(name = "application_parameters_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> applicationParameters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AmortizationSequence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPrepaymentAccountGuid() {
        return this.prepaymentAccountGuid;
    }

    public AmortizationSequence prepaymentAccountGuid(UUID prepaymentAccountGuid) {
        this.setPrepaymentAccountGuid(prepaymentAccountGuid);
        return this;
    }

    public void setPrepaymentAccountGuid(UUID prepaymentAccountGuid) {
        this.prepaymentAccountGuid = prepaymentAccountGuid;
    }

    public UUID getRecurrenceGuid() {
        return this.recurrenceGuid;
    }

    public AmortizationSequence recurrenceGuid(UUID recurrenceGuid) {
        this.setRecurrenceGuid(recurrenceGuid);
        return this;
    }

    public void setRecurrenceGuid(UUID recurrenceGuid) {
        this.recurrenceGuid = recurrenceGuid;
    }

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public AmortizationSequence sequenceNumber(Integer sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getParticulars() {
        return this.particulars;
    }

    public AmortizationSequence particulars(String particulars) {
        this.setParticulars(particulars);
        return this;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public LocalDate getCurrentAmortizationDate() {
        return this.currentAmortizationDate;
    }

    public AmortizationSequence currentAmortizationDate(LocalDate currentAmortizationDate) {
        this.setCurrentAmortizationDate(currentAmortizationDate);
        return this;
    }

    public void setCurrentAmortizationDate(LocalDate currentAmortizationDate) {
        this.currentAmortizationDate = currentAmortizationDate;
    }

    public LocalDate getPreviousAmortizationDate() {
        return this.previousAmortizationDate;
    }

    public AmortizationSequence previousAmortizationDate(LocalDate previousAmortizationDate) {
        this.setPreviousAmortizationDate(previousAmortizationDate);
        return this;
    }

    public void setPreviousAmortizationDate(LocalDate previousAmortizationDate) {
        this.previousAmortizationDate = previousAmortizationDate;
    }

    public LocalDate getNextAmortizationDate() {
        return this.nextAmortizationDate;
    }

    public AmortizationSequence nextAmortizationDate(LocalDate nextAmortizationDate) {
        this.setNextAmortizationDate(nextAmortizationDate);
        return this;
    }

    public void setNextAmortizationDate(LocalDate nextAmortizationDate) {
        this.nextAmortizationDate = nextAmortizationDate;
    }

    public Boolean getIsCommencementSequence() {
        return this.isCommencementSequence;
    }

    public AmortizationSequence isCommencementSequence(Boolean isCommencementSequence) {
        this.setIsCommencementSequence(isCommencementSequence);
        return this;
    }

    public void setIsCommencementSequence(Boolean isCommencementSequence) {
        this.isCommencementSequence = isCommencementSequence;
    }

    public Boolean getIsTerminalSequence() {
        return this.isTerminalSequence;
    }

    public AmortizationSequence isTerminalSequence(Boolean isTerminalSequence) {
        this.setIsTerminalSequence(isTerminalSequence);
        return this;
    }

    public void setIsTerminalSequence(Boolean isTerminalSequence) {
        this.isTerminalSequence = isTerminalSequence;
    }

    public BigDecimal getAmortizationAmount() {
        return this.amortizationAmount;
    }

    public AmortizationSequence amortizationAmount(BigDecimal amortizationAmount) {
        this.setAmortizationAmount(amortizationAmount);
        return this;
    }

    public void setAmortizationAmount(BigDecimal amortizationAmount) {
        this.amortizationAmount = amortizationAmount;
    }

    public UUID getSequenceGuid() {
        return this.sequenceGuid;
    }

    public AmortizationSequence sequenceGuid(UUID sequenceGuid) {
        this.setSequenceGuid(sequenceGuid);
        return this;
    }

    public void setSequenceGuid(UUID sequenceGuid) {
        this.sequenceGuid = sequenceGuid;
    }

    public PrepaymentAccount getPrepaymentAccount() {
        return this.prepaymentAccount;
    }

    public void setPrepaymentAccount(PrepaymentAccount prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public AmortizationSequence prepaymentAccount(PrepaymentAccount prepaymentAccount) {
        this.setPrepaymentAccount(prepaymentAccount);
        return this;
    }

    public AmortizationRecurrence getAmortizationRecurrence() {
        return this.amortizationRecurrence;
    }

    public void setAmortizationRecurrence(AmortizationRecurrence amortizationRecurrence) {
        this.amortizationRecurrence = amortizationRecurrence;
    }

    public AmortizationSequence amortizationRecurrence(AmortizationRecurrence amortizationRecurrence) {
        this.setAmortizationRecurrence(amortizationRecurrence);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public AmortizationSequence placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public AmortizationSequence addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public AmortizationSequence removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<PrepaymentMapping> getPrepaymentMappings() {
        return this.prepaymentMappings;
    }

    public void setPrepaymentMappings(Set<PrepaymentMapping> prepaymentMappings) {
        this.prepaymentMappings = prepaymentMappings;
    }

    public AmortizationSequence prepaymentMappings(Set<PrepaymentMapping> prepaymentMappings) {
        this.setPrepaymentMappings(prepaymentMappings);
        return this;
    }

    public AmortizationSequence addPrepaymentMapping(PrepaymentMapping prepaymentMapping) {
        this.prepaymentMappings.add(prepaymentMapping);
        return this;
    }

    public AmortizationSequence removePrepaymentMapping(PrepaymentMapping prepaymentMapping) {
        this.prepaymentMappings.remove(prepaymentMapping);
        return this;
    }

    public Set<UniversallyUniqueMapping> getApplicationParameters() {
        return this.applicationParameters;
    }

    public void setApplicationParameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.applicationParameters = universallyUniqueMappings;
    }

    public AmortizationSequence applicationParameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setApplicationParameters(universallyUniqueMappings);
        return this;
    }

    public AmortizationSequence addApplicationParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.applicationParameters.add(universallyUniqueMapping);
        return this;
    }

    public AmortizationSequence removeApplicationParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.applicationParameters.remove(universallyUniqueMapping);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AmortizationSequence)) {
            return false;
        }
        return id != null && id.equals(((AmortizationSequence) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmortizationSequence{" +
            "id=" + getId() +
            ", prepaymentAccountGuid='" + getPrepaymentAccountGuid() + "'" +
            ", recurrenceGuid='" + getRecurrenceGuid() + "'" +
            ", sequenceNumber=" + getSequenceNumber() +
            ", particulars='" + getParticulars() + "'" +
            ", currentAmortizationDate='" + getCurrentAmortizationDate() + "'" +
            ", previousAmortizationDate='" + getPreviousAmortizationDate() + "'" +
            ", nextAmortizationDate='" + getNextAmortizationDate() + "'" +
            ", isCommencementSequence='" + getIsCommencementSequence() + "'" +
            ", isTerminalSequence='" + getIsTerminalSequence() + "'" +
            ", amortizationAmount=" + getAmortizationAmount() +
            ", sequenceGuid='" + getSequenceGuid() + "'" +
            "}";
    }
}
