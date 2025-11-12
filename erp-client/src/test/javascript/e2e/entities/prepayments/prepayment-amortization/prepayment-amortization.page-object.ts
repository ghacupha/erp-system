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

export class PrepaymentAmortizationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-prepayment-amortization div table .btn-danger'));
  title = element.all(by.css('jhi-prepayment-amortization div h2#page-heading span')).first();
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

export class PrepaymentAmortizationUpdatePage {
  pageTitle = element(by.id('jhi-prepayment-amortization-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  descriptionInput = element(by.id('field_description'));
  prepaymentPeriodInput = element(by.id('field_prepaymentPeriod'));
  prepaymentAmountInput = element(by.id('field_prepaymentAmount'));
  inactiveInput = element(by.id('field_inactive'));

  prepaymentAccountSelect = element(by.id('field_prepaymentAccount'));
  settlementCurrencySelect = element(by.id('field_settlementCurrency'));
  debitAccountSelect = element(by.id('field_debitAccount'));
  creditAccountSelect = element(by.id('field_creditAccount'));
  placeholderSelect = element(by.id('field_placeholder'));
  fiscalMonthSelect = element(by.id('field_fiscalMonth'));
  prepaymentCompilationRequestSelect = element(by.id('field_prepaymentCompilationRequest'));
  amortizationPeriodSelect = element(by.id('field_amortizationPeriod'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setPrepaymentPeriodInput(prepaymentPeriod: string): Promise<void> {
    await this.prepaymentPeriodInput.sendKeys(prepaymentPeriod);
  }

  async getPrepaymentPeriodInput(): Promise<string> {
    return await this.prepaymentPeriodInput.getAttribute('value');
  }

  async setPrepaymentAmountInput(prepaymentAmount: string): Promise<void> {
    await this.prepaymentAmountInput.sendKeys(prepaymentAmount);
  }

  async getPrepaymentAmountInput(): Promise<string> {
    return await this.prepaymentAmountInput.getAttribute('value');
  }

  getInactiveInput(): ElementFinder {
    return this.inactiveInput;
  }

  async prepaymentAccountSelectLastOption(): Promise<void> {
    await this.prepaymentAccountSelect.all(by.tagName('option')).last().click();
  }

  async prepaymentAccountSelectOption(option: string): Promise<void> {
    await this.prepaymentAccountSelect.sendKeys(option);
  }

  getPrepaymentAccountSelect(): ElementFinder {
    return this.prepaymentAccountSelect;
  }

  async getPrepaymentAccountSelectedOption(): Promise<string> {
    return await this.prepaymentAccountSelect.element(by.css('option:checked')).getText();
  }

  async settlementCurrencySelectLastOption(): Promise<void> {
    await this.settlementCurrencySelect.all(by.tagName('option')).last().click();
  }

  async settlementCurrencySelectOption(option: string): Promise<void> {
    await this.settlementCurrencySelect.sendKeys(option);
  }

  getSettlementCurrencySelect(): ElementFinder {
    return this.settlementCurrencySelect;
  }

  async getSettlementCurrencySelectedOption(): Promise<string> {
    return await this.settlementCurrencySelect.element(by.css('option:checked')).getText();
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

  async fiscalMonthSelectLastOption(): Promise<void> {
    await this.fiscalMonthSelect.all(by.tagName('option')).last().click();
  }

  async fiscalMonthSelectOption(option: string): Promise<void> {
    await this.fiscalMonthSelect.sendKeys(option);
  }

  getFiscalMonthSelect(): ElementFinder {
    return this.fiscalMonthSelect;
  }

  async getFiscalMonthSelectedOption(): Promise<string> {
    return await this.fiscalMonthSelect.element(by.css('option:checked')).getText();
  }

  async prepaymentCompilationRequestSelectLastOption(): Promise<void> {
    await this.prepaymentCompilationRequestSelect.all(by.tagName('option')).last().click();
  }

  async prepaymentCompilationRequestSelectOption(option: string): Promise<void> {
    await this.prepaymentCompilationRequestSelect.sendKeys(option);
  }

  getPrepaymentCompilationRequestSelect(): ElementFinder {
    return this.prepaymentCompilationRequestSelect;
  }

  async getPrepaymentCompilationRequestSelectedOption(): Promise<string> {
    return await this.prepaymentCompilationRequestSelect.element(by.css('option:checked')).getText();
  }

  async amortizationPeriodSelectLastOption(): Promise<void> {
    await this.amortizationPeriodSelect.all(by.tagName('option')).last().click();
  }

  async amortizationPeriodSelectOption(option: string): Promise<void> {
    await this.amortizationPeriodSelect.sendKeys(option);
  }

  getAmortizationPeriodSelect(): ElementFinder {
    return this.amortizationPeriodSelect;
  }

  async getAmortizationPeriodSelectedOption(): Promise<string> {
    return await this.amortizationPeriodSelect.element(by.css('option:checked')).getText();
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

export class PrepaymentAmortizationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-prepaymentAmortization-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-prepaymentAmortization'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
