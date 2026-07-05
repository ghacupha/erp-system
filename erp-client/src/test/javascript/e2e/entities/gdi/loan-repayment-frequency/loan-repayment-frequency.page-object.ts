import { element, by, ElementFinder } from 'protractor';

export class LoanRepaymentFrequencyComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-loan-repayment-frequency div table .btn-danger'));
  title = element.all(by.css('jhi-loan-repayment-frequency div h2#page-heading span')).first();
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

export class LoanRepaymentFrequencyUpdatePage {
  pageTitle = element(by.id('jhi-loan-repayment-frequency-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  frequencyTypeCodeInput = element(by.id('field_frequencyTypeCode'));
  frequencyTypeInput = element(by.id('field_frequencyType'));
  frequencyTypeDetailsInput = element(by.id('field_frequencyTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFrequencyTypeCodeInput(frequencyTypeCode: string): Promise<void> {
    await this.frequencyTypeCodeInput.sendKeys(frequencyTypeCode);
  }

  async getFrequencyTypeCodeInput(): Promise<string> {
    return await this.frequencyTypeCodeInput.getAttribute('value');
  }

  async setFrequencyTypeInput(frequencyType: string): Promise<void> {
    await this.frequencyTypeInput.sendKeys(frequencyType);
  }

  async getFrequencyTypeInput(): Promise<string> {
    return await this.frequencyTypeInput.getAttribute('value');
  }

  async setFrequencyTypeDetailsInput(frequencyTypeDetails: string): Promise<void> {
    await this.frequencyTypeDetailsInput.sendKeys(frequencyTypeDetails);
  }

  async getFrequencyTypeDetailsInput(): Promise<string> {
    return await this.frequencyTypeDetailsInput.getAttribute('value');
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

export class LoanRepaymentFrequencyDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-loanRepaymentFrequency-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-loanRepaymentFrequency'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
