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

export class AccountAttributeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-account-attribute div table .btn-danger'));
  title = element.all(by.css('jhi-account-attribute div h2#page-heading span')).first();
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

export class AccountAttributeUpdatePage {
  pageTitle = element(by.id('jhi-account-attribute-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  customerNumberInput = element(by.id('field_customerNumber'));
  accountContractNumberInput = element(by.id('field_accountContractNumber'));
  accountNameInput = element(by.id('field_accountName'));
  accountOpeningDateInput = element(by.id('field_accountOpeningDate'));
  accountClosingDateInput = element(by.id('field_accountClosingDate'));
  debitInterestRateInput = element(by.id('field_debitInterestRate'));
  creditInterestRateInput = element(by.id('field_creditInterestRate'));
  sanctionedAccountLimitFcyInput = element(by.id('field_sanctionedAccountLimitFcy'));
  sanctionedAccountLimitLcyInput = element(by.id('field_sanctionedAccountLimitLcy'));
  accountStatusChangeDateInput = element(by.id('field_accountStatusChangeDate'));
  expiryDateInput = element(by.id('field_expiryDate'));

  bankCodeSelect = element(by.id('field_bankCode'));
  branchCodeSelect = element(by.id('field_branchCode'));
  accountOwnershipTypeSelect = element(by.id('field_accountOwnershipType'));

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

  async setCustomerNumberInput(customerNumber: string): Promise<void> {
    await this.customerNumberInput.sendKeys(customerNumber);
  }

  async getCustomerNumberInput(): Promise<string> {
    return await this.customerNumberInput.getAttribute('value');
  }

  async setAccountContractNumberInput(accountContractNumber: string): Promise<void> {
    await this.accountContractNumberInput.sendKeys(accountContractNumber);
  }

  async getAccountContractNumberInput(): Promise<string> {
    return await this.accountContractNumberInput.getAttribute('value');
  }

  async setAccountNameInput(accountName: string): Promise<void> {
    await this.accountNameInput.sendKeys(accountName);
  }

  async getAccountNameInput(): Promise<string> {
    return await this.accountNameInput.getAttribute('value');
  }

  async setAccountOpeningDateInput(accountOpeningDate: string): Promise<void> {
    await this.accountOpeningDateInput.sendKeys(accountOpeningDate);
  }

  async getAccountOpeningDateInput(): Promise<string> {
    return await this.accountOpeningDateInput.getAttribute('value');
  }

  async setAccountClosingDateInput(accountClosingDate: string): Promise<void> {
    await this.accountClosingDateInput.sendKeys(accountClosingDate);
  }

  async getAccountClosingDateInput(): Promise<string> {
    return await this.accountClosingDateInput.getAttribute('value');
  }

  async setDebitInterestRateInput(debitInterestRate: string): Promise<void> {
    await this.debitInterestRateInput.sendKeys(debitInterestRate);
  }

  async getDebitInterestRateInput(): Promise<string> {
    return await this.debitInterestRateInput.getAttribute('value');
  }

  async setCreditInterestRateInput(creditInterestRate: string): Promise<void> {
    await this.creditInterestRateInput.sendKeys(creditInterestRate);
  }

  async getCreditInterestRateInput(): Promise<string> {
    return await this.creditInterestRateInput.getAttribute('value');
  }

  async setSanctionedAccountLimitFcyInput(sanctionedAccountLimitFcy: string): Promise<void> {
    await this.sanctionedAccountLimitFcyInput.sendKeys(sanctionedAccountLimitFcy);
  }

  async getSanctionedAccountLimitFcyInput(): Promise<string> {
    return await this.sanctionedAccountLimitFcyInput.getAttribute('value');
  }

  async setSanctionedAccountLimitLcyInput(sanctionedAccountLimitLcy: string): Promise<void> {
    await this.sanctionedAccountLimitLcyInput.sendKeys(sanctionedAccountLimitLcy);
  }

  async getSanctionedAccountLimitLcyInput(): Promise<string> {
    return await this.sanctionedAccountLimitLcyInput.getAttribute('value');
  }

  async setAccountStatusChangeDateInput(accountStatusChangeDate: string): Promise<void> {
    await this.accountStatusChangeDateInput.sendKeys(accountStatusChangeDate);
  }

  async getAccountStatusChangeDateInput(): Promise<string> {
    return await this.accountStatusChangeDateInput.getAttribute('value');
  }

  async setExpiryDateInput(expiryDate: string): Promise<void> {
    await this.expiryDateInput.sendKeys(expiryDate);
  }

  async getExpiryDateInput(): Promise<string> {
    return await this.expiryDateInput.getAttribute('value');
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

  async branchCodeSelectLastOption(): Promise<void> {
    await this.branchCodeSelect.all(by.tagName('option')).last().click();
  }

  async branchCodeSelectOption(option: string): Promise<void> {
    await this.branchCodeSelect.sendKeys(option);
  }

  getBranchCodeSelect(): ElementFinder {
    return this.branchCodeSelect;
  }

  async getBranchCodeSelectedOption(): Promise<string> {
    return await this.branchCodeSelect.element(by.css('option:checked')).getText();
  }

  async accountOwnershipTypeSelectLastOption(): Promise<void> {
    await this.accountOwnershipTypeSelect.all(by.tagName('option')).last().click();
  }

  async accountOwnershipTypeSelectOption(option: string): Promise<void> {
    await this.accountOwnershipTypeSelect.sendKeys(option);
  }

  getAccountOwnershipTypeSelect(): ElementFinder {
    return this.accountOwnershipTypeSelect;
  }

  async getAccountOwnershipTypeSelectedOption(): Promise<string> {
    return await this.accountOwnershipTypeSelect.element(by.css('option:checked')).getText();
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

export class AccountAttributeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-accountAttribute-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-accountAttribute'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
