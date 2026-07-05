import { element, by, ElementFinder } from 'protractor';

export class CrbAccountStatusComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-crb-account-status div table .btn-danger'));
  title = element.all(by.css('jhi-crb-account-status div h2#page-heading span')).first();
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

export class CrbAccountStatusUpdatePage {
  pageTitle = element(by.id('jhi-crb-account-status-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  accountStatusTypeCodeInput = element(by.id('field_accountStatusTypeCode'));
  accountStatusTypeInput = element(by.id('field_accountStatusType'));
  accountStatusTypeDetailsInput = element(by.id('field_accountStatusTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAccountStatusTypeCodeInput(accountStatusTypeCode: string): Promise<void> {
    await this.accountStatusTypeCodeInput.sendKeys(accountStatusTypeCode);
  }

  async getAccountStatusTypeCodeInput(): Promise<string> {
    return await this.accountStatusTypeCodeInput.getAttribute('value');
  }

  async setAccountStatusTypeInput(accountStatusType: string): Promise<void> {
    await this.accountStatusTypeInput.sendKeys(accountStatusType);
  }

  async getAccountStatusTypeInput(): Promise<string> {
    return await this.accountStatusTypeInput.getAttribute('value');
  }

  async setAccountStatusTypeDetailsInput(accountStatusTypeDetails: string): Promise<void> {
    await this.accountStatusTypeDetailsInput.sendKeys(accountStatusTypeDetails);
  }

  async getAccountStatusTypeDetailsInput(): Promise<string> {
    return await this.accountStatusTypeDetailsInput.getAttribute('value');
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

export class CrbAccountStatusDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-crbAccountStatus-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-crbAccountStatus'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
