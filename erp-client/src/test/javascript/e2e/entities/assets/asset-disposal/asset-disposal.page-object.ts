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

export class AssetDisposalComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-asset-disposal div table .btn-danger'));
  title = element.all(by.css('jhi-asset-disposal div h2#page-heading span')).first();
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

export class AssetDisposalUpdatePage {
  pageTitle = element(by.id('jhi-asset-disposal-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  assetDisposalReferenceInput = element(by.id('field_assetDisposalReference'));
  descriptionInput = element(by.id('field_description'));
  assetCostInput = element(by.id('field_assetCost'));
  historicalCostInput = element(by.id('field_historicalCost'));
  accruedDepreciationInput = element(by.id('field_accruedDepreciation'));
  netBookValueInput = element(by.id('field_netBookValue'));
  decommissioningDateInput = element(by.id('field_decommissioningDate'));
  disposalDateInput = element(by.id('field_disposalDate'));
  dormantInput = element(by.id('field_dormant'));

  createdBySelect = element(by.id('field_createdBy'));
  modifiedBySelect = element(by.id('field_modifiedBy'));
  lastAccessedBySelect = element(by.id('field_lastAccessedBy'));
  effectivePeriodSelect = element(by.id('field_effectivePeriod'));
  placeholderSelect = element(by.id('field_placeholder'));
  assetDisposedSelect = element(by.id('field_assetDisposed'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAssetDisposalReferenceInput(assetDisposalReference: string): Promise<void> {
    await this.assetDisposalReferenceInput.sendKeys(assetDisposalReference);
  }

  async getAssetDisposalReferenceInput(): Promise<string> {
    return await this.assetDisposalReferenceInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setAssetCostInput(assetCost: string): Promise<void> {
    await this.assetCostInput.sendKeys(assetCost);
  }

  async getAssetCostInput(): Promise<string> {
    return await this.assetCostInput.getAttribute('value');
  }

  async setHistoricalCostInput(historicalCost: string): Promise<void> {
    await this.historicalCostInput.sendKeys(historicalCost);
  }

  async getHistoricalCostInput(): Promise<string> {
    return await this.historicalCostInput.getAttribute('value');
  }

  async setAccruedDepreciationInput(accruedDepreciation: string): Promise<void> {
    await this.accruedDepreciationInput.sendKeys(accruedDepreciation);
  }

  async getAccruedDepreciationInput(): Promise<string> {
    return await this.accruedDepreciationInput.getAttribute('value');
  }

  async setNetBookValueInput(netBookValue: string): Promise<void> {
    await this.netBookValueInput.sendKeys(netBookValue);
  }

  async getNetBookValueInput(): Promise<string> {
    return await this.netBookValueInput.getAttribute('value');
  }

  async setDecommissioningDateInput(decommissioningDate: string): Promise<void> {
    await this.decommissioningDateInput.sendKeys(decommissioningDate);
  }

  async getDecommissioningDateInput(): Promise<string> {
    return await this.decommissioningDateInput.getAttribute('value');
  }

  async setDisposalDateInput(disposalDate: string): Promise<void> {
    await this.disposalDateInput.sendKeys(disposalDate);
  }

  async getDisposalDateInput(): Promise<string> {
    return await this.disposalDateInput.getAttribute('value');
  }

  getDormantInput(): ElementFinder {
    return this.dormantInput;
  }

  async createdBySelectLastOption(): Promise<void> {
    await this.createdBySelect.all(by.tagName('option')).last().click();
  }

  async createdBySelectOption(option: string): Promise<void> {
    await this.createdBySelect.sendKeys(option);
  }

  getCreatedBySelect(): ElementFinder {
    return this.createdBySelect;
  }

  async getCreatedBySelectedOption(): Promise<string> {
    return await this.createdBySelect.element(by.css('option:checked')).getText();
  }

  async modifiedBySelectLastOption(): Promise<void> {
    await this.modifiedBySelect.all(by.tagName('option')).last().click();
  }

  async modifiedBySelectOption(option: string): Promise<void> {
    await this.modifiedBySelect.sendKeys(option);
  }

  getModifiedBySelect(): ElementFinder {
    return this.modifiedBySelect;
  }

  async getModifiedBySelectedOption(): Promise<string> {
    return await this.modifiedBySelect.element(by.css('option:checked')).getText();
  }

  async lastAccessedBySelectLastOption(): Promise<void> {
    await this.lastAccessedBySelect.all(by.tagName('option')).last().click();
  }

  async lastAccessedBySelectOption(option: string): Promise<void> {
    await this.lastAccessedBySelect.sendKeys(option);
  }

  getLastAccessedBySelect(): ElementFinder {
    return this.lastAccessedBySelect;
  }

  async getLastAccessedBySelectedOption(): Promise<string> {
    return await this.lastAccessedBySelect.element(by.css('option:checked')).getText();
  }

  async effectivePeriodSelectLastOption(): Promise<void> {
    await this.effectivePeriodSelect.all(by.tagName('option')).last().click();
  }

  async effectivePeriodSelectOption(option: string): Promise<void> {
    await this.effectivePeriodSelect.sendKeys(option);
  }

  getEffectivePeriodSelect(): ElementFinder {
    return this.effectivePeriodSelect;
  }

  async getEffectivePeriodSelectedOption(): Promise<string> {
    return await this.effectivePeriodSelect.element(by.css('option:checked')).getText();
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

  async assetDisposedSelectLastOption(): Promise<void> {
    await this.assetDisposedSelect.all(by.tagName('option')).last().click();
  }

  async assetDisposedSelectOption(option: string): Promise<void> {
    await this.assetDisposedSelect.sendKeys(option);
  }

  getAssetDisposedSelect(): ElementFinder {
    return this.assetDisposedSelect;
  }

  async getAssetDisposedSelectedOption(): Promise<string> {
    return await this.assetDisposedSelect.element(by.css('option:checked')).getText();
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

export class AssetDisposalDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-assetDisposal-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-assetDisposal'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
