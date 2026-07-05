import { element, by, ElementFinder } from 'protractor';

export class CardFraudIncidentCategoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-card-fraud-incident-category div table .btn-danger'));
  title = element.all(by.css('jhi-card-fraud-incident-category div h2#page-heading span')).first();
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

export class CardFraudIncidentCategoryUpdatePage {
  pageTitle = element(by.id('jhi-card-fraud-incident-category-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  cardFraudCategoryTypeCodeInput = element(by.id('field_cardFraudCategoryTypeCode'));
  cardFraudCategoryTypeInput = element(by.id('field_cardFraudCategoryType'));
  cardFraudCategoryTypeDescriptionInput = element(by.id('field_cardFraudCategoryTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCardFraudCategoryTypeCodeInput(cardFraudCategoryTypeCode: string): Promise<void> {
    await this.cardFraudCategoryTypeCodeInput.sendKeys(cardFraudCategoryTypeCode);
  }

  async getCardFraudCategoryTypeCodeInput(): Promise<string> {
    return await this.cardFraudCategoryTypeCodeInput.getAttribute('value');
  }

  async setCardFraudCategoryTypeInput(cardFraudCategoryType: string): Promise<void> {
    await this.cardFraudCategoryTypeInput.sendKeys(cardFraudCategoryType);
  }

  async getCardFraudCategoryTypeInput(): Promise<string> {
    return await this.cardFraudCategoryTypeInput.getAttribute('value');
  }

  async setCardFraudCategoryTypeDescriptionInput(cardFraudCategoryTypeDescription: string): Promise<void> {
    await this.cardFraudCategoryTypeDescriptionInput.sendKeys(cardFraudCategoryTypeDescription);
  }

  async getCardFraudCategoryTypeDescriptionInput(): Promise<string> {
    return await this.cardFraudCategoryTypeDescriptionInput.getAttribute('value');
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

export class CardFraudIncidentCategoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cardFraudIncidentCategory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cardFraudIncidentCategory'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
