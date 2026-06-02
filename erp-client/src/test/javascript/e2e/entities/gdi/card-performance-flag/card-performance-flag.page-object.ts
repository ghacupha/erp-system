import { element, by, ElementFinder } from 'protractor';

export class CardPerformanceFlagComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-card-performance-flag div table .btn-danger'));
  title = element.all(by.css('jhi-card-performance-flag div h2#page-heading span')).first();
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

export class CardPerformanceFlagUpdatePage {
  pageTitle = element(by.id('jhi-card-performance-flag-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  cardPerformanceFlagSelect = element(by.id('field_cardPerformanceFlag'));
  cardPerformanceFlagDescriptionInput = element(by.id('field_cardPerformanceFlagDescription'));
  cardPerformanceFlagDetailsInput = element(by.id('field_cardPerformanceFlagDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCardPerformanceFlagSelect(cardPerformanceFlag: string): Promise<void> {
    await this.cardPerformanceFlagSelect.sendKeys(cardPerformanceFlag);
  }

  async getCardPerformanceFlagSelect(): Promise<string> {
    return await this.cardPerformanceFlagSelect.element(by.css('option:checked')).getText();
  }

  async cardPerformanceFlagSelectLastOption(): Promise<void> {
    await this.cardPerformanceFlagSelect.all(by.tagName('option')).last().click();
  }

  async setCardPerformanceFlagDescriptionInput(cardPerformanceFlagDescription: string): Promise<void> {
    await this.cardPerformanceFlagDescriptionInput.sendKeys(cardPerformanceFlagDescription);
  }

  async getCardPerformanceFlagDescriptionInput(): Promise<string> {
    return await this.cardPerformanceFlagDescriptionInput.getAttribute('value');
  }

  async setCardPerformanceFlagDetailsInput(cardPerformanceFlagDetails: string): Promise<void> {
    await this.cardPerformanceFlagDetailsInput.sendKeys(cardPerformanceFlagDetails);
  }

  async getCardPerformanceFlagDetailsInput(): Promise<string> {
    return await this.cardPerformanceFlagDetailsInput.getAttribute('value');
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

export class CardPerformanceFlagDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cardPerformanceFlag-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cardPerformanceFlag'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
