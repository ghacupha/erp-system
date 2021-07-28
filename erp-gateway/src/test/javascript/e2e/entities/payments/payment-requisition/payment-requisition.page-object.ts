import { element, by, ElementFinder } from 'protractor';

export class PaymentRequisitionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-payment-requisition div table .btn-danger'));
  title = element.all(by.css('jhi-payment-requisition div h2#page-heading span')).first();
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

export class PaymentRequisitionUpdatePage {
  pageTitle = element(by.id('jhi-payment-requisition-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  dealerNameInput = element(by.id('field_dealerName'));
  invoicedAmountInput = element(by.id('field_invoicedAmount'));
  disbursementCostInput = element(by.id('field_disbursementCost'));
  vatableAmountInput = element(by.id('field_vatableAmount'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setDealerNameInput(dealerName: string): Promise<void> {
    await this.dealerNameInput.sendKeys(dealerName);
  }

  async getDealerNameInput(): Promise<string> {
    return await this.dealerNameInput.getAttribute('value');
  }

  async setInvoicedAmountInput(invoicedAmount: string): Promise<void> {
    await this.invoicedAmountInput.sendKeys(invoicedAmount);
  }

  async getInvoicedAmountInput(): Promise<string> {
    return await this.invoicedAmountInput.getAttribute('value');
  }

  async setDisbursementCostInput(disbursementCost: string): Promise<void> {
    await this.disbursementCostInput.sendKeys(disbursementCost);
  }

  async getDisbursementCostInput(): Promise<string> {
    return await this.disbursementCostInput.getAttribute('value');
  }

  async setVatableAmountInput(vatableAmount: string): Promise<void> {
    await this.vatableAmountInput.sendKeys(vatableAmount);
  }

  async getVatableAmountInput(): Promise<string> {
    return await this.vatableAmountInput.getAttribute('value');
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

export class PaymentRequisitionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-paymentRequisition-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-paymentRequisition'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
