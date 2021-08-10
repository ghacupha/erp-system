import { element, by, ElementFinder } from 'protractor';

export class FixedAssetNetBookValueComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gha-fixed-asset-net-book-value div table .btn-danger'));
  title = element.all(by.css('gha-fixed-asset-net-book-value div h2#page-heading span')).first();
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

export class FixedAssetNetBookValueUpdatePage {
  pageTitle = element(by.id('gha-fixed-asset-net-book-value-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  assetNumberInput = element(by.id('field_assetNumber'));
  serviceOutletCodeInput = element(by.id('field_serviceOutletCode'));
  assetTagInput = element(by.id('field_assetTag'));
  assetDescriptionInput = element(by.id('field_assetDescription'));
  netBookValueDateInput = element(by.id('field_netBookValueDate'));
  assetCategoryInput = element(by.id('field_assetCategory'));
  netBookValueInput = element(by.id('field_netBookValue'));
  depreciationRegimeSelect = element(by.id('field_depreciationRegime'));
  fileUploadTokenInput = element(by.id('field_fileUploadToken'));
  compilationTokenInput = element(by.id('field_compilationToken'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAssetNumberInput(assetNumber: string): Promise<void> {
    await this.assetNumberInput.sendKeys(assetNumber);
  }

  async getAssetNumberInput(): Promise<string> {
    return await this.assetNumberInput.getAttribute('value');
  }

  async setServiceOutletCodeInput(serviceOutletCode: string): Promise<void> {
    await this.serviceOutletCodeInput.sendKeys(serviceOutletCode);
  }

  async getServiceOutletCodeInput(): Promise<string> {
    return await this.serviceOutletCodeInput.getAttribute('value');
  }

  async setAssetTagInput(assetTag: string): Promise<void> {
    await this.assetTagInput.sendKeys(assetTag);
  }

  async getAssetTagInput(): Promise<string> {
    return await this.assetTagInput.getAttribute('value');
  }

  async setAssetDescriptionInput(assetDescription: string): Promise<void> {
    await this.assetDescriptionInput.sendKeys(assetDescription);
  }

  async getAssetDescriptionInput(): Promise<string> {
    return await this.assetDescriptionInput.getAttribute('value');
  }

  async setNetBookValueDateInput(netBookValueDate: string): Promise<void> {
    await this.netBookValueDateInput.sendKeys(netBookValueDate);
  }

  async getNetBookValueDateInput(): Promise<string> {
    return await this.netBookValueDateInput.getAttribute('value');
  }

  async setAssetCategoryInput(assetCategory: string): Promise<void> {
    await this.assetCategoryInput.sendKeys(assetCategory);
  }

  async getAssetCategoryInput(): Promise<string> {
    return await this.assetCategoryInput.getAttribute('value');
  }

  async setNetBookValueInput(netBookValue: string): Promise<void> {
    await this.netBookValueInput.sendKeys(netBookValue);
  }

  async getNetBookValueInput(): Promise<string> {
    return await this.netBookValueInput.getAttribute('value');
  }

  async setDepreciationRegimeSelect(depreciationRegime: string): Promise<void> {
    await this.depreciationRegimeSelect.sendKeys(depreciationRegime);
  }

  async getDepreciationRegimeSelect(): Promise<string> {
    return await this.depreciationRegimeSelect.element(by.css('option:checked')).getText();
  }

  async depreciationRegimeSelectLastOption(): Promise<void> {
    await this.depreciationRegimeSelect.all(by.tagName('option')).last().click();
  }

  async setFileUploadTokenInput(fileUploadToken: string): Promise<void> {
    await this.fileUploadTokenInput.sendKeys(fileUploadToken);
  }

  async getFileUploadTokenInput(): Promise<string> {
    return await this.fileUploadTokenInput.getAttribute('value');
  }

  async setCompilationTokenInput(compilationToken: string): Promise<void> {
    await this.compilationTokenInput.sendKeys(compilationToken);
  }

  async getCompilationTokenInput(): Promise<string> {
    return await this.compilationTokenInput.getAttribute('value');
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

export class FixedAssetNetBookValueDeleteDialog {
  private dialogTitle = element(by.id('gha-delete-fixedAssetNetBookValue-heading'));
  private confirmButton = element(by.id('gha-confirm-delete-fixedAssetNetBookValue'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
