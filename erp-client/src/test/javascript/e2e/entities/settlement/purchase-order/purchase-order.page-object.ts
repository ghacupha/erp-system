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

export class PurchaseOrderComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-purchase-order div table .btn-danger'));
  title = element.all(by.css('jhi-purchase-order div h2#page-heading span')).first();
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

export class PurchaseOrderUpdatePage {
  pageTitle = element(by.id('jhi-purchase-order-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  purchaseOrderNumberInput = element(by.id('field_purchaseOrderNumber'));
  purchaseOrderDateInput = element(by.id('field_purchaseOrderDate'));
  purchaseOrderAmountInput = element(by.id('field_purchaseOrderAmount'));
  descriptionInput = element(by.id('field_description'));
  notesInput = element(by.id('field_notes'));
  fileUploadTokenInput = element(by.id('field_fileUploadToken'));
  compilationTokenInput = element(by.id('field_compilationToken'));
  remarksInput = element(by.id('field_remarks'));

  settlementCurrencySelect = element(by.id('field_settlementCurrency'));
  placeholderSelect = element(by.id('field_placeholder'));
  signatoriesSelect = element(by.id('field_signatories'));
  vendorSelect = element(by.id('field_vendor'));
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

  async setPurchaseOrderNumberInput(purchaseOrderNumber: string): Promise<void> {
    await this.purchaseOrderNumberInput.sendKeys(purchaseOrderNumber);
  }

  async getPurchaseOrderNumberInput(): Promise<string> {
    return await this.purchaseOrderNumberInput.getAttribute('value');
  }

  async setPurchaseOrderDateInput(purchaseOrderDate: string): Promise<void> {
    await this.purchaseOrderDateInput.sendKeys(purchaseOrderDate);
  }

  async getPurchaseOrderDateInput(): Promise<string> {
    return await this.purchaseOrderDateInput.getAttribute('value');
  }

  async setPurchaseOrderAmountInput(purchaseOrderAmount: string): Promise<void> {
    await this.purchaseOrderAmountInput.sendKeys(purchaseOrderAmount);
  }

  async getPurchaseOrderAmountInput(): Promise<string> {
    return await this.purchaseOrderAmountInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setNotesInput(notes: string): Promise<void> {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput(): Promise<string> {
    return await this.notesInput.getAttribute('value');
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

  async setRemarksInput(remarks: string): Promise<void> {
    await this.remarksInput.sendKeys(remarks);
  }

  async getRemarksInput(): Promise<string> {
    return await this.remarksInput.getAttribute('value');
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

  async vendorSelectLastOption(): Promise<void> {
    await this.vendorSelect.all(by.tagName('option')).last().click();
  }

  async vendorSelectOption(option: string): Promise<void> {
    await this.vendorSelect.sendKeys(option);
  }

  getVendorSelect(): ElementFinder {
    return this.vendorSelect;
  }

  async getVendorSelectedOption(): Promise<string> {
    return await this.vendorSelect.element(by.css('option:checked')).getText();
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

export class PurchaseOrderDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-purchaseOrder-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-purchaseOrder'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
