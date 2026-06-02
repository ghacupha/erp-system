import { element, by, ElementFinder } from 'protractor';

export class DerivativeUnderlyingAssetComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-derivative-underlying-asset div table .btn-danger'));
  title = element.all(by.css('jhi-derivative-underlying-asset div h2#page-heading span')).first();
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

export class DerivativeUnderlyingAssetUpdatePage {
  pageTitle = element(by.id('jhi-derivative-underlying-asset-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  derivativeUnderlyingAssetTypeCodeInput = element(by.id('field_derivativeUnderlyingAssetTypeCode'));
  financialDerivativeUnderlyingAssetTypeInput = element(by.id('field_financialDerivativeUnderlyingAssetType'));
  derivativeUnderlyingAssetTypeDetailsInput = element(by.id('field_derivativeUnderlyingAssetTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDerivativeUnderlyingAssetTypeCodeInput(derivativeUnderlyingAssetTypeCode: string): Promise<void> {
    await this.derivativeUnderlyingAssetTypeCodeInput.sendKeys(derivativeUnderlyingAssetTypeCode);
  }

  async getDerivativeUnderlyingAssetTypeCodeInput(): Promise<string> {
    return await this.derivativeUnderlyingAssetTypeCodeInput.getAttribute('value');
  }

  async setFinancialDerivativeUnderlyingAssetTypeInput(financialDerivativeUnderlyingAssetType: string): Promise<void> {
    await this.financialDerivativeUnderlyingAssetTypeInput.sendKeys(financialDerivativeUnderlyingAssetType);
  }

  async getFinancialDerivativeUnderlyingAssetTypeInput(): Promise<string> {
    return await this.financialDerivativeUnderlyingAssetTypeInput.getAttribute('value');
  }

  async setDerivativeUnderlyingAssetTypeDetailsInput(derivativeUnderlyingAssetTypeDetails: string): Promise<void> {
    await this.derivativeUnderlyingAssetTypeDetailsInput.sendKeys(derivativeUnderlyingAssetTypeDetails);
  }

  async getDerivativeUnderlyingAssetTypeDetailsInput(): Promise<string> {
    return await this.derivativeUnderlyingAssetTypeDetailsInput.getAttribute('value');
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

export class DerivativeUnderlyingAssetDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-derivativeUnderlyingAsset-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-derivativeUnderlyingAsset'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
