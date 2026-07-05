import { element, by, ElementFinder } from 'protractor';

export class MoratoriumItemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-moratorium-item div table .btn-danger'));
  title = element.all(by.css('jhi-moratorium-item div h2#page-heading span')).first();
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

export class MoratoriumItemUpdatePage {
  pageTitle = element(by.id('jhi-moratorium-item-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  moratoriumItemTypeCodeInput = element(by.id('field_moratoriumItemTypeCode'));
  moratoriumItemTypeInput = element(by.id('field_moratoriumItemType'));
  moratoriumTypeDetailsInput = element(by.id('field_moratoriumTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setMoratoriumItemTypeCodeInput(moratoriumItemTypeCode: string): Promise<void> {
    await this.moratoriumItemTypeCodeInput.sendKeys(moratoriumItemTypeCode);
  }

  async getMoratoriumItemTypeCodeInput(): Promise<string> {
    return await this.moratoriumItemTypeCodeInput.getAttribute('value');
  }

  async setMoratoriumItemTypeInput(moratoriumItemType: string): Promise<void> {
    await this.moratoriumItemTypeInput.sendKeys(moratoriumItemType);
  }

  async getMoratoriumItemTypeInput(): Promise<string> {
    return await this.moratoriumItemTypeInput.getAttribute('value');
  }

  async setMoratoriumTypeDetailsInput(moratoriumTypeDetails: string): Promise<void> {
    await this.moratoriumTypeDetailsInput.sendKeys(moratoriumTypeDetails);
  }

  async getMoratoriumTypeDetailsInput(): Promise<string> {
    return await this.moratoriumTypeDetailsInput.getAttribute('value');
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

export class MoratoriumItemDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-moratoriumItem-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-moratoriumItem'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
