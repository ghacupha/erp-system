import { element, by, ElementFinder } from 'protractor';

export class TerminalFunctionsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-terminal-functions div table .btn-danger'));
  title = element.all(by.css('jhi-terminal-functions div h2#page-heading span')).first();
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

export class TerminalFunctionsUpdatePage {
  pageTitle = element(by.id('jhi-terminal-functions-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  functionCodeInput = element(by.id('field_functionCode'));
  terminalFunctionalityInput = element(by.id('field_terminalFunctionality'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFunctionCodeInput(functionCode: string): Promise<void> {
    await this.functionCodeInput.sendKeys(functionCode);
  }

  async getFunctionCodeInput(): Promise<string> {
    return await this.functionCodeInput.getAttribute('value');
  }

  async setTerminalFunctionalityInput(terminalFunctionality: string): Promise<void> {
    await this.terminalFunctionalityInput.sendKeys(terminalFunctionality);
  }

  async getTerminalFunctionalityInput(): Promise<string> {
    return await this.terminalFunctionalityInput.getAttribute('value');
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

export class TerminalFunctionsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-terminalFunctions-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-terminalFunctions'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
