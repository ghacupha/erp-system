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

export class InstitutionCodeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-institution-code div table .btn-danger'));
  title = element.all(by.css('jhi-institution-code div h2#page-heading span')).first();
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

export class InstitutionCodeUpdatePage {
  pageTitle = element(by.id('jhi-institution-code-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  institutionCodeInput = element(by.id('field_institutionCode'));
  institutionNameInput = element(by.id('field_institutionName'));
  shortNameInput = element(by.id('field_shortName'));
  categoryInput = element(by.id('field_category'));
  institutionCategoryInput = element(by.id('field_institutionCategory'));
  institutionOwnershipInput = element(by.id('field_institutionOwnership'));
  dateLicensedInput = element(by.id('field_dateLicensed'));
  institutionStatusInput = element(by.id('field_institutionStatus'));

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

  async setInstitutionCodeInput(institutionCode: string): Promise<void> {
    await this.institutionCodeInput.sendKeys(institutionCode);
  }

  async getInstitutionCodeInput(): Promise<string> {
    return await this.institutionCodeInput.getAttribute('value');
  }

  async setInstitutionNameInput(institutionName: string): Promise<void> {
    await this.institutionNameInput.sendKeys(institutionName);
  }

  async getInstitutionNameInput(): Promise<string> {
    return await this.institutionNameInput.getAttribute('value');
  }

  async setShortNameInput(shortName: string): Promise<void> {
    await this.shortNameInput.sendKeys(shortName);
  }

  async getShortNameInput(): Promise<string> {
    return await this.shortNameInput.getAttribute('value');
  }

  async setCategoryInput(category: string): Promise<void> {
    await this.categoryInput.sendKeys(category);
  }

  async getCategoryInput(): Promise<string> {
    return await this.categoryInput.getAttribute('value');
  }

  async setInstitutionCategoryInput(institutionCategory: string): Promise<void> {
    await this.institutionCategoryInput.sendKeys(institutionCategory);
  }

  async getInstitutionCategoryInput(): Promise<string> {
    return await this.institutionCategoryInput.getAttribute('value');
  }

  async setInstitutionOwnershipInput(institutionOwnership: string): Promise<void> {
    await this.institutionOwnershipInput.sendKeys(institutionOwnership);
  }

  async getInstitutionOwnershipInput(): Promise<string> {
    return await this.institutionOwnershipInput.getAttribute('value');
  }

  async setDateLicensedInput(dateLicensed: string): Promise<void> {
    await this.dateLicensedInput.sendKeys(dateLicensed);
  }

  async getDateLicensedInput(): Promise<string> {
    return await this.dateLicensedInput.getAttribute('value');
  }

  async setInstitutionStatusInput(institutionStatus: string): Promise<void> {
    await this.institutionStatusInput.sendKeys(institutionStatus);
  }

  async getInstitutionStatusInput(): Promise<string> {
    return await this.institutionStatusInput.getAttribute('value');
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

export class InstitutionCodeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-institutionCode-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-institutionCode'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
