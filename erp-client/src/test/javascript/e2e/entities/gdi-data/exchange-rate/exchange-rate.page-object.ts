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

export class ExchangeRateComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-exchange-rate div table .btn-danger'));
  title = element.all(by.css('jhi-exchange-rate div h2#page-heading span')).first();
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

export class ExchangeRateUpdatePage {
  pageTitle = element(by.id('jhi-exchange-rate-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  businessReportingDayInput = element(by.id('field_businessReportingDay'));
  buyingRateInput = element(by.id('field_buyingRate'));
  sellingRateInput = element(by.id('field_sellingRate'));
  meanRateInput = element(by.id('field_meanRate'));
  closingBidRateInput = element(by.id('field_closingBidRate'));
  closingOfferRateInput = element(by.id('field_closingOfferRate'));
  usdCrossRateInput = element(by.id('field_usdCrossRate'));

  institutionCodeSelect = element(by.id('field_institutionCode'));
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

  async setBusinessReportingDayInput(businessReportingDay: string): Promise<void> {
    await this.businessReportingDayInput.sendKeys(businessReportingDay);
  }

  async getBusinessReportingDayInput(): Promise<string> {
    return await this.businessReportingDayInput.getAttribute('value');
  }

  async setBuyingRateInput(buyingRate: string): Promise<void> {
    await this.buyingRateInput.sendKeys(buyingRate);
  }

  async getBuyingRateInput(): Promise<string> {
    return await this.buyingRateInput.getAttribute('value');
  }

  async setSellingRateInput(sellingRate: string): Promise<void> {
    await this.sellingRateInput.sendKeys(sellingRate);
  }

  async getSellingRateInput(): Promise<string> {
    return await this.sellingRateInput.getAttribute('value');
  }

  async setMeanRateInput(meanRate: string): Promise<void> {
    await this.meanRateInput.sendKeys(meanRate);
  }

  async getMeanRateInput(): Promise<string> {
    return await this.meanRateInput.getAttribute('value');
  }

  async setClosingBidRateInput(closingBidRate: string): Promise<void> {
    await this.closingBidRateInput.sendKeys(closingBidRate);
  }

  async getClosingBidRateInput(): Promise<string> {
    return await this.closingBidRateInput.getAttribute('value');
  }

  async setClosingOfferRateInput(closingOfferRate: string): Promise<void> {
    await this.closingOfferRateInput.sendKeys(closingOfferRate);
  }

  async getClosingOfferRateInput(): Promise<string> {
    return await this.closingOfferRateInput.getAttribute('value');
  }

  async setUsdCrossRateInput(usdCrossRate: string): Promise<void> {
    await this.usdCrossRateInput.sendKeys(usdCrossRate);
  }

  async getUsdCrossRateInput(): Promise<string> {
    return await this.usdCrossRateInput.getAttribute('value');
  }

  async institutionCodeSelectLastOption(): Promise<void> {
    await this.institutionCodeSelect.all(by.tagName('option')).last().click();
  }

  async institutionCodeSelectOption(option: string): Promise<void> {
    await this.institutionCodeSelect.sendKeys(option);
  }

  getInstitutionCodeSelect(): ElementFinder {
    return this.institutionCodeSelect;
  }

  async getInstitutionCodeSelectedOption(): Promise<string> {
    return await this.institutionCodeSelect.element(by.css('option:checked')).getText();
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

export class ExchangeRateDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-exchangeRate-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-exchangeRate'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
