import { element, by, ElementFinder } from 'protractor';

export class CreditCardOwnershipComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-credit-card-ownership div table .btn-danger'));
  title = element.all(by.css('jhi-credit-card-ownership div h2#page-heading span')).first();
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

export class CreditCardOwnershipUpdatePage {
  pageTitle = element(by.id('jhi-credit-card-ownership-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  creditCardOwnershipCategoryCodeInput = element(by.id('field_creditCardOwnershipCategoryCode'));
  creditCardOwnershipCategoryTypeSelect = element(by.id('field_creditCardOwnershipCategoryType'));
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

  async setCreditCardOwnershipCategoryCodeInput(creditCardOwnershipCategoryCode: string): Promise<void> {
    await this.creditCardOwnershipCategoryCodeInput.sendKeys(creditCardOwnershipCategoryCode);
  }

  async getCreditCardOwnershipCategoryCodeInput(): Promise<string> {
    return await this.creditCardOwnershipCategoryCodeInput.getAttribute('value');
  }

  async setCreditCardOwnershipCategoryTypeSelect(creditCardOwnershipCategoryType: string): Promise<void> {
    await this.creditCardOwnershipCategoryTypeSelect.sendKeys(creditCardOwnershipCategoryType);
  }

  async getCreditCardOwnershipCategoryTypeSelect(): Promise<string> {
    return await this.creditCardOwnershipCategoryTypeSelect.element(by.css('option:checked')).getText();
  }

  async creditCardOwnershipCategoryTypeSelectLastOption(): Promise<void> {
    await this.creditCardOwnershipCategoryTypeSelect.all(by.tagName('option')).last().click();
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

export class CreditCardOwnershipDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-creditCardOwnership-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-creditCardOwnership'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
