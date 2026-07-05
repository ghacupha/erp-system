import { element, by, ElementFinder } from 'protractor';

export class StringQuestionBaseComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-string-question-base div table .btn-danger'));
  title = element.all(by.css('jhi-string-question-base div h2#page-heading span')).first();
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

export class StringQuestionBaseUpdatePage {
  pageTitle = element(by.id('jhi-string-question-base-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  valueInput = element(by.id('field_value'));
  keyInput = element(by.id('field_key'));
  labelInput = element(by.id('field_label'));
  requiredInput = element(by.id('field_required'));
  orderInput = element(by.id('field_order'));
  controlTypeSelect = element(by.id('field_controlType'));
  placeholderInput = element(by.id('field_placeholder'));
  iterableInput = element(by.id('field_iterable'));

  parametersSelect = element(by.id('field_parameters'));
  placeholderItemSelect = element(by.id('field_placeholderItem'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setValueInput(value: string): Promise<void> {
    await this.valueInput.sendKeys(value);
  }

  async getValueInput(): Promise<string> {
    return await this.valueInput.getAttribute('value');
  }

  async setKeyInput(key: string): Promise<void> {
    await this.keyInput.sendKeys(key);
  }

  async getKeyInput(): Promise<string> {
    return await this.keyInput.getAttribute('value');
  }

  async setLabelInput(label: string): Promise<void> {
    await this.labelInput.sendKeys(label);
  }

  async getLabelInput(): Promise<string> {
    return await this.labelInput.getAttribute('value');
  }

  getRequiredInput(): ElementFinder {
    return this.requiredInput;
  }

  async setOrderInput(order: string): Promise<void> {
    await this.orderInput.sendKeys(order);
  }

  async getOrderInput(): Promise<string> {
    return await this.orderInput.getAttribute('value');
  }

  async setControlTypeSelect(controlType: string): Promise<void> {
    await this.controlTypeSelect.sendKeys(controlType);
  }

  async getControlTypeSelect(): Promise<string> {
    return await this.controlTypeSelect.element(by.css('option:checked')).getText();
  }

  async controlTypeSelectLastOption(): Promise<void> {
    await this.controlTypeSelect.all(by.tagName('option')).last().click();
  }

  async setPlaceholderInput(placeholder: string): Promise<void> {
    await this.placeholderInput.sendKeys(placeholder);
  }

  async getPlaceholderInput(): Promise<string> {
    return await this.placeholderInput.getAttribute('value');
  }

  getIterableInput(): ElementFinder {
    return this.iterableInput;
  }

  async parametersSelectLastOption(): Promise<void> {
    await this.parametersSelect.all(by.tagName('option')).last().click();
  }

  async parametersSelectOption(option: string): Promise<void> {
    await this.parametersSelect.sendKeys(option);
  }

  getParametersSelect(): ElementFinder {
    return this.parametersSelect;
  }

  async getParametersSelectedOption(): Promise<string> {
    return await this.parametersSelect.element(by.css('option:checked')).getText();
  }

  async placeholderItemSelectLastOption(): Promise<void> {
    await this.placeholderItemSelect.all(by.tagName('option')).last().click();
  }

  async placeholderItemSelectOption(option: string): Promise<void> {
    await this.placeholderItemSelect.sendKeys(option);
  }

  getPlaceholderItemSelect(): ElementFinder {
    return this.placeholderItemSelect;
  }

  async getPlaceholderItemSelectedOption(): Promise<string> {
    return await this.placeholderItemSelect.element(by.css('option:checked')).getText();
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

export class StringQuestionBaseDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-stringQuestionBase-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-stringQuestionBase'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
