import { element, by, ElementFinder } from 'protractor';

export class CounterPartyCategoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-counter-party-category div table .btn-danger'));
  title = element.all(by.css('jhi-counter-party-category div h2#page-heading span')).first();
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

export class CounterPartyCategoryUpdatePage {
  pageTitle = element(by.id('jhi-counter-party-category-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  counterpartyCategoryCodeInput = element(by.id('field_counterpartyCategoryCode'));
  counterpartyCategoryCodeDetailsSelect = element(by.id('field_counterpartyCategoryCodeDetails'));
  counterpartyCategoryDescriptionInput = element(by.id('field_counterpartyCategoryDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCounterpartyCategoryCodeInput(counterpartyCategoryCode: string): Promise<void> {
    await this.counterpartyCategoryCodeInput.sendKeys(counterpartyCategoryCode);
  }

  async getCounterpartyCategoryCodeInput(): Promise<string> {
    return await this.counterpartyCategoryCodeInput.getAttribute('value');
  }

  async setCounterpartyCategoryCodeDetailsSelect(counterpartyCategoryCodeDetails: string): Promise<void> {
    await this.counterpartyCategoryCodeDetailsSelect.sendKeys(counterpartyCategoryCodeDetails);
  }

  async getCounterpartyCategoryCodeDetailsSelect(): Promise<string> {
    return await this.counterpartyCategoryCodeDetailsSelect.element(by.css('option:checked')).getText();
  }

  async counterpartyCategoryCodeDetailsSelectLastOption(): Promise<void> {
    await this.counterpartyCategoryCodeDetailsSelect.all(by.tagName('option')).last().click();
  }

  async setCounterpartyCategoryDescriptionInput(counterpartyCategoryDescription: string): Promise<void> {
    await this.counterpartyCategoryDescriptionInput.sendKeys(counterpartyCategoryDescription);
  }

  async getCounterpartyCategoryDescriptionInput(): Promise<string> {
    return await this.counterpartyCategoryDescriptionInput.getAttribute('value');
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

export class CounterPartyCategoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-counterPartyCategory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-counterPartyCategory'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
