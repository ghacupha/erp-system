import { element, by, ElementFinder } from 'protractor';

export class CustomerIDDocumentTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-customer-id-document-type div table .btn-danger'));
  title = element.all(by.css('jhi-customer-id-document-type div h2#page-heading span')).first();
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

export class CustomerIDDocumentTypeUpdatePage {
  pageTitle = element(by.id('jhi-customer-id-document-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  documentCodeInput = element(by.id('field_documentCode'));
  documentTypeInput = element(by.id('field_documentType'));
  documentTypeDescriptionInput = element(by.id('field_documentTypeDescription'));

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

  async setDocumentCodeInput(documentCode: string): Promise<void> {
    await this.documentCodeInput.sendKeys(documentCode);
  }

  async getDocumentCodeInput(): Promise<string> {
    return await this.documentCodeInput.getAttribute('value');
  }

  async setDocumentTypeInput(documentType: string): Promise<void> {
    await this.documentTypeInput.sendKeys(documentType);
  }

  async getDocumentTypeInput(): Promise<string> {
    return await this.documentTypeInput.getAttribute('value');
  }

  async setDocumentTypeDescriptionInput(documentTypeDescription: string): Promise<void> {
    await this.documentTypeDescriptionInput.sendKeys(documentTypeDescription);
  }

  async getDocumentTypeDescriptionInput(): Promise<string> {
    return await this.documentTypeDescriptionInput.getAttribute('value');
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

export class CustomerIDDocumentTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-customerIDDocumentType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-customerIDDocumentType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
