package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.Placeholder} entity.
 */
public class PlaceholderDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    private String token;

    private PlaceholderDTO containingPlaceholder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PlaceholderDTO getContainingPlaceholder() {
        return containingPlaceholder;
    }

    public void setContainingPlaceholder(PlaceholderDTO containingPlaceholder) {
        this.containingPlaceholder = containingPlaceholder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaceholderDTO)) {
            return false;
        }

        PlaceholderDTO placeholderDTO = (PlaceholderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, placeholderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaceholderDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", token='" + getToken() + "'" +
            ", containingPlaceholder=" + getContainingPlaceholder() +
            "}";
    }
}
