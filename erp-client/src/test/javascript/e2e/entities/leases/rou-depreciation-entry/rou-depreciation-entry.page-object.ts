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

export class RouDepreciationEntryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-rou-depreciation-entry div table .btn-danger'));
  title = element.all(by.css('jhi-rou-depreciation-entry div h2#page-heading span')).first();
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

export class RouDepreciationEntryUpdatePage {
  pageTitle = element(by.id('jhi-rou-depreciation-entry-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  descriptionInput = element(by.id('field_description'));
  depreciationAmountInput = element(by.id('field_depreciationAmount'));
  outstandingAmountInput = element(by.id('field_outstandingAmount'));
  rouAssetIdentifierInput = element(by.id('field_rouAssetIdentifier'));
  rouDepreciationIdentifierInput = element(by.id('field_rouDepreciationIdentifier'));
  sequenceNumberInput = element(by.id('field_sequenceNumber'));
  invalidatedInput = element(by.id('field_invalidated'));

  debitAccountSelect = element(by.id('field_debitAccount'));
  creditAccountSelect = element(by.id('field_creditAccount'));
  assetCategorySelect = element(by.id('field_assetCategory'));
  leaseContractSelect = element(by.id('field_leaseContract'));
  rouMetadataSelect = element(by.id('field_rouMetadata'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setDepreciationAmountInput(depreciationAmount: string): Promise<void> {
    await this.depreciationAmountInput.sendKeys(depreciationAmount);
  }

  async getDepreciationAmountInput(): Promise<string> {
    return await this.depreciationAmountInput.getAttribute('value');
  }

  async setOutstandingAmountInput(outstandingAmount: string): Promise<void> {
    await this.outstandingAmountInput.sendKeys(outstandingAmount);
  }

  async getOutstandingAmountInput(): Promise<string> {
    return await this.outstandingAmountInput.getAttribute('value');
  }

  async setRouAssetIdentifierInput(rouAssetIdentifier: string): Promise<void> {
    await this.rouAssetIdentifierInput.sendKeys(rouAssetIdentifier);
  }

  async getRouAssetIdentifierInput(): Promise<string> {
    return await this.rouAssetIdentifierInput.getAttribute('value');
  }

  async setRouDepreciationIdentifierInput(rouDepreciationIdentifier: string): Promise<void> {
    await this.rouDepreciationIdentifierInput.sendKeys(rouDepreciationIdentifier);
  }

  async getRouDepreciationIdentifierInput(): Promise<string> {
    return await this.rouDepreciationIdentifierInput.getAttribute('value');
  }

  async setSequenceNumberInput(sequenceNumber: string): Promise<void> {
    await this.sequenceNumberInput.sendKeys(sequenceNumber);
  }

  async getSequenceNumberInput(): Promise<string> {
    return await this.sequenceNumberInput.getAttribute('value');
  }

  getInvalidatedInput(): ElementFinder {
    return this.invalidatedInput;
  }

  async debitAccountSelectLastOption(): Promise<void> {
    await this.debitAccountSelect.all(by.tagName('option')).last().click();
  }

  async debitAccountSelectOption(option: string): Promise<void> {
    await this.debitAccountSelect.sendKeys(option);
  }

  getDebitAccountSelect(): ElementFinder {
    return this.debitAccountSelect;
  }

  async getDebitAccountSelectedOption(): Promise<string> {
    return await this.debitAccountSelect.element(by.css('option:checked')).getText();
  }

  async creditAccountSelectLastOption(): Promise<void> {
    await this.creditAccountSelect.all(by.tagName('option')).last().click();
  }

  async creditAccountSelectOption(option: string): Promise<void> {
    await this.creditAccountSelect.sendKeys(option);
  }

  getCreditAccountSelect(): ElementFinder {
    return this.creditAccountSelect;
  }

  async getCreditAccountSelectedOption(): Promise<string> {
    return await this.creditAccountSelect.element(by.css('option:checked')).getText();
  }

  async assetCategorySelectLastOption(): Promise<void> {
    await this.assetCategorySelect.all(by.tagName('option')).last().click();
  }

  async assetCategorySelectOption(option: string): Promise<void> {
    await this.assetCategorySelect.sendKeys(option);
  }

  getAssetCategorySelect(): ElementFinder {
    return this.assetCategorySelect;
  }

  async getAssetCategorySelectedOption(): Promise<string> {
    return await this.assetCategorySelect.element(by.css('option:checked')).getText();
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

  async rouMetadataSelectLastOption(): Promise<void> {
    await this.rouMetadataSelect.all(by.tagName('option')).last().click();
  }

  async rouMetadataSelectOption(option: string): Promise<void> {
    await this.rouMetadataSelect.sendKeys(option);
  }

  getRouMetadataSelect(): ElementFinder {
    return this.rouMetadataSelect;
  }

  async getRouMetadataSelectedOption(): Promise<string> {
    return await this.rouMetadataSelect.element(by.css('option:checked')).getText();
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

export class RouDepreciationEntryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-rouDepreciationEntry-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-rouDepreciationEntry'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
