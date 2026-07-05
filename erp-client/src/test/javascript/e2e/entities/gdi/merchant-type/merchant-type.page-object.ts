import { element, by, ElementFinder } from 'protractor';

export class MerchantTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-merchant-type div table .btn-danger'));
  title = element.all(by.css('jhi-merchant-type div h2#page-heading span')).first();
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

export class MerchantTypeUpdatePage {
  pageTitle = element(by.id('jhi-merchant-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  merchantTypeCodeInput = element(by.id('field_merchantTypeCode'));
  merchantTypeInput = element(by.id('field_merchantType'));
  merchantTypeDetailsInput = element(by.id('field_merchantTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setMerchantTypeCodeInput(merchantTypeCode: string): Promise<void> {
    await this.merchantTypeCodeInput.sendKeys(merchantTypeCode);
  }

  async getMerchantTypeCodeInput(): Promise<string> {
    return await this.merchantTypeCodeInput.getAttribute('value');
  }

  async setMerchantTypeInput(merchantType: string): Promise<void> {
    await this.merchantTypeInput.sendKeys(merchantType);
  }

  async getMerchantTypeInput(): Promise<string> {
    return await this.merchantTypeInput.getAttribute('value');
  }

  async setMerchantTypeDetailsInput(merchantTypeDetails: string): Promise<void> {
    await this.merchantTypeDetailsInput.sendKeys(merchantTypeDetails);
  }

  async getMerchantTypeDetailsInput(): Promise<string> {
    return await this.merchantTypeDetailsInput.getAttribute('value');
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

export class MerchantTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-merchantType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-merchantType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
