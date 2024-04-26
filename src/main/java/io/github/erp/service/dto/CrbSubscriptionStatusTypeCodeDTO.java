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
