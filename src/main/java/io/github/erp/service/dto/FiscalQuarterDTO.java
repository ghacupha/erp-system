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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.FiscalQuarter} entity.
 */
public class FiscalQuarterDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer quarterNumber;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private String fiscalQuarterCode;

    private FiscalYearDTO fiscalYear;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> universallyUniqueMappings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuarterNumber() {
        return quarterNumber;
    }

    public void setQuarterNumber(Integer quarterNumber) {
        this.quarterNumber = quarterNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getFiscalQuarterCode() {
        return fiscalQuarterCode;
    }

    public void setFiscalQuarterCode(String fiscalQuarterCode) {
        this.fiscalQuarterCode = fiscalQuarterCode;
    }

    public FiscalYearDTO getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYearDTO fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<UniversallyUniqueMappingDTO> getUniversallyUniqueMappings() {
        return universallyUniqueMappings;
    }

    public void setUniversallyUniqueMappings(Set<UniversallyUniqueMappingDTO> universallyUniqueMappings) {
        this.universallyUniqueMappings = universallyUniqueMappings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiscalQuarterDTO)) {
            return false;
        }

        FiscalQuarterDTO fiscalQuarterDTO = (FiscalQuarterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fiscalQuarterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalQuarterDTO{" +
            "id=" + getId() +
            ", quarterNumber=" + getQuarterNumber() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", fiscalQuarterCode='" + getFiscalQuarterCode() + "'" +
            ", fiscalYear=" + getFiscalYear() +
            ", placeholders=" + getPlaceholders() +
            ", universallyUniqueMappings=" + getUniversallyUniqueMappings() +
            "}";
    }
}
