import { element, by, ElementFinder } from 'protractor';

export class CrbCreditFacilityTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-crb-credit-facility-type div table .btn-danger'));
  title = element.all(by.css('jhi-crb-credit-facility-type div h2#page-heading span')).first();
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

export class CrbCreditFacilityTypeUpdatePage {
  pageTitle = element(by.id('jhi-crb-credit-facility-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  creditFacilityTypeCodeInput = element(by.id('field_creditFacilityTypeCode'));
  creditFacilityTypeInput = element(by.id('field_creditFacilityType'));
  creditFacilityDescriptionInput = element(by.id('field_creditFacilityDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCreditFacilityTypeCodeInput(creditFacilityTypeCode: string): Promise<void> {
    await this.creditFacilityTypeCodeInput.sendKeys(creditFacilityTypeCode);
  }

  async getCreditFacilityTypeCodeInput(): Promise<string> {
    return await this.creditFacilityTypeCodeInput.getAttribute('value');
  }

  async setCreditFacilityTypeInput(creditFacilityType: string): Promise<void> {
    await this.creditFacilityTypeInput.sendKeys(creditFacilityType);
  }

  async getCreditFacilityTypeInput(): Promise<string> {
    return await this.creditFacilityTypeInput.getAttribute('value');
  }

  async setCreditFacilityDescriptionInput(creditFacilityDescription: string): Promise<void> {
    await this.creditFacilityDescriptionInput.sendKeys(creditFacilityDescription);
  }

  async getCreditFacilityDescriptionInput(): Promise<string> {
    return await this.creditFacilityDescriptionInput.getAttribute('value');
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

export class CrbCreditFacilityTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-crbCreditFacilityType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-crbCreditFacilityType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
