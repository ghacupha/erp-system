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

export class JobSheetComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-job-sheet div table .btn-danger'));
  title = element.all(by.css('jhi-job-sheet div h2#page-heading span')).first();
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

export class JobSheetUpdatePage {
  pageTitle = element(by.id('jhi-job-sheet-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  serialNumberInput = element(by.id('field_serialNumber'));
  jobSheetDateInput = element(by.id('field_jobSheetDate'));
  detailsInput = element(by.id('field_details'));
  remarksInput = element(by.id('field_remarks'));

  billerSelect = element(by.id('field_biller'));
  signatoriesSelect = element(by.id('field_signatories'));
  contactPersonSelect = element(by.id('field_contactPerson'));
  businessStampsSelect = element(by.id('field_businessStamps'));
  placeholderSelect = element(by.id('field_placeholder'));
  paymentLabelSelect = element(by.id('field_paymentLabel'));
  businessDocumentSelect = element(by.id('field_businessDocument'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSerialNumberInput(serialNumber: string): Promise<void> {
    await this.serialNumberInput.sendKeys(serialNumber);
  }

  async getSerialNumberInput(): Promise<string> {
    return await this.serialNumberInput.getAttribute('value');
  }

  async setJobSheetDateInput(jobSheetDate: string): Promise<void> {
    await this.jobSheetDateInput.sendKeys(jobSheetDate);
  }

  async getJobSheetDateInput(): Promise<string> {
    return await this.jobSheetDateInput.getAttribute('value');
  }

  async setDetailsInput(details: string): Promise<void> {
    await this.detailsInput.sendKeys(details);
  }

  async getDetailsInput(): Promise<string> {
    return await this.detailsInput.getAttribute('value');
  }

  async setRemarksInput(remarks: string): Promise<void> {
    await this.remarksInput.sendKeys(remarks);
  }

  async getRemarksInput(): Promise<string> {
    return await this.remarksInput.getAttribute('value');
  }

  async billerSelectLastOption(): Promise<void> {
    await this.billerSelect.all(by.tagName('option')).last().click();
  }

  async billerSelectOption(option: string): Promise<void> {
    await this.billerSelect.sendKeys(option);
  }

  getBillerSelect(): ElementFinder {
    return this.billerSelect;
  }

  async getBillerSelectedOption(): Promise<string> {
    return await this.billerSelect.element(by.css('option:checked')).getText();
  }

  async signatoriesSelectLastOption(): Promise<void> {
    await this.signatoriesSelect.all(by.tagName('option')).last().click();
  }

  async signatoriesSelectOption(option: string): Promise<void> {
    await this.signatoriesSelect.sendKeys(option);
  }

  getSignatoriesSelect(): ElementFinder {
    return this.signatoriesSelect;
  }

  async getSignatoriesSelectedOption(): Promise<string> {
    return await this.signatoriesSelect.element(by.css('option:checked')).getText();
  }

  async contactPersonSelectLastOption(): Promise<void> {
    await this.contactPersonSelect.all(by.tagName('option')).last().click();
  }

  async contactPersonSelectOption(option: string): Promise<void> {
    await this.contactPersonSelect.sendKeys(option);
  }

  getContactPersonSelect(): ElementFinder {
    return this.contactPersonSelect;
  }

  async getContactPersonSelectedOption(): Promise<string> {
    return await this.contactPersonSelect.element(by.css('option:checked')).getText();
  }

  async businessStampsSelectLastOption(): Promise<void> {
    await this.businessStampsSelect.all(by.tagName('option')).last().click();
  }

  async businessStampsSelectOption(option: string): Promise<void> {
    await this.businessStampsSelect.sendKeys(option);
  }

  getBusinessStampsSelect(): ElementFinder {
    return this.businessStampsSelect;
  }

  async getBusinessStampsSelectedOption(): Promise<string> {
    return await this.businessStampsSelect.element(by.css('option:checked')).getText();
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

  async paymentLabelSelectLastOption(): Promise<void> {
    await this.paymentLabelSelect.all(by.tagName('option')).last().click();
  }

  async paymentLabelSelectOption(option: string): Promise<void> {
    await this.paymentLabelSelect.sendKeys(option);
  }

  getPaymentLabelSelect(): ElementFinder {
    return this.paymentLabelSelect;
  }

  async getPaymentLabelSelectedOption(): Promise<string> {
    return await this.paymentLabelSelect.element(by.css('option:checked')).getText();
  }

  async businessDocumentSelectLastOption(): Promise<void> {
    await this.businessDocumentSelect.all(by.tagName('option')).last().click();
  }

  async businessDocumentSelectOption(option: string): Promise<void> {
    await this.businessDocumentSelect.sendKeys(option);
  }

  getBusinessDocumentSelect(): ElementFinder {
    return this.businessDocumentSelect;
  }

  async getBusinessDocumentSelectedOption(): Promise<string> {
    return await this.businessDocumentSelect.element(by.css('option:checked')).getText();
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

export class JobSheetDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-jobSheet-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-jobSheet'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
