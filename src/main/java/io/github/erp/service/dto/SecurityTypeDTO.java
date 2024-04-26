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
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SecurityType} entity.
 */
public class SecurityTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String securityTypeCode;

    @NotNull
    private String securityType;

    @Lob
    private String securityTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecurityTypeCode() {
        return securityTypeCode;
    }

    public void setSecurityTypeCode(String securityTypeCode) {
        this.securityTypeCode = securityTypeCode;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getSecurityTypeDetails() {
        return securityTypeDetails;
    }

    public void setSecurityTypeDetails(String securityTypeDetails) {
        this.securityTypeDetails = securityTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityTypeDTO)) {
            return false;
        }

        SecurityTypeDTO securityTypeDTO = (SecurityTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, securityTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityTypeDTO{" +
            "id=" + getId() +
            ", securityTypeCode='" + getSecurityTypeCode() + "'" +
            ", securityType='" + getSecurityType() + "'" +
            ", securityTypeDetails='" + getSecurityTypeDetails() + "'" +
            "}";
    }
}
