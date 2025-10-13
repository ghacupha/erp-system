///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { element, by, ElementFinder } from 'protractor';

export class LeaseLiabilityScheduleItemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-lease-liability-schedule-item div table .btn-danger'));
  title = element.all(by.css('jhi-lease-liability-schedule-item div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class LeaseLiabilityScheduleItemUpdatePage {
  pageTitle = element(by.id('jhi-lease-liability-schedule-item-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  sequenceNumberInput = element(by.id('field_sequenceNumber'));
  openingBalanceInput = element(by.id('field_openingBalance'));
  cashPaymentInput = element(by.id('field_cashPayment'));
  principalPaymentInput = element(by.id('field_principalPayment'));
  interestPaymentInput = element(by.id('field_interestPayment'));
  outstandingBalanceInput = element(by.id('field_outstandingBalance'));
  interestPayableOpeningInput = element(by.id('field_interestPayableOpening'));
  interestAccruedInput = element(by.id('field_interestAccrued'));
  interestPayableClosingInput = element(by.id('field_interestPayableClosing'));

  placeholderSelect = element(by.id('field_placeholder'));
  universallyUniqueMappingSelect = element(by.id('field_universallyUniqueMapping'));
  leaseAmortizationScheduleSelect = element(by.id('field_leaseAmortizationSchedule'));
  leaseContractSelect = element(by.id('field_leaseContract'));
  leaseLiabilitySelect = element(by.id('field_leaseLiability'));
  leasePeriodSelect = element(by.id('field_leasePeriod'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSequenceNumberInput(sequenceNumber: string): Promise<void> {
    await this.sequenceNumberInput.sendKeys(sequenceNumber);
  }

  async getSequenceNumberInput(): Promise<string> {
    return await this.sequenceNumberInput.getAttribute('value');
  }

  async setOpeningBalanceInput(openingBalance: string): Promise<void> {
    await this.openingBalanceInput.sendKeys(openingBalance);
  }

  async getOpeningBalanceInput(): Promise<string> {
    return await this.openingBalanceInput.getAttribute('value');
  }

  async setCashPaymentInput(cashPayment: string): Promise<void> {
    await this.cashPaymentInput.sendKeys(cashPayment);
  }

  async getCashPaymentInput(): Promise<string> {
    return await this.cashPaymentInput.getAttribute('value');
  }

  async setPrincipalPaymentInput(principalPayment: string): Promise<void> {
    await this.principalPaymentInput.sendKeys(principalPayment);
  }

  async getPrincipalPaymentInput(): Promise<string> {
    return await this.principalPaymentInput.getAttribute('value');
  }

  async setInterestPaymentInput(interestPayment: string): Promise<void> {
    await this.interestPaymentInput.sendKeys(interestPayment);
  }

  async getInterestPaymentInput(): Promise<string> {
    return await this.interestPaymentInput.getAttribute('value');
  }

  async setOutstandingBalanceInput(outstandingBalance: string): Promise<void> {
    await this.outstandingBalanceInput.sendKeys(outstandingBalance);
  }

  async getOutstandingBalanceInput(): Promise<string> {
    return await this.outstandingBalanceInput.getAttribute('value');
  }

  async setInterestPayableOpeningInput(interestPayableOpening: string): Promise<void> {
    await this.interestPayableOpeningInput.sendKeys(interestPayableOpening);
  }

  async getInterestPayableOpeningInput(): Promise<string> {
    return await this.interestPayableOpeningInput.getAttribute('value');
  }

  async setInterestAccruedInput(interestAccrued: string): Promise<void> {
    await this.interestAccruedInput.sendKeys(interestAccrued);
  }

  async getInterestAccruedInput(): Promise<string> {
    return await this.interestAccruedInput.getAttribute('value');
  }

  async setInterestPayableClosingInput(interestPayableClosing: string): Promise<void> {
    await this.interestPayableClosingInput.sendKeys(interestPayableClosing);
  }

  async getInterestPayableClosingInput(): Promise<string> {
    return await this.interestPayableClosingInput.getAttribute('value');
  }

  async placeholderSelectLastOption(): Promise<void> {
    await this.placeholderSelect.all(by.tagName('option')).last().click();
  }

  async placeholderSelectOption(option: string): Promise<void> {
    await this.placeholderSelect.sendKeys(option);
  }

  getPlaceholderSelect(): ElementFinder {
    return this.placeholderSelect;
  }

  async getPlaceholderSelectedOption(): Promise<string> {
    return await this.placeholderSelect.element(by.css('option:checked')).getText();
  }

  async universallyUniqueMappingSelectLastOption(): Promise<void> {
    await this.universallyUniqueMappingSelect.all(by.tagName('option')).last().click();
  }

  async universallyUniqueMappingSelectOption(option: string): Promise<void> {
    await this.universallyUniqueMappingSelect.sendKeys(option);
  }

  getUniversallyUniqueMappingSelect(): ElementFinder {
    return this.universallyUniqueMappingSelect;
  }

  async getUniversallyUniqueMappingSelectedOption(): Promise<string> {
    return await this.universallyUniqueMappingSelect.element(by.css('option:checked')).getText();
  }

  async leaseAmortizationScheduleSelectLastOption(): Promise<void> {
    await this.leaseAmortizationScheduleSelect.all(by.tagName('option')).last().click();
  }

  async leaseAmortizationScheduleSelectOption(option: string): Promise<void> {
    await this.leaseAmortizationScheduleSelect.sendKeys(option);
  }

  getLeaseAmortizationScheduleSelect(): ElementFinder {
    return this.leaseAmortizationScheduleSelect;
  }

  async getLeaseAmortizationScheduleSelectedOption(): Promise<string> {
    return await this.leaseAmortizationScheduleSelect.element(by.css('option:checked')).getText();
  }

  async leaseContractSelectLastOption(): Promise<void> {
    await this.leaseContractSelect.all(by.tagName('option')).last().click();
  }

  async leaseContractSelectOption(option: string): Promise<void> {
    await this.leaseContractSelect.sendKeys(option);
  }

  getLeaseContractSelect(): ElementFinder {
    return this.leaseContractSelect;
  }

  async getLeaseContractSelectedOption(): Promise<string> {
    return await this.leaseContractSelect.element(by.css('option:checked')).getText();
  }

  async leaseLiabilitySelectLastOption(): Promise<void> {
    await this.leaseLiabilitySelect.all(by.tagName('option')).last().click();
  }

  async leaseLiabilitySelectOption(option: string): Promise<void> {
    await this.leaseLiabilitySelect.sendKeys(option);
  }

  getLeaseLiabilitySelect(): ElementFinder {
    return this.leaseLiabilitySelect;
  }

  async getLeaseLiabilitySelectedOption(): Promise<string> {
    return await this.leaseLiabilitySelect.element(by.css('option:checked')).getText();
  }

  async leasePeriodSelectLastOption(): Promise<void> {
    await this.leasePeriodSelect.all(by.tagName('option')).last().click();
  }

  async leasePeriodSelectOption(option: string): Promise<void> {
    await this.leasePeriodSelect.sendKeys(option);
  }

  getLeasePeriodSelect(): ElementFinder {
    return this.leasePeriodSelect;
  }

  async getLeasePeriodSelectedOption(): Promise<string> {
    return await this.leasePeriodSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class LeaseLiabilityScheduleItemDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-leaseLiabilityScheduleItem-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-leaseLiabilityScheduleItem'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
