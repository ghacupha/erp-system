import { element, by, ElementFinder } from 'protractor';

export class CrbAccountHolderTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-crb-account-holder-type div table .btn-danger'));
  title = element.all(by.css('jhi-crb-account-holder-type div h2#page-heading span')).first();
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

export class CrbAccountHolderTypeUpdatePage {
  pageTitle = element(by.id('jhi-crb-account-holder-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  accountHolderCategoryTypeCodeInput = element(by.id('field_accountHolderCategoryTypeCode'));
  accountHolderCategoryTypeInput = element(by.id('field_accountHolderCategoryType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAccountHolderCategoryTypeCodeInput(accountHolderCategoryTypeCode: string): Promise<void> {
    await this.accountHolderCategoryTypeCodeInput.sendKeys(accountHolderCategoryTypeCode);
  }

  async getAccountHolderCategoryTypeCodeInput(): Promise<string> {
    return await this.accountHolderCategoryTypeCodeInput.getAttribute('value');
  }

  async setAccountHolderCategoryTypeInput(accountHolderCategoryType: string): Promise<void> {
    await this.accountHolderCategoryTypeInput.sendKeys(accountHolderCategoryType);
  }

  async getAccountHolderCategoryTypeInput(): Promise<string> {
    return await this.accountHolderCategoryTypeInput.getAttribute('value');
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

export class CrbAccountHolderTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-crbAccountHolderType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-crbAccountHolderType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
