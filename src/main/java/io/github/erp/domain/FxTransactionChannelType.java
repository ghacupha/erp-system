package io.github.erp.domain;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
 * A FxTransactionChannelType.
 */
@Entity
@Table(name = "fx_transaction_channel_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fxtransactionchanneltype")
public class FxTransactionChannelType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fx_transaction_channel_code", nullable = false, unique = true)
    private String fxTransactionChannelCode;

    @NotNull
    @Column(name = "fx_transaction_channel_type", nullable = false, unique = true)
    private String fxTransactionChannelType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "fx_channel_type_details")
    private String fxChannelTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FxTransactionChannelType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFxTransactionChannelCode() {
        return this.fxTransactionChannelCode;
    }

    public FxTransactionChannelType fxTransactionChannelCode(String fxTransactionChannelCode) {
        this.setFxTransactionChannelCode(fxTransactionChannelCode);
        return this;
    }

    public void setFxTransactionChannelCode(String fxTransactionChannelCode) {
        this.fxTransactionChannelCode = fxTransactionChannelCode;
    }

    public String getFxTransactionChannelType() {
        return this.fxTransactionChannelType;
    }

    public FxTransactionChannelType fxTransactionChannelType(String fxTransactionChannelType) {
        this.setFxTransactionChannelType(fxTransactionChannelType);
        return this;
    }

    public void setFxTransactionChannelType(String fxTransactionChannelType) {
        this.fxTransactionChannelType = fxTransactionChannelType;
    }

    public String getFxChannelTypeDetails() {
        return this.fxChannelTypeDetails;
    }

    public FxTransactionChannelType fxChannelTypeDetails(String fxChannelTypeDetails) {
        this.setFxChannelTypeDetails(fxChannelTypeDetails);
        return this;
    }

    public void setFxChannelTypeDetails(String fxChannelTypeDetails) {
        this.fxChannelTypeDetails = fxChannelTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxTransactionChannelType)) {
            return false;
        }
        return id != null && id.equals(((FxTransactionChannelType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxTransactionChannelType{" +
            "id=" + getId() +
            ", fxTransactionChannelCode='" + getFxTransactionChannelCode() + "'" +
            ", fxTransactionChannelType='" + getFxTransactionChannelType() + "'" +
            ", fxChannelTypeDetails='" + getFxChannelTypeDetails() + "'" +
            "}";
    }
}
