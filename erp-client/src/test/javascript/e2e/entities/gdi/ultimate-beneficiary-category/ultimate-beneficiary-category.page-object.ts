import { element, by, ElementFinder } from 'protractor';

export class UltimateBeneficiaryCategoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-ultimate-beneficiary-category div table .btn-danger'));
  title = element.all(by.css('jhi-ultimate-beneficiary-category div h2#page-heading span')).first();
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

export class UltimateBeneficiaryCategoryUpdatePage {
  pageTitle = element(by.id('jhi-ultimate-beneficiary-category-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  ultimateBeneficiaryCategoryTypeCodeInput = element(by.id('field_ultimateBeneficiaryCategoryTypeCode'));
  ultimateBeneficiaryTypeInput = element(by.id('field_ultimateBeneficiaryType'));
  ultimateBeneficiaryCategoryTypeDetailsInput = element(by.id('field_ultimateBeneficiaryCategoryTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setUltimateBeneficiaryCategoryTypeCodeInput(ultimateBeneficiaryCategoryTypeCode: string): Promise<void> {
    await this.ultimateBeneficiaryCategoryTypeCodeInput.sendKeys(ultimateBeneficiaryCategoryTypeCode);
  }

  async getUltimateBeneficiaryCategoryTypeCodeInput(): Promise<string> {
    return await this.ultimateBeneficiaryCategoryTypeCodeInput.getAttribute('value');
  }

  async setUltimateBeneficiaryTypeInput(ultimateBeneficiaryType: string): Promise<void> {
    await this.ultimateBeneficiaryTypeInput.sendKeys(ultimateBeneficiaryType);
  }

  async getUltimateBeneficiaryTypeInput(): Promise<string> {
    return await this.ultimateBeneficiaryTypeInput.getAttribute('value');
  }

  async setUltimateBeneficiaryCategoryTypeDetailsInput(ultimateBeneficiaryCategoryTypeDetails: string): Promise<void> {
    await this.ultimateBeneficiaryCategoryTypeDetailsInput.sendKeys(ultimateBeneficiaryCategoryTypeDetails);
  }

  async getUltimateBeneficiaryCategoryTypeDetailsInput(): Promise<string> {
    return await this.ultimateBeneficiaryCategoryTypeDetailsInput.getAttribute('value');
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

export class UltimateBeneficiaryCategoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-ultimateBeneficiaryCategory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-ultimateBeneficiaryCategory'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
