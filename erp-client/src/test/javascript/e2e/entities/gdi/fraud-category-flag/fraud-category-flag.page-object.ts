import { element, by, ElementFinder } from 'protractor';

export class FraudCategoryFlagComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fraud-category-flag div table .btn-danger'));
  title = element.all(by.css('jhi-fraud-category-flag div h2#page-heading span')).first();
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

export class FraudCategoryFlagUpdatePage {
  pageTitle = element(by.id('jhi-fraud-category-flag-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  fraudCategoryFlagSelect = element(by.id('field_fraudCategoryFlag'));
  fraudCategoryTypeDetailsInput = element(by.id('field_fraudCategoryTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFraudCategoryFlagSelect(fraudCategoryFlag: string): Promise<void> {
    await this.fraudCategoryFlagSelect.sendKeys(fraudCategoryFlag);
  }

  async getFraudCategoryFlagSelect(): Promise<string> {
    return await this.fraudCategoryFlagSelect.element(by.css('option:checked')).getText();
  }

  async fraudCategoryFlagSelectLastOption(): Promise<void> {
    await this.fraudCategoryFlagSelect.all(by.tagName('option')).last().click();
  }

  async setFraudCategoryTypeDetailsInput(fraudCategoryTypeDetails: string): Promise<void> {
    await this.fraudCategoryTypeDetailsInput.sendKeys(fraudCategoryTypeDetails);
  }

  async getFraudCategoryTypeDetailsInput(): Promise<string> {
    return await this.fraudCategoryTypeDetailsInput.getAttribute('value');
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

export class FraudCategoryFlagDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fraudCategoryFlag-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fraudCategoryFlag'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
