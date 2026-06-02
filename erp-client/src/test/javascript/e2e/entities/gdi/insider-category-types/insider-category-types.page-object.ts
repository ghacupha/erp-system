import { element, by, ElementFinder } from 'protractor';

export class InsiderCategoryTypesComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-insider-category-types div table .btn-danger'));
  title = element.all(by.css('jhi-insider-category-types div h2#page-heading span')).first();
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

export class InsiderCategoryTypesUpdatePage {
  pageTitle = element(by.id('jhi-insider-category-types-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  insiderCategoryTypeCodeInput = element(by.id('field_insiderCategoryTypeCode'));
  insiderCategoryTypeDetailInput = element(by.id('field_insiderCategoryTypeDetail'));
  insiderCategoryDescriptionInput = element(by.id('field_insiderCategoryDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setInsiderCategoryTypeCodeInput(insiderCategoryTypeCode: string): Promise<void> {
    await this.insiderCategoryTypeCodeInput.sendKeys(insiderCategoryTypeCode);
  }

  async getInsiderCategoryTypeCodeInput(): Promise<string> {
    return await this.insiderCategoryTypeCodeInput.getAttribute('value');
  }

  async setInsiderCategoryTypeDetailInput(insiderCategoryTypeDetail: string): Promise<void> {
    await this.insiderCategoryTypeDetailInput.sendKeys(insiderCategoryTypeDetail);
  }

  async getInsiderCategoryTypeDetailInput(): Promise<string> {
    return await this.insiderCategoryTypeDetailInput.getAttribute('value');
  }

  async setInsiderCategoryDescriptionInput(insiderCategoryDescription: string): Promise<void> {
    await this.insiderCategoryDescriptionInput.sendKeys(insiderCategoryDescription);
  }

  async getInsiderCategoryDescriptionInput(): Promise<string> {
    return await this.insiderCategoryDescriptionInput.getAttribute('value');
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

export class InsiderCategoryTypesDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-insiderCategoryTypes-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-insiderCategoryTypes'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
