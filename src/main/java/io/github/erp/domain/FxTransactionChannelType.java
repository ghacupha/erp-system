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
