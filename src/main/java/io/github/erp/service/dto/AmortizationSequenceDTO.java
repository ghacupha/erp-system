package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AmortizationSequence} entity.
 */
public class AmortizationSequenceDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID prepaymentAccountGuid;

    @NotNull
    private UUID recurrenceGuid;

    @NotNull
    private Integer sequenceNumber;

    private String particulars;

    @NotNull
    private LocalDate currentAmortizationDate;

    private LocalDate previousAmortizationDate;

    private LocalDate nextAmortizationDate;

    @NotNull
    private Boolean isCommencementSequence;

    @NotNull
    private Boolean isTerminalSequence;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amortizationAmount;

    @NotNull
    private UUID sequenceGuid;

    private PrepaymentAccountDTO prepaymentAccount;

    private AmortizationRecurrenceDTO amortizationRecurrence;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<PrepaymentMappingDTO> prepaymentMappings = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> applicationParameters = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPrepaymentAccountGuid() {
        return prepaymentAccountGuid;
    }

    public void setPrepaymentAccountGuid(UUID prepaymentAccountGuid) {
        this.prepaymentAccountGuid = prepaymentAccountGuid;
    }

    public UUID getRecurrenceGuid() {
        return recurrenceGuid;
    }

    public void setRecurrenceGuid(UUID recurrenceGuid) {
        this.recurrenceGuid = recurrenceGuid;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public LocalDate getCurrentAmortizationDate() {
        return currentAmortizationDate;
    }

    public void setCurrentAmortizationDate(LocalDate currentAmortizationDate) {
        this.currentAmortizationDate = currentAmortizationDate;
    }

    public LocalDate getPreviousAmortizationDate() {
        return previousAmortizationDate;
    }

    public void setPreviousAmortizationDate(LocalDate previousAmortizationDate) {
        this.previousAmortizationDate = previousAmortizationDate;
    }

    public LocalDate getNextAmortizationDate() {
        return nextAmortizationDate;
    }

    public void setNextAmortizationDate(LocalDate nextAmortizationDate) {
        this.nextAmortizationDate = nextAmortizationDate;
    }

    public Boolean getIsCommencementSequence() {
        return isCommencementSequence;
    }

    public void setIsCommencementSequence(Boolean isCommencementSequence) {
        this.isCommencementSequence = isCommencementSequence;
    }

    public Boolean getIsTerminalSequence() {
        return isTerminalSequence;
    }

    public void setIsTerminalSequence(Boolean isTerminalSequence) {
        this.isTerminalSequence = isTerminalSequence;
    }

    public BigDecimal getAmortizationAmount() {
        return amortizationAmount;
    }

    public void setAmortizationAmount(BigDecimal amortizationAmount) {
        this.amortizationAmount = amortizationAmount;
    }

    public UUID getSequenceGuid() {
        return sequenceGuid;
    }

    public void setSequenceGuid(UUID sequenceGuid) {
        this.sequenceGuid = sequenceGuid;
    }

    public PrepaymentAccountDTO getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public void setPrepaymentAccount(PrepaymentAccountDTO prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public AmortizationRecurrenceDTO getAmortizationRecurrence() {
        return amortizationRecurrence;
    }

    public void setAmortizationRecurrence(AmortizationRecurrenceDTO amortizationRecurrence) {
        this.amortizationRecurrence = amortizationRecurrence;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<PrepaymentMappingDTO> getPrepaymentMappings() {
        return prepaymentMappings;
    }

    public void setPrepaymentMappings(Set<PrepaymentMappingDTO> prepaymentMappings) {
        this.prepaymentMappings = prepaymentMappings;
    }

    public Set<UniversallyUniqueMappingDTO> getApplicationParameters() {
        return applicationParameters;
    }

    public void setApplicationParameters(Set<UniversallyUniqueMappingDTO> applicationParameters) {
        this.applicationParameters = applicationParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AmortizationSequenceDTO)) {
            return false;
        }

        AmortizationSequenceDTO amortizationSequenceDTO = (AmortizationSequenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, amortizationSequenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmortizationSequenceDTO{" +
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
            ", prepaymentAccount=" + getPrepaymentAccount() +
            ", amortizationRecurrence=" + getAmortizationRecurrence() +
            ", placeholders=" + getPlaceholders() +
            ", prepaymentMappings=" + getPrepaymentMappings() +
            ", applicationParameters=" + getApplicationParameters() +
            "}";
    }
}
