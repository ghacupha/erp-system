package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A FxTransactionType.
 */
@Entity
@Table(name = "fx_transaction_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fxtransactiontype")
public class FxTransactionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fx_transaction_type_code", nullable = false, unique = true)
    private String fxTransactionTypeCode;

    @NotNull
    @Column(name = "fx_transaction_type", nullable = false, unique = true)
    private String fxTransactionType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "fx_transaction_type_description")
    private String fxTransactionTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FxTransactionType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFxTransactionTypeCode() {
        return this.fxTransactionTypeCode;
    }

    public FxTransactionType fxTransactionTypeCode(String fxTransactionTypeCode) {
        this.setFxTransactionTypeCode(fxTransactionTypeCode);
        return this;
    }

    public void setFxTransactionTypeCode(String fxTransactionTypeCode) {
        this.fxTransactionTypeCode = fxTransactionTypeCode;
    }

    public String getFxTransactionType() {
        return this.fxTransactionType;
    }

    public FxTransactionType fxTransactionType(String fxTransactionType) {
        this.setFxTransactionType(fxTransactionType);
        return this;
    }

    public void setFxTransactionType(String fxTransactionType) {
        this.fxTransactionType = fxTransactionType;
    }

    public String getFxTransactionTypeDescription() {
        return this.fxTransactionTypeDescription;
    }

    public FxTransactionType fxTransactionTypeDescription(String fxTransactionTypeDescription) {
        this.setFxTransactionTypeDescription(fxTransactionTypeDescription);
        return this;
    }

    public void setFxTransactionTypeDescription(String fxTransactionTypeDescription) {
        this.fxTransactionTypeDescription = fxTransactionTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxTransactionType)) {
            return false;
        }
        return id != null && id.equals(((FxTransactionType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxTransactionType{" +
            "id=" + getId() +
            ", fxTransactionTypeCode='" + getFxTransactionTypeCode() + "'" +
            ", fxTransactionType='" + getFxTransactionType() + "'" +
            ", fxTransactionTypeDescription='" + getFxTransactionTypeDescription() + "'" +
            "}";
    }
}
