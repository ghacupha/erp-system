import { element, by, ElementFinder } from 'protractor';

export class LeaseRepaymentPeriodComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-lease-repayment-period div table .btn-danger'));
  title = element.all(by.css('jhi-lease-repayment-period div h2#page-heading span')).first();
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

export class LeaseRepaymentPeriodUpdatePage {
  pageTitle = element(by.id('jhi-lease-repayment-period-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  sequenceNumberInput = element(by.id('field_sequenceNumber'));
  startDateInput = element(by.id('field_startDate'));
  endDateInput = element(by.id('field_endDate'));
  periodCodeInput = element(by.id('field_periodCode'));

  fiscalMonthSelect = element(by.id('field_fiscalMonth'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSequenceNumberInput(sequenceNumber: string): Promise<void> {
    await this.sequenceNumberInput.sendKeys(sequenceNumber);
  }

  async getSequenceNumberInput(): Promise<string> {
    return await this.sequenceNumberInput.getAttribute('value');
  }

  async setStartDateInput(startDate: string): Promise<void> {
    await this.startDateInput.sendKeys(startDate);
  }

  async getStartDateInput(): Promise<string> {
    return await this.startDateInput.getAttribute('value');
  }

  async setEndDateInput(endDate: string): Promise<void> {
    await this.endDateInput.sendKeys(endDate);
  }

  async getEndDateInput(): Promise<string> {
    return await this.endDateInput.getAttribute('value');
  }

  async setPeriodCodeInput(periodCode: string): Promise<void> {
    await this.periodCodeInput.sendKeys(periodCode);
  }

  async getPeriodCodeInput(): Promise<string> {
    return await this.periodCodeInput.getAttribute('value');
  }

  async fiscalMonthSelectLastOption(): Promise<void> {
    await this.fiscalMonthSelect.all(by.tagName('option')).last().click();
  }

  async fiscalMonthSelectOption(option: string): Promise<void> {
    await this.fiscalMonthSelect.sendKeys(option);
  }

  getFiscalMonthSelect(): ElementFinder {
    return this.fiscalMonthSelect;
  }

  async getFiscalMonthSelectedOption(): Promise<string> {
    return await this.fiscalMonthSelect.element(by.css('option:checked')).getText();
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

export class LeaseRepaymentPeriodDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-leaseRepaymentPeriod-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-leaseRepaymentPeriod'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
