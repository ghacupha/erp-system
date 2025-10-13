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

export class AgentBankingActivityComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-agent-banking-activity div table .btn-danger'));
  title = element.all(by.css('jhi-agent-banking-activity div h2#page-heading span')).first();
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

export class AgentBankingActivityUpdatePage {
  pageTitle = element(by.id('jhi-agent-banking-activity-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  agentUniqueIdInput = element(by.id('field_agentUniqueId'));
  terminalUniqueIdInput = element(by.id('field_terminalUniqueId'));
  totalCountOfTransactionsInput = element(by.id('field_totalCountOfTransactions'));
  totalValueOfTransactionsInLCYInput = element(by.id('field_totalValueOfTransactionsInLCY'));

  bankCodeSelect = element(by.id('field_bankCode'));
  branchCodeSelect = element(by.id('field_branchCode'));
  transactionTypeSelect = element(by.id('field_transactionType'));

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

  async setAgentUniqueIdInput(agentUniqueId: string): Promise<void> {
    await this.agentUniqueIdInput.sendKeys(agentUniqueId);
  }

  async getAgentUniqueIdInput(): Promise<string> {
    return await this.agentUniqueIdInput.getAttribute('value');
  }

  async setTerminalUniqueIdInput(terminalUniqueId: string): Promise<void> {
    await this.terminalUniqueIdInput.sendKeys(terminalUniqueId);
  }

  async getTerminalUniqueIdInput(): Promise<string> {
    return await this.terminalUniqueIdInput.getAttribute('value');
  }

  async setTotalCountOfTransactionsInput(totalCountOfTransactions: string): Promise<void> {
    await this.totalCountOfTransactionsInput.sendKeys(totalCountOfTransactions);
  }

  async getTotalCountOfTransactionsInput(): Promise<string> {
    return await this.totalCountOfTransactionsInput.getAttribute('value');
  }

  async setTotalValueOfTransactionsInLCYInput(totalValueOfTransactionsInLCY: string): Promise<void> {
    await this.totalValueOfTransactionsInLCYInput.sendKeys(totalValueOfTransactionsInLCY);
  }

  async getTotalValueOfTransactionsInLCYInput(): Promise<string> {
    return await this.totalValueOfTransactionsInLCYInput.getAttribute('value');
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

export class AgentBankingActivityDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-agentBankingActivity-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-agentBankingActivity'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
