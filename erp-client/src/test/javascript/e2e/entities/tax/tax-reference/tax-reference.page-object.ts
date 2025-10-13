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

export class TaxReferenceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-tax-reference div table .btn-danger'));
  title = element.all(by.css('jhi-tax-reference div h2#page-heading span')).first();
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

export class TaxReferenceUpdatePage {
  pageTitle = element(by.id('jhi-tax-reference-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  taxNameInput = element(by.id('field_taxName'));
  taxDescriptionInput = element(by.id('field_taxDescription'));
  taxPercentageInput = element(by.id('field_taxPercentage'));
  taxReferenceTypeSelect = element(by.id('field_taxReferenceType'));
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

  async setTaxNameInput(taxName: string): Promise<void> {
    await this.taxNameInput.sendKeys(taxName);
  }

  async getTaxNameInput(): Promise<string> {
    return await this.taxNameInput.getAttribute('value');
  }

  async setTaxDescriptionInput(taxDescription: string): Promise<void> {
    await this.taxDescriptionInput.sendKeys(taxDescription);
  }

  async getTaxDescriptionInput(): Promise<string> {
    return await this.taxDescriptionInput.getAttribute('value');
  }

  async setTaxPercentageInput(taxPercentage: string): Promise<void> {
    await this.taxPercentageInput.sendKeys(taxPercentage);
  }

  async getTaxPercentageInput(): Promise<string> {
    return await this.taxPercentageInput.getAttribute('value');
  }

  async setTaxReferenceTypeSelect(taxReferenceType: string): Promise<void> {
    await this.taxReferenceTypeSelect.sendKeys(taxReferenceType);
  }

  async getTaxReferenceTypeSelect(): Promise<string> {
    return await this.taxReferenceTypeSelect.element(by.css('option:checked')).getText();
  }

  async taxReferenceTypeSelectLastOption(): Promise<void> {
    await this.taxReferenceTypeSelect.all(by.tagName('option')).last().click();
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

export class TaxReferenceDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-taxReference-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-taxReference'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
