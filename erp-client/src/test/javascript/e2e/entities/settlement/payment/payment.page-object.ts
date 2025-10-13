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

export class PaymentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-payment div table .btn-danger'));
  title = element.all(by.css('jhi-payment div h2#page-heading span')).first();
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

export class PaymentUpdatePage {
  pageTitle = element(by.id('jhi-payment-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  paymentNumberInput = element(by.id('field_paymentNumber'));
  paymentDateInput = element(by.id('field_paymentDate'));
  invoicedAmountInput = element(by.id('field_invoicedAmount'));
  paymentAmountInput = element(by.id('field_paymentAmount'));
  descriptionInput = element(by.id('field_description'));
  settlementCurrencySelect = element(by.id('field_settlementCurrency'));
  calculationFileInput = element(by.id('file_calculationFile'));
  dealerNameInput = element(by.id('field_dealerName'));
  purchaseOrderNumberInput = element(by.id('field_purchaseOrderNumber'));
  fileUploadTokenInput = element(by.id('field_fileUploadToken'));
  compilationTokenInput = element(by.id('field_compilationToken'));

  paymentLabelSelect = element(by.id('field_paymentLabel'));
  paymentCategorySelect = element(by.id('field_paymentCategory'));
  placeholderSelect = element(by.id('field_placeholder'));
  paymentGroupSelect = element(by.id('field_paymentGroup'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setPaymentNumberInput(paymentNumber: string): Promise<void> {
    await this.paymentNumberInput.sendKeys(paymentNumber);
  }

  async getPaymentNumberInput(): Promise<string> {
    return await this.paymentNumberInput.getAttribute('value');
  }

  async setPaymentDateInput(paymentDate: string): Promise<void> {
    await this.paymentDateInput.sendKeys(paymentDate);
  }

  async getPaymentDateInput(): Promise<string> {
    return await this.paymentDateInput.getAttribute('value');
  }

  async setInvoicedAmountInput(invoicedAmount: string): Promise<void> {
    await this.invoicedAmountInput.sendKeys(invoicedAmount);
  }

  async getInvoicedAmountInput(): Promise<string> {
    return await this.invoicedAmountInput.getAttribute('value');
  }

  async setPaymentAmountInput(paymentAmount: string): Promise<void> {
    await this.paymentAmountInput.sendKeys(paymentAmount);
  }

  async getPaymentAmountInput(): Promise<string> {
    return await this.paymentAmountInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setSettlementCurrencySelect(settlementCurrency: string): Promise<void> {
    await this.settlementCurrencySelect.sendKeys(settlementCurrency);
  }

  async getSettlementCurrencySelect(): Promise<string> {
    return await this.settlementCurrencySelect.element(by.css('option:checked')).getText();
  }

  async settlementCurrencySelectLastOption(): Promise<void> {
    await this.settlementCurrencySelect.all(by.tagName('option')).last().click();
  }

  async setCalculationFileInput(calculationFile: string): Promise<void> {
    await this.calculationFileInput.sendKeys(calculationFile);
  }

  async getCalculationFileInput(): Promise<string> {
    return await this.calculationFileInput.getAttribute('value');
  }

  async setDealerNameInput(dealerName: string): Promise<void> {
    await this.dealerNameInput.sendKeys(dealerName);
  }

  async getDealerNameInput(): Promise<string> {
    return await this.dealerNameInput.getAttribute('value');
  }

  async setPurchaseOrderNumberInput(purchaseOrderNumber: string): Promise<void> {
    await this.purchaseOrderNumberInput.sendKeys(purchaseOrderNumber);
  }

  async getPurchaseOrderNumberInput(): Promise<string> {
    return await this.purchaseOrderNumberInput.getAttribute('value');
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

  async paymentCategorySelectLastOption(): Promise<void> {
    await this.paymentCategorySelect.all(by.tagName('option')).last().click();
  }

  async paymentCategorySelectOption(option: string): Promise<void> {
    await this.paymentCategorySelect.sendKeys(option);
  }

  getPaymentCategorySelect(): ElementFinder {
    return this.paymentCategorySelect;
  }

  async getPaymentCategorySelectedOption(): Promise<string> {
    return await this.paymentCategorySelect.element(by.css('option:checked')).getText();
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

  async paymentGroupSelectLastOption(): Promise<void> {
    await this.paymentGroupSelect.all(by.tagName('option')).last().click();
  }

  async paymentGroupSelectOption(option: string): Promise<void> {
    await this.paymentGroupSelect.sendKeys(option);
  }

  getPaymentGroupSelect(): ElementFinder {
    return this.paymentGroupSelect;
  }

  async getPaymentGroupSelectedOption(): Promise<string> {
    return await this.paymentGroupSelect.element(by.css('option:checked')).getText();
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

export class PaymentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-payment-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-payment'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
