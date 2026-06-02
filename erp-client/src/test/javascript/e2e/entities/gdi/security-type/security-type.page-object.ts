import { element, by, ElementFinder } from 'protractor';

export class SecurityTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-security-type div table .btn-danger'));
  title = element.all(by.css('jhi-security-type div h2#page-heading span')).first();
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

export class SecurityTypeUpdatePage {
  pageTitle = element(by.id('jhi-security-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  securityTypeCodeInput = element(by.id('field_securityTypeCode'));
  securityTypeInput = element(by.id('field_securityType'));
  securityTypeDetailsInput = element(by.id('field_securityTypeDetails'));
  securityTypeDescriptionInput = element(by.id('field_securityTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSecurityTypeCodeInput(securityTypeCode: string): Promise<void> {
    await this.securityTypeCodeInput.sendKeys(securityTypeCode);
  }

  async getSecurityTypeCodeInput(): Promise<string> {
    return await this.securityTypeCodeInput.getAttribute('value');
  }

  async setSecurityTypeInput(securityType: string): Promise<void> {
    await this.securityTypeInput.sendKeys(securityType);
  }

  async getSecurityTypeInput(): Promise<string> {
    return await this.securityTypeInput.getAttribute('value');
  }

  async setSecurityTypeDetailsInput(securityTypeDetails: string): Promise<void> {
    await this.securityTypeDetailsInput.sendKeys(securityTypeDetails);
  }

  async getSecurityTypeDetailsInput(): Promise<string> {
    return await this.securityTypeDetailsInput.getAttribute('value');
  }

  async setSecurityTypeDescriptionInput(securityTypeDescription: string): Promise<void> {
    await this.securityTypeDescriptionInput.sendKeys(securityTypeDescription);
  }

  async getSecurityTypeDescriptionInput(): Promise<string> {
    return await this.securityTypeDescriptionInput.getAttribute('value');
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

export class SecurityTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-securityType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-securityType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
