import { element, by, ElementFinder } from 'protractor';

export class LeaseAmortizationScheduleComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-lease-amortization-schedule div table .btn-danger'));
  title = element.all(by.css('jhi-lease-amortization-schedule div h2#page-heading span')).first();
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

export class LeaseAmortizationScheduleUpdatePage {
  pageTitle = element(by.id('jhi-lease-amortization-schedule-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  identifierInput = element(by.id('field_identifier'));

  leaseLiabilitySelect = element(by.id('field_leaseLiability'));
  leaseContractSelect = element(by.id('field_leaseContract'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setIdentifierInput(identifier: string): Promise<void> {
    await this.identifierInput.sendKeys(identifier);
  }

  async getIdentifierInput(): Promise<string> {
    return await this.identifierInput.getAttribute('value');
  }

  async leaseLiabilitySelectLastOption(): Promise<void> {
    await this.leaseLiabilitySelect.all(by.tagName('option')).last().click();
  }

  async leaseLiabilitySelectOption(option: string): Promise<void> {
    await this.leaseLiabilitySelect.sendKeys(option);
  }

  getLeaseLiabilitySelect(): ElementFinder {
    return this.leaseLiabilitySelect;
  }

  async getLeaseLiabilitySelectedOption(): Promise<string> {
    return await this.leaseLiabilitySelect.element(by.css('option:checked')).getText();
  }

  async leaseContractSelectLastOption(): Promise<void> {
    await this.leaseContractSelect.all(by.tagName('option')).last().click();
  }

  async leaseContractSelectOption(option: string): Promise<void> {
    await this.leaseContractSelect.sendKeys(option);
  }

  getLeaseContractSelect(): ElementFinder {
    return this.leaseContractSelect;
  }

  async getLeaseContractSelectedOption(): Promise<string> {
    return await this.leaseContractSelect.element(by.css('option:checked')).getText();
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

export class LeaseAmortizationScheduleDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-leaseAmortizationSchedule-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-leaseAmortizationSchedule'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
