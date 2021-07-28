import { element, by, ElementFinder } from 'protractor';

export class PaymentCategoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-payment-category div table .btn-danger'));
  title = element.all(by.css('jhi-payment-category div h2#page-heading span')).first();
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

export class PaymentCategoryUpdatePage {
  pageTitle = element(by.id('jhi-payment-category-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  categoryNameInput = element(by.id('field_categoryName'));
  categoryDescriptionInput = element(by.id('field_categoryDescription'));
  categoryTypeSelect = element(by.id('field_categoryType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setCategoryNameInput(categoryName: string): Promise<void> {
    await this.categoryNameInput.sendKeys(categoryName);
  }

  async getCategoryNameInput(): Promise<string> {
    return await this.categoryNameInput.getAttribute('value');
  }

  async setCategoryDescriptionInput(categoryDescription: string): Promise<void> {
    await this.categoryDescriptionInput.sendKeys(categoryDescription);
  }

  async getCategoryDescriptionInput(): Promise<string> {
    return await this.categoryDescriptionInput.getAttribute('value');
  }

  async setCategoryTypeSelect(categoryType: string): Promise<void> {
    await this.categoryTypeSelect.sendKeys(categoryType);
  }

  async getCategoryTypeSelect(): Promise<string> {
    return await this.categoryTypeSelect.element(by.css('option:checked')).getText();
  }

  async categoryTypeSelectLastOption(): Promise<void> {
    await this.categoryTypeSelect.all(by.tagName('option')).last().click();
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

export class PaymentCategoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-paymentCategory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-paymentCategory'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
