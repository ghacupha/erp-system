import { element, by, ElementFinder } from 'protractor';

export class CardChargesComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-card-charges div table .btn-danger'));
  title = element.all(by.css('jhi-card-charges div h2#page-heading span')).first();
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

export class CardChargesUpdatePage {
  pageTitle = element(by.id('jhi-card-charges-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  cardChargeTypeInput = element(by.id('field_cardChargeType'));
  cardChargeTypeNameInput = element(by.id('field_cardChargeTypeName'));
  cardChargeDetailsInput = element(by.id('field_cardChargeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCardChargeTypeInput(cardChargeType: string): Promise<void> {
    await this.cardChargeTypeInput.sendKeys(cardChargeType);
  }

  async getCardChargeTypeInput(): Promise<string> {
    return await this.cardChargeTypeInput.getAttribute('value');
  }

  async setCardChargeTypeNameInput(cardChargeTypeName: string): Promise<void> {
    await this.cardChargeTypeNameInput.sendKeys(cardChargeTypeName);
  }

  async getCardChargeTypeNameInput(): Promise<string> {
    return await this.cardChargeTypeNameInput.getAttribute('value');
  }

  async setCardChargeDetailsInput(cardChargeDetails: string): Promise<void> {
    await this.cardChargeDetailsInput.sendKeys(cardChargeDetails);
  }

  async getCardChargeDetailsInput(): Promise<string> {
    return await this.cardChargeDetailsInput.getAttribute('value');
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

export class CardChargesDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cardCharges-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cardCharges'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
