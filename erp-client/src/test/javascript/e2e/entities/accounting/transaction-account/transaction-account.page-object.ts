import { element, by, ElementFinder } from 'protractor';

export class TransactionAccountComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-transaction-account div table .btn-danger'));
  title = element.all(by.css('jhi-transaction-account div h2#page-heading span')).first();
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

export class TransactionAccountUpdatePage {
  pageTitle = element(by.id('jhi-transaction-account-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  accountNumberInput = element(by.id('field_accountNumber'));
  accountNameInput = element(by.id('field_accountName'));
  notesInput = element(by.id('file_notes'));
  accountTypeSelect = element(by.id('field_accountType'));
  accountSubTypeSelect = element(by.id('field_accountSubType'));
  dummyAccountInput = element(by.id('field_dummyAccount'));

  accountLedgerSelect = element(by.id('field_accountLedger'));
  accountCategorySelect = element(by.id('field_accountCategory'));
  placeholderSelect = element(by.id('field_placeholder'));
  serviceOutletSelect = element(by.id('field_serviceOutlet'));
  settlementCurrencySelect = element(by.id('field_settlementCurrency'));
  institutionSelect = element(by.id('field_institution'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAccountNumberInput(accountNumber: string): Promise<void> {
    await this.accountNumberInput.sendKeys(accountNumber);
  }

  async getAccountNumberInput(): Promise<string> {
    return await this.accountNumberInput.getAttribute('value');
  }

  async setAccountNameInput(accountName: string): Promise<void> {
    await this.accountNameInput.sendKeys(accountName);
  }

  async getAccountNameInput(): Promise<string> {
    return await this.accountNameInput.getAttribute('value');
  }

  async setNotesInput(notes: string): Promise<void> {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput(): Promise<string> {
    return await this.notesInput.getAttribute('value');
  }

  async setAccountTypeSelect(accountType: string): Promise<void> {
    await this.accountTypeSelect.sendKeys(accountType);
  }

  async getAccountTypeSelect(): Promise<string> {
    return await this.accountTypeSelect.element(by.css('option:checked')).getText();
  }

  async accountTypeSelectLastOption(): Promise<void> {
    await this.accountTypeSelect.all(by.tagName('option')).last().click();
  }

  async setAccountSubTypeSelect(accountSubType: string): Promise<void> {
    await this.accountSubTypeSelect.sendKeys(accountSubType);
  }

  async getAccountSubTypeSelect(): Promise<string> {
    return await this.accountSubTypeSelect.element(by.css('option:checked')).getText();
  }

  async accountSubTypeSelectLastOption(): Promise<void> {
    await this.accountSubTypeSelect.all(by.tagName('option')).last().click();
  }

  getDummyAccountInput(): ElementFinder {
    return this.dummyAccountInput;
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

  async accountCategorySelectLastOption(): Promise<void> {
    await this.accountCategorySelect.all(by.tagName('option')).last().click();
  }

  async accountCategorySelectOption(option: string): Promise<void> {
    await this.accountCategorySelect.sendKeys(option);
  }

  getAccountCategorySelect(): ElementFinder {
    return this.accountCategorySelect;
  }

  async getAccountCategorySelectedOption(): Promise<string> {
    return await this.accountCategorySelect.element(by.css('option:checked')).getText();
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

  async serviceOutletSelectLastOption(): Promise<void> {
    await this.serviceOutletSelect.all(by.tagName('option')).last().click();
  }

  async serviceOutletSelectOption(option: string): Promise<void> {
    await this.serviceOutletSelect.sendKeys(option);
  }

  getServiceOutletSelect(): ElementFinder {
    return this.serviceOutletSelect;
  }

  async getServiceOutletSelectedOption(): Promise<string> {
    return await this.serviceOutletSelect.element(by.css('option:checked')).getText();
  }

  async settlementCurrencySelectLastOption(): Promise<void> {
    await this.settlementCurrencySelect.all(by.tagName('option')).last().click();
  }

  async settlementCurrencySelectOption(option: string): Promise<void> {
    await this.settlementCurrencySelect.sendKeys(option);
  }

  getSettlementCurrencySelect(): ElementFinder {
    return this.settlementCurrencySelect;
  }

  async getSettlementCurrencySelectedOption(): Promise<string> {
    return await this.settlementCurrencySelect.element(by.css('option:checked')).getText();
  }

  async institutionSelectLastOption(): Promise<void> {
    await this.institutionSelect.all(by.tagName('option')).last().click();
  }

  async institutionSelectOption(option: string): Promise<void> {
    await this.institutionSelect.sendKeys(option);
  }

  getInstitutionSelect(): ElementFinder {
    return this.institutionSelect;
  }

  async getInstitutionSelectedOption(): Promise<string> {
    return await this.institutionSelect.element(by.css('option:checked')).getText();
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

export class TransactionAccountDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-transactionAccount-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-transactionAccount'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
