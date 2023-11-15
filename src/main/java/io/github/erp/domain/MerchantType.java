package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
