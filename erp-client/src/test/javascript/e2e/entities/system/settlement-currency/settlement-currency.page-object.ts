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

export class SettlementCurrencyComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-settlement-currency div table .btn-danger'));
  title = element.all(by.css('jhi-settlement-currency div h2#page-heading span')).first();
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

export class SettlementCurrencyUpdatePage {
  pageTitle = element(by.id('jhi-settlement-currency-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  iso4217CurrencyCodeInput = element(by.id('field_iso4217CurrencyCode'));
  currencyNameInput = element(by.id('field_currencyName'));
  countryInput = element(by.id('field_country'));
  numericCodeInput = element(by.id('field_numericCode'));
  minorUnitInput = element(by.id('field_minorUnit'));
  fileUploadTokenInput = element(by.id('field_fileUploadToken'));
  compilationTokenInput = element(by.id('field_compilationToken'));

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

  async setIso4217CurrencyCodeInput(iso4217CurrencyCode: string): Promise<void> {
    await this.iso4217CurrencyCodeInput.sendKeys(iso4217CurrencyCode);
  }

  async getIso4217CurrencyCodeInput(): Promise<string> {
    return await this.iso4217CurrencyCodeInput.getAttribute('value');
  }

  async setCurrencyNameInput(currencyName: string): Promise<void> {
    await this.currencyNameInput.sendKeys(currencyName);
  }

  async getCurrencyNameInput(): Promise<string> {
    return await this.currencyNameInput.getAttribute('value');
  }

  async setCountryInput(country: string): Promise<void> {
    await this.countryInput.sendKeys(country);
  }

  async getCountryInput(): Promise<string> {
    return await this.countryInput.getAttribute('value');
  }

  async setNumericCodeInput(numericCode: string): Promise<void> {
    await this.numericCodeInput.sendKeys(numericCode);
  }

  async getNumericCodeInput(): Promise<string> {
    return await this.numericCodeInput.getAttribute('value');
  }

  async setMinorUnitInput(minorUnit: string): Promise<void> {
    await this.minorUnitInput.sendKeys(minorUnit);
  }

  async getMinorUnitInput(): Promise<string> {
    return await this.minorUnitInput.getAttribute('value');
  }

  async setFileUploadTokenInput(fileUploadToken: string): Promise<void> {
    await this.fileUploadTokenInput.sendKeys(fileUploadToken);
  }

  async getFileUploadTokenInput(): Promise<string> {
    return await this.fileUploadTokenInput.getAttribute('value');
  }

  async setCompilationTokenInput(compilationToken: string): Promise<void> {
    await this.compilationTokenInput.sendKeys(compilationToken);
  }

  async getCompilationTokenInput(): Promise<string> {
    return await this.compilationTokenInput.getAttribute('value');
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

export class SettlementCurrencyDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-settlementCurrency-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-settlementCurrency'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
