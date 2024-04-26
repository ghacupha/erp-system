package io.github.erp.domain;

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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A MerchantType.
 */
@Entity
@Table(name = "merchant_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "merchanttype")
public class MerchantType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "merchant_type_code", nullable = false, unique = true)
    private String merchantTypeCode;

    @NotNull
    @Column(name = "merchant_type", nullable = false, unique = true)
    private String merchantType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "merchant_type_details")
    private String merchantTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MerchantType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantTypeCode() {
        return this.merchantTypeCode;
    }

    public MerchantType merchantTypeCode(String merchantTypeCode) {
        this.setMerchantTypeCode(merchantTypeCode);
        return this;
    }

    public void setMerchantTypeCode(String merchantTypeCode) {
        this.merchantTypeCode = merchantTypeCode;
    }

    public String getMerchantType() {
        return this.merchantType;
    }

    public MerchantType merchantType(String merchantType) {
        this.setMerchantType(merchantType);
        return this;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMerchantTypeDetails() {
        return this.merchantTypeDetails;
    }

    public MerchantType merchantTypeDetails(String merchantTypeDetails) {
        this.setMerchantTypeDetails(merchantTypeDetails);
        return this;
    }

    public void setMerchantTypeDetails(String merchantTypeDetails) {
        this.merchantTypeDetails = merchantTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MerchantType)) {
            return false;
        }
        return id != null && id.equals(((MerchantType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MerchantType{" +
            "id=" + getId() +
            ", merchantTypeCode='" + getMerchantTypeCode() + "'" +
            ", merchantType='" + getMerchantType() + "'" +
            ", merchantTypeDetails='" + getMerchantTypeDetails() + "'" +
            "}";
    }
}
