package io.github.erp.service.dto;

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

    private String narration;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private ApplicationUserDTO createdBy;

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

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public ApplicationUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUserDTO createdBy) {
        this.createdBy = createdBy;
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
            ", narration='" + getNarration() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
