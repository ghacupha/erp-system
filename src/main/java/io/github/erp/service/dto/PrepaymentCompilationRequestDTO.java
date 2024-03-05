package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.enumeration.CompilationStatusTypes;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PrepaymentCompilationRequest} entity.
 */
public class PrepaymentCompilationRequestDTO implements Serializable {

    private Long id;

    private ZonedDateTime timeOfRequest;

    private CompilationStatusTypes compilationStatus;

    private Integer itemsProcessed;

    @NotNull
    private UUID compilationToken;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeOfRequest() {
        return timeOfRequest;
    }

    public void setTimeOfRequest(ZonedDateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public CompilationStatusTypes getCompilationStatus() {
        return compilationStatus;
    }

    public void setCompilationStatus(CompilationStatusTypes compilationStatus) {
        this.compilationStatus = compilationStatus;
    }

    public Integer getItemsProcessed() {
        return itemsProcessed;
    }

    public void setItemsProcessed(Integer itemsProcessed) {
        this.itemsProcessed = itemsProcessed;
    }

    public UUID getCompilationToken() {
        return compilationToken;
    }

    public void setCompilationToken(UUID compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentCompilationRequestDTO)) {
            return false;
        }

        PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO = (PrepaymentCompilationRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prepaymentCompilationRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentCompilationRequestDTO{" +
            "id=" + getId() +
            ", timeOfRequest='" + getTimeOfRequest() + "'" +
            ", compilationStatus='" + getCompilationStatus() + "'" +
            ", itemsProcessed=" + getItemsProcessed() +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
