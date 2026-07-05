import { element, by, ElementFinder } from 'protractor';

export class StaffCurrentEmploymentStatusComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-staff-current-employment-status div table .btn-danger'));
  title = element.all(by.css('jhi-staff-current-employment-status div h2#page-heading span')).first();
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

export class StaffCurrentEmploymentStatusUpdatePage {
  pageTitle = element(by.id('jhi-staff-current-employment-status-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  staffCurrentEmploymentStatusTypeCodeInput = element(by.id('field_staffCurrentEmploymentStatusTypeCode'));
  staffCurrentEmploymentStatusTypeInput = element(by.id('field_staffCurrentEmploymentStatusType'));
  staffCurrentEmploymentStatusTypeDetailsInput = element(by.id('field_staffCurrentEmploymentStatusTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setStaffCurrentEmploymentStatusTypeCodeInput(staffCurrentEmploymentStatusTypeCode: string): Promise<void> {
    await this.staffCurrentEmploymentStatusTypeCodeInput.sendKeys(staffCurrentEmploymentStatusTypeCode);
  }

  async getStaffCurrentEmploymentStatusTypeCodeInput(): Promise<string> {
    return await this.staffCurrentEmploymentStatusTypeCodeInput.getAttribute('value');
  }

  async setStaffCurrentEmploymentStatusTypeInput(staffCurrentEmploymentStatusType: string): Promise<void> {
    await this.staffCurrentEmploymentStatusTypeInput.sendKeys(staffCurrentEmploymentStatusType);
  }

  async getStaffCurrentEmploymentStatusTypeInput(): Promise<string> {
    return await this.staffCurrentEmploymentStatusTypeInput.getAttribute('value');
  }

  async setStaffCurrentEmploymentStatusTypeDetailsInput(staffCurrentEmploymentStatusTypeDetails: string): Promise<void> {
    await this.staffCurrentEmploymentStatusTypeDetailsInput.sendKeys(staffCurrentEmploymentStatusTypeDetails);
  }

  async getStaffCurrentEmploymentStatusTypeDetailsInput(): Promise<string> {
    return await this.staffCurrentEmploymentStatusTypeDetailsInput.getAttribute('value');
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

export class StaffCurrentEmploymentStatusDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-staffCurrentEmploymentStatus-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-staffCurrentEmploymentStatus'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
