import { element, by, ElementFinder } from 'protractor';

export class SecurityClassificationTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-security-classification-type div table .btn-danger'));
  title = element.all(by.css('jhi-security-classification-type div h2#page-heading span')).first();
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

export class SecurityClassificationTypeUpdatePage {
  pageTitle = element(by.id('jhi-security-classification-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  securityClassificationTypeCodeInput = element(by.id('field_securityClassificationTypeCode'));
  securityClassificationTypeInput = element(by.id('field_securityClassificationType'));
  securityClassificationDetailsInput = element(by.id('field_securityClassificationDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSecurityClassificationTypeCodeInput(securityClassificationTypeCode: string): Promise<void> {
    await this.securityClassificationTypeCodeInput.sendKeys(securityClassificationTypeCode);
  }

  async getSecurityClassificationTypeCodeInput(): Promise<string> {
    return await this.securityClassificationTypeCodeInput.getAttribute('value');
  }

  async setSecurityClassificationTypeInput(securityClassificationType: string): Promise<void> {
    await this.securityClassificationTypeInput.sendKeys(securityClassificationType);
  }

  async getSecurityClassificationTypeInput(): Promise<string> {
    return await this.securityClassificationTypeInput.getAttribute('value');
  }

  async setSecurityClassificationDetailsInput(securityClassificationDetails: string): Promise<void> {
    await this.securityClassificationDetailsInput.sendKeys(securityClassificationDetails);
  }

  async getSecurityClassificationDetailsInput(): Promise<string> {
    return await this.securityClassificationDetailsInput.getAttribute('value');
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

export class SecurityClassificationTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-securityClassificationType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-securityClassificationType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
