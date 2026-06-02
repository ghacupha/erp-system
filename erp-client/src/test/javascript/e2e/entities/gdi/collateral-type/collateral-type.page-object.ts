import { element, by, ElementFinder } from 'protractor';

export class CollateralTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-collateral-type div table .btn-danger'));
  title = element.all(by.css('jhi-collateral-type div h2#page-heading span')).first();
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

export class CollateralTypeUpdatePage {
  pageTitle = element(by.id('jhi-collateral-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  collateralTypeCodeInput = element(by.id('field_collateralTypeCode'));
  collateralTypeInput = element(by.id('field_collateralType'));
  collateralTypeDescriptionInput = element(by.id('field_collateralTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCollateralTypeCodeInput(collateralTypeCode: string): Promise<void> {
    await this.collateralTypeCodeInput.sendKeys(collateralTypeCode);
  }

  async getCollateralTypeCodeInput(): Promise<string> {
    return await this.collateralTypeCodeInput.getAttribute('value');
  }

  async setCollateralTypeInput(collateralType: string): Promise<void> {
    await this.collateralTypeInput.sendKeys(collateralType);
  }

  async getCollateralTypeInput(): Promise<string> {
    return await this.collateralTypeInput.getAttribute('value');
  }

  async setCollateralTypeDescriptionInput(collateralTypeDescription: string): Promise<void> {
    await this.collateralTypeDescriptionInput.sendKeys(collateralTypeDescription);
  }

  async getCollateralTypeDescriptionInput(): Promise<string> {
    return await this.collateralTypeDescriptionInput.getAttribute('value');
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

export class CollateralTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-collateralType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-collateralType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
