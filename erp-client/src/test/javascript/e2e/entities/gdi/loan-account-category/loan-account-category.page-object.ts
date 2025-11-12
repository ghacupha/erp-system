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

export class LoanAccountCategoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-loan-account-category div table .btn-danger'));
  title = element.all(by.css('jhi-loan-account-category div h2#page-heading span')).first();
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

export class LoanAccountCategoryUpdatePage {
  pageTitle = element(by.id('jhi-loan-account-category-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  loanAccountMutationCodeInput = element(by.id('field_loanAccountMutationCode'));
  loanAccountMutationTypeSelect = element(by.id('field_loanAccountMutationType'));
  loanAccountMutationDetailsInput = element(by.id('field_loanAccountMutationDetails'));
  loanAccountMutationDescriptionInput = element(by.id('field_loanAccountMutationDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setLoanAccountMutationCodeInput(loanAccountMutationCode: string): Promise<void> {
    await this.loanAccountMutationCodeInput.sendKeys(loanAccountMutationCode);
  }

  async getLoanAccountMutationCodeInput(): Promise<string> {
    return await this.loanAccountMutationCodeInput.getAttribute('value');
  }

  async setLoanAccountMutationTypeSelect(loanAccountMutationType: string): Promise<void> {
    await this.loanAccountMutationTypeSelect.sendKeys(loanAccountMutationType);
  }

  async getLoanAccountMutationTypeSelect(): Promise<string> {
    return await this.loanAccountMutationTypeSelect.element(by.css('option:checked')).getText();
  }

  async loanAccountMutationTypeSelectLastOption(): Promise<void> {
    await this.loanAccountMutationTypeSelect.all(by.tagName('option')).last().click();
  }

  async setLoanAccountMutationDetailsInput(loanAccountMutationDetails: string): Promise<void> {
    await this.loanAccountMutationDetailsInput.sendKeys(loanAccountMutationDetails);
  }

  async getLoanAccountMutationDetailsInput(): Promise<string> {
    return await this.loanAccountMutationDetailsInput.getAttribute('value');
  }

  async setLoanAccountMutationDescriptionInput(loanAccountMutationDescription: string): Promise<void> {
    await this.loanAccountMutationDescriptionInput.sendKeys(loanAccountMutationDescription);
  }

  async getLoanAccountMutationDescriptionInput(): Promise<string> {
    return await this.loanAccountMutationDescriptionInput.getAttribute('value');
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

export class LoanAccountCategoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-loanAccountCategory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-loanAccountCategory'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
