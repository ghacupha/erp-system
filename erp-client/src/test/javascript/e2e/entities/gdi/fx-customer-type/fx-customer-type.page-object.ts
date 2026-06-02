import { element, by, ElementFinder } from 'protractor';

export class FxCustomerTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fx-customer-type div table .btn-danger'));
  title = element.all(by.css('jhi-fx-customer-type div h2#page-heading span')).first();
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

export class FxCustomerTypeUpdatePage {
  pageTitle = element(by.id('jhi-fx-customer-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  foreignExchangeCustomerTypeCodeInput = element(by.id('field_foreignExchangeCustomerTypeCode'));
  foreignCustomerTypeInput = element(by.id('field_foreignCustomerType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setForeignExchangeCustomerTypeCodeInput(foreignExchangeCustomerTypeCode: string): Promise<void> {
    await this.foreignExchangeCustomerTypeCodeInput.sendKeys(foreignExchangeCustomerTypeCode);
  }

  async getForeignExchangeCustomerTypeCodeInput(): Promise<string> {
    return await this.foreignExchangeCustomerTypeCodeInput.getAttribute('value');
  }

  async setForeignCustomerTypeInput(foreignCustomerType: string): Promise<void> {
    await this.foreignCustomerTypeInput.sendKeys(foreignCustomerType);
  }

  async getForeignCustomerTypeInput(): Promise<string> {
    return await this.foreignCustomerTypeInput.getAttribute('value');
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

export class FxCustomerTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fxCustomerType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fxCustomerType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
