import { element, by, ElementFinder } from 'protractor';

export class ReportTemplateComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-report-template div table .btn-danger'));
  title = element.all(by.css('jhi-report-template div h2#page-heading span')).first();
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

export class ReportTemplateUpdatePage {
  pageTitle = element(by.id('jhi-report-template-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  catalogueNumberInput = element(by.id('field_catalogueNumber'));
  descriptionInput = element(by.id('field_description'));
  notesInput = element(by.id('file_notes'));
  reportFileInput = element(by.id('file_reportFile'));
  compileReportFileInput = element(by.id('file_compileReportFile'));

  placeholderSelect = element(by.id('field_placeholder'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCatalogueNumberInput(catalogueNumber: string): Promise<void> {
    await this.catalogueNumberInput.sendKeys(catalogueNumber);
  }

  async getCatalogueNumberInput(): Promise<string> {
    return await this.catalogueNumberInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setNotesInput(notes: string): Promise<void> {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput(): Promise<string> {
    return await this.notesInput.getAttribute('value');
  }

  async setReportFileInput(reportFile: string): Promise<void> {
    await this.reportFileInput.sendKeys(reportFile);
  }

  async getReportFileInput(): Promise<string> {
    return await this.reportFileInput.getAttribute('value');
  }

  async setCompileReportFileInput(compileReportFile: string): Promise<void> {
    await this.compileReportFileInput.sendKeys(compileReportFile);
  }

  async getCompileReportFileInput(): Promise<string> {
    return await this.compileReportFileInput.getAttribute('value');
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

export class ReportTemplateDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-reportTemplate-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-reportTemplate'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
