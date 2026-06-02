import { element, by, ElementFinder } from 'protractor';

export class LoanPerformanceClassificationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-loan-performance-classification div table .btn-danger'));
  title = element.all(by.css('jhi-loan-performance-classification div h2#page-heading span')).first();
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

export class LoanPerformanceClassificationUpdatePage {
  pageTitle = element(by.id('jhi-loan-performance-classification-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  loanPerformanceClassificationCodeInput = element(by.id('field_loanPerformanceClassificationCode'));
  loanPerformanceClassificationTypeInput = element(by.id('field_loanPerformanceClassificationType'));
  commercialBankDescriptionInput = element(by.id('field_commercialBankDescription'));
  microfinanceDescriptionInput = element(by.id('field_microfinanceDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setLoanPerformanceClassificationCodeInput(loanPerformanceClassificationCode: string): Promise<void> {
    await this.loanPerformanceClassificationCodeInput.sendKeys(loanPerformanceClassificationCode);
  }

  async getLoanPerformanceClassificationCodeInput(): Promise<string> {
    return await this.loanPerformanceClassificationCodeInput.getAttribute('value');
  }

  async setLoanPerformanceClassificationTypeInput(loanPerformanceClassificationType: string): Promise<void> {
    await this.loanPerformanceClassificationTypeInput.sendKeys(loanPerformanceClassificationType);
  }

  async getLoanPerformanceClassificationTypeInput(): Promise<string> {
    return await this.loanPerformanceClassificationTypeInput.getAttribute('value');
  }

  async setCommercialBankDescriptionInput(commercialBankDescription: string): Promise<void> {
    await this.commercialBankDescriptionInput.sendKeys(commercialBankDescription);
  }

  async getCommercialBankDescriptionInput(): Promise<string> {
    return await this.commercialBankDescriptionInput.getAttribute('value');
  }

  async setMicrofinanceDescriptionInput(microfinanceDescription: string): Promise<void> {
    await this.microfinanceDescriptionInput.sendKeys(microfinanceDescription);
  }

  async getMicrofinanceDescriptionInput(): Promise<string> {
    return await this.microfinanceDescriptionInput.getAttribute('value');
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

export class LoanPerformanceClassificationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-loanPerformanceClassification-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-loanPerformanceClassification'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
