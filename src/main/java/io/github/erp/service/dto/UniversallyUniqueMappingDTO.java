package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.UniversallyUniqueMapping} entity.
 */
public class UniversallyUniqueMappingDTO implements Serializable {

    private Long id;

    @NotNull
    private String universalKey;

    private String mappedValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniversalKey() {
        return universalKey;
    }

    public void setUniversalKey(String universalKey) {
        this.universalKey = universalKey;
    }

    public String getMappedValue() {
        return mappedValue;
    }

    public void setMappedValue(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniversallyUniqueMappingDTO)) {
            return false;
        }

        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = (UniversallyUniqueMappingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, universallyUniqueMappingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniversallyUniqueMappingDTO{" +
            "id=" + getId() +
            ", universalKey='" + getUniversalKey() + "'" +
            ", mappedValue='" + getMappedValue() + "'" +
            "}";
    }
}
