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

export class AssetGeneralAdjustmentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-asset-general-adjustment div table .btn-danger'));
  title = element.all(by.css('jhi-asset-general-adjustment div h2#page-heading span')).first();
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

export class AssetGeneralAdjustmentUpdatePage {
  pageTitle = element(by.id('jhi-asset-general-adjustment-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  descriptionInput = element(by.id('field_description'));
  devaluationAmountInput = element(by.id('field_devaluationAmount'));
  adjustmentDateInput = element(by.id('field_adjustmentDate'));
  timeOfCreationInput = element(by.id('field_timeOfCreation'));
  adjustmentReferenceIdInput = element(by.id('field_adjustmentReferenceId'));

  effectivePeriodSelect = element(by.id('field_effectivePeriod'));
  assetRegistrationSelect = element(by.id('field_assetRegistration'));
  createdBySelect = element(by.id('field_createdBy'));
  lastModifiedBySelect = element(by.id('field_lastModifiedBy'));
  lastAccessedBySelect = element(by.id('field_lastAccessedBy'));
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

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setDevaluationAmountInput(devaluationAmount: string): Promise<void> {
    await this.devaluationAmountInput.sendKeys(devaluationAmount);
  }

  async getDevaluationAmountInput(): Promise<string> {
    return await this.devaluationAmountInput.getAttribute('value');
  }

  async setAdjustmentDateInput(adjustmentDate: string): Promise<void> {
    await this.adjustmentDateInput.sendKeys(adjustmentDate);
  }

  async getAdjustmentDateInput(): Promise<string> {
    return await this.adjustmentDateInput.getAttribute('value');
  }

  async setTimeOfCreationInput(timeOfCreation: string): Promise<void> {
    await this.timeOfCreationInput.sendKeys(timeOfCreation);
  }

  async getTimeOfCreationInput(): Promise<string> {
    return await this.timeOfCreationInput.getAttribute('value');
  }

  async setAdjustmentReferenceIdInput(adjustmentReferenceId: string): Promise<void> {
    await this.adjustmentReferenceIdInput.sendKeys(adjustmentReferenceId);
  }

  async getAdjustmentReferenceIdInput(): Promise<string> {
    return await this.adjustmentReferenceIdInput.getAttribute('value');
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

  async lastModifiedBySelectLastOption(): Promise<void> {
    await this.lastModifiedBySelect.all(by.tagName('option')).last().click();
  }

  async lastModifiedBySelectOption(option: string): Promise<void> {
    await this.lastModifiedBySelect.sendKeys(option);
  }

  getLastModifiedBySelect(): ElementFinder {
    return this.lastModifiedBySelect;
  }

  async getLastModifiedBySelectedOption(): Promise<string> {
    return await this.lastModifiedBySelect.element(by.css('option:checked')).getText();
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

export class AssetGeneralAdjustmentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-assetGeneralAdjustment-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-assetGeneralAdjustment'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
