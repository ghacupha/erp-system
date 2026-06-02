import { element, by, ElementFinder } from 'protractor';

export class ManagementMemberTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-management-member-type div table .btn-danger'));
  title = element.all(by.css('jhi-management-member-type div h2#page-heading span')).first();
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

export class ManagementMemberTypeUpdatePage {
  pageTitle = element(by.id('jhi-management-member-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  managementMemberTypeCodeInput = element(by.id('field_managementMemberTypeCode'));
  managementMemberTypeInput = element(by.id('field_managementMemberType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setManagementMemberTypeCodeInput(managementMemberTypeCode: string): Promise<void> {
    await this.managementMemberTypeCodeInput.sendKeys(managementMemberTypeCode);
  }

  async getManagementMemberTypeCodeInput(): Promise<string> {
    return await this.managementMemberTypeCodeInput.getAttribute('value');
  }

  async setManagementMemberTypeInput(managementMemberType: string): Promise<void> {
    await this.managementMemberTypeInput.sendKeys(managementMemberType);
  }

  async getManagementMemberTypeInput(): Promise<string> {
    return await this.managementMemberTypeInput.getAttribute('value');
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

export class ManagementMemberTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-managementMemberType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-managementMemberType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
