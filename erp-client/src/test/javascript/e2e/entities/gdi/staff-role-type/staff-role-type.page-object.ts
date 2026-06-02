import { element, by, ElementFinder } from 'protractor';

export class StaffRoleTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-staff-role-type div table .btn-danger'));
  title = element.all(by.css('jhi-staff-role-type div h2#page-heading span')).first();
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

export class StaffRoleTypeUpdatePage {
  pageTitle = element(by.id('jhi-staff-role-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  staffRoleTypeCodeInput = element(by.id('field_staffRoleTypeCode'));
  staffRoleTypeInput = element(by.id('field_staffRoleType'));
  staffRoleTypeDetailsInput = element(by.id('field_staffRoleTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setStaffRoleTypeCodeInput(staffRoleTypeCode: string): Promise<void> {
    await this.staffRoleTypeCodeInput.sendKeys(staffRoleTypeCode);
  }

  async getStaffRoleTypeCodeInput(): Promise<string> {
    return await this.staffRoleTypeCodeInput.getAttribute('value');
  }

  async setStaffRoleTypeInput(staffRoleType: string): Promise<void> {
    await this.staffRoleTypeInput.sendKeys(staffRoleType);
  }

  async getStaffRoleTypeInput(): Promise<string> {
    return await this.staffRoleTypeInput.getAttribute('value');
  }

  async setStaffRoleTypeDetailsInput(staffRoleTypeDetails: string): Promise<void> {
    await this.staffRoleTypeDetailsInput.sendKeys(staffRoleTypeDetails);
  }

  async getStaffRoleTypeDetailsInput(): Promise<string> {
    return await this.staffRoleTypeDetailsInput.getAttribute('value');
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

export class StaffRoleTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-staffRoleType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-staffRoleType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
