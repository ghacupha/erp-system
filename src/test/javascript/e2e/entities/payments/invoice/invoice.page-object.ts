import { element, by, ElementFinder } from 'protractor';

export class InvoiceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gha-invoice div table .btn-danger'));
  title = element.all(by.css('gha-invoice div h2#page-heading span')).first();
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

export class InvoiceUpdatePage {
  pageTitle = element(by.id('gha-invoice-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  invoiceNumberInput = element(by.id('field_invoiceNumber'));
  invoiceDateInput = element(by.id('field_invoiceDate'));
  invoiceAmountInput = element(by.id('field_invoiceAmount'));
  currencySelect = element(by.id('field_currency'));
  conversionRateInput = element(by.id('field_conversionRate'));

  paymentSelect = element(by.id('field_payment'));
  dealerSelect = element(by.id('field_dealer'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setInvoiceNumberInput(invoiceNumber: string): Promise<void> {
    await this.invoiceNumberInput.sendKeys(invoiceNumber);
  }

  async getInvoiceNumberInput(): Promise<string> {
    return await this.invoiceNumberInput.getAttribute('value');
  }

  async setInvoiceDateInput(invoiceDate: string): Promise<void> {
    await this.invoiceDateInput.sendKeys(invoiceDate);
  }

  async getInvoiceDateInput(): Promise<string> {
    return await this.invoiceDateInput.getAttribute('value');
  }

  async setInvoiceAmountInput(invoiceAmount: string): Promise<void> {
    await this.invoiceAmountInput.sendKeys(invoiceAmount);
  }

  async getInvoiceAmountInput(): Promise<string> {
    return await this.invoiceAmountInput.getAttribute('value');
  }

  async setCurrencySelect(currency: string): Promise<void> {
    await this.currencySelect.sendKeys(currency);
  }

  async getCurrencySelect(): Promise<string> {
    return await this.currencySelect.element(by.css('option:checked')).getText();
  }

  async currencySelectLastOption(): Promise<void> {
    await this.currencySelect.all(by.tagName('option')).last().click();
  }

  async setConversionRateInput(conversionRate: string): Promise<void> {
    await this.conversionRateInput.sendKeys(conversionRate);
  }

  async getConversionRateInput(): Promise<string> {
    return await this.conversionRateInput.getAttribute('value');
  }

  async paymentSelectLastOption(): Promise<void> {
    await this.paymentSelect.all(by.tagName('option')).last().click();
  }

  async paymentSelectOption(option: string): Promise<void> {
    await this.paymentSelect.sendKeys(option);
  }

  getPaymentSelect(): ElementFinder {
    return this.paymentSelect;
  }

  async getPaymentSelectedOption(): Promise<string> {
    return await this.paymentSelect.element(by.css('option:checked')).getText();
  }

  async dealerSelectLastOption(): Promise<void> {
    await this.dealerSelect.all(by.tagName('option')).last().click();
  }

  async dealerSelectOption(option: string): Promise<void> {
    await this.dealerSelect.sendKeys(option);
  }

  getDealerSelect(): ElementFinder {
    return this.dealerSelect;
  }

  async getDealerSelectedOption(): Promise<string> {
    return await this.dealerSelect.element(by.css('option:checked')).getText();
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

export class InvoiceDeleteDialog {
  private dialogTitle = element(by.id('gha-delete-invoice-heading'));
  private confirmButton = element(by.id('gha-confirm-delete-invoice'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
