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

export class TerminalsAndPOSComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-terminals-and-pos div table .btn-danger'));
  title = element.all(by.css('jhi-terminals-and-pos div h2#page-heading span')).first();
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

export class TerminalsAndPOSUpdatePage {
  pageTitle = element(by.id('jhi-terminals-and-pos-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  terminalIdInput = element(by.id('field_terminalId'));
  merchantIdInput = element(by.id('field_merchantId'));
  terminalNameInput = element(by.id('field_terminalName'));
  terminalLocationInput = element(by.id('field_terminalLocation'));
  iso6709LatituteInput = element(by.id('field_iso6709Latitute'));
  iso6709LongitudeInput = element(by.id('field_iso6709Longitude'));
  terminalOpeningDateInput = element(by.id('field_terminalOpeningDate'));
  terminalClosureDateInput = element(by.id('field_terminalClosureDate'));

  terminalTypeSelect = element(by.id('field_terminalType'));
  terminalFunctionalitySelect = element(by.id('field_terminalFunctionality'));
  physicalLocationSelect = element(by.id('field_physicalLocation'));
  bankIdSelect = element(by.id('field_bankId'));
  branchIdSelect = element(by.id('field_branchId'));

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

  async setTerminalIdInput(terminalId: string): Promise<void> {
    await this.terminalIdInput.sendKeys(terminalId);
  }

  async getTerminalIdInput(): Promise<string> {
    return await this.terminalIdInput.getAttribute('value');
  }

  async setMerchantIdInput(merchantId: string): Promise<void> {
    await this.merchantIdInput.sendKeys(merchantId);
  }

  async getMerchantIdInput(): Promise<string> {
    return await this.merchantIdInput.getAttribute('value');
  }

  async setTerminalNameInput(terminalName: string): Promise<void> {
    await this.terminalNameInput.sendKeys(terminalName);
  }

  async getTerminalNameInput(): Promise<string> {
    return await this.terminalNameInput.getAttribute('value');
  }

  async setTerminalLocationInput(terminalLocation: string): Promise<void> {
    await this.terminalLocationInput.sendKeys(terminalLocation);
  }

  async getTerminalLocationInput(): Promise<string> {
    return await this.terminalLocationInput.getAttribute('value');
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

  async setTerminalOpeningDateInput(terminalOpeningDate: string): Promise<void> {
    await this.terminalOpeningDateInput.sendKeys(terminalOpeningDate);
  }

  async getTerminalOpeningDateInput(): Promise<string> {
    return await this.terminalOpeningDateInput.getAttribute('value');
  }

  async setTerminalClosureDateInput(terminalClosureDate: string): Promise<void> {
    await this.terminalClosureDateInput.sendKeys(terminalClosureDate);
  }

  async getTerminalClosureDateInput(): Promise<string> {
    return await this.terminalClosureDateInput.getAttribute('value');
  }

  async terminalTypeSelectLastOption(): Promise<void> {
    await this.terminalTypeSelect.all(by.tagName('option')).last().click();
  }

  async terminalTypeSelectOption(option: string): Promise<void> {
    await this.terminalTypeSelect.sendKeys(option);
  }

  getTerminalTypeSelect(): ElementFinder {
    return this.terminalTypeSelect;
  }

  async getTerminalTypeSelectedOption(): Promise<string> {
    return await this.terminalTypeSelect.element(by.css('option:checked')).getText();
  }

  async terminalFunctionalitySelectLastOption(): Promise<void> {
    await this.terminalFunctionalitySelect.all(by.tagName('option')).last().click();
  }

  async terminalFunctionalitySelectOption(option: string): Promise<void> {
    await this.terminalFunctionalitySelect.sendKeys(option);
  }

  getTerminalFunctionalitySelect(): ElementFinder {
    return this.terminalFunctionalitySelect;
  }

  async getTerminalFunctionalitySelectedOption(): Promise<string> {
    return await this.terminalFunctionalitySelect.element(by.css('option:checked')).getText();
  }

  async physicalLocationSelectLastOption(): Promise<void> {
    await this.physicalLocationSelect.all(by.tagName('option')).last().click();
  }

  async physicalLocationSelectOption(option: string): Promise<void> {
    await this.physicalLocationSelect.sendKeys(option);
  }

  getPhysicalLocationSelect(): ElementFinder {
    return this.physicalLocationSelect;
  }

  async getPhysicalLocationSelectedOption(): Promise<string> {
    return await this.physicalLocationSelect.element(by.css('option:checked')).getText();
  }

  async bankIdSelectLastOption(): Promise<void> {
    await this.bankIdSelect.all(by.tagName('option')).last().click();
  }

  async bankIdSelectOption(option: string): Promise<void> {
    await this.bankIdSelect.sendKeys(option);
  }

  getBankIdSelect(): ElementFinder {
    return this.bankIdSelect;
  }

  async getBankIdSelectedOption(): Promise<string> {
    return await this.bankIdSelect.element(by.css('option:checked')).getText();
  }

  async branchIdSelectLastOption(): Promise<void> {
    await this.branchIdSelect.all(by.tagName('option')).last().click();
  }

  async branchIdSelectOption(option: string): Promise<void> {
    await this.branchIdSelect.sendKeys(option);
  }

  getBranchIdSelect(): ElementFinder {
    return this.branchIdSelect;
  }

  async getBranchIdSelectedOption(): Promise<string> {
    return await this.branchIdSelect.element(by.css('option:checked')).getText();
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

export class TerminalsAndPOSDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-terminalsAndPOS-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-terminalsAndPOS'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
