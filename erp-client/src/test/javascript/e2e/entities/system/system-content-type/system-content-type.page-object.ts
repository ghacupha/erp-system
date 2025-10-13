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

export class SystemContentTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-system-content-type div table .btn-danger'));
  title = element.all(by.css('jhi-system-content-type div h2#page-heading span')).first();
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

export class SystemContentTypeUpdatePage {
  pageTitle = element(by.id('jhi-system-content-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  contentTypeNameInput = element(by.id('field_contentTypeName'));
  contentTypeHeaderInput = element(by.id('field_contentTypeHeader'));
  commentsInput = element(by.id('field_comments'));
  availabilitySelect = element(by.id('field_availability'));

  placeholdersSelect = element(by.id('field_placeholders'));
  sysMapsSelect = element(by.id('field_sysMaps'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setContentTypeNameInput(contentTypeName: string): Promise<void> {
    await this.contentTypeNameInput.sendKeys(contentTypeName);
  }

  async getContentTypeNameInput(): Promise<string> {
    return await this.contentTypeNameInput.getAttribute('value');
  }

  async setContentTypeHeaderInput(contentTypeHeader: string): Promise<void> {
    await this.contentTypeHeaderInput.sendKeys(contentTypeHeader);
  }

  async getContentTypeHeaderInput(): Promise<string> {
    return await this.contentTypeHeaderInput.getAttribute('value');
  }

  async setCommentsInput(comments: string): Promise<void> {
    await this.commentsInput.sendKeys(comments);
  }

  async getCommentsInput(): Promise<string> {
    return await this.commentsInput.getAttribute('value');
  }

  async setAvailabilitySelect(availability: string): Promise<void> {
    await this.availabilitySelect.sendKeys(availability);
  }

  async getAvailabilitySelect(): Promise<string> {
    return await this.availabilitySelect.element(by.css('option:checked')).getText();
  }

  async availabilitySelectLastOption(): Promise<void> {
    await this.availabilitySelect.all(by.tagName('option')).last().click();
  }

  async placeholdersSelectLastOption(): Promise<void> {
    await this.placeholdersSelect.all(by.tagName('option')).last().click();
  }

  async placeholdersSelectOption(option: string): Promise<void> {
    await this.placeholdersSelect.sendKeys(option);
  }

  getPlaceholdersSelect(): ElementFinder {
    return this.placeholdersSelect;
  }

  async getPlaceholdersSelectedOption(): Promise<string> {
    return await this.placeholdersSelect.element(by.css('option:checked')).getText();
  }

  async sysMapsSelectLastOption(): Promise<void> {
    await this.sysMapsSelect.all(by.tagName('option')).last().click();
  }

  async sysMapsSelectOption(option: string): Promise<void> {
    await this.sysMapsSelect.sendKeys(option);
  }

  getSysMapsSelect(): ElementFinder {
    return this.sysMapsSelect;
  }

  async getSysMapsSelectedOption(): Promise<string> {
    return await this.sysMapsSelect.element(by.css('option:checked')).getText();
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

export class SystemContentTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-systemContentType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-systemContentType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
