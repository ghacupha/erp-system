import { element, by, ElementFinder } from 'protractor';

export class AccountBalanceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-account-balance div table .btn-danger'));
  title = element.all(by.css('jhi-account-balance div h2#page-heading span')).first();
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

export class AccountBalanceUpdatePage {
  pageTitle = element(by.id('jhi-account-balance-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  customerIdInput = element(by.id('field_customerId'));
  accountContractNumberInput = element(by.id('field_accountContractNumber'));
  accruedInterestBalanceFCYInput = element(by.id('field_accruedInterestBalanceFCY'));
  accruedInterestBalanceLCYInput = element(by.id('field_accruedInterestBalanceLCY'));
  accountBalanceFCYInput = element(by.id('field_accountBalanceFCY'));
  accountBalanceLCYInput = element(by.id('field_accountBalanceLCY'));

  bankCodeSelect = element(by.id('field_bankCode'));
  branchIdSelect = element(by.id('field_branchId'));
  currencyCodeSelect = element(by.id('field_currencyCode'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setReportingDateInput(reportingDate: string): Promise<void> {
    await this.reportingDateInput.sendKeys(reportingDate);
  }

  async getReportingDateInput(): Promise<string> {
    return await this.reportingDateInput.getAttribute('value');
  }

  async setCustomerIdInput(customerId: string): Promise<void> {
    await this.customerIdInput.sendKeys(customerId);
  }

  async getCustomerIdInput(): Promise<string> {
    return await this.customerIdInput.getAttribute('value');
  }

  async setAccountContractNumberInput(accountContractNumber: string): Promise<void> {
    await this.accountContractNumberInput.sendKeys(accountContractNumber);
  }

  async getAccountContractNumberInput(): Promise<string> {
    return await this.accountContractNumberInput.getAttribute('value');
  }

  async setAccruedInterestBalanceFCYInput(accruedInterestBalanceFCY: string): Promise<void> {
    await this.accruedInterestBalanceFCYInput.sendKeys(accruedInterestBalanceFCY);
  }

  async getAccruedInterestBalanceFCYInput(): Promise<string> {
    return await this.accruedInterestBalanceFCYInput.getAttribute('value');
  }

  async setAccruedInterestBalanceLCYInput(accruedInterestBalanceLCY: string): Promise<void> {
    await this.accruedInterestBalanceLCYInput.sendKeys(accruedInterestBalanceLCY);
  }

  async getAccruedInterestBalanceLCYInput(): Promise<string> {
    return await this.accruedInterestBalanceLCYInput.getAttribute('value');
  }

  async setAccountBalanceFCYInput(accountBalanceFCY: string): Promise<void> {
    await this.accountBalanceFCYInput.sendKeys(accountBalanceFCY);
  }

  async getAccountBalanceFCYInput(): Promise<string> {
    return await this.accountBalanceFCYInput.getAttribute('value');
  }

  async setAccountBalanceLCYInput(accountBalanceLCY: string): Promise<void> {
    await this.accountBalanceLCYInput.sendKeys(accountBalanceLCY);
  }

  async getAccountBalanceLCYInput(): Promise<string> {
    return await this.accountBalanceLCYInput.getAttribute('value');
  }

  async bankCodeSelectLastOption(): Promise<void> {
    await this.bankCodeSelect.all(by.tagName('option')).last().click();
  }

  async bankCodeSelectOption(option: string): Promise<void> {
    await this.bankCodeSelect.sendKeys(option);
  }

  getBankCodeSelect(): ElementFinder {
    return this.bankCodeSelect;
  }

  async getBankCodeSelectedOption(): Promise<string> {
    return await this.bankCodeSelect.element(by.css('option:checked')).getText();
  }

  async branchIdSelectLastOption(): Promise<void> {
    await this.branchIdSelect.all(by.tagName('option')).last().click();
  }

  async branchIdSelectOption(option: string): Promise<void> {
    await this.branchIdSelect.sendKeys(option);
  }

  getBranchIdSelect(): ElementFinder {
    return this.branchIdSelect;
  }

  async getBranchIdSelectedOption(): Promise<string> {
    return await this.branchIdSelect.element(by.css('option:checked')).getText();
  }

  async currencyCodeSelectLastOption(): Promise<void> {
    await this.currencyCodeSelect.all(by.tagName('option')).last().click();
  }

  async currencyCodeSelectOption(option: string): Promise<void> {
    await this.currencyCodeSelect.sendKeys(option);
  }

  getCurrencyCodeSelect(): ElementFinder {
    return this.currencyCodeSelect;
  }

  async getCurrencyCodeSelectedOption(): Promise<string> {
    return await this.currencyCodeSelect.element(by.css('option:checked')).getText();
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

export class AccountBalanceDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-accountBalance-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-accountBalance'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
