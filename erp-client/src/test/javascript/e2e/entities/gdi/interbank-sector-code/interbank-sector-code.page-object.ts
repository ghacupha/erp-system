import { element, by, ElementFinder } from 'protractor';

export class InterbankSectorCodeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-interbank-sector-code div table .btn-danger'));
  title = element.all(by.css('jhi-interbank-sector-code div h2#page-heading span')).first();
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

export class InterbankSectorCodeUpdatePage {
  pageTitle = element(by.id('jhi-interbank-sector-code-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  interbankSectorCodeInput = element(by.id('field_interbankSectorCode'));
  interbankSectorCodeDescriptionInput = element(by.id('field_interbankSectorCodeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setInterbankSectorCodeInput(interbankSectorCode: string): Promise<void> {
    await this.interbankSectorCodeInput.sendKeys(interbankSectorCode);
  }

  async getInterbankSectorCodeInput(): Promise<string> {
    return await this.interbankSectorCodeInput.getAttribute('value');
  }

  async setInterbankSectorCodeDescriptionInput(interbankSectorCodeDescription: string): Promise<void> {
    await this.interbankSectorCodeDescriptionInput.sendKeys(interbankSectorCodeDescription);
  }

  async getInterbankSectorCodeDescriptionInput(): Promise<string> {
    return await this.interbankSectorCodeDescriptionInput.getAttribute('value');
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

export class InterbankSectorCodeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-interbankSectorCode-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-interbankSectorCode'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
