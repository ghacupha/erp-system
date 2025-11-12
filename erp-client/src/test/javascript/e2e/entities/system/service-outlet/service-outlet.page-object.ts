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

export class ServiceOutletComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-service-outlet div table .btn-danger'));
  title = element.all(by.css('jhi-service-outlet div h2#page-heading span')).first();
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

export class ServiceOutletUpdatePage {
  pageTitle = element(by.id('jhi-service-outlet-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  outletCodeInput = element(by.id('field_outletCode'));
  outletNameInput = element(by.id('field_outletName'));
  townInput = element(by.id('field_town'));
  parliamentaryConstituencyInput = element(by.id('field_parliamentaryConstituency'));
  gpsCoordinatesInput = element(by.id('field_gpsCoordinates'));
  outletOpeningDateInput = element(by.id('field_outletOpeningDate'));
  regulatorApprovalDateInput = element(by.id('field_regulatorApprovalDate'));
  outletClosureDateInput = element(by.id('field_outletClosureDate'));
  dateLastModifiedInput = element(by.id('field_dateLastModified'));
  licenseFeePayableInput = element(by.id('field_licenseFeePayable'));

  placeholderSelect = element(by.id('field_placeholder'));
  bankCodeSelect = element(by.id('field_bankCode'));
  outletTypeSelect = element(by.id('field_outletType'));
  outletStatusSelect = element(by.id('field_outletStatus'));
  countyNameSelect = element(by.id('field_countyName'));
  subCountyNameSelect = element(by.id('field_subCountyName'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setOutletCodeInput(outletCode: string): Promise<void> {
    await this.outletCodeInput.sendKeys(outletCode);
  }

  async getOutletCodeInput(): Promise<string> {
    return await this.outletCodeInput.getAttribute('value');
  }

  async setOutletNameInput(outletName: string): Promise<void> {
    await this.outletNameInput.sendKeys(outletName);
  }

  async getOutletNameInput(): Promise<string> {
    return await this.outletNameInput.getAttribute('value');
  }

  async setTownInput(town: string): Promise<void> {
    await this.townInput.sendKeys(town);
  }

  async getTownInput(): Promise<string> {
    return await this.townInput.getAttribute('value');
  }

  async setParliamentaryConstituencyInput(parliamentaryConstituency: string): Promise<void> {
    await this.parliamentaryConstituencyInput.sendKeys(parliamentaryConstituency);
  }

  async getParliamentaryConstituencyInput(): Promise<string> {
    return await this.parliamentaryConstituencyInput.getAttribute('value');
  }

  async setGpsCoordinatesInput(gpsCoordinates: string): Promise<void> {
    await this.gpsCoordinatesInput.sendKeys(gpsCoordinates);
  }

  async getGpsCoordinatesInput(): Promise<string> {
    return await this.gpsCoordinatesInput.getAttribute('value');
  }

  async setOutletOpeningDateInput(outletOpeningDate: string): Promise<void> {
    await this.outletOpeningDateInput.sendKeys(outletOpeningDate);
  }

  async getOutletOpeningDateInput(): Promise<string> {
    return await this.outletOpeningDateInput.getAttribute('value');
  }

  async setRegulatorApprovalDateInput(regulatorApprovalDate: string): Promise<void> {
    await this.regulatorApprovalDateInput.sendKeys(regulatorApprovalDate);
  }

  async getRegulatorApprovalDateInput(): Promise<string> {
    return await this.regulatorApprovalDateInput.getAttribute('value');
  }

  async setOutletClosureDateInput(outletClosureDate: string): Promise<void> {
    await this.outletClosureDateInput.sendKeys(outletClosureDate);
  }

  async getOutletClosureDateInput(): Promise<string> {
    return await this.outletClosureDateInput.getAttribute('value');
  }

  async setDateLastModifiedInput(dateLastModified: string): Promise<void> {
    await this.dateLastModifiedInput.sendKeys(dateLastModified);
  }

  async getDateLastModifiedInput(): Promise<string> {
    return await this.dateLastModifiedInput.getAttribute('value');
  }

  async setLicenseFeePayableInput(licenseFeePayable: string): Promise<void> {
    await this.licenseFeePayableInput.sendKeys(licenseFeePayable);
  }

  async getLicenseFeePayableInput(): Promise<string> {
    return await this.licenseFeePayableInput.getAttribute('value');
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

  async outletTypeSelectLastOption(): Promise<void> {
    await this.outletTypeSelect.all(by.tagName('option')).last().click();
  }

  async outletTypeSelectOption(option: string): Promise<void> {
    await this.outletTypeSelect.sendKeys(option);
  }

  getOutletTypeSelect(): ElementFinder {
    return this.outletTypeSelect;
  }

  async getOutletTypeSelectedOption(): Promise<string> {
    return await this.outletTypeSelect.element(by.css('option:checked')).getText();
  }

  async outletStatusSelectLastOption(): Promise<void> {
    await this.outletStatusSelect.all(by.tagName('option')).last().click();
  }

  async outletStatusSelectOption(option: string): Promise<void> {
    await this.outletStatusSelect.sendKeys(option);
  }

  getOutletStatusSelect(): ElementFinder {
    return this.outletStatusSelect;
  }

  async getOutletStatusSelectedOption(): Promise<string> {
    return await this.outletStatusSelect.element(by.css('option:checked')).getText();
  }

  async countyNameSelectLastOption(): Promise<void> {
    await this.countyNameSelect.all(by.tagName('option')).last().click();
  }

  async countyNameSelectOption(option: string): Promise<void> {
    await this.countyNameSelect.sendKeys(option);
  }

  getCountyNameSelect(): ElementFinder {
    return this.countyNameSelect;
  }

  async getCountyNameSelectedOption(): Promise<string> {
    return await this.countyNameSelect.element(by.css('option:checked')).getText();
  }

  async subCountyNameSelectLastOption(): Promise<void> {
    await this.subCountyNameSelect.all(by.tagName('option')).last().click();
  }

  async subCountyNameSelectOption(option: string): Promise<void> {
    await this.subCountyNameSelect.sendKeys(option);
  }

  getSubCountyNameSelect(): ElementFinder {
    return this.subCountyNameSelect;
  }

  async getSubCountyNameSelectedOption(): Promise<string> {
    return await this.subCountyNameSelect.element(by.css('option:checked')).getText();
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

export class ServiceOutletDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-serviceOutlet-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-serviceOutlet'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
