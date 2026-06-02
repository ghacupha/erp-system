import { element, by, ElementFinder } from 'protractor';

export class PrepaymentMappingComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-prepayment-mapping div table .btn-danger'));
  title = element.all(by.css('jhi-prepayment-mapping div h2#page-heading span')).first();
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

export class PrepaymentMappingUpdatePage {
  pageTitle = element(by.id('jhi-prepayment-mapping-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  parameterKeyInput = element(by.id('field_parameterKey'));
  parameterGuidInput = element(by.id('field_parameterGuid'));
  parameterInput = element(by.id('field_parameter'));

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

  async setParameterKeyInput(parameterKey: string): Promise<void> {
    await this.parameterKeyInput.sendKeys(parameterKey);
  }

  async getParameterKeyInput(): Promise<string> {
    return await this.parameterKeyInput.getAttribute('value');
  }

  async setParameterGuidInput(parameterGuid: string): Promise<void> {
    await this.parameterGuidInput.sendKeys(parameterGuid);
  }

  async getParameterGuidInput(): Promise<string> {
    return await this.parameterGuidInput.getAttribute('value');
  }

  async setParameterInput(parameter: string): Promise<void> {
    await this.parameterInput.sendKeys(parameter);
  }

  async getParameterInput(): Promise<string> {
    return await this.parameterInput.getAttribute('value');
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

export class PrepaymentMappingDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-prepaymentMapping-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-prepaymentMapping'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
