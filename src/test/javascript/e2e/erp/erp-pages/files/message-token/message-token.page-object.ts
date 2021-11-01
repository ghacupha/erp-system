import { element, by, ElementFinder } from 'protractor';

export class MessageTokenComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-message-token div table .btn-danger'));
  title = element.all(by.css('jhi-message-token div h2#page-heading span')).first();
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

export class MessageTokenUpdatePage {
  pageTitle = element(by.id('jhi-message-token-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  descriptionInput = element(by.id('field_description'));
  timeSentInput = element(by.id('field_timeSent'));
  tokenValueInput = element(by.id('field_tokenValue'));
  receivedInput = element(by.id('field_received'));
  actionedInput = element(by.id('field_actioned'));
  contentFullyEnqueuedInput = element(by.id('field_contentFullyEnqueued'));

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

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setTimeSentInput(timeSent: string): Promise<void> {
    await this.timeSentInput.sendKeys(timeSent);
  }

  async getTimeSentInput(): Promise<string> {
    return await this.timeSentInput.getAttribute('value');
  }

  async setTokenValueInput(tokenValue: string): Promise<void> {
    await this.tokenValueInput.sendKeys(tokenValue);
  }

  async getTokenValueInput(): Promise<string> {
    return await this.tokenValueInput.getAttribute('value');
  }

  getReceivedInput(): ElementFinder {
    return this.receivedInput;
  }

  getActionedInput(): ElementFinder {
    return this.actionedInput;
  }

  getContentFullyEnqueuedInput(): ElementFinder {
    return this.contentFullyEnqueuedInput;
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

export class MessageTokenDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-messageToken-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-messageToken'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
