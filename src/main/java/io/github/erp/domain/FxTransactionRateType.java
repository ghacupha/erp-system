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
 * A FxTransactionRateType.
 */
@Entity
@Table(name = "fx_transaction_rate_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fxtransactionratetype")
public class FxTransactionRateType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fx_transaction_rate_type_code", nullable = false, unique = true)
    private String fxTransactionRateTypeCode;

    @NotNull
    @Column(name = "fx_transaction_rate_type", nullable = false, unique = true)
    private String fxTransactionRateType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "fx_transaction_rate_type_details")
    private String fxTransactionRateTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FxTransactionRateType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFxTransactionRateTypeCode() {
        return this.fxTransactionRateTypeCode;
    }

    public FxTransactionRateType fxTransactionRateTypeCode(String fxTransactionRateTypeCode) {
        this.setFxTransactionRateTypeCode(fxTransactionRateTypeCode);
        return this;
    }

    public void setFxTransactionRateTypeCode(String fxTransactionRateTypeCode) {
        this.fxTransactionRateTypeCode = fxTransactionRateTypeCode;
    }

    public String getFxTransactionRateType() {
        return this.fxTransactionRateType;
    }

    public FxTransactionRateType fxTransactionRateType(String fxTransactionRateType) {
        this.setFxTransactionRateType(fxTransactionRateType);
        return this;
    }

    public void setFxTransactionRateType(String fxTransactionRateType) {
        this.fxTransactionRateType = fxTransactionRateType;
    }

    public String getFxTransactionRateTypeDetails() {
        return this.fxTransactionRateTypeDetails;
    }

    public FxTransactionRateType fxTransactionRateTypeDetails(String fxTransactionRateTypeDetails) {
        this.setFxTransactionRateTypeDetails(fxTransactionRateTypeDetails);
        return this;
    }

    public void setFxTransactionRateTypeDetails(String fxTransactionRateTypeDetails) {
        this.fxTransactionRateTypeDetails = fxTransactionRateTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxTransactionRateType)) {
            return false;
        }
        return id != null && id.equals(((FxTransactionRateType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxTransactionRateType{" +
            "id=" + getId() +
            ", fxTransactionRateTypeCode='" + getFxTransactionRateTypeCode() + "'" +
            ", fxTransactionRateType='" + getFxTransactionRateType() + "'" +
            ", fxTransactionRateTypeDetails='" + getFxTransactionRateTypeDetails() + "'" +
            "}";
    }
}
