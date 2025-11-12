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

export class CardUsageInformationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-card-usage-information div table .btn-danger'));
  title = element.all(by.css('jhi-card-usage-information div h2#page-heading span')).first();
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

export class CardUsageInformationUpdatePage {
  pageTitle = element(by.id('jhi-card-usage-information-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  totalNumberOfLiveCardsInput = element(by.id('field_totalNumberOfLiveCards'));
  totalActiveCardsInput = element(by.id('field_totalActiveCards'));
  totalNumberOfTransactionsDoneInput = element(by.id('field_totalNumberOfTransactionsDone'));
  totalValueOfTransactionsDoneInLCYInput = element(by.id('field_totalValueOfTransactionsDoneInLCY'));

  bankCodeSelect = element(by.id('field_bankCode'));
  cardTypeSelect = element(by.id('field_cardType'));
  cardBrandSelect = element(by.id('field_cardBrand'));
  cardCategoryTypeSelect = element(by.id('field_cardCategoryType'));
  transactionTypeSelect = element(by.id('field_transactionType'));
  channelTypeSelect = element(by.id('field_channelType'));
  cardStateSelect = element(by.id('field_cardState'));

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

  async setTotalNumberOfLiveCardsInput(totalNumberOfLiveCards: string): Promise<void> {
    await this.totalNumberOfLiveCardsInput.sendKeys(totalNumberOfLiveCards);
  }

  async getTotalNumberOfLiveCardsInput(): Promise<string> {
    return await this.totalNumberOfLiveCardsInput.getAttribute('value');
  }

  async setTotalActiveCardsInput(totalActiveCards: string): Promise<void> {
    await this.totalActiveCardsInput.sendKeys(totalActiveCards);
  }

  async getTotalActiveCardsInput(): Promise<string> {
    return await this.totalActiveCardsInput.getAttribute('value');
  }

  async setTotalNumberOfTransactionsDoneInput(totalNumberOfTransactionsDone: string): Promise<void> {
    await this.totalNumberOfTransactionsDoneInput.sendKeys(totalNumberOfTransactionsDone);
  }

  async getTotalNumberOfTransactionsDoneInput(): Promise<string> {
    return await this.totalNumberOfTransactionsDoneInput.getAttribute('value');
  }

  async setTotalValueOfTransactionsDoneInLCYInput(totalValueOfTransactionsDoneInLCY: string): Promise<void> {
    await this.totalValueOfTransactionsDoneInLCYInput.sendKeys(totalValueOfTransactionsDoneInLCY);
  }

  async getTotalValueOfTransactionsDoneInLCYInput(): Promise<string> {
    return await this.totalValueOfTransactionsDoneInLCYInput.getAttribute('value');
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

  async cardTypeSelectLastOption(): Promise<void> {
    await this.cardTypeSelect.all(by.tagName('option')).last().click();
  }

  async cardTypeSelectOption(option: string): Promise<void> {
    await this.cardTypeSelect.sendKeys(option);
  }

  getCardTypeSelect(): ElementFinder {
    return this.cardTypeSelect;
  }

  async getCardTypeSelectedOption(): Promise<string> {
    return await this.cardTypeSelect.element(by.css('option:checked')).getText();
  }

  async cardBrandSelectLastOption(): Promise<void> {
    await this.cardBrandSelect.all(by.tagName('option')).last().click();
  }

  async cardBrandSelectOption(option: string): Promise<void> {
    await this.cardBrandSelect.sendKeys(option);
  }

  getCardBrandSelect(): ElementFinder {
    return this.cardBrandSelect;
  }

  async getCardBrandSelectedOption(): Promise<string> {
    return await this.cardBrandSelect.element(by.css('option:checked')).getText();
  }

  async cardCategoryTypeSelectLastOption(): Promise<void> {
    await this.cardCategoryTypeSelect.all(by.tagName('option')).last().click();
  }

  async cardCategoryTypeSelectOption(option: string): Promise<void> {
    await this.cardCategoryTypeSelect.sendKeys(option);
  }

  getCardCategoryTypeSelect(): ElementFinder {
    return this.cardCategoryTypeSelect;
  }

  async getCardCategoryTypeSelectedOption(): Promise<string> {
    return await this.cardCategoryTypeSelect.element(by.css('option:checked')).getText();
  }

  async transactionTypeSelectLastOption(): Promise<void> {
    await this.transactionTypeSelect.all(by.tagName('option')).last().click();
  }

  async transactionTypeSelectOption(option: string): Promise<void> {
    await this.transactionTypeSelect.sendKeys(option);
  }

  getTransactionTypeSelect(): ElementFinder {
    return this.transactionTypeSelect;
  }

  async getTransactionTypeSelectedOption(): Promise<string> {
    return await this.transactionTypeSelect.element(by.css('option:checked')).getText();
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

  async cardStateSelectLastOption(): Promise<void> {
    await this.cardStateSelect.all(by.tagName('option')).last().click();
  }

  async cardStateSelectOption(option: string): Promise<void> {
    await this.cardStateSelect.sendKeys(option);
  }

  getCardStateSelect(): ElementFinder {
    return this.cardStateSelect;
  }

  async getCardStateSelectedOption(): Promise<string> {
    return await this.cardStateSelect.element(by.css('option:checked')).getText();
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

export class CardUsageInformationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cardUsageInformation-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cardUsageInformation'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
