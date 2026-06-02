import { element, by, ElementFinder } from 'protractor';

export class LoanDeclineReasonComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-loan-decline-reason div table .btn-danger'));
  title = element.all(by.css('jhi-loan-decline-reason div h2#page-heading span')).first();
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

export class LoanDeclineReasonUpdatePage {
  pageTitle = element(by.id('jhi-loan-decline-reason-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  loanDeclineReasonTypeCodeInput = element(by.id('field_loanDeclineReasonTypeCode'));
  loanDeclineReasonTypeInput = element(by.id('field_loanDeclineReasonType'));
  loanDeclineReasonDetailsInput = element(by.id('field_loanDeclineReasonDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setLoanDeclineReasonTypeCodeInput(loanDeclineReasonTypeCode: string): Promise<void> {
    await this.loanDeclineReasonTypeCodeInput.sendKeys(loanDeclineReasonTypeCode);
  }

  async getLoanDeclineReasonTypeCodeInput(): Promise<string> {
    return await this.loanDeclineReasonTypeCodeInput.getAttribute('value');
  }

  async setLoanDeclineReasonTypeInput(loanDeclineReasonType: string): Promise<void> {
    await this.loanDeclineReasonTypeInput.sendKeys(loanDeclineReasonType);
  }

  async getLoanDeclineReasonTypeInput(): Promise<string> {
    return await this.loanDeclineReasonTypeInput.getAttribute('value');
  }

  async setLoanDeclineReasonDetailsInput(loanDeclineReasonDetails: string): Promise<void> {
    await this.loanDeclineReasonDetailsInput.sendKeys(loanDeclineReasonDetails);
  }

  async getLoanDeclineReasonDetailsInput(): Promise<string> {
    return await this.loanDeclineReasonDetailsInput.getAttribute('value');
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

export class LoanDeclineReasonDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-loanDeclineReason-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-loanDeclineReason'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
