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

export class FileUploadComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-file-upload div table .btn-danger'));
  title = element.all(by.css('jhi-file-upload div h2#page-heading span')).first();
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

export class FileUploadUpdatePage {
  pageTitle = element(by.id('jhi-file-upload-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  descriptionInput = element(by.id('field_description'));
  fileNameInput = element(by.id('field_fileName'));
  periodFromInput = element(by.id('field_periodFrom'));
  periodToInput = element(by.id('field_periodTo'));
  fileTypeIdInput = element(by.id('field_fileTypeId'));
  dataFileInput = element(by.id('file_dataFile'));
  uploadSuccessfulInput = element(by.id('field_uploadSuccessful'));
  uploadProcessedInput = element(by.id('field_uploadProcessed'));
  uploadTokenInput = element(by.id('field_uploadToken'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setFileNameInput(fileName: string): Promise<void> {
    await this.fileNameInput.sendKeys(fileName);
  }

  async getFileNameInput(): Promise<string> {
    return await this.fileNameInput.getAttribute('value');
  }

  async setPeriodFromInput(periodFrom: string): Promise<void> {
    await this.periodFromInput.sendKeys(periodFrom);
  }

  async getPeriodFromInput(): Promise<string> {
    return await this.periodFromInput.getAttribute('value');
  }

  async setPeriodToInput(periodTo: string): Promise<void> {
    await this.periodToInput.sendKeys(periodTo);
  }

  async getPeriodToInput(): Promise<string> {
    return await this.periodToInput.getAttribute('value');
  }

  async setFileTypeIdInput(fileTypeId: string): Promise<void> {
    await this.fileTypeIdInput.sendKeys(fileTypeId);
  }

  async getFileTypeIdInput(): Promise<string> {
    return await this.fileTypeIdInput.getAttribute('value');
  }

  async setDataFileInput(dataFile: string): Promise<void> {
    await this.dataFileInput.sendKeys(dataFile);
  }

  async getDataFileInput(): Promise<string> {
    return await this.dataFileInput.getAttribute('value');
  }

  getUploadSuccessfulInput(): ElementFinder {
    return this.uploadSuccessfulInput;
  }

  getUploadProcessedInput(): ElementFinder {
    return this.uploadProcessedInput;
  }

  async setUploadTokenInput(uploadToken: string): Promise<void> {
    await this.uploadTokenInput.sendKeys(uploadToken);
  }

  async getUploadTokenInput(): Promise<string> {
    return await this.uploadTokenInput.getAttribute('value');
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

export class FileUploadDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fileUpload-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fileUpload'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
