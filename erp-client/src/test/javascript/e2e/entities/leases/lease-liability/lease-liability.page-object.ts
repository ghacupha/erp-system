import { element, by, ElementFinder } from 'protractor';

export class LeaseLiabilityComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-lease-liability div table .btn-danger'));
  title = element.all(by.css('jhi-lease-liability div h2#page-heading span')).first();
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

export class LeaseLiabilityUpdatePage {
  pageTitle = element(by.id('jhi-lease-liability-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  leaseIdInput = element(by.id('field_leaseId'));
  liabilityAmountInput = element(by.id('field_liabilityAmount'));
  startDateInput = element(by.id('field_startDate'));
  endDateInput = element(by.id('field_endDate'));
  interestRateInput = element(by.id('field_interestRate'));
  hasBeenFullyAmortisedInput = element(by.id('field_hasBeenFullyAmortised'));

  leaseAmortizationCalculationSelect = element(by.id('field_leaseAmortizationCalculation'));
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

  async setLeaseIdInput(leaseId: string): Promise<void> {
    await this.leaseIdInput.sendKeys(leaseId);
  }

  async getLeaseIdInput(): Promise<string> {
    return await this.leaseIdInput.getAttribute('value');
  }

  async setLiabilityAmountInput(liabilityAmount: string): Promise<void> {
    await this.liabilityAmountInput.sendKeys(liabilityAmount);
  }

  async getLiabilityAmountInput(): Promise<string> {
    return await this.liabilityAmountInput.getAttribute('value');
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

  async setInterestRateInput(interestRate: string): Promise<void> {
    await this.interestRateInput.sendKeys(interestRate);
  }

  async getInterestRateInput(): Promise<string> {
    return await this.interestRateInput.getAttribute('value');
  }

  getHasBeenFullyAmortisedInput(): ElementFinder {
    return this.hasBeenFullyAmortisedInput;
  }

  async leaseAmortizationCalculationSelectLastOption(): Promise<void> {
    await this.leaseAmortizationCalculationSelect.all(by.tagName('option')).last().click();
  }

  async leaseAmortizationCalculationSelectOption(option: string): Promise<void> {
    await this.leaseAmortizationCalculationSelect.sendKeys(option);
  }

  getLeaseAmortizationCalculationSelect(): ElementFinder {
    return this.leaseAmortizationCalculationSelect;
  }

  async getLeaseAmortizationCalculationSelectedOption(): Promise<string> {
    return await this.leaseAmortizationCalculationSelect.element(by.css('option:checked')).getText();
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

export class LeaseLiabilityDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-leaseLiability-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-leaseLiability'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
