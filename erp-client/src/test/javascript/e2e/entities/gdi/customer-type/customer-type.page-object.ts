import { element, by, ElementFinder } from 'protractor';

export class CustomerTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-customer-type div table .btn-danger'));
  title = element.all(by.css('jhi-customer-type div h2#page-heading span')).first();
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

export class CustomerTypeUpdatePage {
  pageTitle = element(by.id('jhi-customer-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  customerTypeCodeInput = element(by.id('field_customerTypeCode'));
  customerTypeInput = element(by.id('field_customerType'));
  customerTypeDescriptionInput = element(by.id('field_customerTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCustomerTypeCodeInput(customerTypeCode: string): Promise<void> {
    await this.customerTypeCodeInput.sendKeys(customerTypeCode);
  }

  async getCustomerTypeCodeInput(): Promise<string> {
    return await this.customerTypeCodeInput.getAttribute('value');
  }

  async setCustomerTypeInput(customerType: string): Promise<void> {
    await this.customerTypeInput.sendKeys(customerType);
  }

  async getCustomerTypeInput(): Promise<string> {
    return await this.customerTypeInput.getAttribute('value');
  }

  async setCustomerTypeDescriptionInput(customerTypeDescription: string): Promise<void> {
    await this.customerTypeDescriptionInput.sendKeys(customerTypeDescription);
  }

  async getCustomerTypeDescriptionInput(): Promise<string> {
    return await this.customerTypeDescriptionInput.getAttribute('value');
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

export class CustomerTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-customerType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-customerType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
