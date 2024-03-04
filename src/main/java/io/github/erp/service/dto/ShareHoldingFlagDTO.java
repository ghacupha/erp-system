package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.enumeration.ShareholdingFlagTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ShareHoldingFlag} entity.
 */
public class ShareHoldingFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private ShareholdingFlagTypes shareholdingFlagTypeCode;

    @NotNull
    private String shareholdingFlagType;

    @Lob
    private String shareholdingTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShareholdingFlagTypes getShareholdingFlagTypeCode() {
        return shareholdingFlagTypeCode;
    }

    public void setShareholdingFlagTypeCode(ShareholdingFlagTypes shareholdingFlagTypeCode) {
        this.shareholdingFlagTypeCode = shareholdingFlagTypeCode;
    }

    public String getShareholdingFlagType() {
        return shareholdingFlagType;
    }

    public void setShareholdingFlagType(String shareholdingFlagType) {
        this.shareholdingFlagType = shareholdingFlagType;
    }

    public String getShareholdingTypeDescription() {
        return shareholdingTypeDescription;
    }

    public void setShareholdingTypeDescription(String shareholdingTypeDescription) {
        this.shareholdingTypeDescription = shareholdingTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShareHoldingFlagDTO)) {
            return false;
        }

        ShareHoldingFlagDTO shareHoldingFlagDTO = (ShareHoldingFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shareHoldingFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareHoldingFlagDTO{" +
            "id=" + getId() +
            ", shareholdingFlagTypeCode='" + getShareholdingFlagTypeCode() + "'" +
            ", shareholdingFlagType='" + getShareholdingFlagType() + "'" +
            ", shareholdingTypeDescription='" + getShareholdingTypeDescription() + "'" +
            "}";
    }
}
