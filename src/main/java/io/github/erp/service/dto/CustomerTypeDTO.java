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
 * A DTO for the {@link io.github.erp.domain.CustomerType} entity.
 */
public class CustomerTypeDTO implements Serializable {

    private Long id;

    private String customerTypeCode;

    private String customerType;

    @Lob
    private String customerTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerTypeCode() {
        return customerTypeCode;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerTypeDescription() {
        return customerTypeDescription;
    }

    public void setCustomerTypeDescription(String customerTypeDescription) {
        this.customerTypeDescription = customerTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerTypeDTO)) {
            return false;
        }

        CustomerTypeDTO customerTypeDTO = (CustomerTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerTypeDTO{" +
            "id=" + getId() +
            ", customerTypeCode='" + getCustomerTypeCode() + "'" +
            ", customerType='" + getCustomerType() + "'" +
            ", customerTypeDescription='" + getCustomerTypeDescription() + "'" +
            "}";
    }
}
