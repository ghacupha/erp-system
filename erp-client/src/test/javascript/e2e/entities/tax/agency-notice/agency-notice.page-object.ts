import { element, by, ElementFinder } from 'protractor';

export class AgencyNoticeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-agency-notice div table .btn-danger'));
  title = element.all(by.css('jhi-agency-notice div h2#page-heading span')).first();
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

export class AgencyNoticeUpdatePage {
  pageTitle = element(by.id('jhi-agency-notice-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  referenceNumberInput = element(by.id('field_referenceNumber'));
  referenceDateInput = element(by.id('field_referenceDate'));
  assessmentAmountInput = element(by.id('field_assessmentAmount'));
  agencyStatusSelect = element(by.id('field_agencyStatus'));
  assessmentNoticeInput = element(by.id('file_assessmentNotice'));

  correspondentsSelect = element(by.id('field_correspondents'));
  settlementCurrencySelect = element(by.id('field_settlementCurrency'));
  assessorSelect = element(by.id('field_assessor'));
  placeholderSelect = element(by.id('field_placeholder'));
  businessDocumentSelect = element(by.id('field_businessDocument'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setReferenceNumberInput(referenceNumber: string): Promise<void> {
    await this.referenceNumberInput.sendKeys(referenceNumber);
  }

  async getReferenceNumberInput(): Promise<string> {
    return await this.referenceNumberInput.getAttribute('value');
  }

  async setReferenceDateInput(referenceDate: string): Promise<void> {
    await this.referenceDateInput.sendKeys(referenceDate);
  }

  async getReferenceDateInput(): Promise<string> {
    return await this.referenceDateInput.getAttribute('value');
  }

  async setAssessmentAmountInput(assessmentAmount: string): Promise<void> {
    await this.assessmentAmountInput.sendKeys(assessmentAmount);
  }

  async getAssessmentAmountInput(): Promise<string> {
    return await this.assessmentAmountInput.getAttribute('value');
  }

  async setAgencyStatusSelect(agencyStatus: string): Promise<void> {
    await this.agencyStatusSelect.sendKeys(agencyStatus);
  }

  async getAgencyStatusSelect(): Promise<string> {
    return await this.agencyStatusSelect.element(by.css('option:checked')).getText();
  }

  async agencyStatusSelectLastOption(): Promise<void> {
    await this.agencyStatusSelect.all(by.tagName('option')).last().click();
  }

  async setAssessmentNoticeInput(assessmentNotice: string): Promise<void> {
    await this.assessmentNoticeInput.sendKeys(assessmentNotice);
  }

  async getAssessmentNoticeInput(): Promise<string> {
    return await this.assessmentNoticeInput.getAttribute('value');
  }

  async correspondentsSelectLastOption(): Promise<void> {
    await this.correspondentsSelect.all(by.tagName('option')).last().click();
  }

  async correspondentsSelectOption(option: string): Promise<void> {
    await this.correspondentsSelect.sendKeys(option);
  }

  getCorrespondentsSelect(): ElementFinder {
    return this.correspondentsSelect;
  }

  async getCorrespondentsSelectedOption(): Promise<string> {
    return await this.correspondentsSelect.element(by.css('option:checked')).getText();
  }

  async settlementCurrencySelectLastOption(): Promise<void> {
    await this.settlementCurrencySelect.all(by.tagName('option')).last().click();
  }

  async settlementCurrencySelectOption(option: string): Promise<void> {
    await this.settlementCurrencySelect.sendKeys(option);
  }

  getSettlementCurrencySelect(): ElementFinder {
    return this.settlementCurrencySelect;
  }

  async getSettlementCurrencySelectedOption(): Promise<string> {
    return await this.settlementCurrencySelect.element(by.css('option:checked')).getText();
  }

  async assessorSelectLastOption(): Promise<void> {
    await this.assessorSelect.all(by.tagName('option')).last().click();
  }

  async assessorSelectOption(option: string): Promise<void> {
    await this.assessorSelect.sendKeys(option);
  }

  getAssessorSelect(): ElementFinder {
    return this.assessorSelect;
  }

  async getAssessorSelectedOption(): Promise<string> {
    return await this.assessorSelect.element(by.css('option:checked')).getText();
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

  async businessDocumentSelectLastOption(): Promise<void> {
    await this.businessDocumentSelect.all(by.tagName('option')).last().click();
  }

  async businessDocumentSelectOption(option: string): Promise<void> {
    await this.businessDocumentSelect.sendKeys(option);
  }

  getBusinessDocumentSelect(): ElementFinder {
    return this.businessDocumentSelect;
  }

  async getBusinessDocumentSelectedOption(): Promise<string> {
    return await this.businessDocumentSelect.element(by.css('option:checked')).getText();
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

export class AgencyNoticeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-agencyNotice-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-agencyNotice'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
