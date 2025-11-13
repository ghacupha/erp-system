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

export class BusinessDocumentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-business-document div table .btn-danger'));
  title = element.all(by.css('jhi-business-document div h2#page-heading span')).first();
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

export class BusinessDocumentUpdatePage {
  pageTitle = element(by.id('jhi-business-document-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  documentTitleInput = element(by.id('field_documentTitle'));
  descriptionInput = element(by.id('field_description'));
  documentSerialInput = element(by.id('field_documentSerial'));
  lastModifiedInput = element(by.id('field_lastModified'));
  attachmentFilePathInput = element(by.id('field_attachmentFilePath'));
  documentFileInput = element(by.id('file_documentFile'));
  fileTamperedInput = element(by.id('field_fileTampered'));
  documentFileChecksumInput = element(by.id('field_documentFileChecksum'));

  createdBySelect = element(by.id('field_createdBy'));
  lastModifiedBySelect = element(by.id('field_lastModifiedBy'));
  originatingDepartmentSelect = element(by.id('field_originatingDepartment'));
  applicationMappingsSelect = element(by.id('field_applicationMappings'));
  placeholderSelect = element(by.id('field_placeholder'));
  fileChecksumAlgorithmSelect = element(by.id('field_fileChecksumAlgorithm'));
  securityClearanceSelect = element(by.id('field_securityClearance'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDocumentTitleInput(documentTitle: string): Promise<void> {
    await this.documentTitleInput.sendKeys(documentTitle);
  }

  async getDocumentTitleInput(): Promise<string> {
    return await this.documentTitleInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setDocumentSerialInput(documentSerial: string): Promise<void> {
    await this.documentSerialInput.sendKeys(documentSerial);
  }

  async getDocumentSerialInput(): Promise<string> {
    return await this.documentSerialInput.getAttribute('value');
  }

  async setLastModifiedInput(lastModified: string): Promise<void> {
    await this.lastModifiedInput.sendKeys(lastModified);
  }

  async getLastModifiedInput(): Promise<string> {
    return await this.lastModifiedInput.getAttribute('value');
  }

  async setAttachmentFilePathInput(attachmentFilePath: string): Promise<void> {
    await this.attachmentFilePathInput.sendKeys(attachmentFilePath);
  }

  async getAttachmentFilePathInput(): Promise<string> {
    return await this.attachmentFilePathInput.getAttribute('value');
  }

  async setDocumentFileInput(documentFile: string): Promise<void> {
    await this.documentFileInput.sendKeys(documentFile);
  }

  async getDocumentFileInput(): Promise<string> {
    return await this.documentFileInput.getAttribute('value');
  }

  getFileTamperedInput(): ElementFinder {
    return this.fileTamperedInput;
  }

  async setDocumentFileChecksumInput(documentFileChecksum: string): Promise<void> {
    await this.documentFileChecksumInput.sendKeys(documentFileChecksum);
  }

  async getDocumentFileChecksumInput(): Promise<string> {
    return await this.documentFileChecksumInput.getAttribute('value');
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

  async originatingDepartmentSelectLastOption(): Promise<void> {
    await this.originatingDepartmentSelect.all(by.tagName('option')).last().click();
  }

  async originatingDepartmentSelectOption(option: string): Promise<void> {
    await this.originatingDepartmentSelect.sendKeys(option);
  }

  getOriginatingDepartmentSelect(): ElementFinder {
    return this.originatingDepartmentSelect;
  }

  async getOriginatingDepartmentSelectedOption(): Promise<string> {
    return await this.originatingDepartmentSelect.element(by.css('option:checked')).getText();
  }

  async applicationMappingsSelectLastOption(): Promise<void> {
    await this.applicationMappingsSelect.all(by.tagName('option')).last().click();
  }

  async applicationMappingsSelectOption(option: string): Promise<void> {
    await this.applicationMappingsSelect.sendKeys(option);
  }

  getApplicationMappingsSelect(): ElementFinder {
    return this.applicationMappingsSelect;
  }

  async getApplicationMappingsSelectedOption(): Promise<string> {
    return await this.applicationMappingsSelect.element(by.css('option:checked')).getText();
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

  async fileChecksumAlgorithmSelectLastOption(): Promise<void> {
    await this.fileChecksumAlgorithmSelect.all(by.tagName('option')).last().click();
  }

  async fileChecksumAlgorithmSelectOption(option: string): Promise<void> {
    await this.fileChecksumAlgorithmSelect.sendKeys(option);
  }

  getFileChecksumAlgorithmSelect(): ElementFinder {
    return this.fileChecksumAlgorithmSelect;
  }

  async getFileChecksumAlgorithmSelectedOption(): Promise<string> {
    return await this.fileChecksumAlgorithmSelect.element(by.css('option:checked')).getText();
  }

  async securityClearanceSelectLastOption(): Promise<void> {
    await this.securityClearanceSelect.all(by.tagName('option')).last().click();
  }

  async securityClearanceSelectOption(option: string): Promise<void> {
    await this.securityClearanceSelect.sendKeys(option);
  }

  getSecurityClearanceSelect(): ElementFinder {
    return this.securityClearanceSelect;
  }

  async getSecurityClearanceSelectedOption(): Promise<string> {
    return await this.securityClearanceSelect.element(by.css('option:checked')).getText();
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

export class BusinessDocumentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-businessDocument-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-businessDocument'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
