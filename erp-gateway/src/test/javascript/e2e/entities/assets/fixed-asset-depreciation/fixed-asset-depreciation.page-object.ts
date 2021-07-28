///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
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

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
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
