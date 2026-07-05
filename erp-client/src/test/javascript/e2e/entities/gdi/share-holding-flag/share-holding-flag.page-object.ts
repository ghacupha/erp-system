import { element, by, ElementFinder } from 'protractor';

export class ShareHoldingFlagComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-share-holding-flag div table .btn-danger'));
  title = element.all(by.css('jhi-share-holding-flag div h2#page-heading span')).first();
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

export class ShareHoldingFlagUpdatePage {
  pageTitle = element(by.id('jhi-share-holding-flag-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  shareholdingFlagTypeCodeSelect = element(by.id('field_shareholdingFlagTypeCode'));
  shareholdingFlagTypeInput = element(by.id('field_shareholdingFlagType'));
  shareholdingTypeDescriptionInput = element(by.id('field_shareholdingTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setShareholdingFlagTypeCodeSelect(shareholdingFlagTypeCode: string): Promise<void> {
    await this.shareholdingFlagTypeCodeSelect.sendKeys(shareholdingFlagTypeCode);
  }

  async getShareholdingFlagTypeCodeSelect(): Promise<string> {
    return await this.shareholdingFlagTypeCodeSelect.element(by.css('option:checked')).getText();
  }

  async shareholdingFlagTypeCodeSelectLastOption(): Promise<void> {
    await this.shareholdingFlagTypeCodeSelect.all(by.tagName('option')).last().click();
  }

  async setShareholdingFlagTypeInput(shareholdingFlagType: string): Promise<void> {
    await this.shareholdingFlagTypeInput.sendKeys(shareholdingFlagType);
  }

  async getShareholdingFlagTypeInput(): Promise<string> {
    return await this.shareholdingFlagTypeInput.getAttribute('value');
  }

  async setShareholdingTypeDescriptionInput(shareholdingTypeDescription: string): Promise<void> {
    await this.shareholdingTypeDescriptionInput.sendKeys(shareholdingTypeDescription);
  }

  async getShareholdingTypeDescriptionInput(): Promise<string> {
    return await this.shareholdingTypeDescriptionInput.getAttribute('value');
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

export class ShareHoldingFlagDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-shareHoldingFlag-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-shareHoldingFlag'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
