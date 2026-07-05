import { element, by, ElementFinder } from 'protractor';

export class FxTransactionTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fx-transaction-type div table .btn-danger'));
  title = element.all(by.css('jhi-fx-transaction-type div h2#page-heading span')).first();
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

export class FxTransactionTypeUpdatePage {
  pageTitle = element(by.id('jhi-fx-transaction-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  fxTransactionTypeCodeInput = element(by.id('field_fxTransactionTypeCode'));
  fxTransactionTypeInput = element(by.id('field_fxTransactionType'));
  fxTransactionTypeDescriptionInput = element(by.id('field_fxTransactionTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFxTransactionTypeCodeInput(fxTransactionTypeCode: string): Promise<void> {
    await this.fxTransactionTypeCodeInput.sendKeys(fxTransactionTypeCode);
  }

  async getFxTransactionTypeCodeInput(): Promise<string> {
    return await this.fxTransactionTypeCodeInput.getAttribute('value');
  }

  async setFxTransactionTypeInput(fxTransactionType: string): Promise<void> {
    await this.fxTransactionTypeInput.sendKeys(fxTransactionType);
  }

  async getFxTransactionTypeInput(): Promise<string> {
    return await this.fxTransactionTypeInput.getAttribute('value');
  }

  async setFxTransactionTypeDescriptionInput(fxTransactionTypeDescription: string): Promise<void> {
    await this.fxTransactionTypeDescriptionInput.sendKeys(fxTransactionTypeDescription);
  }

  async getFxTransactionTypeDescriptionInput(): Promise<string> {
    return await this.fxTransactionTypeDescriptionInput.getAttribute('value');
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

export class FxTransactionTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fxTransactionType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fxTransactionType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
