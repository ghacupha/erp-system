import { element, by, ElementFinder } from 'protractor';

export class CardTypesComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-card-types div table .btn-danger'));
  title = element.all(by.css('jhi-card-types div h2#page-heading span')).first();
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

export class CardTypesUpdatePage {
  pageTitle = element(by.id('jhi-card-types-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  cardTypeCodeInput = element(by.id('field_cardTypeCode'));
  cardTypeInput = element(by.id('field_cardType'));
  cardTypeDetailsInput = element(by.id('field_cardTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCardTypeCodeInput(cardTypeCode: string): Promise<void> {
    await this.cardTypeCodeInput.sendKeys(cardTypeCode);
  }

  async getCardTypeCodeInput(): Promise<string> {
    return await this.cardTypeCodeInput.getAttribute('value');
  }

  async setCardTypeInput(cardType: string): Promise<void> {
    await this.cardTypeInput.sendKeys(cardType);
  }

  async getCardTypeInput(): Promise<string> {
    return await this.cardTypeInput.getAttribute('value');
  }

  async setCardTypeDetailsInput(cardTypeDetails: string): Promise<void> {
    await this.cardTypeDetailsInput.sendKeys(cardTypeDetails);
  }

  async getCardTypeDetailsInput(): Promise<string> {
    return await this.cardTypeDetailsInput.getAttribute('value');
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

export class CardTypesDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cardTypes-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cardTypes'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
