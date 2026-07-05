import { element, by, ElementFinder } from 'protractor';

export class ExecutiveCategoryTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-executive-category-type div table .btn-danger'));
  title = element.all(by.css('jhi-executive-category-type div h2#page-heading span')).first();
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

export class ExecutiveCategoryTypeUpdatePage {
  pageTitle = element(by.id('jhi-executive-category-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  directorCategoryTypeCodeInput = element(by.id('field_directorCategoryTypeCode'));
  directorCategoryTypeInput = element(by.id('field_directorCategoryType'));
  directorCategoryTypeDetailsInput = element(by.id('field_directorCategoryTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDirectorCategoryTypeCodeInput(directorCategoryTypeCode: string): Promise<void> {
    await this.directorCategoryTypeCodeInput.sendKeys(directorCategoryTypeCode);
  }

  async getDirectorCategoryTypeCodeInput(): Promise<string> {
    return await this.directorCategoryTypeCodeInput.getAttribute('value');
  }

  async setDirectorCategoryTypeInput(directorCategoryType: string): Promise<void> {
    await this.directorCategoryTypeInput.sendKeys(directorCategoryType);
  }

  async getDirectorCategoryTypeInput(): Promise<string> {
    return await this.directorCategoryTypeInput.getAttribute('value');
  }

  async setDirectorCategoryTypeDetailsInput(directorCategoryTypeDetails: string): Promise<void> {
    await this.directorCategoryTypeDetailsInput.sendKeys(directorCategoryTypeDetails);
  }

  async getDirectorCategoryTypeDetailsInput(): Promise<string> {
    return await this.directorCategoryTypeDetailsInput.getAttribute('value');
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

export class ExecutiveCategoryTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-executiveCategoryType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-executiveCategoryType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
