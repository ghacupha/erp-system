import { element, by, ElementFinder } from 'protractor';

export class PaymentCalculationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gha-payment-calculation div table .btn-danger'));
  title = element.all(by.css('gha-payment-calculation div h2#page-heading span')).first();
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

export class PaymentCalculationUpdatePage {
  pageTitle = element(by.id('gha-payment-calculation-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  paymentExpenseInput = element(by.id('field_paymentExpense'));
  withholdingVATInput = element(by.id('field_withholdingVAT'));
  withholdingTaxInput = element(by.id('field_withholdingTax'));
  paymentAmountInput = element(by.id('field_paymentAmount'));

  paymentCategorySelect = element(by.id('field_paymentCategory'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setPaymentExpenseInput(paymentExpense: string): Promise<void> {
    await this.paymentExpenseInput.sendKeys(paymentExpense);
  }

  async getPaymentExpenseInput(): Promise<string> {
    return await this.paymentExpenseInput.getAttribute('value');
  }

  async setWithholdingVATInput(withholdingVAT: string): Promise<void> {
    await this.withholdingVATInput.sendKeys(withholdingVAT);
  }

  async getWithholdingVATInput(): Promise<string> {
    return await this.withholdingVATInput.getAttribute('value');
  }

  async setWithholdingTaxInput(withholdingTax: string): Promise<void> {
    await this.withholdingTaxInput.sendKeys(withholdingTax);
  }

  async getWithholdingTaxInput(): Promise<string> {
    return await this.withholdingTaxInput.getAttribute('value');
  }

  async setPaymentAmountInput(paymentAmount: string): Promise<void> {
    await this.paymentAmountInput.sendKeys(paymentAmount);
  }

  async getPaymentAmountInput(): Promise<string> {
    return await this.paymentAmountInput.getAttribute('value');
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

export class PaymentCalculationDeleteDialog {
  private dialogTitle = element(by.id('gha-delete-paymentCalculation-heading'));
  private confirmButton = element(by.id('gha-confirm-delete-paymentCalculation'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
