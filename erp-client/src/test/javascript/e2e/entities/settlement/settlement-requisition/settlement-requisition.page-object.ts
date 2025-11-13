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

export class SettlementRequisitionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-settlement-requisition div table .btn-danger'));
  title = element.all(by.css('jhi-settlement-requisition div h2#page-heading span')).first();
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

export class SettlementRequisitionUpdatePage {
  pageTitle = element(by.id('jhi-settlement-requisition-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  descriptionInput = element(by.id('field_description'));
  serialNumberInput = element(by.id('field_serialNumber'));
  timeOfRequisitionInput = element(by.id('field_timeOfRequisition'));
  requisitionNumberInput = element(by.id('field_requisitionNumber'));
  paymentAmountInput = element(by.id('field_paymentAmount'));
  paymentStatusSelect = element(by.id('field_paymentStatus'));

  settlementCurrencySelect = element(by.id('field_settlementCurrency'));
  currentOwnerSelect = element(by.id('field_currentOwner'));
  nativeOwnerSelect = element(by.id('field_nativeOwner'));
  nativeDepartmentSelect = element(by.id('field_nativeDepartment'));
  billerSelect = element(by.id('field_biller'));
  paymentInvoiceSelect = element(by.id('field_paymentInvoice'));
  deliveryNoteSelect = element(by.id('field_deliveryNote'));
  jobSheetSelect = element(by.id('field_jobSheet'));
  signaturesSelect = element(by.id('field_signatures'));
  businessDocumentSelect = element(by.id('field_businessDocument'));
  applicationMappingSelect = element(by.id('field_applicationMapping'));
  placeholderSelect = element(by.id('field_placeholder'));
  settlementSelect = element(by.id('field_settlement'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setSerialNumberInput(serialNumber: string): Promise<void> {
    await this.serialNumberInput.sendKeys(serialNumber);
  }

  async getSerialNumberInput(): Promise<string> {
    return await this.serialNumberInput.getAttribute('value');
  }

  async setTimeOfRequisitionInput(timeOfRequisition: string): Promise<void> {
    await this.timeOfRequisitionInput.sendKeys(timeOfRequisition);
  }

  async getTimeOfRequisitionInput(): Promise<string> {
    return await this.timeOfRequisitionInput.getAttribute('value');
  }

  async setRequisitionNumberInput(requisitionNumber: string): Promise<void> {
    await this.requisitionNumberInput.sendKeys(requisitionNumber);
  }

  async getRequisitionNumberInput(): Promise<string> {
    return await this.requisitionNumberInput.getAttribute('value');
  }

  async setPaymentAmountInput(paymentAmount: string): Promise<void> {
    await this.paymentAmountInput.sendKeys(paymentAmount);
  }

  async getPaymentAmountInput(): Promise<string> {
    return await this.paymentAmountInput.getAttribute('value');
  }

  async setPaymentStatusSelect(paymentStatus: string): Promise<void> {
    await this.paymentStatusSelect.sendKeys(paymentStatus);
  }

  async getPaymentStatusSelect(): Promise<string> {
    return await this.paymentStatusSelect.element(by.css('option:checked')).getText();
  }

  async paymentStatusSelectLastOption(): Promise<void> {
    await this.paymentStatusSelect.all(by.tagName('option')).last().click();
  }

  async settlementCurrencySelectLastOption(): Promise<void> {
    await this.settlementCurrencySelect.all(by.tagName('option')).last().click();
  }

  async settlementCurrencySelectOption(option: string): Promise<void> {
    await this.settlementCurrencySelect.sendKeys(option);
  }

  getSettlementCurrencySelect(): ElementFinder {
    return this.settlementCurrencySelect;
  }

  async getSettlementCurrencySelectedOption(): Promise<string> {
    return await this.settlementCurrencySelect.element(by.css('option:checked')).getText();
  }

  async currentOwnerSelectLastOption(): Promise<void> {
    await this.currentOwnerSelect.all(by.tagName('option')).last().click();
  }

  async currentOwnerSelectOption(option: string): Promise<void> {
    await this.currentOwnerSelect.sendKeys(option);
  }

  getCurrentOwnerSelect(): ElementFinder {
    return this.currentOwnerSelect;
  }

  async getCurrentOwnerSelectedOption(): Promise<string> {
    return await this.currentOwnerSelect.element(by.css('option:checked')).getText();
  }

  async nativeOwnerSelectLastOption(): Promise<void> {
    await this.nativeOwnerSelect.all(by.tagName('option')).last().click();
  }

  async nativeOwnerSelectOption(option: string): Promise<void> {
    await this.nativeOwnerSelect.sendKeys(option);
  }

  getNativeOwnerSelect(): ElementFinder {
    return this.nativeOwnerSelect;
  }

  async getNativeOwnerSelectedOption(): Promise<string> {
    return await this.nativeOwnerSelect.element(by.css('option:checked')).getText();
  }

  async nativeDepartmentSelectLastOption(): Promise<void> {
    await this.nativeDepartmentSelect.all(by.tagName('option')).last().click();
  }

  async nativeDepartmentSelectOption(option: string): Promise<void> {
    await this.nativeDepartmentSelect.sendKeys(option);
  }

  getNativeDepartmentSelect(): ElementFinder {
    return this.nativeDepartmentSelect;
  }

  async getNativeDepartmentSelectedOption(): Promise<string> {
    return await this.nativeDepartmentSelect.element(by.css('option:checked')).getText();
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

  async paymentInvoiceSelectLastOption(): Promise<void> {
    await this.paymentInvoiceSelect.all(by.tagName('option')).last().click();
  }

  async paymentInvoiceSelectOption(option: string): Promise<void> {
    await this.paymentInvoiceSelect.sendKeys(option);
  }

  getPaymentInvoiceSelect(): ElementFinder {
    return this.paymentInvoiceSelect;
  }

  async getPaymentInvoiceSelectedOption(): Promise<string> {
    return await this.paymentInvoiceSelect.element(by.css('option:checked')).getText();
  }

  async deliveryNoteSelectLastOption(): Promise<void> {
    await this.deliveryNoteSelect.all(by.tagName('option')).last().click();
  }

  async deliveryNoteSelectOption(option: string): Promise<void> {
    await this.deliveryNoteSelect.sendKeys(option);
  }

  getDeliveryNoteSelect(): ElementFinder {
    return this.deliveryNoteSelect;
  }

  async getDeliveryNoteSelectedOption(): Promise<string> {
    return await this.deliveryNoteSelect.element(by.css('option:checked')).getText();
  }

  async jobSheetSelectLastOption(): Promise<void> {
    await this.jobSheetSelect.all(by.tagName('option')).last().click();
  }

  async jobSheetSelectOption(option: string): Promise<void> {
    await this.jobSheetSelect.sendKeys(option);
  }

  getJobSheetSelect(): ElementFinder {
    return this.jobSheetSelect;
  }

  async getJobSheetSelectedOption(): Promise<string> {
    return await this.jobSheetSelect.element(by.css('option:checked')).getText();
  }

  async signaturesSelectLastOption(): Promise<void> {
    await this.signaturesSelect.all(by.tagName('option')).last().click();
  }

  async signaturesSelectOption(option: string): Promise<void> {
    await this.signaturesSelect.sendKeys(option);
  }

  getSignaturesSelect(): ElementFinder {
    return this.signaturesSelect;
  }

  async getSignaturesSelectedOption(): Promise<string> {
    return await this.signaturesSelect.element(by.css('option:checked')).getText();
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

  async applicationMappingSelectLastOption(): Promise<void> {
    await this.applicationMappingSelect.all(by.tagName('option')).last().click();
  }

  async applicationMappingSelectOption(option: string): Promise<void> {
    await this.applicationMappingSelect.sendKeys(option);
  }

  getApplicationMappingSelect(): ElementFinder {
    return this.applicationMappingSelect;
  }

  async getApplicationMappingSelectedOption(): Promise<string> {
    return await this.applicationMappingSelect.element(by.css('option:checked')).getText();
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

  async settlementSelectLastOption(): Promise<void> {
    await this.settlementSelect.all(by.tagName('option')).last().click();
  }

  async settlementSelectOption(option: string): Promise<void> {
    await this.settlementSelect.sendKeys(option);
  }

  getSettlementSelect(): ElementFinder {
    return this.settlementSelect;
  }

  async getSettlementSelectedOption(): Promise<string> {
    return await this.settlementSelect.element(by.css('option:checked')).getText();
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

export class SettlementRequisitionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-settlementRequisition-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-settlementRequisition'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
