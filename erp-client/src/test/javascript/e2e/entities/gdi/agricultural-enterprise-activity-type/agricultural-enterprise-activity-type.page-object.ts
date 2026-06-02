import { element, by, ElementFinder } from 'protractor';

export class AgriculturalEnterpriseActivityTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-agricultural-enterprise-activity-type div table .btn-danger'));
  title = element.all(by.css('jhi-agricultural-enterprise-activity-type div h2#page-heading span')).first();
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

export class AgriculturalEnterpriseActivityTypeUpdatePage {
  pageTitle = element(by.id('jhi-agricultural-enterprise-activity-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  agriculturalEnterpriseActivityTypeCodeInput = element(by.id('field_agriculturalEnterpriseActivityTypeCode'));
  agriculturalEnterpriseActivityTypeInput = element(by.id('field_agriculturalEnterpriseActivityType'));
  agriculturalEnterpriseActivityTypeDescriptionInput = element(by.id('field_agriculturalEnterpriseActivityTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAgriculturalEnterpriseActivityTypeCodeInput(agriculturalEnterpriseActivityTypeCode: string): Promise<void> {
    await this.agriculturalEnterpriseActivityTypeCodeInput.sendKeys(agriculturalEnterpriseActivityTypeCode);
  }

  async getAgriculturalEnterpriseActivityTypeCodeInput(): Promise<string> {
    return await this.agriculturalEnterpriseActivityTypeCodeInput.getAttribute('value');
  }

  async setAgriculturalEnterpriseActivityTypeInput(agriculturalEnterpriseActivityType: string): Promise<void> {
    await this.agriculturalEnterpriseActivityTypeInput.sendKeys(agriculturalEnterpriseActivityType);
  }

  async getAgriculturalEnterpriseActivityTypeInput(): Promise<string> {
    return await this.agriculturalEnterpriseActivityTypeInput.getAttribute('value');
  }

  async setAgriculturalEnterpriseActivityTypeDescriptionInput(agriculturalEnterpriseActivityTypeDescription: string): Promise<void> {
    await this.agriculturalEnterpriseActivityTypeDescriptionInput.sendKeys(agriculturalEnterpriseActivityTypeDescription);
  }

  async getAgriculturalEnterpriseActivityTypeDescriptionInput(): Promise<string> {
    return await this.agriculturalEnterpriseActivityTypeDescriptionInput.getAttribute('value');
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

export class AgriculturalEnterpriseActivityTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-agriculturalEnterpriseActivityType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-agriculturalEnterpriseActivityType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
