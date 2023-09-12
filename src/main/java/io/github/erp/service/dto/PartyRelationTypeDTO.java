package io.github.erp.service.dto;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PartyRelationType} entity.
 */
public class PartyRelationTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String partyRelationTypeCode;

    @NotNull
    private String partyRelationType;

    @Lob
    private String partyRelationTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartyRelationTypeCode() {
        return partyRelationTypeCode;
    }

    public void setPartyRelationTypeCode(String partyRelationTypeCode) {
        this.partyRelationTypeCode = partyRelationTypeCode;
    }

    public String getPartyRelationType() {
        return partyRelationType;
    }

    public void setPartyRelationType(String partyRelationType) {
        this.partyRelationType = partyRelationType;
    }

    public String getPartyRelationTypeDescription() {
        return partyRelationTypeDescription;
    }

    public void setPartyRelationTypeDescription(String partyRelationTypeDescription) {
        this.partyRelationTypeDescription = partyRelationTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartyRelationTypeDTO)) {
            return false;
        }

        PartyRelationTypeDTO partyRelationTypeDTO = (PartyRelationTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, partyRelationTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyRelationTypeDTO{" +
            "id=" + getId() +
            ", partyRelationTypeCode='" + getPartyRelationTypeCode() + "'" +
            ", partyRelationType='" + getPartyRelationType() + "'" +
            ", partyRelationTypeDescription='" + getPartyRelationTypeDescription() + "'" +
            "}";
    }
}
