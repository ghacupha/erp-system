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

export class KenyanCurrencyDenominationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-kenyan-currency-denomination div table .btn-danger'));
  title = element.all(by.css('jhi-kenyan-currency-denomination div h2#page-heading span')).first();
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

export class KenyanCurrencyDenominationUpdatePage {
  pageTitle = element(by.id('jhi-kenyan-currency-denomination-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  currencyDenominationCodeInput = element(by.id('field_currencyDenominationCode'));
  currencyDenominationTypeInput = element(by.id('field_currencyDenominationType'));
  currencyDenominationTypeDetailsInput = element(by.id('field_currencyDenominationTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCurrencyDenominationCodeInput(currencyDenominationCode: string): Promise<void> {
    await this.currencyDenominationCodeInput.sendKeys(currencyDenominationCode);
  }

  async getCurrencyDenominationCodeInput(): Promise<string> {
    return await this.currencyDenominationCodeInput.getAttribute('value');
  }

  async setCurrencyDenominationTypeInput(currencyDenominationType: string): Promise<void> {
    await this.currencyDenominationTypeInput.sendKeys(currencyDenominationType);
  }

  async getCurrencyDenominationTypeInput(): Promise<string> {
    return await this.currencyDenominationTypeInput.getAttribute('value');
  }

  async setCurrencyDenominationTypeDetailsInput(currencyDenominationTypeDetails: string): Promise<void> {
    await this.currencyDenominationTypeDetailsInput.sendKeys(currencyDenominationTypeDetails);
  }

  async getCurrencyDenominationTypeDetailsInput(): Promise<string> {
    return await this.currencyDenominationTypeDetailsInput.getAttribute('value');
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

export class KenyanCurrencyDenominationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-kenyanCurrencyDenomination-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-kenyanCurrencyDenomination'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
