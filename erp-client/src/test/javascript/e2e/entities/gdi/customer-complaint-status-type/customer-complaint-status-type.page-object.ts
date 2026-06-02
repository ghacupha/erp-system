import { element, by, ElementFinder } from 'protractor';

export class CustomerComplaintStatusTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-customer-complaint-status-type div table .btn-danger'));
  title = element.all(by.css('jhi-customer-complaint-status-type div h2#page-heading span')).first();
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

export class CustomerComplaintStatusTypeUpdatePage {
  pageTitle = element(by.id('jhi-customer-complaint-status-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  customerComplaintStatusTypeCodeInput = element(by.id('field_customerComplaintStatusTypeCode'));
  customerComplaintStatusTypeInput = element(by.id('field_customerComplaintStatusType'));
  customerComplaintStatusTypeDetailsInput = element(by.id('field_customerComplaintStatusTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCustomerComplaintStatusTypeCodeInput(customerComplaintStatusTypeCode: string): Promise<void> {
    await this.customerComplaintStatusTypeCodeInput.sendKeys(customerComplaintStatusTypeCode);
  }

  async getCustomerComplaintStatusTypeCodeInput(): Promise<string> {
    return await this.customerComplaintStatusTypeCodeInput.getAttribute('value');
  }

  async setCustomerComplaintStatusTypeInput(customerComplaintStatusType: string): Promise<void> {
    await this.customerComplaintStatusTypeInput.sendKeys(customerComplaintStatusType);
  }

  async getCustomerComplaintStatusTypeInput(): Promise<string> {
    return await this.customerComplaintStatusTypeInput.getAttribute('value');
  }

  async setCustomerComplaintStatusTypeDetailsInput(customerComplaintStatusTypeDetails: string): Promise<void> {
    await this.customerComplaintStatusTypeDetailsInput.sendKeys(customerComplaintStatusTypeDetails);
  }

  async getCustomerComplaintStatusTypeDetailsInput(): Promise<string> {
    return await this.customerComplaintStatusTypeDetailsInput.getAttribute('value');
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

export class CustomerComplaintStatusTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-customerComplaintStatusType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-customerComplaintStatusType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
