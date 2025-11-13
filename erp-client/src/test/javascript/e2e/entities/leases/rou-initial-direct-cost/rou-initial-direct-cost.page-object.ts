///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

export class RouInitialDirectCostComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-rou-initial-direct-cost div table .btn-danger'));
  title = element.all(by.css('jhi-rou-initial-direct-cost div h2#page-heading span')).first();
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

export class RouInitialDirectCostUpdatePage {
  pageTitle = element(by.id('jhi-rou-initial-direct-cost-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  transactionDateInput = element(by.id('field_transactionDate'));
  descriptionInput = element(by.id('field_description'));
  costInput = element(by.id('field_cost'));
  referenceNumberInput = element(by.id('field_referenceNumber'));

  leaseContractSelect = element(by.id('field_leaseContract'));
  settlementDetailsSelect = element(by.id('field_settlementDetails'));
  targetROUAccountSelect = element(by.id('field_targetROUAccount'));
  transferAccountSelect = element(by.id('field_transferAccount'));
  placeholderSelect = element(by.id('field_placeholder'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setTransactionDateInput(transactionDate: string): Promise<void> {
    await this.transactionDateInput.sendKeys(transactionDate);
  }

  async getTransactionDateInput(): Promise<string> {
    return await this.transactionDateInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setCostInput(cost: string): Promise<void> {
    await this.costInput.sendKeys(cost);
  }

  async getCostInput(): Promise<string> {
    return await this.costInput.getAttribute('value');
  }

  async setReferenceNumberInput(referenceNumber: string): Promise<void> {
    await this.referenceNumberInput.sendKeys(referenceNumber);
  }

  async getReferenceNumberInput(): Promise<string> {
    return await this.referenceNumberInput.getAttribute('value');
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

  async settlementDetailsSelectLastOption(): Promise<void> {
    await this.settlementDetailsSelect.all(by.tagName('option')).last().click();
  }

  async settlementDetailsSelectOption(option: string): Promise<void> {
    await this.settlementDetailsSelect.sendKeys(option);
  }

  getSettlementDetailsSelect(): ElementFinder {
    return this.settlementDetailsSelect;
  }

  async getSettlementDetailsSelectedOption(): Promise<string> {
    return await this.settlementDetailsSelect.element(by.css('option:checked')).getText();
  }

  async targetROUAccountSelectLastOption(): Promise<void> {
    await this.targetROUAccountSelect.all(by.tagName('option')).last().click();
  }

  async targetROUAccountSelectOption(option: string): Promise<void> {
    await this.targetROUAccountSelect.sendKeys(option);
  }

  getTargetROUAccountSelect(): ElementFinder {
    return this.targetROUAccountSelect;
  }

  async getTargetROUAccountSelectedOption(): Promise<string> {
    return await this.targetROUAccountSelect.element(by.css('option:checked')).getText();
  }

  async transferAccountSelectLastOption(): Promise<void> {
    await this.transferAccountSelect.all(by.tagName('option')).last().click();
  }

  async transferAccountSelectOption(option: string): Promise<void> {
    await this.transferAccountSelect.sendKeys(option);
  }

  getTransferAccountSelect(): ElementFinder {
    return this.transferAccountSelect;
  }

  async getTransferAccountSelectedOption(): Promise<string> {
    return await this.transferAccountSelect.element(by.css('option:checked')).getText();
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

export class RouInitialDirectCostDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-rouInitialDirectCost-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-rouInitialDirectCost'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
