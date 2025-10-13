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

export class TransactionDetailsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-transaction-details div table .btn-danger'));
  title = element.all(by.css('jhi-transaction-details div h2#page-heading span')).first();
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

export class TransactionDetailsUpdatePage {
  pageTitle = element(by.id('jhi-transaction-details-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  entryIdInput = element(by.id('field_entryId'));
  transactionDateInput = element(by.id('field_transactionDate'));
  descriptionInput = element(by.id('field_description'));
  amountInput = element(by.id('field_amount'));
  createdAtInput = element(by.id('field_createdAt'));
  modifiedAtInput = element(by.id('field_modifiedAt'));
  transactionTypeInput = element(by.id('field_transactionType'));

  debitAccountSelect = element(by.id('field_debitAccount'));
  creditAccountSelect = element(by.id('field_creditAccount'));
  placeholderSelect = element(by.id('field_placeholder'));
  postedBySelect = element(by.id('field_postedBy'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setEntryIdInput(entryId: string): Promise<void> {
    await this.entryIdInput.sendKeys(entryId);
  }

  async getEntryIdInput(): Promise<string> {
    return await this.entryIdInput.getAttribute('value');
  }

  async setTransactionDateInput(transactionDate: string): Promise<void> {
    await this.transactionDateInput.sendKeys(transactionDate);
  }

  async getTransactionDateInput(): Promise<string> {
    return await this.transactionDateInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setAmountInput(amount: string): Promise<void> {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput(): Promise<string> {
    return await this.amountInput.getAttribute('value');
  }

  async setCreatedAtInput(createdAt: string): Promise<void> {
    await this.createdAtInput.sendKeys(createdAt);
  }

  async getCreatedAtInput(): Promise<string> {
    return await this.createdAtInput.getAttribute('value');
  }

  async setModifiedAtInput(modifiedAt: string): Promise<void> {
    await this.modifiedAtInput.sendKeys(modifiedAt);
  }

  async getModifiedAtInput(): Promise<string> {
    return await this.modifiedAtInput.getAttribute('value');
  }

  async setTransactionTypeInput(transactionType: string): Promise<void> {
    await this.transactionTypeInput.sendKeys(transactionType);
  }

  async getTransactionTypeInput(): Promise<string> {
    return await this.transactionTypeInput.getAttribute('value');
  }

  async debitAccountSelectLastOption(): Promise<void> {
    await this.debitAccountSelect.all(by.tagName('option')).last().click();
  }

  async debitAccountSelectOption(option: string): Promise<void> {
    await this.debitAccountSelect.sendKeys(option);
  }

  getDebitAccountSelect(): ElementFinder {
    return this.debitAccountSelect;
  }

  async getDebitAccountSelectedOption(): Promise<string> {
    return await this.debitAccountSelect.element(by.css('option:checked')).getText();
  }

  async creditAccountSelectLastOption(): Promise<void> {
    await this.creditAccountSelect.all(by.tagName('option')).last().click();
  }

  async creditAccountSelectOption(option: string): Promise<void> {
    await this.creditAccountSelect.sendKeys(option);
  }

  getCreditAccountSelect(): ElementFinder {
    return this.creditAccountSelect;
  }

  async getCreditAccountSelectedOption(): Promise<string> {
    return await this.creditAccountSelect.element(by.css('option:checked')).getText();
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

  async postedBySelectLastOption(): Promise<void> {
    await this.postedBySelect.all(by.tagName('option')).last().click();
  }

  async postedBySelectOption(option: string): Promise<void> {
    await this.postedBySelect.sendKeys(option);
  }

  getPostedBySelect(): ElementFinder {
    return this.postedBySelect;
  }

  async getPostedBySelectedOption(): Promise<string> {
    return await this.postedBySelect.element(by.css('option:checked')).getText();
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

export class TransactionDetailsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-transactionDetails-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-transactionDetails'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
