package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.WIPTransferListItem} entity.
 */
public class WIPTransferListItemDTO implements Serializable {

    private Long id;

    private Long wipSequence;

    private String wipParticulars;

    private String transferType;

    private String transferSettlement;

    private LocalDate transferSettlementDate;

    private BigDecimal transferAmount;

    private LocalDate wipTransferDate;

    private String originalSettlement;

    private LocalDate originalSettlementDate;

    private String assetCategory;

    private String serviceOutlet;

    private String workProject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWipSequence() {
        return wipSequence;
    }

    public void setWipSequence(Long wipSequence) {
        this.wipSequence = wipSequence;
    }

    public String getWipParticulars() {
        return wipParticulars;
    }

    public void setWipParticulars(String wipParticulars) {
        this.wipParticulars = wipParticulars;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferSettlement() {
        return transferSettlement;
    }

    public void setTransferSettlement(String transferSettlement) {
        this.transferSettlement = transferSettlement;
    }

    public LocalDate getTransferSettlementDate() {
        return transferSettlementDate;
    }

    public void setTransferSettlementDate(LocalDate transferSettlementDate) {
        this.transferSettlementDate = transferSettlementDate;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public LocalDate getWipTransferDate() {
        return wipTransferDate;
    }

    public void setWipTransferDate(LocalDate wipTransferDate) {
        this.wipTransferDate = wipTransferDate;
    }

    public String getOriginalSettlement() {
        return originalSettlement;
    }

    public void setOriginalSettlement(String originalSettlement) {
        this.originalSettlement = originalSettlement;
    }

    public LocalDate getOriginalSettlementDate() {
        return originalSettlementDate;
    }

    public void setOriginalSettlementDate(LocalDate originalSettlementDate) {
        this.originalSettlementDate = originalSettlementDate;
    }

    public String getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public String getServiceOutlet() {
        return serviceOutlet;
    }

    public void setServiceOutlet(String serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public String getWorkProject() {
        return workProject;
    }

    public void setWorkProject(String workProject) {
        this.workProject = workProject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WIPTransferListItemDTO)) {
            return false;
        }

        WIPTransferListItemDTO wIPTransferListItemDTO = (WIPTransferListItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, wIPTransferListItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WIPTransferListItemDTO{" +
            "id=" + getId() +
            ", wipSequence=" + getWipSequence() +
            ", wipParticulars='" + getWipParticulars() + "'" +
            ", transferType='" + getTransferType() + "'" +
            ", transferSettlement='" + getTransferSettlement() + "'" +
            ", transferSettlementDate='" + getTransferSettlementDate() + "'" +
            ", transferAmount=" + getTransferAmount() +
            ", wipTransferDate='" + getWipTransferDate() + "'" +
            ", originalSettlement='" + getOriginalSettlement() + "'" +
            ", originalSettlementDate='" + getOriginalSettlementDate() + "'" +
            ", assetCategory='" + getAssetCategory() + "'" +
            ", serviceOutlet='" + getServiceOutlet() + "'" +
            ", workProject='" + getWorkProject() + "'" +
            "}";
    }
}
