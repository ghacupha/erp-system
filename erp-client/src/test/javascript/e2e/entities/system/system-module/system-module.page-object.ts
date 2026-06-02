import { element, by, ElementFinder } from 'protractor';

export class SystemModuleComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-system-module div table .btn-danger'));
  title = element.all(by.css('jhi-system-module div h2#page-heading span')).first();
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

export class SystemModuleUpdatePage {
  pageTitle = element(by.id('jhi-system-module-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  moduleNameInput = element(by.id('field_moduleName'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setModuleNameInput(moduleName: string): Promise<void> {
    await this.moduleNameInput.sendKeys(moduleName);
  }

  async getModuleNameInput(): Promise<string> {
    return await this.moduleNameInput.getAttribute('value');
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

export class SystemModuleDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-systemModule-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-systemModule'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
