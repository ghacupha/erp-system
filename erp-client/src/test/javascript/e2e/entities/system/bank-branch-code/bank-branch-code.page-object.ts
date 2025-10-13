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

export class BankBranchCodeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-bank-branch-code div table .btn-danger'));
  title = element.all(by.css('jhi-bank-branch-code div h2#page-heading span')).first();
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

export class BankBranchCodeUpdatePage {
  pageTitle = element(by.id('jhi-bank-branch-code-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  bankCodeInput = element(by.id('field_bankCode'));
  bankNameInput = element(by.id('field_bankName'));
  branchCodeInput = element(by.id('field_branchCode'));
  branchNameInput = element(by.id('field_branchName'));
  notesInput = element(by.id('field_notes'));

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

  async setBankCodeInput(bankCode: string): Promise<void> {
    await this.bankCodeInput.sendKeys(bankCode);
  }

  async getBankCodeInput(): Promise<string> {
    return await this.bankCodeInput.getAttribute('value');
  }

  async setBankNameInput(bankName: string): Promise<void> {
    await this.bankNameInput.sendKeys(bankName);
  }

  async getBankNameInput(): Promise<string> {
    return await this.bankNameInput.getAttribute('value');
  }

  async setBranchCodeInput(branchCode: string): Promise<void> {
    await this.branchCodeInput.sendKeys(branchCode);
  }

  async getBranchCodeInput(): Promise<string> {
    return await this.branchCodeInput.getAttribute('value');
  }

  async setBranchNameInput(branchName: string): Promise<void> {
    await this.branchNameInput.sendKeys(branchName);
  }

  async getBranchNameInput(): Promise<string> {
    return await this.branchNameInput.getAttribute('value');
  }

  async setNotesInput(notes: string): Promise<void> {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput(): Promise<string> {
    return await this.notesInput.getAttribute('value');
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

export class BankBranchCodeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-bankBranchCode-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-bankBranchCode'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
