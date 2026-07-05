import { element, by, ElementFinder } from 'protractor';

export class AcquiringIssuingFlagComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-acquiring-issuing-flag div table .btn-danger'));
  title = element.all(by.css('jhi-acquiring-issuing-flag div h2#page-heading span')).first();
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

export class AcquiringIssuingFlagUpdatePage {
  pageTitle = element(by.id('jhi-acquiring-issuing-flag-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  cardAcquiringIssuingFlagCodeInput = element(by.id('field_cardAcquiringIssuingFlagCode'));
  cardAcquiringIssuingDescriptionInput = element(by.id('field_cardAcquiringIssuingDescription'));
  cardAcquiringIssuingDetailsInput = element(by.id('field_cardAcquiringIssuingDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCardAcquiringIssuingFlagCodeInput(cardAcquiringIssuingFlagCode: string): Promise<void> {
    await this.cardAcquiringIssuingFlagCodeInput.sendKeys(cardAcquiringIssuingFlagCode);
  }

  async getCardAcquiringIssuingFlagCodeInput(): Promise<string> {
    return await this.cardAcquiringIssuingFlagCodeInput.getAttribute('value');
  }

  async setCardAcquiringIssuingDescriptionInput(cardAcquiringIssuingDescription: string): Promise<void> {
    await this.cardAcquiringIssuingDescriptionInput.sendKeys(cardAcquiringIssuingDescription);
  }

  async getCardAcquiringIssuingDescriptionInput(): Promise<string> {
    return await this.cardAcquiringIssuingDescriptionInput.getAttribute('value');
  }

  async setCardAcquiringIssuingDetailsInput(cardAcquiringIssuingDetails: string): Promise<void> {
    await this.cardAcquiringIssuingDetailsInput.sendKeys(cardAcquiringIssuingDetails);
  }

  async getCardAcquiringIssuingDetailsInput(): Promise<string> {
    return await this.cardAcquiringIssuingDetailsInput.getAttribute('value');
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

export class AcquiringIssuingFlagDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-acquiringIssuingFlag-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-acquiringIssuingFlag'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
