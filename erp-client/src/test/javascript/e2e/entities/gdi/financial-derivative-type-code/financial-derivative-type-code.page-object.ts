import { element, by, ElementFinder } from 'protractor';

export class FinancialDerivativeTypeCodeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-financial-derivative-type-code div table .btn-danger'));
  title = element.all(by.css('jhi-financial-derivative-type-code div h2#page-heading span')).first();
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

export class FinancialDerivativeTypeCodeUpdatePage {
  pageTitle = element(by.id('jhi-financial-derivative-type-code-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  financialDerivativeTypeCodeInput = element(by.id('field_financialDerivativeTypeCode'));
  financialDerivativeTypeInput = element(by.id('field_financialDerivativeType'));
  financialDerivativeTypeDetailsInput = element(by.id('field_financialDerivativeTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFinancialDerivativeTypeCodeInput(financialDerivativeTypeCode: string): Promise<void> {
    await this.financialDerivativeTypeCodeInput.sendKeys(financialDerivativeTypeCode);
  }

  async getFinancialDerivativeTypeCodeInput(): Promise<string> {
    return await this.financialDerivativeTypeCodeInput.getAttribute('value');
  }

  async setFinancialDerivativeTypeInput(financialDerivativeType: string): Promise<void> {
    await this.financialDerivativeTypeInput.sendKeys(financialDerivativeType);
  }

  async getFinancialDerivativeTypeInput(): Promise<string> {
    return await this.financialDerivativeTypeInput.getAttribute('value');
  }

  async setFinancialDerivativeTypeDetailsInput(financialDerivativeTypeDetails: string): Promise<void> {
    await this.financialDerivativeTypeDetailsInput.sendKeys(financialDerivativeTypeDetails);
  }

  async getFinancialDerivativeTypeDetailsInput(): Promise<string> {
    return await this.financialDerivativeTypeDetailsInput.getAttribute('value');
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

export class FinancialDerivativeTypeCodeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-financialDerivativeTypeCode-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-financialDerivativeTypeCode'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
