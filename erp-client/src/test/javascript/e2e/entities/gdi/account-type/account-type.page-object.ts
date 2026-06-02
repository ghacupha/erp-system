import { element, by, ElementFinder } from 'protractor';

export class AccountTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-account-type div table .btn-danger'));
  title = element.all(by.css('jhi-account-type div h2#page-heading span')).first();
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

export class AccountTypeUpdatePage {
  pageTitle = element(by.id('jhi-account-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  accountTypeCodeInput = element(by.id('field_accountTypeCode'));
  accountTypeInput = element(by.id('field_accountType'));
  descriptionInput = element(by.id('field_description'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAccountTypeCodeInput(accountTypeCode: string): Promise<void> {
    await this.accountTypeCodeInput.sendKeys(accountTypeCode);
  }

  async getAccountTypeCodeInput(): Promise<string> {
    return await this.accountTypeCodeInput.getAttribute('value');
  }

  async setAccountTypeInput(accountType: string): Promise<void> {
    await this.accountTypeInput.sendKeys(accountType);
  }

  async getAccountTypeInput(): Promise<string> {
    return await this.accountTypeInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
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

export class AccountTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-accountType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-accountType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
