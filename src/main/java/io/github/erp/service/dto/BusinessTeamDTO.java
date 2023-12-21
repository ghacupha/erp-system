package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
