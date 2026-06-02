import { element, by, ElementFinder } from 'protractor';

export class ProductTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-product-type div table .btn-danger'));
  title = element.all(by.css('jhi-product-type div h2#page-heading span')).first();
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

export class ProductTypeUpdatePage {
  pageTitle = element(by.id('jhi-product-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  productCodeInput = element(by.id('field_productCode'));
  productTypeInput = element(by.id('field_productType'));
  productTypeDescriptionInput = element(by.id('field_productTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setProductCodeInput(productCode: string): Promise<void> {
    await this.productCodeInput.sendKeys(productCode);
  }

  async getProductCodeInput(): Promise<string> {
    return await this.productCodeInput.getAttribute('value');
  }

  async setProductTypeInput(productType: string): Promise<void> {
    await this.productTypeInput.sendKeys(productType);
  }

  async getProductTypeInput(): Promise<string> {
    return await this.productTypeInput.getAttribute('value');
  }

  async setProductTypeDescriptionInput(productTypeDescription: string): Promise<void> {
    await this.productTypeDescriptionInput.sendKeys(productTypeDescription);
  }

  async getProductTypeDescriptionInput(): Promise<string> {
    return await this.productTypeDescriptionInput.getAttribute('value');
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

export class ProductTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-productType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-productType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
