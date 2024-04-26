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
