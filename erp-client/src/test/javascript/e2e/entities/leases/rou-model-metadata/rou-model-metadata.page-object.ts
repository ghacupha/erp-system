///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

export class RouModelMetadataComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-rou-model-metadata div table .btn-danger'));
  title = element.all(by.css('jhi-rou-model-metadata div h2#page-heading span')).first();
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

export class RouModelMetadataUpdatePage {
  pageTitle = element(by.id('jhi-rou-model-metadata-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  modelTitleInput = element(by.id('field_modelTitle'));
  modelVersionInput = element(by.id('field_modelVersion'));
  descriptionInput = element(by.id('field_description'));
  leaseTermPeriodsInput = element(by.id('field_leaseTermPeriods'));
  leaseAmountInput = element(by.id('field_leaseAmount'));
  rouModelReferenceInput = element(by.id('field_rouModelReference'));
  commencementDateInput = element(by.id('field_commencementDate'));
  expirationDateInput = element(by.id('field_expirationDate'));
  hasBeenFullyAmortisedInput = element(by.id('field_hasBeenFullyAmortised'));
  hasBeenDecommissionedInput = element(by.id('field_hasBeenDecommissioned'));

  ifrs16LeaseContractSelect = element(by.id('field_ifrs16LeaseContract'));
  assetAccountSelect = element(by.id('field_assetAccount'));
  depreciationAccountSelect = element(by.id('field_depreciationAccount'));
  accruedDepreciationAccountSelect = element(by.id('field_accruedDepreciationAccount'));
  assetCategorySelect = element(by.id('field_assetCategory'));
  documentAttachmentsSelect = element(by.id('field_documentAttachments'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setModelTitleInput(modelTitle: string): Promise<void> {
    await this.modelTitleInput.sendKeys(modelTitle);
  }

  async getModelTitleInput(): Promise<string> {
    return await this.modelTitleInput.getAttribute('value');
  }

  async setModelVersionInput(modelVersion: string): Promise<void> {
    await this.modelVersionInput.sendKeys(modelVersion);
  }

  async getModelVersionInput(): Promise<string> {
    return await this.modelVersionInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setLeaseTermPeriodsInput(leaseTermPeriods: string): Promise<void> {
    await this.leaseTermPeriodsInput.sendKeys(leaseTermPeriods);
  }

  async getLeaseTermPeriodsInput(): Promise<string> {
    return await this.leaseTermPeriodsInput.getAttribute('value');
  }

  async setLeaseAmountInput(leaseAmount: string): Promise<void> {
    await this.leaseAmountInput.sendKeys(leaseAmount);
  }

  async getLeaseAmountInput(): Promise<string> {
    return await this.leaseAmountInput.getAttribute('value');
  }

  async setRouModelReferenceInput(rouModelReference: string): Promise<void> {
    await this.rouModelReferenceInput.sendKeys(rouModelReference);
  }

  async getRouModelReferenceInput(): Promise<string> {
    return await this.rouModelReferenceInput.getAttribute('value');
  }

  async setCommencementDateInput(commencementDate: string): Promise<void> {
    await this.commencementDateInput.sendKeys(commencementDate);
  }

  async getCommencementDateInput(): Promise<string> {
    return await this.commencementDateInput.getAttribute('value');
  }

  async setExpirationDateInput(expirationDate: string): Promise<void> {
    await this.expirationDateInput.sendKeys(expirationDate);
  }

  async getExpirationDateInput(): Promise<string> {
    return await this.expirationDateInput.getAttribute('value');
  }

  getHasBeenFullyAmortisedInput(): ElementFinder {
    return this.hasBeenFullyAmortisedInput;
  }

  getHasBeenDecommissionedInput(): ElementFinder {
    return this.hasBeenDecommissionedInput;
  }

  async ifrs16LeaseContractSelectLastOption(): Promise<void> {
    await this.ifrs16LeaseContractSelect.all(by.tagName('option')).last().click();
  }

  async ifrs16LeaseContractSelectOption(option: string): Promise<void> {
    await this.ifrs16LeaseContractSelect.sendKeys(option);
  }

  getIfrs16LeaseContractSelect(): ElementFinder {
    return this.ifrs16LeaseContractSelect;
  }

  async getIfrs16LeaseContractSelectedOption(): Promise<string> {
    return await this.ifrs16LeaseContractSelect.element(by.css('option:checked')).getText();
  }

  async assetAccountSelectLastOption(): Promise<void> {
    await this.assetAccountSelect.all(by.tagName('option')).last().click();
  }

  async assetAccountSelectOption(option: string): Promise<void> {
    await this.assetAccountSelect.sendKeys(option);
  }

  getAssetAccountSelect(): ElementFinder {
    return this.assetAccountSelect;
  }

  async getAssetAccountSelectedOption(): Promise<string> {
    return await this.assetAccountSelect.element(by.css('option:checked')).getText();
  }

  async depreciationAccountSelectLastOption(): Promise<void> {
    await this.depreciationAccountSelect.all(by.tagName('option')).last().click();
  }

  async depreciationAccountSelectOption(option: string): Promise<void> {
    await this.depreciationAccountSelect.sendKeys(option);
  }

  getDepreciationAccountSelect(): ElementFinder {
    return this.depreciationAccountSelect;
  }

  async getDepreciationAccountSelectedOption(): Promise<string> {
    return await this.depreciationAccountSelect.element(by.css('option:checked')).getText();
  }

  async accruedDepreciationAccountSelectLastOption(): Promise<void> {
    await this.accruedDepreciationAccountSelect.all(by.tagName('option')).last().click();
  }

  async accruedDepreciationAccountSelectOption(option: string): Promise<void> {
    await this.accruedDepreciationAccountSelect.sendKeys(option);
  }

  getAccruedDepreciationAccountSelect(): ElementFinder {
    return this.accruedDepreciationAccountSelect;
  }

  async getAccruedDepreciationAccountSelectedOption(): Promise<string> {
    return await this.accruedDepreciationAccountSelect.element(by.css('option:checked')).getText();
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

  async documentAttachmentsSelectLastOption(): Promise<void> {
    await this.documentAttachmentsSelect.all(by.tagName('option')).last().click();
  }

  async documentAttachmentsSelectOption(option: string): Promise<void> {
    await this.documentAttachmentsSelect.sendKeys(option);
  }

  getDocumentAttachmentsSelect(): ElementFinder {
    return this.documentAttachmentsSelect;
  }

  async getDocumentAttachmentsSelectedOption(): Promise<string> {
    return await this.documentAttachmentsSelect.element(by.css('option:checked')).getText();
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

export class RouModelMetadataDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-rouModelMetadata-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-rouModelMetadata'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
