package io.github.erp.domain;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
