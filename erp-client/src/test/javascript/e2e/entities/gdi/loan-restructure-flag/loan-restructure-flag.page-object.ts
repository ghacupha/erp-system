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

export class LoanRestructureFlagComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-loan-restructure-flag div table .btn-danger'));
  title = element.all(by.css('jhi-loan-restructure-flag div h2#page-heading span')).first();
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

export class LoanRestructureFlagUpdatePage {
  pageTitle = element(by.id('jhi-loan-restructure-flag-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  loanRestructureFlagCodeSelect = element(by.id('field_loanRestructureFlagCode'));
  loanRestructureFlagTypeInput = element(by.id('field_loanRestructureFlagType'));
  loanRestructureFlagDetailsInput = element(by.id('field_loanRestructureFlagDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setLoanRestructureFlagCodeSelect(loanRestructureFlagCode: string): Promise<void> {
    await this.loanRestructureFlagCodeSelect.sendKeys(loanRestructureFlagCode);
  }

  async getLoanRestructureFlagCodeSelect(): Promise<string> {
    return await this.loanRestructureFlagCodeSelect.element(by.css('option:checked')).getText();
  }

  async loanRestructureFlagCodeSelectLastOption(): Promise<void> {
    await this.loanRestructureFlagCodeSelect.all(by.tagName('option')).last().click();
  }

  async setLoanRestructureFlagTypeInput(loanRestructureFlagType: string): Promise<void> {
    await this.loanRestructureFlagTypeInput.sendKeys(loanRestructureFlagType);
  }

  async getLoanRestructureFlagTypeInput(): Promise<string> {
    return await this.loanRestructureFlagTypeInput.getAttribute('value');
  }

  async setLoanRestructureFlagDetailsInput(loanRestructureFlagDetails: string): Promise<void> {
    await this.loanRestructureFlagDetailsInput.sendKeys(loanRestructureFlagDetails);
  }

  async getLoanRestructureFlagDetailsInput(): Promise<string> {
    return await this.loanRestructureFlagDetailsInput.getAttribute('value');
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

export class LoanRestructureFlagDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-loanRestructureFlag-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-loanRestructureFlag'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
