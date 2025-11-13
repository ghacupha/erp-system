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

export class DepartmentTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-department-type div table .btn-danger'));
  title = element.all(by.css('jhi-department-type div h2#page-heading span')).first();
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

export class DepartmentTypeUpdatePage {
  pageTitle = element(by.id('jhi-department-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  departmentTypeCodeInput = element(by.id('field_departmentTypeCode'));
  departmentTypeInput = element(by.id('field_departmentType'));
  departmentTypeDetailsInput = element(by.id('field_departmentTypeDetails'));

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

  async setDepartmentTypeCodeInput(departmentTypeCode: string): Promise<void> {
    await this.departmentTypeCodeInput.sendKeys(departmentTypeCode);
  }

  async getDepartmentTypeCodeInput(): Promise<string> {
    return await this.departmentTypeCodeInput.getAttribute('value');
  }

  async setDepartmentTypeInput(departmentType: string): Promise<void> {
    await this.departmentTypeInput.sendKeys(departmentType);
  }

  async getDepartmentTypeInput(): Promise<string> {
    return await this.departmentTypeInput.getAttribute('value');
  }

  async setDepartmentTypeDetailsInput(departmentTypeDetails: string): Promise<void> {
    await this.departmentTypeDetailsInput.sendKeys(departmentTypeDetails);
  }

  async getDepartmentTypeDetailsInput(): Promise<string> {
    return await this.departmentTypeDetailsInput.getAttribute('value');
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

export class DepartmentTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-departmentType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-departmentType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
