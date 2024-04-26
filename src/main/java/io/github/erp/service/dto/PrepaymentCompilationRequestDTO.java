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
