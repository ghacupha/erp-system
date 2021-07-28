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

export class FileTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-file-type div table .btn-danger'));
  title = element.all(by.css('jhi-file-type div h2#page-heading span')).first();
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

export class FileTypeUpdatePage {
  pageTitle = element(by.id('jhi-file-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  fileTypeNameInput = element(by.id('field_fileTypeName'));
  fileMediumTypeSelect = element(by.id('field_fileMediumType'));
  descriptionInput = element(by.id('field_description'));
  fileTemplateInput = element(by.id('file_fileTemplate'));
  fileTypeSelect = element(by.id('field_fileType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setFileTypeNameInput(fileTypeName: string): Promise<void> {
    await this.fileTypeNameInput.sendKeys(fileTypeName);
  }

  async getFileTypeNameInput(): Promise<string> {
    return await this.fileTypeNameInput.getAttribute('value');
  }

  async setFileMediumTypeSelect(fileMediumType: string): Promise<void> {
    await this.fileMediumTypeSelect.sendKeys(fileMediumType);
  }

  async getFileMediumTypeSelect(): Promise<string> {
    return await this.fileMediumTypeSelect.element(by.css('option:checked')).getText();
  }

  async fileMediumTypeSelectLastOption(): Promise<void> {
    await this.fileMediumTypeSelect.all(by.tagName('option')).last().click();
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setFileTemplateInput(fileTemplate: string): Promise<void> {
    await this.fileTemplateInput.sendKeys(fileTemplate);
  }

  async getFileTemplateInput(): Promise<string> {
    return await this.fileTemplateInput.getAttribute('value');
  }

  async setFileTypeSelect(fileType: string): Promise<void> {
    await this.fileTypeSelect.sendKeys(fileType);
  }

  async getFileTypeSelect(): Promise<string> {
    return await this.fileTypeSelect.element(by.css('option:checked')).getText();
  }

  async fileTypeSelectLastOption(): Promise<void> {
    await this.fileTypeSelect.all(by.tagName('option')).last().click();
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

export class FileTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fileType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fileType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
