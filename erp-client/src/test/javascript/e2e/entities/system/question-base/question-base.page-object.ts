///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { element, by, ElementFinder } from 'protractor';

export class QuestionBaseComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-question-base div table .btn-danger'));
  title = element.all(by.css('jhi-question-base div h2#page-heading span')).first();
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

export class QuestionBaseUpdatePage {
  pageTitle = element(by.id('jhi-question-base-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  contextInput = element(by.id('field_context'));
  serialInput = element(by.id('field_serial'));
  questionBaseValueInput = element(by.id('field_questionBaseValue'));
  questionBaseKeyInput = element(by.id('field_questionBaseKey'));
  questionBaseLabelInput = element(by.id('field_questionBaseLabel'));
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

  async setContextInput(context: string): Promise<void> {
    await this.contextInput.sendKeys(context);
  }

  async getContextInput(): Promise<string> {
    return await this.contextInput.getAttribute('value');
  }

  async setSerialInput(serial: string): Promise<void> {
    await this.serialInput.sendKeys(serial);
  }

  async getSerialInput(): Promise<string> {
    return await this.serialInput.getAttribute('value');
  }

  async setQuestionBaseValueInput(questionBaseValue: string): Promise<void> {
    await this.questionBaseValueInput.sendKeys(questionBaseValue);
  }

  async getQuestionBaseValueInput(): Promise<string> {
    return await this.questionBaseValueInput.getAttribute('value');
  }

  async setQuestionBaseKeyInput(questionBaseKey: string): Promise<void> {
    await this.questionBaseKeyInput.sendKeys(questionBaseKey);
  }

  async getQuestionBaseKeyInput(): Promise<string> {
    return await this.questionBaseKeyInput.getAttribute('value');
  }

  async setQuestionBaseLabelInput(questionBaseLabel: string): Promise<void> {
    await this.questionBaseLabelInput.sendKeys(questionBaseLabel);
  }

  async getQuestionBaseLabelInput(): Promise<string> {
    return await this.questionBaseLabelInput.getAttribute('value');
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

export class QuestionBaseDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-questionBase-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-questionBase'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
