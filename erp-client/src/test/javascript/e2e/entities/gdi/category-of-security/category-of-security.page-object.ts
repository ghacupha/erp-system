import { element, by, ElementFinder } from 'protractor';

export class CategoryOfSecurityComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-category-of-security div table .btn-danger'));
  title = element.all(by.css('jhi-category-of-security div h2#page-heading span')).first();
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

export class CategoryOfSecurityUpdatePage {
  pageTitle = element(by.id('jhi-category-of-security-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  categoryOfSecurityInput = element(by.id('field_categoryOfSecurity'));
  categoryOfSecurityDetailsInput = element(by.id('field_categoryOfSecurityDetails'));
  categoryOfSecurityDescriptionInput = element(by.id('field_categoryOfSecurityDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCategoryOfSecurityInput(categoryOfSecurity: string): Promise<void> {
    await this.categoryOfSecurityInput.sendKeys(categoryOfSecurity);
  }

  async getCategoryOfSecurityInput(): Promise<string> {
    return await this.categoryOfSecurityInput.getAttribute('value');
  }

  async setCategoryOfSecurityDetailsInput(categoryOfSecurityDetails: string): Promise<void> {
    await this.categoryOfSecurityDetailsInput.sendKeys(categoryOfSecurityDetails);
  }

  async getCategoryOfSecurityDetailsInput(): Promise<string> {
    return await this.categoryOfSecurityDetailsInput.getAttribute('value');
  }

  async setCategoryOfSecurityDescriptionInput(categoryOfSecurityDescription: string): Promise<void> {
    await this.categoryOfSecurityDescriptionInput.sendKeys(categoryOfSecurityDescription);
  }

  async getCategoryOfSecurityDescriptionInput(): Promise<string> {
    return await this.categoryOfSecurityDescriptionInput.getAttribute('value');
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

export class CategoryOfSecurityDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-categoryOfSecurity-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-categoryOfSecurity'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
