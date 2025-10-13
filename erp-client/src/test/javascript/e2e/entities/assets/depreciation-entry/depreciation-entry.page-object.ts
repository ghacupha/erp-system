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

export class DepreciationEntryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-depreciation-entry div table .btn-danger'));
  title = element.all(by.css('jhi-depreciation-entry div h2#page-heading span')).first();
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

export class DepreciationEntryUpdatePage {
  pageTitle = element(by.id('jhi-depreciation-entry-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  postedAtInput = element(by.id('field_postedAt'));
  depreciationAmountInput = element(by.id('field_depreciationAmount'));
  assetNumberInput = element(by.id('field_assetNumber'));
  batchSequenceNumberInput = element(by.id('field_batchSequenceNumber'));
  processedItemsInput = element(by.id('field_processedItems'));
  totalItemsProcessedInput = element(by.id('field_totalItemsProcessed'));

  serviceOutletSelect = element(by.id('field_serviceOutlet'));
  assetCategorySelect = element(by.id('field_assetCategory'));
  depreciationMethodSelect = element(by.id('field_depreciationMethod'));
  assetRegistrationSelect = element(by.id('field_assetRegistration'));
  depreciationPeriodSelect = element(by.id('field_depreciationPeriod'));
  fiscalMonthSelect = element(by.id('field_fiscalMonth'));
  fiscalQuarterSelect = element(by.id('field_fiscalQuarter'));
  fiscalYearSelect = element(by.id('field_fiscalYear'));
  depreciationJobSelect = element(by.id('field_depreciationJob'));
  depreciationBatchSequenceSelect = element(by.id('field_depreciationBatchSequence'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setPostedAtInput(postedAt: string): Promise<void> {
    await this.postedAtInput.sendKeys(postedAt);
  }

  async getPostedAtInput(): Promise<string> {
    return await this.postedAtInput.getAttribute('value');
  }

  async setDepreciationAmountInput(depreciationAmount: string): Promise<void> {
    await this.depreciationAmountInput.sendKeys(depreciationAmount);
  }

  async getDepreciationAmountInput(): Promise<string> {
    return await this.depreciationAmountInput.getAttribute('value');
  }

  async setAssetNumberInput(assetNumber: string): Promise<void> {
    await this.assetNumberInput.sendKeys(assetNumber);
  }

  async getAssetNumberInput(): Promise<string> {
    return await this.assetNumberInput.getAttribute('value');
  }

  async setBatchSequenceNumberInput(batchSequenceNumber: string): Promise<void> {
    await this.batchSequenceNumberInput.sendKeys(batchSequenceNumber);
  }

  async getBatchSequenceNumberInput(): Promise<string> {
    return await this.batchSequenceNumberInput.getAttribute('value');
  }

  async setProcessedItemsInput(processedItems: string): Promise<void> {
    await this.processedItemsInput.sendKeys(processedItems);
  }

  async getProcessedItemsInput(): Promise<string> {
    return await this.processedItemsInput.getAttribute('value');
  }

  async setTotalItemsProcessedInput(totalItemsProcessed: string): Promise<void> {
    await this.totalItemsProcessedInput.sendKeys(totalItemsProcessed);
  }

  async getTotalItemsProcessedInput(): Promise<string> {
    return await this.totalItemsProcessedInput.getAttribute('value');
  }

  async serviceOutletSelectLastOption(): Promise<void> {
    await this.serviceOutletSelect.all(by.tagName('option')).last().click();
  }

  async serviceOutletSelectOption(option: string): Promise<void> {
    await this.serviceOutletSelect.sendKeys(option);
  }

  getServiceOutletSelect(): ElementFinder {
    return this.serviceOutletSelect;
  }

  async getServiceOutletSelectedOption(): Promise<string> {
    return await this.serviceOutletSelect.element(by.css('option:checked')).getText();
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

  async depreciationMethodSelectLastOption(): Promise<void> {
    await this.depreciationMethodSelect.all(by.tagName('option')).last().click();
  }

  async depreciationMethodSelectOption(option: string): Promise<void> {
    await this.depreciationMethodSelect.sendKeys(option);
  }

  getDepreciationMethodSelect(): ElementFinder {
    return this.depreciationMethodSelect;
  }

  async getDepreciationMethodSelectedOption(): Promise<string> {
    return await this.depreciationMethodSelect.element(by.css('option:checked')).getText();
  }

  async assetRegistrationSelectLastOption(): Promise<void> {
    await this.assetRegistrationSelect.all(by.tagName('option')).last().click();
  }

  async assetRegistrationSelectOption(option: string): Promise<void> {
    await this.assetRegistrationSelect.sendKeys(option);
  }

  getAssetRegistrationSelect(): ElementFinder {
    return this.assetRegistrationSelect;
  }

  async getAssetRegistrationSelectedOption(): Promise<string> {
    return await this.assetRegistrationSelect.element(by.css('option:checked')).getText();
  }

  async depreciationPeriodSelectLastOption(): Promise<void> {
    await this.depreciationPeriodSelect.all(by.tagName('option')).last().click();
  }

  async depreciationPeriodSelectOption(option: string): Promise<void> {
    await this.depreciationPeriodSelect.sendKeys(option);
  }

  getDepreciationPeriodSelect(): ElementFinder {
    return this.depreciationPeriodSelect;
  }

  async getDepreciationPeriodSelectedOption(): Promise<string> {
    return await this.depreciationPeriodSelect.element(by.css('option:checked')).getText();
  }

  async fiscalMonthSelectLastOption(): Promise<void> {
    await this.fiscalMonthSelect.all(by.tagName('option')).last().click();
  }

  async fiscalMonthSelectOption(option: string): Promise<void> {
    await this.fiscalMonthSelect.sendKeys(option);
  }

  getFiscalMonthSelect(): ElementFinder {
    return this.fiscalMonthSelect;
  }

  async getFiscalMonthSelectedOption(): Promise<string> {
    return await this.fiscalMonthSelect.element(by.css('option:checked')).getText();
  }

  async fiscalQuarterSelectLastOption(): Promise<void> {
    await this.fiscalQuarterSelect.all(by.tagName('option')).last().click();
  }

  async fiscalQuarterSelectOption(option: string): Promise<void> {
    await this.fiscalQuarterSelect.sendKeys(option);
  }

  getFiscalQuarterSelect(): ElementFinder {
    return this.fiscalQuarterSelect;
  }

  async getFiscalQuarterSelectedOption(): Promise<string> {
    return await this.fiscalQuarterSelect.element(by.css('option:checked')).getText();
  }

  async fiscalYearSelectLastOption(): Promise<void> {
    await this.fiscalYearSelect.all(by.tagName('option')).last().click();
  }

  async fiscalYearSelectOption(option: string): Promise<void> {
    await this.fiscalYearSelect.sendKeys(option);
  }

  getFiscalYearSelect(): ElementFinder {
    return this.fiscalYearSelect;
  }

  async getFiscalYearSelectedOption(): Promise<string> {
    return await this.fiscalYearSelect.element(by.css('option:checked')).getText();
  }

  async depreciationJobSelectLastOption(): Promise<void> {
    await this.depreciationJobSelect.all(by.tagName('option')).last().click();
  }

  async depreciationJobSelectOption(option: string): Promise<void> {
    await this.depreciationJobSelect.sendKeys(option);
  }

  getDepreciationJobSelect(): ElementFinder {
    return this.depreciationJobSelect;
  }

  async getDepreciationJobSelectedOption(): Promise<string> {
    return await this.depreciationJobSelect.element(by.css('option:checked')).getText();
  }

  async depreciationBatchSequenceSelectLastOption(): Promise<void> {
    await this.depreciationBatchSequenceSelect.all(by.tagName('option')).last().click();
  }

  async depreciationBatchSequenceSelectOption(option: string): Promise<void> {
    await this.depreciationBatchSequenceSelect.sendKeys(option);
  }

  getDepreciationBatchSequenceSelect(): ElementFinder {
    return this.depreciationBatchSequenceSelect;
  }

  async getDepreciationBatchSequenceSelectedOption(): Promise<string> {
    return await this.depreciationBatchSequenceSelect.element(by.css('option:checked')).getText();
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

export class DepreciationEntryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-depreciationEntry-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-depreciationEntry'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
