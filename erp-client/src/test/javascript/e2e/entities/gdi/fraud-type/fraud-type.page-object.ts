import { element, by, ElementFinder } from 'protractor';

export class FraudTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fraud-type div table .btn-danger'));
  title = element.all(by.css('jhi-fraud-type div h2#page-heading span')).first();
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

export class FraudTypeUpdatePage {
  pageTitle = element(by.id('jhi-fraud-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  fraudTypeCodeInput = element(by.id('field_fraudTypeCode'));
  fraudTypeInput = element(by.id('field_fraudType'));
  fraudTypeDetailsInput = element(by.id('field_fraudTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFraudTypeCodeInput(fraudTypeCode: string): Promise<void> {
    await this.fraudTypeCodeInput.sendKeys(fraudTypeCode);
  }

  async getFraudTypeCodeInput(): Promise<string> {
    return await this.fraudTypeCodeInput.getAttribute('value');
  }

  async setFraudTypeInput(fraudType: string): Promise<void> {
    await this.fraudTypeInput.sendKeys(fraudType);
  }

  async getFraudTypeInput(): Promise<string> {
    return await this.fraudTypeInput.getAttribute('value');
  }

  async setFraudTypeDetailsInput(fraudTypeDetails: string): Promise<void> {
    await this.fraudTypeDetailsInput.sendKeys(fraudTypeDetails);
  }

  async getFraudTypeDetailsInput(): Promise<string> {
    return await this.fraudTypeDetailsInput.getAttribute('value');
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

export class FraudTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fraudType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fraudType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
