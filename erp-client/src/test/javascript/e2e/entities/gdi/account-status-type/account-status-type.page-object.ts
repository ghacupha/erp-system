import { element, by, ElementFinder } from 'protractor';

export class AccountStatusTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-account-status-type div table .btn-danger'));
  title = element.all(by.css('jhi-account-status-type div h2#page-heading span')).first();
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

export class AccountStatusTypeUpdatePage {
  pageTitle = element(by.id('jhi-account-status-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  accountStatusCodeInput = element(by.id('field_accountStatusCode'));
  accountStatusTypeSelect = element(by.id('field_accountStatusType'));
  accountStatusDescriptionInput = element(by.id('field_accountStatusDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAccountStatusCodeInput(accountStatusCode: string): Promise<void> {
    await this.accountStatusCodeInput.sendKeys(accountStatusCode);
  }

  async getAccountStatusCodeInput(): Promise<string> {
    return await this.accountStatusCodeInput.getAttribute('value');
  }

  async setAccountStatusTypeSelect(accountStatusType: string): Promise<void> {
    await this.accountStatusTypeSelect.sendKeys(accountStatusType);
  }

  async getAccountStatusTypeSelect(): Promise<string> {
    return await this.accountStatusTypeSelect.element(by.css('option:checked')).getText();
  }

  async accountStatusTypeSelectLastOption(): Promise<void> {
    await this.accountStatusTypeSelect.all(by.tagName('option')).last().click();
  }

  async setAccountStatusDescriptionInput(accountStatusDescription: string): Promise<void> {
    await this.accountStatusDescriptionInput.sendKeys(accountStatusDescription);
  }

  async getAccountStatusDescriptionInput(): Promise<string> {
    return await this.accountStatusDescriptionInput.getAttribute('value');
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

export class AccountStatusTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-accountStatusType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-accountStatusType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
