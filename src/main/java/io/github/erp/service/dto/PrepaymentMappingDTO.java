package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PrepaymentMapping} entity.
 */
public class PrepaymentMappingDTO implements Serializable {

    private Long id;

    @NotNull
    private String parameterKey;

    @NotNull
    private UUID parameterGuid;

    @NotNull
    private String parameter;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParameterKey() {
        return parameterKey;
    }

    public void setParameterKey(String parameterKey) {
        this.parameterKey = parameterKey;
    }

    public UUID getParameterGuid() {
        return parameterGuid;
    }

    public void setParameterGuid(UUID parameterGuid) {
        this.parameterGuid = parameterGuid;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
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
        if (!(o instanceof PrepaymentMappingDTO)) {
            return false;
        }

        PrepaymentMappingDTO prepaymentMappingDTO = (PrepaymentMappingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prepaymentMappingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentMappingDTO{" +
            "id=" + getId() +
            ", parameterKey='" + getParameterKey() + "'" +
            ", parameterGuid='" + getParameterGuid() + "'" +
            ", parameter='" + getParameter() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
