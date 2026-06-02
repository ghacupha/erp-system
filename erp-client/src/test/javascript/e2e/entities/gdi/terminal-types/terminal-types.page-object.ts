import { element, by, ElementFinder } from 'protractor';

export class TerminalTypesComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-terminal-types div table .btn-danger'));
  title = element.all(by.css('jhi-terminal-types div h2#page-heading span')).first();
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

export class TerminalTypesUpdatePage {
  pageTitle = element(by.id('jhi-terminal-types-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  txnTerminalTypeCodeInput = element(by.id('field_txnTerminalTypeCode'));
  txnChannelTypeInput = element(by.id('field_txnChannelType'));
  txnChannelTypeDetailsInput = element(by.id('field_txnChannelTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setTxnTerminalTypeCodeInput(txnTerminalTypeCode: string): Promise<void> {
    await this.txnTerminalTypeCodeInput.sendKeys(txnTerminalTypeCode);
  }

  async getTxnTerminalTypeCodeInput(): Promise<string> {
    return await this.txnTerminalTypeCodeInput.getAttribute('value');
  }

  async setTxnChannelTypeInput(txnChannelType: string): Promise<void> {
    await this.txnChannelTypeInput.sendKeys(txnChannelType);
  }

  async getTxnChannelTypeInput(): Promise<string> {
    return await this.txnChannelTypeInput.getAttribute('value');
  }

  async setTxnChannelTypeDetailsInput(txnChannelTypeDetails: string): Promise<void> {
    await this.txnChannelTypeDetailsInput.sendKeys(txnChannelTypeDetails);
  }

  async getTxnChannelTypeDetailsInput(): Promise<string> {
    return await this.txnChannelTypeDetailsInput.getAttribute('value');
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

export class TerminalTypesDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-terminalTypes-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-terminalTypes'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
