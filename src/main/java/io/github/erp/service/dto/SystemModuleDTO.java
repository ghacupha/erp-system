package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SystemModule} entity.
 */
public class SystemModuleDTO implements Serializable {

    private Long id;

    @NotNull
    private String moduleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemModuleDTO)) {
            return false;
        }

        SystemModuleDTO systemModuleDTO = (SystemModuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemModuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemModuleDTO{" +
            "id=" + getId() +
            ", moduleName='" + getModuleName() + "'" +
            "}";
    }
}
