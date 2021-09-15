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
  paymentAmountInput = element(by.id('field_paymentAmount'));
  descriptionInput = element(by.id('field_description'));
  currencySelect = element(by.id('field_currency'));
  conversionRateInput = element(by.id('field_conversionRate'));

  paymentCategorySelect = element(by.id('field_paymentCategory'));
  taxRuleSelect = element(by.id('field_taxRule'));
  paymentCalculationSelect = element(by.id('field_paymentCalculation'));
  paymentRequisitionSelect = element(by.id('field_paymentRequisition'));
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

  async taxRuleSelectLastOption(): Promise<void> {
    await this.taxRuleSelect.all(by.tagName('option')).last().click();
  }

  async taxRuleSelectOption(option: string): Promise<void> {
    await this.taxRuleSelect.sendKeys(option);
  }

  getTaxRuleSelect(): ElementFinder {
    return this.taxRuleSelect;
  }

  async getTaxRuleSelectedOption(): Promise<string> {
    return await this.taxRuleSelect.element(by.css('option:checked')).getText();
  }

  async paymentCalculationSelectLastOption(): Promise<void> {
    await this.paymentCalculationSelect.all(by.tagName('option')).last().click();
  }

  async paymentCalculationSelectOption(option: string): Promise<void> {
    await this.paymentCalculationSelect.sendKeys(option);
  }

  getPaymentCalculationSelect(): ElementFinder {
    return this.paymentCalculationSelect;
  }

  async getPaymentCalculationSelectedOption(): Promise<string> {
    return await this.paymentCalculationSelect.element(by.css('option:checked')).getText();
  }

  async paymentRequisitionSelectLastOption(): Promise<void> {
    await this.paymentRequisitionSelect.all(by.tagName('option')).last().click();
  }

  async paymentRequisitionSelectOption(option: string): Promise<void> {
    await this.paymentRequisitionSelect.sendKeys(option);
  }

  getPaymentRequisitionSelect(): ElementFinder {
    return this.paymentRequisitionSelect;
  }

  async getPaymentRequisitionSelectedOption(): Promise<string> {
    return await this.paymentRequisitionSelect.element(by.css('option:checked')).getText();
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
