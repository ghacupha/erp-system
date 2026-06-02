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

  idInput = element(by.id('field_id'));
  receptionDateInput = element(by.id('field_receptionDate'));
  dealerNameInput = element(by.id('field_dealerName'));
  briefDescriptionInput = element(by.id('field_briefDescription'));
  requisitionNumberInput = element(by.id('field_requisitionNumber'));
  invoicedAmountInput = element(by.id('field_invoicedAmount'));
  disbursementCostInput = element(by.id('field_disbursementCost'));
  taxableAmountInput = element(by.id('field_taxableAmount'));
  requisitionProcessedInput = element(by.id('field_requisitionProcessed'));
  fileUploadTokenInput = element(by.id('field_fileUploadToken'));
  compilationTokenInput = element(by.id('field_compilationToken'));

  paymentLabelSelect = element(by.id('field_paymentLabel'));
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

  async setReceptionDateInput(receptionDate: string): Promise<void> {
    await this.receptionDateInput.sendKeys(receptionDate);
  }

  async getReceptionDateInput(): Promise<string> {
    return await this.receptionDateInput.getAttribute('value');
  }

  async setDealerNameInput(dealerName: string): Promise<void> {
    await this.dealerNameInput.sendKeys(dealerName);
  }

  async getDealerNameInput(): Promise<string> {
    return await this.dealerNameInput.getAttribute('value');
  }

  async setBriefDescriptionInput(briefDescription: string): Promise<void> {
    await this.briefDescriptionInput.sendKeys(briefDescription);
  }

  async getBriefDescriptionInput(): Promise<string> {
    return await this.briefDescriptionInput.getAttribute('value');
  }

  async setRequisitionNumberInput(requisitionNumber: string): Promise<void> {
    await this.requisitionNumberInput.sendKeys(requisitionNumber);
  }

  async getRequisitionNumberInput(): Promise<string> {
    return await this.requisitionNumberInput.getAttribute('value');
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

  async setTaxableAmountInput(taxableAmount: string): Promise<void> {
    await this.taxableAmountInput.sendKeys(taxableAmount);
  }

  async getTaxableAmountInput(): Promise<string> {
    return await this.taxableAmountInput.getAttribute('value');
  }

  getRequisitionProcessedInput(): ElementFinder {
    return this.requisitionProcessedInput;
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
