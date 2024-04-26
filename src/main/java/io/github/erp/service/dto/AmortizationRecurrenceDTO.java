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
import io.github.erp.domain.enumeration.recurrenceFrequency;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AmortizationRecurrence} entity.
 */
public class AmortizationRecurrenceDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate firstAmortizationDate;

    @NotNull
    private recurrenceFrequency amortizationFrequency;

    @NotNull
    private Integer numberOfRecurrences;

    @Lob
    private byte[] notes;

    private String notesContentType;
    private String particulars;

    private Boolean isActive;

    private Boolean isOverWritten;

    @NotNull
    private ZonedDateTime timeOfInstallation;

    @NotNull
    private UUID recurrenceGuid;

    @NotNull
    private UUID prepaymentAccountGuid;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<PrepaymentMappingDTO> parameters = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> applicationParameters = new HashSet<>();

    private DepreciationMethodDTO depreciationMethod;

    private PrepaymentAccountDTO prepaymentAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFirstAmortizationDate() {
        return firstAmortizationDate;
    }

    public void setFirstAmortizationDate(LocalDate firstAmortizationDate) {
        this.firstAmortizationDate = firstAmortizationDate;
    }

    public recurrenceFrequency getAmortizationFrequency() {
        return amortizationFrequency;
    }

    public void setAmortizationFrequency(recurrenceFrequency amortizationFrequency) {
        this.amortizationFrequency = amortizationFrequency;
    }

    public Integer getNumberOfRecurrences() {
        return numberOfRecurrences;
    }

    public void setNumberOfRecurrences(Integer numberOfRecurrences) {
        this.numberOfRecurrences = numberOfRecurrences;
    }

    public byte[] getNotes() {
        return notes;
    }

    public void setNotes(byte[] notes) {
        this.notes = notes;
    }

    public String getNotesContentType() {
        return notesContentType;
    }

    public void setNotesContentType(String notesContentType) {
        this.notesContentType = notesContentType;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsOverWritten() {
        return isOverWritten;
    }

    public void setIsOverWritten(Boolean isOverWritten) {
        this.isOverWritten = isOverWritten;
    }

    public ZonedDateTime getTimeOfInstallation() {
        return timeOfInstallation;
    }

    public void setTimeOfInstallation(ZonedDateTime timeOfInstallation) {
        this.timeOfInstallation = timeOfInstallation;
    }

    public UUID getRecurrenceGuid() {
        return recurrenceGuid;
    }

    public void setRecurrenceGuid(UUID recurrenceGuid) {
        this.recurrenceGuid = recurrenceGuid;
    }

    public UUID getPrepaymentAccountGuid() {
        return prepaymentAccountGuid;
    }

    public void setPrepaymentAccountGuid(UUID prepaymentAccountGuid) {
        this.prepaymentAccountGuid = prepaymentAccountGuid;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<PrepaymentMappingDTO> getParameters() {
        return parameters;
    }

    public void setParameters(Set<PrepaymentMappingDTO> parameters) {
        this.parameters = parameters;
    }

    public Set<UniversallyUniqueMappingDTO> getApplicationParameters() {
        return applicationParameters;
    }

    public void setApplicationParameters(Set<UniversallyUniqueMappingDTO> applicationParameters) {
        this.applicationParameters = applicationParameters;
    }

    public DepreciationMethodDTO getDepreciationMethod() {
        return depreciationMethod;
    }

    public void setDepreciationMethod(DepreciationMethodDTO depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public PrepaymentAccountDTO getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public void setPrepaymentAccount(PrepaymentAccountDTO prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AmortizationRecurrenceDTO)) {
            return false;
        }

        AmortizationRecurrenceDTO amortizationRecurrenceDTO = (AmortizationRecurrenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, amortizationRecurrenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmortizationRecurrenceDTO{" +
            "id=" + getId() +
            ", firstAmortizationDate='" + getFirstAmortizationDate() + "'" +
            ", amortizationFrequency='" + getAmortizationFrequency() + "'" +
            ", numberOfRecurrences=" + getNumberOfRecurrences() +
            ", notes='" + getNotes() + "'" +
            ", particulars='" + getParticulars() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isOverWritten='" + getIsOverWritten() + "'" +
            ", timeOfInstallation='" + getTimeOfInstallation() + "'" +
            ", recurrenceGuid='" + getRecurrenceGuid() + "'" +
            ", prepaymentAccountGuid='" + getPrepaymentAccountGuid() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", parameters=" + getParameters() +
            ", applicationParameters=" + getApplicationParameters() +
            ", depreciationMethod=" + getDepreciationMethod() +
            ", prepaymentAccount=" + getPrepaymentAccount() +
            "}";
    }
}
