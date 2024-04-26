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
 * A TerminalTypes.
 */
@Entity
@Table(name = "terminal_types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "terminaltypes")
public class TerminalTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "txn_terminal_type_code", nullable = false, unique = true)
    private String txnTerminalTypeCode;

    @NotNull
    @Column(name = "txn_channel_type", nullable = false, unique = true)
    private String txnChannelType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "txn_channel_type_details")
    private String txnChannelTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TerminalTypes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxnTerminalTypeCode() {
        return this.txnTerminalTypeCode;
    }

    public TerminalTypes txnTerminalTypeCode(String txnTerminalTypeCode) {
        this.setTxnTerminalTypeCode(txnTerminalTypeCode);
        return this;
    }

    public void setTxnTerminalTypeCode(String txnTerminalTypeCode) {
        this.txnTerminalTypeCode = txnTerminalTypeCode;
    }

    public String getTxnChannelType() {
        return this.txnChannelType;
    }

    public TerminalTypes txnChannelType(String txnChannelType) {
        this.setTxnChannelType(txnChannelType);
        return this;
    }

    public void setTxnChannelType(String txnChannelType) {
        this.txnChannelType = txnChannelType;
    }

    public String getTxnChannelTypeDetails() {
        return this.txnChannelTypeDetails;
    }

    public TerminalTypes txnChannelTypeDetails(String txnChannelTypeDetails) {
        this.setTxnChannelTypeDetails(txnChannelTypeDetails);
        return this;
    }

    public void setTxnChannelTypeDetails(String txnChannelTypeDetails) {
        this.txnChannelTypeDetails = txnChannelTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TerminalTypes)) {
            return false;
        }
        return id != null && id.equals(((TerminalTypes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TerminalTypes{" +
            "id=" + getId() +
            ", txnTerminalTypeCode='" + getTxnTerminalTypeCode() + "'" +
            ", txnChannelType='" + getTxnChannelType() + "'" +
            ", txnChannelTypeDetails='" + getTxnChannelTypeDetails() + "'" +
            "}";
    }
}
