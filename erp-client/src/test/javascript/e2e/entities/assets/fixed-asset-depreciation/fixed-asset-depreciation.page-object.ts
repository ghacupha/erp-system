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

export class FixedAssetDepreciationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fixed-asset-depreciation div table .btn-danger'));
  title = element.all(by.css('jhi-fixed-asset-depreciation div h2#page-heading span')).first();
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

export class FixedAssetDepreciationUpdatePage {
  pageTitle = element(by.id('jhi-fixed-asset-depreciation-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  assetNumberInput = element(by.id('field_assetNumber'));
  serviceOutletCodeInput = element(by.id('field_serviceOutletCode'));
  assetTagInput = element(by.id('field_assetTag'));
  assetDescriptionInput = element(by.id('field_assetDescription'));
  depreciationDateInput = element(by.id('field_depreciationDate'));
  assetCategoryInput = element(by.id('field_assetCategory'));
  depreciationAmountInput = element(by.id('field_depreciationAmount'));
  depreciationRegimeSelect = element(by.id('field_depreciationRegime'));
  fileUploadTokenInput = element(by.id('field_fileUploadToken'));
  compilationTokenInput = element(by.id('field_compilationToken'));

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

  async setServiceOutletCodeInput(serviceOutletCode: string): Promise<void> {
    await this.serviceOutletCodeInput.sendKeys(serviceOutletCode);
  }

  async getServiceOutletCodeInput(): Promise<string> {
    return await this.serviceOutletCodeInput.getAttribute('value');
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

  async setDepreciationDateInput(depreciationDate: string): Promise<void> {
    await this.depreciationDateInput.sendKeys(depreciationDate);
  }

  async getDepreciationDateInput(): Promise<string> {
    return await this.depreciationDateInput.getAttribute('value');
  }

  async setAssetCategoryInput(assetCategory: string): Promise<void> {
    await this.assetCategoryInput.sendKeys(assetCategory);
  }

  async getAssetCategoryInput(): Promise<string> {
    return await this.assetCategoryInput.getAttribute('value');
  }

  async setDepreciationAmountInput(depreciationAmount: string): Promise<void> {
    await this.depreciationAmountInput.sendKeys(depreciationAmount);
  }

  async getDepreciationAmountInput(): Promise<string> {
    return await this.depreciationAmountInput.getAttribute('value');
  }

  async setDepreciationRegimeSelect(depreciationRegime: string): Promise<void> {
    await this.depreciationRegimeSelect.sendKeys(depreciationRegime);
  }

  async getDepreciationRegimeSelect(): Promise<string> {
    return await this.depreciationRegimeSelect.element(by.css('option:checked')).getText();
  }

  async depreciationRegimeSelectLastOption(): Promise<void> {
    await this.depreciationRegimeSelect.all(by.tagName('option')).last().click();
  }

  async setFileUploadTokenInput(fileUploadToken: string): Promise<void> {
    await this.fileUploadTokenInput.sendKeys(fileUploadToken);
  }

  async getFileUploadTokenInput(): Promise<string> {
    return await this.fileUploadTokenInput.getAttribute('value');
  }

  async setCompilationTokenInput(compilationToken: string): Promise<void> {
    await this.compilationTokenInput.sendKeys(compilationToken);
  }

  async getCompilationTokenInput(): Promise<string> {
    return await this.compilationTokenInput.getAttribute('value');
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

export class FixedAssetDepreciationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fixedAssetDepreciation-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fixedAssetDepreciation'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
