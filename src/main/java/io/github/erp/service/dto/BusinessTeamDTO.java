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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.BusinessTeam} entity.
 */
public class BusinessTeamDTO implements Serializable {

    private Long id;

    @NotNull
    private String businessTeam;

    private UserDTO teamMembers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessTeam() {
        return businessTeam;
    }

    public void setBusinessTeam(String businessTeam) {
        this.businessTeam = businessTeam;
    }

    public UserDTO getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(UserDTO teamMembers) {
        this.teamMembers = teamMembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessTeamDTO)) {
            return false;
        }

        BusinessTeamDTO businessTeamDTO = (BusinessTeamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, businessTeamDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessTeamDTO{" +
            "id=" + getId() +
            ", businessTeam='" + getBusinessTeam() + "'" +
            ", teamMembers=" + getTeamMembers() +
            "}";
    }
}
