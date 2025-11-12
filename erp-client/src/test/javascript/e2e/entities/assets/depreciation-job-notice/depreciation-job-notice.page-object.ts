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

export class DepreciationJobNoticeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-depreciation-job-notice div table .btn-danger'));
  title = element.all(by.css('jhi-depreciation-job-notice div h2#page-heading span')).first();
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

export class DepreciationJobNoticeUpdatePage {
  pageTitle = element(by.id('jhi-depreciation-job-notice-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  eventNarrativeInput = element(by.id('field_eventNarrative'));
  eventTimeStampInput = element(by.id('field_eventTimeStamp'));
  depreciationNoticeStatusSelect = element(by.id('field_depreciationNoticeStatus'));
  sourceModuleInput = element(by.id('field_sourceModule'));
  sourceEntityInput = element(by.id('field_sourceEntity'));
  errorCodeInput = element(by.id('field_errorCode'));
  errorMessageInput = element(by.id('field_errorMessage'));
  userActionInput = element(by.id('field_userAction'));
  technicalDetailsInput = element(by.id('field_technicalDetails'));

  depreciationJobSelect = element(by.id('field_depreciationJob'));
  depreciationBatchSequenceSelect = element(by.id('field_depreciationBatchSequence'));
  depreciationPeriodSelect = element(by.id('field_depreciationPeriod'));
  placeholderSelect = element(by.id('field_placeholder'));
  universallyUniqueMappingSelect = element(by.id('field_universallyUniqueMapping'));
  superintendedSelect = element(by.id('field_superintended'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setEventNarrativeInput(eventNarrative: string): Promise<void> {
    await this.eventNarrativeInput.sendKeys(eventNarrative);
  }

  async getEventNarrativeInput(): Promise<string> {
    return await this.eventNarrativeInput.getAttribute('value');
  }

  async setEventTimeStampInput(eventTimeStamp: string): Promise<void> {
    await this.eventTimeStampInput.sendKeys(eventTimeStamp);
  }

  async getEventTimeStampInput(): Promise<string> {
    return await this.eventTimeStampInput.getAttribute('value');
  }

  async setDepreciationNoticeStatusSelect(depreciationNoticeStatus: string): Promise<void> {
    await this.depreciationNoticeStatusSelect.sendKeys(depreciationNoticeStatus);
  }

  async getDepreciationNoticeStatusSelect(): Promise<string> {
    return await this.depreciationNoticeStatusSelect.element(by.css('option:checked')).getText();
  }

  async depreciationNoticeStatusSelectLastOption(): Promise<void> {
    await this.depreciationNoticeStatusSelect.all(by.tagName('option')).last().click();
  }

  async setSourceModuleInput(sourceModule: string): Promise<void> {
    await this.sourceModuleInput.sendKeys(sourceModule);
  }

  async getSourceModuleInput(): Promise<string> {
    return await this.sourceModuleInput.getAttribute('value');
  }

  async setSourceEntityInput(sourceEntity: string): Promise<void> {
    await this.sourceEntityInput.sendKeys(sourceEntity);
  }

  async getSourceEntityInput(): Promise<string> {
    return await this.sourceEntityInput.getAttribute('value');
  }

  async setErrorCodeInput(errorCode: string): Promise<void> {
    await this.errorCodeInput.sendKeys(errorCode);
  }

  async getErrorCodeInput(): Promise<string> {
    return await this.errorCodeInput.getAttribute('value');
  }

  async setErrorMessageInput(errorMessage: string): Promise<void> {
    await this.errorMessageInput.sendKeys(errorMessage);
  }

  async getErrorMessageInput(): Promise<string> {
    return await this.errorMessageInput.getAttribute('value');
  }

  async setUserActionInput(userAction: string): Promise<void> {
    await this.userActionInput.sendKeys(userAction);
  }

  async getUserActionInput(): Promise<string> {
    return await this.userActionInput.getAttribute('value');
  }

  async setTechnicalDetailsInput(technicalDetails: string): Promise<void> {
    await this.technicalDetailsInput.sendKeys(technicalDetails);
  }

  async getTechnicalDetailsInput(): Promise<string> {
    return await this.technicalDetailsInput.getAttribute('value');
  }

  async depreciationJobSelectLastOption(): Promise<void> {
    await this.depreciationJobSelect.all(by.tagName('option')).last().click();
  }

  async depreciationJobSelectOption(option: string): Promise<void> {
    await this.depreciationJobSelect.sendKeys(option);
  }

  getDepreciationJobSelect(): ElementFinder {
    return this.depreciationJobSelect;
  }

  async getDepreciationJobSelectedOption(): Promise<string> {
    return await this.depreciationJobSelect.element(by.css('option:checked')).getText();
  }

  async depreciationBatchSequenceSelectLastOption(): Promise<void> {
    await this.depreciationBatchSequenceSelect.all(by.tagName('option')).last().click();
  }

  async depreciationBatchSequenceSelectOption(option: string): Promise<void> {
    await this.depreciationBatchSequenceSelect.sendKeys(option);
  }

  getDepreciationBatchSequenceSelect(): ElementFinder {
    return this.depreciationBatchSequenceSelect;
  }

  async getDepreciationBatchSequenceSelectedOption(): Promise<string> {
    return await this.depreciationBatchSequenceSelect.element(by.css('option:checked')).getText();
  }

  async depreciationPeriodSelectLastOption(): Promise<void> {
    await this.depreciationPeriodSelect.all(by.tagName('option')).last().click();
  }

  async depreciationPeriodSelectOption(option: string): Promise<void> {
    await this.depreciationPeriodSelect.sendKeys(option);
  }

  getDepreciationPeriodSelect(): ElementFinder {
    return this.depreciationPeriodSelect;
  }

  async getDepreciationPeriodSelectedOption(): Promise<string> {
    return await this.depreciationPeriodSelect.element(by.css('option:checked')).getText();
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

  async universallyUniqueMappingSelectLastOption(): Promise<void> {
    await this.universallyUniqueMappingSelect.all(by.tagName('option')).last().click();
  }

  async universallyUniqueMappingSelectOption(option: string): Promise<void> {
    await this.universallyUniqueMappingSelect.sendKeys(option);
  }

  getUniversallyUniqueMappingSelect(): ElementFinder {
    return this.universallyUniqueMappingSelect;
  }

  async getUniversallyUniqueMappingSelectedOption(): Promise<string> {
    return await this.universallyUniqueMappingSelect.element(by.css('option:checked')).getText();
  }

  async superintendedSelectLastOption(): Promise<void> {
    await this.superintendedSelect.all(by.tagName('option')).last().click();
  }

  async superintendedSelectOption(option: string): Promise<void> {
    await this.superintendedSelect.sendKeys(option);
  }

  getSuperintendedSelect(): ElementFinder {
    return this.superintendedSelect;
  }

  async getSuperintendedSelectedOption(): Promise<string> {
    return await this.superintendedSelect.element(by.css('option:checked')).getText();
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

export class DepreciationJobNoticeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-depreciationJobNotice-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-depreciationJobNotice'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
