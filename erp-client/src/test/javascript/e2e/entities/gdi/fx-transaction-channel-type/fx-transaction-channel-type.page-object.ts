import { element, by, ElementFinder } from 'protractor';

export class FxTransactionChannelTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fx-transaction-channel-type div table .btn-danger'));
  title = element.all(by.css('jhi-fx-transaction-channel-type div h2#page-heading span')).first();
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

export class FxTransactionChannelTypeUpdatePage {
  pageTitle = element(by.id('jhi-fx-transaction-channel-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  fxTransactionChannelCodeInput = element(by.id('field_fxTransactionChannelCode'));
  fxTransactionChannelTypeInput = element(by.id('field_fxTransactionChannelType'));
  fxChannelTypeDetailsInput = element(by.id('field_fxChannelTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFxTransactionChannelCodeInput(fxTransactionChannelCode: string): Promise<void> {
    await this.fxTransactionChannelCodeInput.sendKeys(fxTransactionChannelCode);
  }

  async getFxTransactionChannelCodeInput(): Promise<string> {
    return await this.fxTransactionChannelCodeInput.getAttribute('value');
  }

  async setFxTransactionChannelTypeInput(fxTransactionChannelType: string): Promise<void> {
    await this.fxTransactionChannelTypeInput.sendKeys(fxTransactionChannelType);
  }

  async getFxTransactionChannelTypeInput(): Promise<string> {
    return await this.fxTransactionChannelTypeInput.getAttribute('value');
  }

  async setFxChannelTypeDetailsInput(fxChannelTypeDetails: string): Promise<void> {
    await this.fxChannelTypeDetailsInput.sendKeys(fxChannelTypeDetails);
  }

  async getFxChannelTypeDetailsInput(): Promise<string> {
    return await this.fxChannelTypeDetailsInput.getAttribute('value');
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

export class FxTransactionChannelTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fxTransactionChannelType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fxTransactionChannelType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
