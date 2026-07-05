import { element, by, ElementFinder } from 'protractor';

export class CrbRecordFileTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-crb-record-file-type div table .btn-danger'));
  title = element.all(by.css('jhi-crb-record-file-type div h2#page-heading span')).first();
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

export class CrbRecordFileTypeUpdatePage {
  pageTitle = element(by.id('jhi-crb-record-file-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  recordFileTypeCodeInput = element(by.id('field_recordFileTypeCode'));
  recordFileTypeInput = element(by.id('field_recordFileType'));
  recordFileTypeDetailsInput = element(by.id('field_recordFileTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setRecordFileTypeCodeInput(recordFileTypeCode: string): Promise<void> {
    await this.recordFileTypeCodeInput.sendKeys(recordFileTypeCode);
  }

  async getRecordFileTypeCodeInput(): Promise<string> {
    return await this.recordFileTypeCodeInput.getAttribute('value');
  }

  async setRecordFileTypeInput(recordFileType: string): Promise<void> {
    await this.recordFileTypeInput.sendKeys(recordFileType);
  }

  async getRecordFileTypeInput(): Promise<string> {
    return await this.recordFileTypeInput.getAttribute('value');
  }

  async setRecordFileTypeDetailsInput(recordFileTypeDetails: string): Promise<void> {
    await this.recordFileTypeDetailsInput.sendKeys(recordFileTypeDetails);
  }

  async getRecordFileTypeDetailsInput(): Promise<string> {
    return await this.recordFileTypeDetailsInput.getAttribute('value');
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

export class CrbRecordFileTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-crbRecordFileType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-crbRecordFileType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
