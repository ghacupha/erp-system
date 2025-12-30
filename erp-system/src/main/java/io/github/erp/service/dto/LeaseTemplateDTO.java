package io.github.erp.service.dto;
/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LeaseTemplate} entity.
 */
public class LeaseTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    private String templateTitle;

    private TransactionAccountDTO assetAccount;

    private TransactionAccountDTO depreciationAccount;

    private TransactionAccountDTO accruedDepreciationAccount;

    private TransactionAccountDTO interestPaidTransferDebitAccount;

    private TransactionAccountDTO interestPaidTransferCreditAccount;

    private TransactionAccountDTO interestAccruedDebitAccount;

    private TransactionAccountDTO interestAccruedCreditAccount;

    private TransactionAccountDTO leaseRecognitionDebitAccount;

    private TransactionAccountDTO leaseRecognitionCreditAccount;

    private TransactionAccountDTO leaseRepaymentDebitAccount;

    private TransactionAccountDTO leaseRepaymentCreditAccount;

    private TransactionAccountDTO rouRecognitionCreditAccount;

    private TransactionAccountDTO rouRecognitionDebitAccount;

    private AssetCategoryDTO assetCategory;

    private ServiceOutletDTO serviceOutlet;

    private DealerDTO mainDealer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    public TransactionAccountDTO getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(TransactionAccountDTO assetAccount) {
        this.assetAccount = assetAccount;
    }

    public TransactionAccountDTO getDepreciationAccount() {
        return depreciationAccount;
    }

    public void setDepreciationAccount(TransactionAccountDTO depreciationAccount) {
        this.depreciationAccount = depreciationAccount;
    }

    public TransactionAccountDTO getAccruedDepreciationAccount() {
        return accruedDepreciationAccount;
    }

    public void setAccruedDepreciationAccount(TransactionAccountDTO accruedDepreciationAccount) {
        this.accruedDepreciationAccount = accruedDepreciationAccount;
    }

    public TransactionAccountDTO getInterestPaidTransferDebitAccount() {
        return interestPaidTransferDebitAccount;
    }

    public void setInterestPaidTransferDebitAccount(TransactionAccountDTO interestPaidTransferDebitAccount) {
        this.interestPaidTransferDebitAccount = interestPaidTransferDebitAccount;
    }

    public TransactionAccountDTO getInterestPaidTransferCreditAccount() {
        return interestPaidTransferCreditAccount;
    }

    public void setInterestPaidTransferCreditAccount(TransactionAccountDTO interestPaidTransferCreditAccount) {
        this.interestPaidTransferCreditAccount = interestPaidTransferCreditAccount;
    }

    public TransactionAccountDTO getInterestAccruedDebitAccount() {
        return interestAccruedDebitAccount;
    }

    public void setInterestAccruedDebitAccount(TransactionAccountDTO interestAccruedDebitAccount) {
        this.interestAccruedDebitAccount = interestAccruedDebitAccount;
    }

    public TransactionAccountDTO getInterestAccruedCreditAccount() {
        return interestAccruedCreditAccount;
    }

    public void setInterestAccruedCreditAccount(TransactionAccountDTO interestAccruedCreditAccount) {
        this.interestAccruedCreditAccount = interestAccruedCreditAccount;
    }

    public TransactionAccountDTO getLeaseRecognitionDebitAccount() {
        return leaseRecognitionDebitAccount;
    }

    public void setLeaseRecognitionDebitAccount(TransactionAccountDTO leaseRecognitionDebitAccount) {
        this.leaseRecognitionDebitAccount = leaseRecognitionDebitAccount;
    }

    public TransactionAccountDTO getLeaseRecognitionCreditAccount() {
        return leaseRecognitionCreditAccount;
    }

    public void setLeaseRecognitionCreditAccount(TransactionAccountDTO leaseRecognitionCreditAccount) {
        this.leaseRecognitionCreditAccount = leaseRecognitionCreditAccount;
    }

    public TransactionAccountDTO getLeaseRepaymentDebitAccount() {
        return leaseRepaymentDebitAccount;
    }

    public void setLeaseRepaymentDebitAccount(TransactionAccountDTO leaseRepaymentDebitAccount) {
        this.leaseRepaymentDebitAccount = leaseRepaymentDebitAccount;
    }

    public TransactionAccountDTO getLeaseRepaymentCreditAccount() {
        return leaseRepaymentCreditAccount;
    }

    public void setLeaseRepaymentCreditAccount(TransactionAccountDTO leaseRepaymentCreditAccount) {
        this.leaseRepaymentCreditAccount = leaseRepaymentCreditAccount;
    }

    public TransactionAccountDTO getRouRecognitionCreditAccount() {
        return rouRecognitionCreditAccount;
    }

    public void setRouRecognitionCreditAccount(TransactionAccountDTO rouRecognitionCreditAccount) {
        this.rouRecognitionCreditAccount = rouRecognitionCreditAccount;
    }

    public TransactionAccountDTO getRouRecognitionDebitAccount() {
        return rouRecognitionDebitAccount;
    }

    public void setRouRecognitionDebitAccount(TransactionAccountDTO rouRecognitionDebitAccount) {
        this.rouRecognitionDebitAccount = rouRecognitionDebitAccount;
    }

    public AssetCategoryDTO getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategoryDTO assetCategory) {
        this.assetCategory = assetCategory;
    }

    public ServiceOutletDTO getServiceOutlet() {
        return serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutletDTO serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public DealerDTO getMainDealer() {
        return mainDealer;
    }

    public void setMainDealer(DealerDTO mainDealer) {
        this.mainDealer = mainDealer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseTemplateDTO)) {
            return false;
        }

        LeaseTemplateDTO leaseTemplateDTO = (LeaseTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaseTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseTemplateDTO{" +
            "id=" + getId() +
            ", templateTitle='" + getTemplateTitle() + "'" +
            "}";
    }
}
