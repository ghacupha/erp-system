import { element, by, ElementFinder } from 'protractor';

export class PaymentCalculationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-payment-calculation div table .btn-danger'));
  title = element.all(by.css('jhi-payment-calculation div h2#page-heading span')).first();
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
  pageTitle = element(by.id('jhi-payment-calculation-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  paymentNumberInput = element(by.id('field_paymentNumber'));
  paymentDateInput = element(by.id('field_paymentDate'));
  paymentCategoryInput = element(by.id('field_paymentCategory'));
  paymentExpenseInput = element(by.id('field_paymentExpense'));
  withholdingVATInput = element(by.id('field_withholdingVAT'));
  withholdingTaxInput = element(by.id('field_withholdingTax'));
  paymentAmountInput = element(by.id('field_paymentAmount'));

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

  async setPaymentCategoryInput(paymentCategory: string): Promise<void> {
    await this.paymentCategoryInput.sendKeys(paymentCategory);
  }

  async getPaymentCategoryInput(): Promise<string> {
    return await this.paymentCategoryInput.getAttribute('value');
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
  private dialogTitle = element(by.id('jhi-delete-paymentCalculation-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-paymentCalculation'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}