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

export class CrbSubmittingInstitutionCategoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-crb-submitting-institution-category div table .btn-danger'));
  title = element.all(by.css('jhi-crb-submitting-institution-category div h2#page-heading span')).first();
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

export class CrbSubmittingInstitutionCategoryUpdatePage {
  pageTitle = element(by.id('jhi-crb-submitting-institution-category-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  submittingInstitutionCategoryTypeCodeInput = element(by.id('field_submittingInstitutionCategoryTypeCode'));
  submittingInstitutionCategoryTypeInput = element(by.id('field_submittingInstitutionCategoryType'));
  submittingInstitutionCategoryDetailsInput = element(by.id('field_submittingInstitutionCategoryDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSubmittingInstitutionCategoryTypeCodeInput(submittingInstitutionCategoryTypeCode: string): Promise<void> {
    await this.submittingInstitutionCategoryTypeCodeInput.sendKeys(submittingInstitutionCategoryTypeCode);
  }

  async getSubmittingInstitutionCategoryTypeCodeInput(): Promise<string> {
    return await this.submittingInstitutionCategoryTypeCodeInput.getAttribute('value');
  }

  async setSubmittingInstitutionCategoryTypeInput(submittingInstitutionCategoryType: string): Promise<void> {
    await this.submittingInstitutionCategoryTypeInput.sendKeys(submittingInstitutionCategoryType);
  }

  async getSubmittingInstitutionCategoryTypeInput(): Promise<string> {
    return await this.submittingInstitutionCategoryTypeInput.getAttribute('value');
  }

  async setSubmittingInstitutionCategoryDetailsInput(submittingInstitutionCategoryDetails: string): Promise<void> {
    await this.submittingInstitutionCategoryDetailsInput.sendKeys(submittingInstitutionCategoryDetails);
  }

  async getSubmittingInstitutionCategoryDetailsInput(): Promise<string> {
    return await this.submittingInstitutionCategoryDetailsInput.getAttribute('value');
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

export class CrbSubmittingInstitutionCategoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-crbSubmittingInstitutionCategory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-crbSubmittingInstitutionCategory'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
