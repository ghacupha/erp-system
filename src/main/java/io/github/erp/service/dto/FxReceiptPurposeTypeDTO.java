package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.FxReceiptPurposeType} entity.
 */
public class FxReceiptPurposeTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String itemCode;

    private String attribute1ReceiptPaymentPurposeCode;

    private String attribute1ReceiptPaymentPurposeType;

    private String attribute2ReceiptPaymentPurposeCode;

    private String attribute2ReceiptPaymentPurposeDescription;

    private String attribute3ReceiptPaymentPurposeCode;

    private String attribute3ReceiptPaymentPurposeDescription;

    private String attribute4ReceiptPaymentPurposeCode;

    private String attribute4ReceiptPaymentPurposeDescription;

    private String attribute5ReceiptPaymentPurposeCode;

    private String attribute5ReceiptPaymentPurposeDescription;

    private String lastChild;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getAttribute1ReceiptPaymentPurposeCode() {
        return attribute1ReceiptPaymentPurposeCode;
    }

    public void setAttribute1ReceiptPaymentPurposeCode(String attribute1ReceiptPaymentPurposeCode) {
        this.attribute1ReceiptPaymentPurposeCode = attribute1ReceiptPaymentPurposeCode;
    }

    public String getAttribute1ReceiptPaymentPurposeType() {
        return attribute1ReceiptPaymentPurposeType;
    }

    public void setAttribute1ReceiptPaymentPurposeType(String attribute1ReceiptPaymentPurposeType) {
        this.attribute1ReceiptPaymentPurposeType = attribute1ReceiptPaymentPurposeType;
    }

    public String getAttribute2ReceiptPaymentPurposeCode() {
        return attribute2ReceiptPaymentPurposeCode;
    }

    public void setAttribute2ReceiptPaymentPurposeCode(String attribute2ReceiptPaymentPurposeCode) {
        this.attribute2ReceiptPaymentPurposeCode = attribute2ReceiptPaymentPurposeCode;
    }

    public String getAttribute2ReceiptPaymentPurposeDescription() {
        return attribute2ReceiptPaymentPurposeDescription;
    }

    public void setAttribute2ReceiptPaymentPurposeDescription(String attribute2ReceiptPaymentPurposeDescription) {
        this.attribute2ReceiptPaymentPurposeDescription = attribute2ReceiptPaymentPurposeDescription;
    }

    public String getAttribute3ReceiptPaymentPurposeCode() {
        return attribute3ReceiptPaymentPurposeCode;
    }

    public void setAttribute3ReceiptPaymentPurposeCode(String attribute3ReceiptPaymentPurposeCode) {
        this.attribute3ReceiptPaymentPurposeCode = attribute3ReceiptPaymentPurposeCode;
    }

    public String getAttribute3ReceiptPaymentPurposeDescription() {
        return attribute3ReceiptPaymentPurposeDescription;
    }

    public void setAttribute3ReceiptPaymentPurposeDescription(String attribute3ReceiptPaymentPurposeDescription) {
        this.attribute3ReceiptPaymentPurposeDescription = attribute3ReceiptPaymentPurposeDescription;
    }

    public String getAttribute4ReceiptPaymentPurposeCode() {
        return attribute4ReceiptPaymentPurposeCode;
    }

    public void setAttribute4ReceiptPaymentPurposeCode(String attribute4ReceiptPaymentPurposeCode) {
        this.attribute4ReceiptPaymentPurposeCode = attribute4ReceiptPaymentPurposeCode;
    }

    public String getAttribute4ReceiptPaymentPurposeDescription() {
        return attribute4ReceiptPaymentPurposeDescription;
    }

    public void setAttribute4ReceiptPaymentPurposeDescription(String attribute4ReceiptPaymentPurposeDescription) {
        this.attribute4ReceiptPaymentPurposeDescription = attribute4ReceiptPaymentPurposeDescription;
    }

    public String getAttribute5ReceiptPaymentPurposeCode() {
        return attribute5ReceiptPaymentPurposeCode;
    }

    public void setAttribute5ReceiptPaymentPurposeCode(String attribute5ReceiptPaymentPurposeCode) {
        this.attribute5ReceiptPaymentPurposeCode = attribute5ReceiptPaymentPurposeCode;
    }

    public String getAttribute5ReceiptPaymentPurposeDescription() {
        return attribute5ReceiptPaymentPurposeDescription;
    }

    public void setAttribute5ReceiptPaymentPurposeDescription(String attribute5ReceiptPaymentPurposeDescription) {
        this.attribute5ReceiptPaymentPurposeDescription = attribute5ReceiptPaymentPurposeDescription;
    }

    public String getLastChild() {
        return lastChild;
    }

    public void setLastChild(String lastChild) {
        this.lastChild = lastChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxReceiptPurposeTypeDTO)) {
            return false;
        }

        FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO = (FxReceiptPurposeTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fxReceiptPurposeTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxReceiptPurposeTypeDTO{" +
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
