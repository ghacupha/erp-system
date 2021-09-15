import { element, by, ElementFinder } from 'protractor';

export class PlaceholderComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-placeholder div table .btn-danger'));
  title = element.all(by.css('jhi-placeholder div h2#page-heading span')).first();
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

export class PlaceholderUpdatePage {
  pageTitle = element(by.id('jhi-placeholder-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  descriptionInput = element(by.id('field_description'));
  tokenInput = element(by.id('field_token'));

  containingPlaceholderSelect = element(by.id('field_containingPlaceholder'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setTokenInput(token: string): Promise<void> {
    await this.tokenInput.sendKeys(token);
  }

  async getTokenInput(): Promise<string> {
    return await this.tokenInput.getAttribute('value');
  }

  async containingPlaceholderSelectLastOption(): Promise<void> {
    await this.containingPlaceholderSelect.all(by.tagName('option')).last().click();
  }

  async containingPlaceholderSelectOption(option: string): Promise<void> {
    await this.containingPlaceholderSelect.sendKeys(option);
  }

  getContainingPlaceholderSelect(): ElementFinder {
    return this.containingPlaceholderSelect;
  }

  async getContainingPlaceholderSelectedOption(): Promise<string> {
    return await this.containingPlaceholderSelect.element(by.css('option:checked')).getText();
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

export class PlaceholderDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-placeholder-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-placeholder'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
