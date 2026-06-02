import { element, by, ElementFinder } from 'protractor';

export class PrepaymentMarshallingComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-prepayment-marshalling div table .btn-danger'));
  title = element.all(by.css('jhi-prepayment-marshalling div h2#page-heading span')).first();
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

export class PrepaymentMarshallingUpdatePage {
  pageTitle = element(by.id('jhi-prepayment-marshalling-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  inactiveInput = element(by.id('field_inactive'));
  amortizationPeriodsInput = element(by.id('field_amortizationPeriods'));
  processedInput = element(by.id('field_processed'));

  prepaymentAccountSelect = element(by.id('field_prepaymentAccount'));
  firstAmortizationPeriodSelect = element(by.id('field_firstAmortizationPeriod'));
  placeholderSelect = element(by.id('field_placeholder'));
  firstFiscalMonthSelect = element(by.id('field_firstFiscalMonth'));
  lastFiscalMonthSelect = element(by.id('field_lastFiscalMonth'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  getInactiveInput(): ElementFinder {
    return this.inactiveInput;
  }

  async setAmortizationPeriodsInput(amortizationPeriods: string): Promise<void> {
    await this.amortizationPeriodsInput.sendKeys(amortizationPeriods);
  }

  async getAmortizationPeriodsInput(): Promise<string> {
    return await this.amortizationPeriodsInput.getAttribute('value');
  }

  getProcessedInput(): ElementFinder {
    return this.processedInput;
  }

  async prepaymentAccountSelectLastOption(): Promise<void> {
    await this.prepaymentAccountSelect.all(by.tagName('option')).last().click();
  }

  async prepaymentAccountSelectOption(option: string): Promise<void> {
    await this.prepaymentAccountSelect.sendKeys(option);
  }

  getPrepaymentAccountSelect(): ElementFinder {
    return this.prepaymentAccountSelect;
  }

  async getPrepaymentAccountSelectedOption(): Promise<string> {
    return await this.prepaymentAccountSelect.element(by.css('option:checked')).getText();
  }

  async firstAmortizationPeriodSelectLastOption(): Promise<void> {
    await this.firstAmortizationPeriodSelect.all(by.tagName('option')).last().click();
  }

  async firstAmortizationPeriodSelectOption(option: string): Promise<void> {
    await this.firstAmortizationPeriodSelect.sendKeys(option);
  }

  getFirstAmortizationPeriodSelect(): ElementFinder {
    return this.firstAmortizationPeriodSelect;
  }

  async getFirstAmortizationPeriodSelectedOption(): Promise<string> {
    return await this.firstAmortizationPeriodSelect.element(by.css('option:checked')).getText();
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

  async firstFiscalMonthSelectLastOption(): Promise<void> {
    await this.firstFiscalMonthSelect.all(by.tagName('option')).last().click();
  }

  async firstFiscalMonthSelectOption(option: string): Promise<void> {
    await this.firstFiscalMonthSelect.sendKeys(option);
  }

  getFirstFiscalMonthSelect(): ElementFinder {
    return this.firstFiscalMonthSelect;
  }

  async getFirstFiscalMonthSelectedOption(): Promise<string> {
    return await this.firstFiscalMonthSelect.element(by.css('option:checked')).getText();
  }

  async lastFiscalMonthSelectLastOption(): Promise<void> {
    await this.lastFiscalMonthSelect.all(by.tagName('option')).last().click();
  }

  async lastFiscalMonthSelectOption(option: string): Promise<void> {
    await this.lastFiscalMonthSelect.sendKeys(option);
  }

  getLastFiscalMonthSelect(): ElementFinder {
    return this.lastFiscalMonthSelect;
  }

  async getLastFiscalMonthSelectedOption(): Promise<string> {
    return await this.lastFiscalMonthSelect.element(by.css('option:checked')).getText();
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

export class PrepaymentMarshallingDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-prepaymentMarshalling-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-prepaymentMarshalling'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
