import { element, by, ElementFinder } from 'protractor';

export class CrbGlCodeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-crb-gl-code div table .btn-danger'));
  title = element.all(by.css('jhi-crb-gl-code div h2#page-heading span')).first();
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

export class CrbGlCodeUpdatePage {
  pageTitle = element(by.id('jhi-crb-gl-code-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  glCodeInput = element(by.id('field_glCode'));
  glDescriptionInput = element(by.id('field_glDescription'));
  glTypeInput = element(by.id('field_glType'));
  institutionCategoryInput = element(by.id('field_institutionCategory'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setGlCodeInput(glCode: string): Promise<void> {
    await this.glCodeInput.sendKeys(glCode);
  }

  async getGlCodeInput(): Promise<string> {
    return await this.glCodeInput.getAttribute('value');
  }

  async setGlDescriptionInput(glDescription: string): Promise<void> {
    await this.glDescriptionInput.sendKeys(glDescription);
  }

  async getGlDescriptionInput(): Promise<string> {
    return await this.glDescriptionInput.getAttribute('value');
  }

  async setGlTypeInput(glType: string): Promise<void> {
    await this.glTypeInput.sendKeys(glType);
  }

  async getGlTypeInput(): Promise<string> {
    return await this.glTypeInput.getAttribute('value');
  }

  async setInstitutionCategoryInput(institutionCategory: string): Promise<void> {
    await this.institutionCategoryInput.sendKeys(institutionCategory);
  }

  async getInstitutionCategoryInput(): Promise<string> {
    return await this.institutionCategoryInput.getAttribute('value');
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

export class CrbGlCodeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-crbGlCode-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-crbGlCode'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
