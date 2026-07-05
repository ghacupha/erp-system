import { element, by, ElementFinder } from 'protractor';

export class DepreciationMethodComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-depreciation-method div table .btn-danger'));
  title = element.all(by.css('jhi-depreciation-method div h2#page-heading span')).first();
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

export class DepreciationMethodUpdatePage {
  pageTitle = element(by.id('jhi-depreciation-method-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  depreciationMethodNameInput = element(by.id('field_depreciationMethodName'));
  descriptionInput = element(by.id('field_description'));
  depreciationTypeSelect = element(by.id('field_depreciationType'));
  remarksInput = element(by.id('field_remarks'));

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

  async setDepreciationMethodNameInput(depreciationMethodName: string): Promise<void> {
    await this.depreciationMethodNameInput.sendKeys(depreciationMethodName);
  }

  async getDepreciationMethodNameInput(): Promise<string> {
    return await this.depreciationMethodNameInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setDepreciationTypeSelect(depreciationType: string): Promise<void> {
    await this.depreciationTypeSelect.sendKeys(depreciationType);
  }

  async getDepreciationTypeSelect(): Promise<string> {
    return await this.depreciationTypeSelect.element(by.css('option:checked')).getText();
  }

  async depreciationTypeSelectLastOption(): Promise<void> {
    await this.depreciationTypeSelect.all(by.tagName('option')).last().click();
  }

  async setRemarksInput(remarks: string): Promise<void> {
    await this.remarksInput.sendKeys(remarks);
  }

  async getRemarksInput(): Promise<string> {
    return await this.remarksInput.getAttribute('value');
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

export class DepreciationMethodDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-depreciationMethod-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-depreciationMethod'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
