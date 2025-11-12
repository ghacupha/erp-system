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

export class ParticularsOfOutletComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-particulars-of-outlet div table .btn-danger'));
  title = element.all(by.css('jhi-particulars-of-outlet div h2#page-heading span')).first();
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

export class ParticularsOfOutletUpdatePage {
  pageTitle = element(by.id('jhi-particulars-of-outlet-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  businessReportingDateInput = element(by.id('field_businessReportingDate'));
  outletNameInput = element(by.id('field_outletName'));
  townInput = element(by.id('field_town'));
  iso6709LatituteInput = element(by.id('field_iso6709Latitute'));
  iso6709LongitudeInput = element(by.id('field_iso6709Longitude'));
  cbkApprovalDateInput = element(by.id('field_cbkApprovalDate'));
  outletOpeningDateInput = element(by.id('field_outletOpeningDate'));
  outletClosureDateInput = element(by.id('field_outletClosureDate'));
  licenseFeePayableInput = element(by.id('field_licenseFeePayable'));

  subCountyCodeSelect = element(by.id('field_subCountyCode'));
  bankCodeSelect = element(by.id('field_bankCode'));
  outletIdSelect = element(by.id('field_outletId'));
  typeOfOutletSelect = element(by.id('field_typeOfOutlet'));
  outletStatusSelect = element(by.id('field_outletStatus'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setBusinessReportingDateInput(businessReportingDate: string): Promise<void> {
    await this.businessReportingDateInput.sendKeys(businessReportingDate);
  }

  async getBusinessReportingDateInput(): Promise<string> {
    return await this.businessReportingDateInput.getAttribute('value');
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

  async setIso6709LatituteInput(iso6709Latitute: string): Promise<void> {
    await this.iso6709LatituteInput.sendKeys(iso6709Latitute);
  }

  async getIso6709LatituteInput(): Promise<string> {
    return await this.iso6709LatituteInput.getAttribute('value');
  }

  async setIso6709LongitudeInput(iso6709Longitude: string): Promise<void> {
    await this.iso6709LongitudeInput.sendKeys(iso6709Longitude);
  }

  async getIso6709LongitudeInput(): Promise<string> {
    return await this.iso6709LongitudeInput.getAttribute('value');
  }

  async setCbkApprovalDateInput(cbkApprovalDate: string): Promise<void> {
    await this.cbkApprovalDateInput.sendKeys(cbkApprovalDate);
  }

  async getCbkApprovalDateInput(): Promise<string> {
    return await this.cbkApprovalDateInput.getAttribute('value');
  }

  async setOutletOpeningDateInput(outletOpeningDate: string): Promise<void> {
    await this.outletOpeningDateInput.sendKeys(outletOpeningDate);
  }

  async getOutletOpeningDateInput(): Promise<string> {
    return await this.outletOpeningDateInput.getAttribute('value');
  }

  async setOutletClosureDateInput(outletClosureDate: string): Promise<void> {
    await this.outletClosureDateInput.sendKeys(outletClosureDate);
  }

  async getOutletClosureDateInput(): Promise<string> {
    return await this.outletClosureDateInput.getAttribute('value');
  }

  async setLicenseFeePayableInput(licenseFeePayable: string): Promise<void> {
    await this.licenseFeePayableInput.sendKeys(licenseFeePayable);
  }

  async getLicenseFeePayableInput(): Promise<string> {
    return await this.licenseFeePayableInput.getAttribute('value');
  }

  async subCountyCodeSelectLastOption(): Promise<void> {
    await this.subCountyCodeSelect.all(by.tagName('option')).last().click();
  }

  async subCountyCodeSelectOption(option: string): Promise<void> {
    await this.subCountyCodeSelect.sendKeys(option);
  }

  getSubCountyCodeSelect(): ElementFinder {
    return this.subCountyCodeSelect;
  }

  async getSubCountyCodeSelectedOption(): Promise<string> {
    return await this.subCountyCodeSelect.element(by.css('option:checked')).getText();
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

  async outletIdSelectLastOption(): Promise<void> {
    await this.outletIdSelect.all(by.tagName('option')).last().click();
  }

  async outletIdSelectOption(option: string): Promise<void> {
    await this.outletIdSelect.sendKeys(option);
  }

  getOutletIdSelect(): ElementFinder {
    return this.outletIdSelect;
  }

  async getOutletIdSelectedOption(): Promise<string> {
    return await this.outletIdSelect.element(by.css('option:checked')).getText();
  }

  async typeOfOutletSelectLastOption(): Promise<void> {
    await this.typeOfOutletSelect.all(by.tagName('option')).last().click();
  }

  async typeOfOutletSelectOption(option: string): Promise<void> {
    await this.typeOfOutletSelect.sendKeys(option);
  }

  getTypeOfOutletSelect(): ElementFinder {
    return this.typeOfOutletSelect;
  }

  async getTypeOfOutletSelectedOption(): Promise<string> {
    return await this.typeOfOutletSelect.element(by.css('option:checked')).getText();
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

export class ParticularsOfOutletDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-particularsOfOutlet-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-particularsOfOutlet'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
