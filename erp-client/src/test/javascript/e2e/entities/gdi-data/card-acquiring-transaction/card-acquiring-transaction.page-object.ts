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

export class CardAcquiringTransactionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-card-acquiring-transaction div table .btn-danger'));
  title = element.all(by.css('jhi-card-acquiring-transaction div h2#page-heading span')).first();
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

export class CardAcquiringTransactionUpdatePage {
  pageTitle = element(by.id('jhi-card-acquiring-transaction-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  terminalIdInput = element(by.id('field_terminalId'));
  numberOfTransactionsInput = element(by.id('field_numberOfTransactions'));
  valueOfTransactionsInLCYInput = element(by.id('field_valueOfTransactionsInLCY'));

  bankCodeSelect = element(by.id('field_bankCode'));
  channelTypeSelect = element(by.id('field_channelType'));
  cardBrandTypeSelect = element(by.id('field_cardBrandType'));
  currencyOfTransactionSelect = element(by.id('field_currencyOfTransaction'));
  cardIssuerCategorySelect = element(by.id('field_cardIssuerCategory'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setReportingDateInput(reportingDate: string): Promise<void> {
    await this.reportingDateInput.sendKeys(reportingDate);
  }

  async getReportingDateInput(): Promise<string> {
    return await this.reportingDateInput.getAttribute('value');
  }

  async setTerminalIdInput(terminalId: string): Promise<void> {
    await this.terminalIdInput.sendKeys(terminalId);
  }

  async getTerminalIdInput(): Promise<string> {
    return await this.terminalIdInput.getAttribute('value');
  }

  async setNumberOfTransactionsInput(numberOfTransactions: string): Promise<void> {
    await this.numberOfTransactionsInput.sendKeys(numberOfTransactions);
  }

  async getNumberOfTransactionsInput(): Promise<string> {
    return await this.numberOfTransactionsInput.getAttribute('value');
  }

  async setValueOfTransactionsInLCYInput(valueOfTransactionsInLCY: string): Promise<void> {
    await this.valueOfTransactionsInLCYInput.sendKeys(valueOfTransactionsInLCY);
  }

  async getValueOfTransactionsInLCYInput(): Promise<string> {
    return await this.valueOfTransactionsInLCYInput.getAttribute('value');
  }

  async bankCodeSelectLastOption(): Promise<void> {
    await this.bankCodeSelect.all(by.tagName('option')).last().click();
  }

  async bankCodeSelectOption(option: string): Promise<void> {
    await this.bankCodeSelect.sendKeys(option);
  }

  getBankCodeSelect(): ElementFinder {
    return this.bankCodeSelect;
  }

  async getBankCodeSelectedOption(): Promise<string> {
    return await this.bankCodeSelect.element(by.css('option:checked')).getText();
  }

  async channelTypeSelectLastOption(): Promise<void> {
    await this.channelTypeSelect.all(by.tagName('option')).last().click();
  }

  async channelTypeSelectOption(option: string): Promise<void> {
    await this.channelTypeSelect.sendKeys(option);
  }

  getChannelTypeSelect(): ElementFinder {
    return this.channelTypeSelect;
  }

  async getChannelTypeSelectedOption(): Promise<string> {
    return await this.channelTypeSelect.element(by.css('option:checked')).getText();
  }

  async cardBrandTypeSelectLastOption(): Promise<void> {
    await this.cardBrandTypeSelect.all(by.tagName('option')).last().click();
  }

  async cardBrandTypeSelectOption(option: string): Promise<void> {
    await this.cardBrandTypeSelect.sendKeys(option);
  }

  getCardBrandTypeSelect(): ElementFinder {
    return this.cardBrandTypeSelect;
  }

  async getCardBrandTypeSelectedOption(): Promise<string> {
    return await this.cardBrandTypeSelect.element(by.css('option:checked')).getText();
  }

  async currencyOfTransactionSelectLastOption(): Promise<void> {
    await this.currencyOfTransactionSelect.all(by.tagName('option')).last().click();
  }

  async currencyOfTransactionSelectOption(option: string): Promise<void> {
    await this.currencyOfTransactionSelect.sendKeys(option);
  }

  getCurrencyOfTransactionSelect(): ElementFinder {
    return this.currencyOfTransactionSelect;
  }

  async getCurrencyOfTransactionSelectedOption(): Promise<string> {
    return await this.currencyOfTransactionSelect.element(by.css('option:checked')).getText();
  }

  async cardIssuerCategorySelectLastOption(): Promise<void> {
    await this.cardIssuerCategorySelect.all(by.tagName('option')).last().click();
  }

  async cardIssuerCategorySelectOption(option: string): Promise<void> {
    await this.cardIssuerCategorySelect.sendKeys(option);
  }

  getCardIssuerCategorySelect(): ElementFinder {
    return this.cardIssuerCategorySelect;
  }

  async getCardIssuerCategorySelectedOption(): Promise<string> {
    return await this.cardIssuerCategorySelect.element(by.css('option:checked')).getText();
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

export class CardAcquiringTransactionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cardAcquiringTransaction-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cardAcquiringTransaction'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
