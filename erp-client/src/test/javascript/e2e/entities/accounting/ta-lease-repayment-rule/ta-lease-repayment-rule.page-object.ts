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

export class TALeaseRepaymentRuleComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-ta-lease-repayment-rule div table .btn-danger'));
  title = element.all(by.css('jhi-ta-lease-repayment-rule div h2#page-heading span')).first();
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

export class TALeaseRepaymentRuleUpdatePage {
  pageTitle = element(by.id('jhi-ta-lease-repayment-rule-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  identifierInput = element(by.id('field_identifier'));

  leaseContractSelect = element(by.id('field_leaseContract'));
  debitSelect = element(by.id('field_debit'));
  creditSelect = element(by.id('field_credit'));
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

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setIdentifierInput(identifier: string): Promise<void> {
    await this.identifierInput.sendKeys(identifier);
  }

  async getIdentifierInput(): Promise<string> {
    return await this.identifierInput.getAttribute('value');
  }

  async leaseContractSelectLastOption(): Promise<void> {
    await this.leaseContractSelect.all(by.tagName('option')).last().click();
  }

  async leaseContractSelectOption(option: string): Promise<void> {
    await this.leaseContractSelect.sendKeys(option);
  }

  getLeaseContractSelect(): ElementFinder {
    return this.leaseContractSelect;
  }

  async getLeaseContractSelectedOption(): Promise<string> {
    return await this.leaseContractSelect.element(by.css('option:checked')).getText();
  }

  async debitSelectLastOption(): Promise<void> {
    await this.debitSelect.all(by.tagName('option')).last().click();
  }

  async debitSelectOption(option: string): Promise<void> {
    await this.debitSelect.sendKeys(option);
  }

  getDebitSelect(): ElementFinder {
    return this.debitSelect;
  }

  async getDebitSelectedOption(): Promise<string> {
    return await this.debitSelect.element(by.css('option:checked')).getText();
  }

  async creditSelectLastOption(): Promise<void> {
    await this.creditSelect.all(by.tagName('option')).last().click();
  }

  async creditSelectOption(option: string): Promise<void> {
    await this.creditSelect.sendKeys(option);
  }

  getCreditSelect(): ElementFinder {
    return this.creditSelect;
  }

  async getCreditSelectedOption(): Promise<string> {
    return await this.creditSelect.element(by.css('option:checked')).getText();
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

export class TALeaseRepaymentRuleDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-tALeaseRepaymentRule-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-tALeaseRepaymentRule'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
