import { element, by, ElementFinder } from 'protractor';

export class TransactionAccountCategoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-transaction-account-category div table .btn-danger'));
  title = element.all(by.css('jhi-transaction-account-category div h2#page-heading span')).first();
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

export class TransactionAccountCategoryUpdatePage {
  pageTitle = element(by.id('jhi-transaction-account-category-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  transactionAccountPostingTypeSelect = element(by.id('field_transactionAccountPostingType'));

  placeholderSelect = element(by.id('field_placeholder'));
  accountLedgerSelect = element(by.id('field_accountLedger'));

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

  async setTransactionAccountPostingTypeSelect(transactionAccountPostingType: string): Promise<void> {
    await this.transactionAccountPostingTypeSelect.sendKeys(transactionAccountPostingType);
  }

  async getTransactionAccountPostingTypeSelect(): Promise<string> {
    return await this.transactionAccountPostingTypeSelect.element(by.css('option:checked')).getText();
  }

  async transactionAccountPostingTypeSelectLastOption(): Promise<void> {
    await this.transactionAccountPostingTypeSelect.all(by.tagName('option')).last().click();
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

  async accountLedgerSelectLastOption(): Promise<void> {
    await this.accountLedgerSelect.all(by.tagName('option')).last().click();
  }

  async accountLedgerSelectOption(option: string): Promise<void> {
    await this.accountLedgerSelect.sendKeys(option);
  }

  getAccountLedgerSelect(): ElementFinder {
    return this.accountLedgerSelect;
  }

  async getAccountLedgerSelectedOption(): Promise<string> {
    return await this.accountLedgerSelect.element(by.css('option:checked')).getText();
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

export class TransactionAccountCategoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-transactionAccountCategory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-transactionAccountCategory'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
