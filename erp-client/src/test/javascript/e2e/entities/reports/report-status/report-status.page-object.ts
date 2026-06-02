import { element, by, ElementFinder } from 'protractor';

export class ReportStatusComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-report-status div table .btn-danger'));
  title = element.all(by.css('jhi-report-status div h2#page-heading span')).first();
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

export class ReportStatusUpdatePage {
  pageTitle = element(by.id('jhi-report-status-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportNameInput = element(by.id('field_reportName'));
  reportIdInput = element(by.id('field_reportId'));

  placeholderSelect = element(by.id('field_placeholder'));
  processStatusSelect = element(by.id('field_processStatus'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setReportNameInput(reportName: string): Promise<void> {
    await this.reportNameInput.sendKeys(reportName);
  }

  async getReportNameInput(): Promise<string> {
    return await this.reportNameInput.getAttribute('value');
  }

  async setReportIdInput(reportId: string): Promise<void> {
    await this.reportIdInput.sendKeys(reportId);
  }

  async getReportIdInput(): Promise<string> {
    return await this.reportIdInput.getAttribute('value');
  }

  async placeholderSelectLastOption(): Promise<void> {
    await this.placeholderSelect.all(by.tagName('option')).last().click();
  }

  async placeholderSelectOption(option: string): Promise<void> {
    await this.placeholderSelect.sendKeys(option);
  }

  getPlaceholderSelect(): ElementFinder {
    return this.placeholderSelect;
  }

  async getPlaceholderSelectedOption(): Promise<string> {
    return await this.placeholderSelect.element(by.css('option:checked')).getText();
  }

  async processStatusSelectLastOption(): Promise<void> {
    await this.processStatusSelect.all(by.tagName('option')).last().click();
  }

  async processStatusSelectOption(option: string): Promise<void> {
    await this.processStatusSelect.sendKeys(option);
  }

  getProcessStatusSelect(): ElementFinder {
    return this.processStatusSelect;
  }

  async getProcessStatusSelectedOption(): Promise<string> {
    return await this.processStatusSelect.element(by.css('option:checked')).getText();
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

export class ReportStatusDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-reportStatus-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-reportStatus'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
