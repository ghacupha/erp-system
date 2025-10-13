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

export class AssetWarrantyComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-asset-warranty div table .btn-danger'));
  title = element.all(by.css('jhi-asset-warranty div h2#page-heading span')).first();
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

export class AssetWarrantyUpdatePage {
  pageTitle = element(by.id('jhi-asset-warranty-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  assetTagInput = element(by.id('field_assetTag'));
  descriptionInput = element(by.id('field_description'));
  modelNumberInput = element(by.id('field_modelNumber'));
  serialNumberInput = element(by.id('field_serialNumber'));
  expiryDateInput = element(by.id('field_expiryDate'));

  placeholderSelect = element(by.id('field_placeholder'));
  universallyUniqueMappingSelect = element(by.id('field_universallyUniqueMapping'));
  dealerSelect = element(by.id('field_dealer'));
  warrantyAttachmentSelect = element(by.id('field_warrantyAttachment'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAssetTagInput(assetTag: string): Promise<void> {
    await this.assetTagInput.sendKeys(assetTag);
  }

  async getAssetTagInput(): Promise<string> {
    return await this.assetTagInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setModelNumberInput(modelNumber: string): Promise<void> {
    await this.modelNumberInput.sendKeys(modelNumber);
  }

  async getModelNumberInput(): Promise<string> {
    return await this.modelNumberInput.getAttribute('value');
  }

  async setSerialNumberInput(serialNumber: string): Promise<void> {
    await this.serialNumberInput.sendKeys(serialNumber);
  }

  async getSerialNumberInput(): Promise<string> {
    return await this.serialNumberInput.getAttribute('value');
  }

  async setExpiryDateInput(expiryDate: string): Promise<void> {
    await this.expiryDateInput.sendKeys(expiryDate);
  }

  async getExpiryDateInput(): Promise<string> {
    return await this.expiryDateInput.getAttribute('value');
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

  async dealerSelectLastOption(): Promise<void> {
    await this.dealerSelect.all(by.tagName('option')).last().click();
  }

  async dealerSelectOption(option: string): Promise<void> {
    await this.dealerSelect.sendKeys(option);
  }

  getDealerSelect(): ElementFinder {
    return this.dealerSelect;
  }

  async getDealerSelectedOption(): Promise<string> {
    return await this.dealerSelect.element(by.css('option:checked')).getText();
  }

  async warrantyAttachmentSelectLastOption(): Promise<void> {
    await this.warrantyAttachmentSelect.all(by.tagName('option')).last().click();
  }

  async warrantyAttachmentSelectOption(option: string): Promise<void> {
    await this.warrantyAttachmentSelect.sendKeys(option);
  }

  getWarrantyAttachmentSelect(): ElementFinder {
    return this.warrantyAttachmentSelect;
  }

  async getWarrantyAttachmentSelectedOption(): Promise<string> {
    return await this.warrantyAttachmentSelect.element(by.css('option:checked')).getText();
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

export class AssetWarrantyDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-assetWarranty-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-assetWarranty'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
