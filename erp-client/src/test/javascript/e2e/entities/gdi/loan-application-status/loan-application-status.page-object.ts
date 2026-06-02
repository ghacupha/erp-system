import { element, by, ElementFinder } from 'protractor';

export class LoanApplicationStatusComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-loan-application-status div table .btn-danger'));
  title = element.all(by.css('jhi-loan-application-status div h2#page-heading span')).first();
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

export class LoanApplicationStatusUpdatePage {
  pageTitle = element(by.id('jhi-loan-application-status-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  loanApplicationStatusTypeCodeInput = element(by.id('field_loanApplicationStatusTypeCode'));
  loanApplicationStatusTypeInput = element(by.id('field_loanApplicationStatusType'));
  loanApplicationStatusDetailsInput = element(by.id('field_loanApplicationStatusDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setLoanApplicationStatusTypeCodeInput(loanApplicationStatusTypeCode: string): Promise<void> {
    await this.loanApplicationStatusTypeCodeInput.sendKeys(loanApplicationStatusTypeCode);
  }

  async getLoanApplicationStatusTypeCodeInput(): Promise<string> {
    return await this.loanApplicationStatusTypeCodeInput.getAttribute('value');
  }

  async setLoanApplicationStatusTypeInput(loanApplicationStatusType: string): Promise<void> {
    await this.loanApplicationStatusTypeInput.sendKeys(loanApplicationStatusType);
  }

  async getLoanApplicationStatusTypeInput(): Promise<string> {
    return await this.loanApplicationStatusTypeInput.getAttribute('value');
  }

  async setLoanApplicationStatusDetailsInput(loanApplicationStatusDetails: string): Promise<void> {
    await this.loanApplicationStatusDetailsInput.sendKeys(loanApplicationStatusDetails);
  }

  async getLoanApplicationStatusDetailsInput(): Promise<string> {
    return await this.loanApplicationStatusDetailsInput.getAttribute('value');
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

export class LoanApplicationStatusDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-loanApplicationStatus-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-loanApplicationStatus'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
