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

export class NetBookValueEntryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-net-book-value-entry div table .btn-danger'));
  title = element.all(by.css('jhi-net-book-value-entry div h2#page-heading span')).first();
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

export class NetBookValueEntryUpdatePage {
  pageTitle = element(by.id('jhi-net-book-value-entry-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  assetNumberInput = element(by.id('field_assetNumber'));
  assetTagInput = element(by.id('field_assetTag'));
  assetDescriptionInput = element(by.id('field_assetDescription'));
  nbvIdentifierInput = element(by.id('field_nbvIdentifier'));
  compilationJobIdentifierInput = element(by.id('field_compilationJobIdentifier'));
  compilationBatchIdentifierInput = element(by.id('field_compilationBatchIdentifier'));
  elapsedMonthsInput = element(by.id('field_elapsedMonths'));
  priorMonthsInput = element(by.id('field_priorMonths'));
  usefulLifeYearsInput = element(by.id('field_usefulLifeYears'));
  netBookValueAmountInput = element(by.id('field_netBookValueAmount'));
  previousNetBookValueAmountInput = element(by.id('field_previousNetBookValueAmount'));
  historicalCostInput = element(by.id('field_historicalCost'));

  serviceOutletSelect = element(by.id('field_serviceOutlet'));
  depreciationPeriodSelect = element(by.id('field_depreciationPeriod'));
  fiscalMonthSelect = element(by.id('field_fiscalMonth'));
  depreciationMethodSelect = element(by.id('field_depreciationMethod'));
  assetRegistrationSelect = element(by.id('field_assetRegistration'));
  assetCategorySelect = element(by.id('field_assetCategory'));
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

  async setAssetNumberInput(assetNumber: string): Promise<void> {
    await this.assetNumberInput.sendKeys(assetNumber);
  }

  async getAssetNumberInput(): Promise<string> {
    return await this.assetNumberInput.getAttribute('value');
  }

  async setAssetTagInput(assetTag: string): Promise<void> {
    await this.assetTagInput.sendKeys(assetTag);
  }

  async getAssetTagInput(): Promise<string> {
    return await this.assetTagInput.getAttribute('value');
  }

  async setAssetDescriptionInput(assetDescription: string): Promise<void> {
    await this.assetDescriptionInput.sendKeys(assetDescription);
  }

  async getAssetDescriptionInput(): Promise<string> {
    return await this.assetDescriptionInput.getAttribute('value');
  }

  async setNbvIdentifierInput(nbvIdentifier: string): Promise<void> {
    await this.nbvIdentifierInput.sendKeys(nbvIdentifier);
  }

  async getNbvIdentifierInput(): Promise<string> {
    return await this.nbvIdentifierInput.getAttribute('value');
  }

  async setCompilationJobIdentifierInput(compilationJobIdentifier: string): Promise<void> {
    await this.compilationJobIdentifierInput.sendKeys(compilationJobIdentifier);
  }

  async getCompilationJobIdentifierInput(): Promise<string> {
    return await this.compilationJobIdentifierInput.getAttribute('value');
  }

  async setCompilationBatchIdentifierInput(compilationBatchIdentifier: string): Promise<void> {
    await this.compilationBatchIdentifierInput.sendKeys(compilationBatchIdentifier);
  }

  async getCompilationBatchIdentifierInput(): Promise<string> {
    return await this.compilationBatchIdentifierInput.getAttribute('value');
  }

  async setElapsedMonthsInput(elapsedMonths: string): Promise<void> {
    await this.elapsedMonthsInput.sendKeys(elapsedMonths);
  }

  async getElapsedMonthsInput(): Promise<string> {
    return await this.elapsedMonthsInput.getAttribute('value');
  }

  async setPriorMonthsInput(priorMonths: string): Promise<void> {
    await this.priorMonthsInput.sendKeys(priorMonths);
  }

  async getPriorMonthsInput(): Promise<string> {
    return await this.priorMonthsInput.getAttribute('value');
  }

  async setUsefulLifeYearsInput(usefulLifeYears: string): Promise<void> {
    await this.usefulLifeYearsInput.sendKeys(usefulLifeYears);
  }

  async getUsefulLifeYearsInput(): Promise<string> {
    return await this.usefulLifeYearsInput.getAttribute('value');
  }

  async setNetBookValueAmountInput(netBookValueAmount: string): Promise<void> {
    await this.netBookValueAmountInput.sendKeys(netBookValueAmount);
  }

  async getNetBookValueAmountInput(): Promise<string> {
    return await this.netBookValueAmountInput.getAttribute('value');
  }

  async setPreviousNetBookValueAmountInput(previousNetBookValueAmount: string): Promise<void> {
    await this.previousNetBookValueAmountInput.sendKeys(previousNetBookValueAmount);
  }

  async getPreviousNetBookValueAmountInput(): Promise<string> {
    return await this.previousNetBookValueAmountInput.getAttribute('value');
  }

  async setHistoricalCostInput(historicalCost: string): Promise<void> {
    await this.historicalCostInput.sendKeys(historicalCost);
  }

  async getHistoricalCostInput(): Promise<string> {
    return await this.historicalCostInput.getAttribute('value');
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

export class NetBookValueEntryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-netBookValueEntry-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-netBookValueEntry'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
