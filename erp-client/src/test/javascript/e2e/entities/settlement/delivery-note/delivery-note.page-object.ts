import { element, by, ElementFinder } from 'protractor';

export class DeliveryNoteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-delivery-note div table .btn-danger'));
  title = element.all(by.css('jhi-delivery-note div h2#page-heading span')).first();
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

export class DeliveryNoteUpdatePage {
  pageTitle = element(by.id('jhi-delivery-note-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  deliveryNoteNumberInput = element(by.id('field_deliveryNoteNumber'));
  documentDateInput = element(by.id('field_documentDate'));
  descriptionInput = element(by.id('field_description'));
  serialNumberInput = element(by.id('field_serialNumber'));
  quantityInput = element(by.id('field_quantity'));
  remarksInput = element(by.id('field_remarks'));

  placeholderSelect = element(by.id('field_placeholder'));
  receivedBySelect = element(by.id('field_receivedBy'));
  deliveryStampsSelect = element(by.id('field_deliveryStamps'));
  purchaseOrderSelect = element(by.id('field_purchaseOrder'));
  supplierSelect = element(by.id('field_supplier'));
  signatoriesSelect = element(by.id('field_signatories'));
  otherPurchaseOrdersSelect = element(by.id('field_otherPurchaseOrders'));
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

  async setDeliveryNoteNumberInput(deliveryNoteNumber: string): Promise<void> {
    await this.deliveryNoteNumberInput.sendKeys(deliveryNoteNumber);
  }

  async getDeliveryNoteNumberInput(): Promise<string> {
    return await this.deliveryNoteNumberInput.getAttribute('value');
  }

  async setDocumentDateInput(documentDate: string): Promise<void> {
    await this.documentDateInput.sendKeys(documentDate);
  }

  async getDocumentDateInput(): Promise<string> {
    return await this.documentDateInput.getAttribute('value');
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

  async setQuantityInput(quantity: string): Promise<void> {
    await this.quantityInput.sendKeys(quantity);
  }

  async getQuantityInput(): Promise<string> {
    return await this.quantityInput.getAttribute('value');
  }

  async setRemarksInput(remarks: string): Promise<void> {
    await this.remarksInput.sendKeys(remarks);
  }

  async getRemarksInput(): Promise<string> {
    return await this.remarksInput.getAttribute('value');
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

  async receivedBySelectLastOption(): Promise<void> {
    await this.receivedBySelect.all(by.tagName('option')).last().click();
  }

  async receivedBySelectOption(option: string): Promise<void> {
    await this.receivedBySelect.sendKeys(option);
  }

  getReceivedBySelect(): ElementFinder {
    return this.receivedBySelect;
  }

  async getReceivedBySelectedOption(): Promise<string> {
    return await this.receivedBySelect.element(by.css('option:checked')).getText();
  }

  async deliveryStampsSelectLastOption(): Promise<void> {
    await this.deliveryStampsSelect.all(by.tagName('option')).last().click();
  }

  async deliveryStampsSelectOption(option: string): Promise<void> {
    await this.deliveryStampsSelect.sendKeys(option);
  }

  getDeliveryStampsSelect(): ElementFinder {
    return this.deliveryStampsSelect;
  }

  async getDeliveryStampsSelectedOption(): Promise<string> {
    return await this.deliveryStampsSelect.element(by.css('option:checked')).getText();
  }

  async purchaseOrderSelectLastOption(): Promise<void> {
    await this.purchaseOrderSelect.all(by.tagName('option')).last().click();
  }

  async purchaseOrderSelectOption(option: string): Promise<void> {
    await this.purchaseOrderSelect.sendKeys(option);
  }

  getPurchaseOrderSelect(): ElementFinder {
    return this.purchaseOrderSelect;
  }

  async getPurchaseOrderSelectedOption(): Promise<string> {
    return await this.purchaseOrderSelect.element(by.css('option:checked')).getText();
  }

  async supplierSelectLastOption(): Promise<void> {
    await this.supplierSelect.all(by.tagName('option')).last().click();
  }

  async supplierSelectOption(option: string): Promise<void> {
    await this.supplierSelect.sendKeys(option);
  }

  getSupplierSelect(): ElementFinder {
    return this.supplierSelect;
  }

  async getSupplierSelectedOption(): Promise<string> {
    return await this.supplierSelect.element(by.css('option:checked')).getText();
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

  async otherPurchaseOrdersSelectLastOption(): Promise<void> {
    await this.otherPurchaseOrdersSelect.all(by.tagName('option')).last().click();
  }

  async otherPurchaseOrdersSelectOption(option: string): Promise<void> {
    await this.otherPurchaseOrdersSelect.sendKeys(option);
  }

  getOtherPurchaseOrdersSelect(): ElementFinder {
    return this.otherPurchaseOrdersSelect;
  }

  async getOtherPurchaseOrdersSelectedOption(): Promise<string> {
    return await this.otherPurchaseOrdersSelect.element(by.css('option:checked')).getText();
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

export class DeliveryNoteDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-deliveryNote-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-deliveryNote'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
