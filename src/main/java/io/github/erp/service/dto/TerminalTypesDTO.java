package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.TerminalTypes} entity.
 */
public class TerminalTypesDTO implements Serializable {

    private Long id;

    @NotNull
    private String txnTerminalTypeCode;

    @NotNull
    private String txnChannelType;

    @Lob
    private String txnChannelTypeDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxnTerminalTypeCode() {
        return txnTerminalTypeCode;
    }

    public void setTxnTerminalTypeCode(String txnTerminalTypeCode) {
        this.txnTerminalTypeCode = txnTerminalTypeCode;
    }

    public String getTxnChannelType() {
        return txnChannelType;
    }

    public void setTxnChannelType(String txnChannelType) {
        this.txnChannelType = txnChannelType;
    }

    public String getTxnChannelTypeDetails() {
        return txnChannelTypeDetails;
    }

    public void setTxnChannelTypeDetails(String txnChannelTypeDetails) {
        this.txnChannelTypeDetails = txnChannelTypeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TerminalTypesDTO)) {
            return false;
        }

        TerminalTypesDTO terminalTypesDTO = (TerminalTypesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, terminalTypesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TerminalTypesDTO{" +
            "id=" + getId() +
            ", txnTerminalTypeCode='" + getTxnTerminalTypeCode() + "'" +
            ", txnChannelType='" + getTxnChannelType() + "'" +
            ", txnChannelTypeDetails='" + getTxnChannelTypeDetails() + "'" +
            "}";
    }
}
