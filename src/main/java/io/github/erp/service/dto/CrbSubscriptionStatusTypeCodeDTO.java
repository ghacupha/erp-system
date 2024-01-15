package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
 * A DTO for the {@link io.github.erp.domain.CrbSubscriptionStatusTypeCode} entity.
 */
public class CrbSubscriptionStatusTypeCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String subscriptionStatusTypeCode;

    @NotNull
    private String subscriptionStatusType;

    @Lob
    private String subscriptionStatusTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubscriptionStatusTypeCode() {
        return subscriptionStatusTypeCode;
    }

    public void setSubscriptionStatusTypeCode(String subscriptionStatusTypeCode) {
        this.subscriptionStatusTypeCode = subscriptionStatusTypeCode;
    }

    public String getSubscriptionStatusType() {
        return subscriptionStatusType;
    }

    public void setSubscriptionStatusType(String subscriptionStatusType) {
        this.subscriptionStatusType = subscriptionStatusType;
    }

    public String getSubscriptionStatusTypeDescription() {
        return subscriptionStatusTypeDescription;
    }

    public void setSubscriptionStatusTypeDescription(String subscriptionStatusTypeDescription) {
        this.subscriptionStatusTypeDescription = subscriptionStatusTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbSubscriptionStatusTypeCodeDTO)) {
            return false;
        }

        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = (CrbSubscriptionStatusTypeCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crbSubscriptionStatusTypeCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbSubscriptionStatusTypeCodeDTO{" +
            "id=" + getId() +
            ", subscriptionStatusTypeCode='" + getSubscriptionStatusTypeCode() + "'" +
            ", subscriptionStatusType='" + getSubscriptionStatusType() + "'" +
            ", subscriptionStatusTypeDescription='" + getSubscriptionStatusTypeDescription() + "'" +
            "}";
    }
}
