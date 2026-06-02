import { element, by, ElementFinder } from 'protractor';

export class TransactionAccountPostingProcessTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-transaction-account-posting-process-type div table .btn-danger'));
  title = element.all(by.css('jhi-transaction-account-posting-process-type div h2#page-heading span')).first();
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

export class TransactionAccountPostingProcessTypeUpdatePage {
  pageTitle = element(by.id('jhi-transaction-account-posting-process-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));

  debitAccountTypeSelect = element(by.id('field_debitAccountType'));
  creditAccountTypeSelect = element(by.id('field_creditAccountType'));
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

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async debitAccountTypeSelectLastOption(): Promise<void> {
    await this.debitAccountTypeSelect.all(by.tagName('option')).last().click();
  }

  async debitAccountTypeSelectOption(option: string): Promise<void> {
    await this.debitAccountTypeSelect.sendKeys(option);
  }

  getDebitAccountTypeSelect(): ElementFinder {
    return this.debitAccountTypeSelect;
  }

  async getDebitAccountTypeSelectedOption(): Promise<string> {
    return await this.debitAccountTypeSelect.element(by.css('option:checked')).getText();
  }

  async creditAccountTypeSelectLastOption(): Promise<void> {
    await this.creditAccountTypeSelect.all(by.tagName('option')).last().click();
  }

  async creditAccountTypeSelectOption(option: string): Promise<void> {
    await this.creditAccountTypeSelect.sendKeys(option);
  }

  getCreditAccountTypeSelect(): ElementFinder {
    return this.creditAccountTypeSelect;
  }

  async getCreditAccountTypeSelectedOption(): Promise<string> {
    return await this.creditAccountTypeSelect.element(by.css('option:checked')).getText();
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

export class TransactionAccountPostingProcessTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-transactionAccountPostingProcessType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-transactionAccountPostingProcessType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
