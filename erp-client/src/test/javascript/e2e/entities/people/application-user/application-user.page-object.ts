///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

export class ApplicationUserComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-application-user div table .btn-danger'));
  title = element.all(by.css('jhi-application-user div h2#page-heading span')).first();
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

export class ApplicationUserUpdatePage {
  pageTitle = element(by.id('jhi-application-user-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  designationInput = element(by.id('field_designation'));
  applicationIdentityInput = element(by.id('field_applicationIdentity'));

  organizationSelect = element(by.id('field_organization'));
  departmentSelect = element(by.id('field_department'));
  securityClearanceSelect = element(by.id('field_securityClearance'));
  systemIdentitySelect = element(by.id('field_systemIdentity'));
  userPropertiesSelect = element(by.id('field_userProperties'));
  dealerIdentitySelect = element(by.id('field_dealerIdentity'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDesignationInput(designation: string): Promise<void> {
    await this.designationInput.sendKeys(designation);
  }

  async getDesignationInput(): Promise<string> {
    return await this.designationInput.getAttribute('value');
  }

  async setApplicationIdentityInput(applicationIdentity: string): Promise<void> {
    await this.applicationIdentityInput.sendKeys(applicationIdentity);
  }

  async getApplicationIdentityInput(): Promise<string> {
    return await this.applicationIdentityInput.getAttribute('value');
  }

  async organizationSelectLastOption(): Promise<void> {
    await this.organizationSelect.all(by.tagName('option')).last().click();
  }

  async organizationSelectOption(option: string): Promise<void> {
    await this.organizationSelect.sendKeys(option);
  }

  getOrganizationSelect(): ElementFinder {
    return this.organizationSelect;
  }

  async getOrganizationSelectedOption(): Promise<string> {
    return await this.organizationSelect.element(by.css('option:checked')).getText();
  }

  async departmentSelectLastOption(): Promise<void> {
    await this.departmentSelect.all(by.tagName('option')).last().click();
  }

  async departmentSelectOption(option: string): Promise<void> {
    await this.departmentSelect.sendKeys(option);
  }

  getDepartmentSelect(): ElementFinder {
    return this.departmentSelect;
  }

  async getDepartmentSelectedOption(): Promise<string> {
    return await this.departmentSelect.element(by.css('option:checked')).getText();
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

  async systemIdentitySelectLastOption(): Promise<void> {
    await this.systemIdentitySelect.all(by.tagName('option')).last().click();
  }

  async systemIdentitySelectOption(option: string): Promise<void> {
    await this.systemIdentitySelect.sendKeys(option);
  }

  getSystemIdentitySelect(): ElementFinder {
    return this.systemIdentitySelect;
  }

  async getSystemIdentitySelectedOption(): Promise<string> {
    return await this.systemIdentitySelect.element(by.css('option:checked')).getText();
  }

  async userPropertiesSelectLastOption(): Promise<void> {
    await this.userPropertiesSelect.all(by.tagName('option')).last().click();
  }

  async userPropertiesSelectOption(option: string): Promise<void> {
    await this.userPropertiesSelect.sendKeys(option);
  }

  getUserPropertiesSelect(): ElementFinder {
    return this.userPropertiesSelect;
  }

  async getUserPropertiesSelectedOption(): Promise<string> {
    return await this.userPropertiesSelect.element(by.css('option:checked')).getText();
  }

  async dealerIdentitySelectLastOption(): Promise<void> {
    await this.dealerIdentitySelect.all(by.tagName('option')).last().click();
  }

  async dealerIdentitySelectOption(option: string): Promise<void> {
    await this.dealerIdentitySelect.sendKeys(option);
  }

  getDealerIdentitySelect(): ElementFinder {
    return this.dealerIdentitySelect;
  }

  async getDealerIdentitySelectedOption(): Promise<string> {
    return await this.dealerIdentitySelect.element(by.css('option:checked')).getText();
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

export class ApplicationUserDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-applicationUser-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-applicationUser'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
