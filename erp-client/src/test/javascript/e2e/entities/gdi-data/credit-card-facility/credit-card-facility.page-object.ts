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

export class CreditCardFacilityComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-credit-card-facility div table .btn-danger'));
  title = element.all(by.css('jhi-credit-card-facility div h2#page-heading span')).first();
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

export class CreditCardFacilityUpdatePage {
  pageTitle = element(by.id('jhi-credit-card-facility-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  totalNumberOfActiveCreditCardsInput = element(by.id('field_totalNumberOfActiveCreditCards'));
  totalCreditCardLimitsInCCYInput = element(by.id('field_totalCreditCardLimitsInCCY'));
  totalCreditCardLimitsInLCYInput = element(by.id('field_totalCreditCardLimitsInLCY'));
  totalCreditCardAmountUtilisedInCCYInput = element(by.id('field_totalCreditCardAmountUtilisedInCCY'));
  totalCreditCardAmountUtilisedInLcyInput = element(by.id('field_totalCreditCardAmountUtilisedInLcy'));
  totalNPACreditCardAmountInFCYInput = element(by.id('field_totalNPACreditCardAmountInFCY'));
  totalNPACreditCardAmountInLCYInput = element(by.id('field_totalNPACreditCardAmountInLCY'));

  bankCodeSelect = element(by.id('field_bankCode'));
  customerCategorySelect = element(by.id('field_customerCategory'));
  currencyCodeSelect = element(by.id('field_currencyCode'));

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

  async setTotalNumberOfActiveCreditCardsInput(totalNumberOfActiveCreditCards: string): Promise<void> {
    await this.totalNumberOfActiveCreditCardsInput.sendKeys(totalNumberOfActiveCreditCards);
  }

  async getTotalNumberOfActiveCreditCardsInput(): Promise<string> {
    return await this.totalNumberOfActiveCreditCardsInput.getAttribute('value');
  }

  async setTotalCreditCardLimitsInCCYInput(totalCreditCardLimitsInCCY: string): Promise<void> {
    await this.totalCreditCardLimitsInCCYInput.sendKeys(totalCreditCardLimitsInCCY);
  }

  async getTotalCreditCardLimitsInCCYInput(): Promise<string> {
    return await this.totalCreditCardLimitsInCCYInput.getAttribute('value');
  }

  async setTotalCreditCardLimitsInLCYInput(totalCreditCardLimitsInLCY: string): Promise<void> {
    await this.totalCreditCardLimitsInLCYInput.sendKeys(totalCreditCardLimitsInLCY);
  }

  async getTotalCreditCardLimitsInLCYInput(): Promise<string> {
    return await this.totalCreditCardLimitsInLCYInput.getAttribute('value');
  }

  async setTotalCreditCardAmountUtilisedInCCYInput(totalCreditCardAmountUtilisedInCCY: string): Promise<void> {
    await this.totalCreditCardAmountUtilisedInCCYInput.sendKeys(totalCreditCardAmountUtilisedInCCY);
  }

  async getTotalCreditCardAmountUtilisedInCCYInput(): Promise<string> {
    return await this.totalCreditCardAmountUtilisedInCCYInput.getAttribute('value');
  }

  async setTotalCreditCardAmountUtilisedInLcyInput(totalCreditCardAmountUtilisedInLcy: string): Promise<void> {
    await this.totalCreditCardAmountUtilisedInLcyInput.sendKeys(totalCreditCardAmountUtilisedInLcy);
  }

  async getTotalCreditCardAmountUtilisedInLcyInput(): Promise<string> {
    return await this.totalCreditCardAmountUtilisedInLcyInput.getAttribute('value');
  }

  async setTotalNPACreditCardAmountInFCYInput(totalNPACreditCardAmountInFCY: string): Promise<void> {
    await this.totalNPACreditCardAmountInFCYInput.sendKeys(totalNPACreditCardAmountInFCY);
  }

  async getTotalNPACreditCardAmountInFCYInput(): Promise<string> {
    return await this.totalNPACreditCardAmountInFCYInput.getAttribute('value');
  }

  async setTotalNPACreditCardAmountInLCYInput(totalNPACreditCardAmountInLCY: string): Promise<void> {
    await this.totalNPACreditCardAmountInLCYInput.sendKeys(totalNPACreditCardAmountInLCY);
  }

  async getTotalNPACreditCardAmountInLCYInput(): Promise<string> {
    return await this.totalNPACreditCardAmountInLCYInput.getAttribute('value');
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

  async customerCategorySelectLastOption(): Promise<void> {
    await this.customerCategorySelect.all(by.tagName('option')).last().click();
  }

  async customerCategorySelectOption(option: string): Promise<void> {
    await this.customerCategorySelect.sendKeys(option);
  }

  getCustomerCategorySelect(): ElementFinder {
    return this.customerCategorySelect;
  }

  async getCustomerCategorySelectedOption(): Promise<string> {
    return await this.customerCategorySelect.element(by.css('option:checked')).getText();
  }

  async currencyCodeSelectLastOption(): Promise<void> {
    await this.currencyCodeSelect.all(by.tagName('option')).last().click();
  }

  async currencyCodeSelectOption(option: string): Promise<void> {
    await this.currencyCodeSelect.sendKeys(option);
  }

  getCurrencyCodeSelect(): ElementFinder {
    return this.currencyCodeSelect;
  }

  async getCurrencyCodeSelectedOption(): Promise<string> {
    return await this.currencyCodeSelect.element(by.css('option:checked')).getText();
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

export class CreditCardFacilityDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-creditCardFacility-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-creditCardFacility'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
