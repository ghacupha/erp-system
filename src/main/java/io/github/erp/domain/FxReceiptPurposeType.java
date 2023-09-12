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

/**
 * A FxReceiptPurposeType.
 */
@Entity
@Table(name = "fx_receipt_purpose_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fxreceiptpurposetype")
public class FxReceiptPurposeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "item_code", nullable = false, unique = true)
    private String itemCode;

    @Column(name = "attribute_1_receipt_payment_purpose_code")
    private String attribute1ReceiptPaymentPurposeCode;

    @Column(name = "attribute_1_receipt_payment_purpose_type")
    private String attribute1ReceiptPaymentPurposeType;

    @Column(name = "attribute_2_receipt_payment_purpose_code")
    private String attribute2ReceiptPaymentPurposeCode;

    @Column(name = "attribute_2_receipt_payment_purpose_description")
    private String attribute2ReceiptPaymentPurposeDescription;

    @Column(name = "attribute_3_receipt_payment_purpose_code")
    private String attribute3ReceiptPaymentPurposeCode;

    @Column(name = "attribute_3_receipt_payment_purpose_description")
    private String attribute3ReceiptPaymentPurposeDescription;

    @Column(name = "attribute_4_receipt_payment_purpose_code")
    private String attribute4ReceiptPaymentPurposeCode;

    @Column(name = "attribute_4_receipt_payment_purpose_description")
    private String attribute4ReceiptPaymentPurposeDescription;

    @Column(name = "attribute_5_receipt_payment_purpose_code")
    private String attribute5ReceiptPaymentPurposeCode;

    @Column(name = "attribute_5_receipt_payment_purpose_description")
    private String attribute5ReceiptPaymentPurposeDescription;

    @Column(name = "last_child")
    private String lastChild;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FxReceiptPurposeType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public FxReceiptPurposeType itemCode(String itemCode) {
        this.setItemCode(itemCode);
        return this;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getAttribute1ReceiptPaymentPurposeCode() {
        return this.attribute1ReceiptPaymentPurposeCode;
    }

    public FxReceiptPurposeType attribute1ReceiptPaymentPurposeCode(String attribute1ReceiptPaymentPurposeCode) {
        this.setAttribute1ReceiptPaymentPurposeCode(attribute1ReceiptPaymentPurposeCode);
        return this;
    }

    public void setAttribute1ReceiptPaymentPurposeCode(String attribute1ReceiptPaymentPurposeCode) {
        this.attribute1ReceiptPaymentPurposeCode = attribute1ReceiptPaymentPurposeCode;
    }

    public String getAttribute1ReceiptPaymentPurposeType() {
        return this.attribute1ReceiptPaymentPurposeType;
    }

    public FxReceiptPurposeType attribute1ReceiptPaymentPurposeType(String attribute1ReceiptPaymentPurposeType) {
        this.setAttribute1ReceiptPaymentPurposeType(attribute1ReceiptPaymentPurposeType);
        return this;
    }

    public void setAttribute1ReceiptPaymentPurposeType(String attribute1ReceiptPaymentPurposeType) {
        this.attribute1ReceiptPaymentPurposeType = attribute1ReceiptPaymentPurposeType;
    }

    public String getAttribute2ReceiptPaymentPurposeCode() {
        return this.attribute2ReceiptPaymentPurposeCode;
    }

    public FxReceiptPurposeType attribute2ReceiptPaymentPurposeCode(String attribute2ReceiptPaymentPurposeCode) {
        this.setAttribute2ReceiptPaymentPurposeCode(attribute2ReceiptPaymentPurposeCode);
        return this;
    }

    public void setAttribute2ReceiptPaymentPurposeCode(String attribute2ReceiptPaymentPurposeCode) {
        this.attribute2ReceiptPaymentPurposeCode = attribute2ReceiptPaymentPurposeCode;
    }

    public String getAttribute2ReceiptPaymentPurposeDescription() {
        return this.attribute2ReceiptPaymentPurposeDescription;
    }

    public FxReceiptPurposeType attribute2ReceiptPaymentPurposeDescription(String attribute2ReceiptPaymentPurposeDescription) {
        this.setAttribute2ReceiptPaymentPurposeDescription(attribute2ReceiptPaymentPurposeDescription);
        return this;
    }

    public void setAttribute2ReceiptPaymentPurposeDescription(String attribute2ReceiptPaymentPurposeDescription) {
        this.attribute2ReceiptPaymentPurposeDescription = attribute2ReceiptPaymentPurposeDescription;
    }

    public String getAttribute3ReceiptPaymentPurposeCode() {
        return this.attribute3ReceiptPaymentPurposeCode;
    }

    public FxReceiptPurposeType attribute3ReceiptPaymentPurposeCode(String attribute3ReceiptPaymentPurposeCode) {
        this.setAttribute3ReceiptPaymentPurposeCode(attribute3ReceiptPaymentPurposeCode);
        return this;
    }

    public void setAttribute3ReceiptPaymentPurposeCode(String attribute3ReceiptPaymentPurposeCode) {
        this.attribute3ReceiptPaymentPurposeCode = attribute3ReceiptPaymentPurposeCode;
    }

    public String getAttribute3ReceiptPaymentPurposeDescription() {
        return this.attribute3ReceiptPaymentPurposeDescription;
    }

    public FxReceiptPurposeType attribute3ReceiptPaymentPurposeDescription(String attribute3ReceiptPaymentPurposeDescription) {
        this.setAttribute3ReceiptPaymentPurposeDescription(attribute3ReceiptPaymentPurposeDescription);
        return this;
    }

    public void setAttribute3ReceiptPaymentPurposeDescription(String attribute3ReceiptPaymentPurposeDescription) {
        this.attribute3ReceiptPaymentPurposeDescription = attribute3ReceiptPaymentPurposeDescription;
    }

    public String getAttribute4ReceiptPaymentPurposeCode() {
        return this.attribute4ReceiptPaymentPurposeCode;
    }

    public FxReceiptPurposeType attribute4ReceiptPaymentPurposeCode(String attribute4ReceiptPaymentPurposeCode) {
        this.setAttribute4ReceiptPaymentPurposeCode(attribute4ReceiptPaymentPurposeCode);
        return this;
    }

    public void setAttribute4ReceiptPaymentPurposeCode(String attribute4ReceiptPaymentPurposeCode) {
        this.attribute4ReceiptPaymentPurposeCode = attribute4ReceiptPaymentPurposeCode;
    }

    public String getAttribute4ReceiptPaymentPurposeDescription() {
        return this.attribute4ReceiptPaymentPurposeDescription;
    }

    public FxReceiptPurposeType attribute4ReceiptPaymentPurposeDescription(String attribute4ReceiptPaymentPurposeDescription) {
        this.setAttribute4ReceiptPaymentPurposeDescription(attribute4ReceiptPaymentPurposeDescription);
        return this;
    }

    public void setAttribute4ReceiptPaymentPurposeDescription(String attribute4ReceiptPaymentPurposeDescription) {
        this.attribute4ReceiptPaymentPurposeDescription = attribute4ReceiptPaymentPurposeDescription;
    }

    public String getAttribute5ReceiptPaymentPurposeCode() {
        return this.attribute5ReceiptPaymentPurposeCode;
    }

    public FxReceiptPurposeType attribute5ReceiptPaymentPurposeCode(String attribute5ReceiptPaymentPurposeCode) {
        this.setAttribute5ReceiptPaymentPurposeCode(attribute5ReceiptPaymentPurposeCode);
        return this;
    }

    public void setAttribute5ReceiptPaymentPurposeCode(String attribute5ReceiptPaymentPurposeCode) {
        this.attribute5ReceiptPaymentPurposeCode = attribute5ReceiptPaymentPurposeCode;
    }

    public String getAttribute5ReceiptPaymentPurposeDescription() {
        return this.attribute5ReceiptPaymentPurposeDescription;
    }

    public FxReceiptPurposeType attribute5ReceiptPaymentPurposeDescription(String attribute5ReceiptPaymentPurposeDescription) {
        this.setAttribute5ReceiptPaymentPurposeDescription(attribute5ReceiptPaymentPurposeDescription);
        return this;
    }

    public void setAttribute5ReceiptPaymentPurposeDescription(String attribute5ReceiptPaymentPurposeDescription) {
        this.attribute5ReceiptPaymentPurposeDescription = attribute5ReceiptPaymentPurposeDescription;
    }

    public String getLastChild() {
        return this.lastChild;
    }

    public FxReceiptPurposeType lastChild(String lastChild) {
        this.setLastChild(lastChild);
        return this;
    }

    public void setLastChild(String lastChild) {
        this.lastChild = lastChild;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxReceiptPurposeType)) {
            return false;
        }
        return id != null && id.equals(((FxReceiptPurposeType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxReceiptPurposeType{" +
            "id=" + getId() +
            ", itemCode='" + getItemCode() + "'" +
            ", attribute1ReceiptPaymentPurposeCode='" + getAttribute1ReceiptPaymentPurposeCode() + "'" +
            ", attribute1ReceiptPaymentPurposeType='" + getAttribute1ReceiptPaymentPurposeType() + "'" +
            ", attribute2ReceiptPaymentPurposeCode='" + getAttribute2ReceiptPaymentPurposeCode() + "'" +
            ", attribute2ReceiptPaymentPurposeDescription='" + getAttribute2ReceiptPaymentPurposeDescription() + "'" +
            ", attribute3ReceiptPaymentPurposeCode='" + getAttribute3ReceiptPaymentPurposeCode() + "'" +
            ", attribute3ReceiptPaymentPurposeDescription='" + getAttribute3ReceiptPaymentPurposeDescription() + "'" +
            ", attribute4ReceiptPaymentPurposeCode='" + getAttribute4ReceiptPaymentPurposeCode() + "'" +
            ", attribute4ReceiptPaymentPurposeDescription='" + getAttribute4ReceiptPaymentPurposeDescription() + "'" +
            ", attribute5ReceiptPaymentPurposeCode='" + getAttribute5ReceiptPaymentPurposeCode() + "'" +
            ", attribute5ReceiptPaymentPurposeDescription='" + getAttribute5ReceiptPaymentPurposeDescription() + "'" +
            ", lastChild='" + getLastChild() + "'" +
            "}";
    }
}
