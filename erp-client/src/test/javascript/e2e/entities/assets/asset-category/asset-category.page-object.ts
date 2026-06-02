import { element, by, ElementFinder } from 'protractor';

export class AssetCategoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-asset-category div table .btn-danger'));
  title = element.all(by.css('jhi-asset-category div h2#page-heading span')).first();
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

export class AssetCategoryUpdatePage {
  pageTitle = element(by.id('jhi-asset-category-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  assetCategoryNameInput = element(by.id('field_assetCategoryName'));
  descriptionInput = element(by.id('field_description'));
  notesInput = element(by.id('field_notes'));
  remarksInput = element(by.id('field_remarks'));
  depreciationRateYearlyInput = element(by.id('field_depreciationRateYearly'));

  depreciationMethodSelect = element(by.id('field_depreciationMethod'));
  placeholderSelect = element(by.id('field_placeholder'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAssetCategoryNameInput(assetCategoryName: string): Promise<void> {
    await this.assetCategoryNameInput.sendKeys(assetCategoryName);
  }

  async getAssetCategoryNameInput(): Promise<string> {
    return await this.assetCategoryNameInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setNotesInput(notes: string): Promise<void> {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput(): Promise<string> {
    return await this.notesInput.getAttribute('value');
  }

  async setRemarksInput(remarks: string): Promise<void> {
    await this.remarksInput.sendKeys(remarks);
  }

  async getRemarksInput(): Promise<string> {
    return await this.remarksInput.getAttribute('value');
  }

  async setDepreciationRateYearlyInput(depreciationRateYearly: string): Promise<void> {
    await this.depreciationRateYearlyInput.sendKeys(depreciationRateYearly);
  }

  async getDepreciationRateYearlyInput(): Promise<string> {
    return await this.depreciationRateYearlyInput.getAttribute('value');
  }

  async depreciationMethodSelectLastOption(): Promise<void> {
    await this.depreciationMethodSelect.all(by.tagName('option')).last().click();
  }

  async depreciationMethodSelectOption(option: string): Promise<void> {
    await this.depreciationMethodSelect.sendKeys(option);
  }

  getDepreciationMethodSelect(): ElementFinder {
    return this.depreciationMethodSelect;
  }

  async getDepreciationMethodSelectedOption(): Promise<string> {
    return await this.depreciationMethodSelect.element(by.css('option:checked')).getText();
  }

  async placeholderSelectLastOption(): Promise<void> {
    await this.placeholderSelect.all(by.tagName('option')).last().click();
  }

  async placeholderSelectOption(option: string): Promise<void> {
    await this.placeholderSelect.sendKeys(option);
  }

  getPlaceholderSelect(): ElementFinder {
    return this.placeholderSelect;
  }

  async getPlaceholderSelectedOption(): Promise<string> {
    return await this.placeholderSelect.element(by.css('option:checked')).getText();
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

export class AssetCategoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-assetCategory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-assetCategory'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
