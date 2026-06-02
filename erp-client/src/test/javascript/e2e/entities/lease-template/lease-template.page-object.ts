import { element, by, ElementFinder } from 'protractor';

export class LeaseTemplateComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-lease-template div table .btn-danger'));
  title = element.all(by.css('jhi-lease-template div h2#page-heading span')).first();
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

export class LeaseTemplateUpdatePage {
  pageTitle = element(by.id('jhi-lease-template-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  templateTitleInput = element(by.id('field_templateTitle'));

  assetAccountSelect = element(by.id('field_assetAccount'));
  depreciationAccountSelect = element(by.id('field_depreciationAccount'));
  accruedDepreciationAccountSelect = element(by.id('field_accruedDepreciationAccount'));
  interestPaidTransferDebitAccountSelect = element(by.id('field_interestPaidTransferDebitAccount'));
  interestPaidTransferCreditAccountSelect = element(by.id('field_interestPaidTransferCreditAccount'));
  interestAccruedDebitAccountSelect = element(by.id('field_interestAccruedDebitAccount'));
  interestAccruedCreditAccountSelect = element(by.id('field_interestAccruedCreditAccount'));
  leaseRecognitionDebitAccountSelect = element(by.id('field_leaseRecognitionDebitAccount'));
  leaseRecognitionCreditAccountSelect = element(by.id('field_leaseRecognitionCreditAccount'));
  leaseRepaymentDebitAccountSelect = element(by.id('field_leaseRepaymentDebitAccount'));
  leaseRepaymentCreditAccountSelect = element(by.id('field_leaseRepaymentCreditAccount'));
  rouRecognitionCreditAccountSelect = element(by.id('field_rouRecognitionCreditAccount'));
  rouRecognitionDebitAccountSelect = element(by.id('field_rouRecognitionDebitAccount'));
  assetCategorySelect = element(by.id('field_assetCategory'));
  serviceOutletSelect = element(by.id('field_serviceOutlet'));
  mainDealerSelect = element(by.id('field_mainDealer'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setTemplateTitleInput(templateTitle: string): Promise<void> {
    await this.templateTitleInput.sendKeys(templateTitle);
  }

  async getTemplateTitleInput(): Promise<string> {
    return await this.templateTitleInput.getAttribute('value');
  }

  async assetAccountSelectLastOption(): Promise<void> {
    await this.assetAccountSelect.all(by.tagName('option')).last().click();
  }

  async assetAccountSelectOption(option: string): Promise<void> {
    await this.assetAccountSelect.sendKeys(option);
  }

  getAssetAccountSelect(): ElementFinder {
    return this.assetAccountSelect;
  }

  async getAssetAccountSelectedOption(): Promise<string> {
    return await this.assetAccountSelect.element(by.css('option:checked')).getText();
  }

  async depreciationAccountSelectLastOption(): Promise<void> {
    await this.depreciationAccountSelect.all(by.tagName('option')).last().click();
  }

  async depreciationAccountSelectOption(option: string): Promise<void> {
    await this.depreciationAccountSelect.sendKeys(option);
  }

  getDepreciationAccountSelect(): ElementFinder {
    return this.depreciationAccountSelect;
  }

  async getDepreciationAccountSelectedOption(): Promise<string> {
    return await this.depreciationAccountSelect.element(by.css('option:checked')).getText();
  }

  async accruedDepreciationAccountSelectLastOption(): Promise<void> {
    await this.accruedDepreciationAccountSelect.all(by.tagName('option')).last().click();
  }

  async accruedDepreciationAccountSelectOption(option: string): Promise<void> {
    await this.accruedDepreciationAccountSelect.sendKeys(option);
  }

  getAccruedDepreciationAccountSelect(): ElementFinder {
    return this.accruedDepreciationAccountSelect;
  }

  async getAccruedDepreciationAccountSelectedOption(): Promise<string> {
    return await this.accruedDepreciationAccountSelect.element(by.css('option:checked')).getText();
  }

  async interestPaidTransferDebitAccountSelectLastOption(): Promise<void> {
    await this.interestPaidTransferDebitAccountSelect.all(by.tagName('option')).last().click();
  }

  async interestPaidTransferDebitAccountSelectOption(option: string): Promise<void> {
    await this.interestPaidTransferDebitAccountSelect.sendKeys(option);
  }

  getInterestPaidTransferDebitAccountSelect(): ElementFinder {
    return this.interestPaidTransferDebitAccountSelect;
  }

  async getInterestPaidTransferDebitAccountSelectedOption(): Promise<string> {
    return await this.interestPaidTransferDebitAccountSelect.element(by.css('option:checked')).getText();
  }

  async interestPaidTransferCreditAccountSelectLastOption(): Promise<void> {
    await this.interestPaidTransferCreditAccountSelect.all(by.tagName('option')).last().click();
  }

  async interestPaidTransferCreditAccountSelectOption(option: string): Promise<void> {
    await this.interestPaidTransferCreditAccountSelect.sendKeys(option);
  }

  getInterestPaidTransferCreditAccountSelect(): ElementFinder {
    return this.interestPaidTransferCreditAccountSelect;
  }

  async getInterestPaidTransferCreditAccountSelectedOption(): Promise<string> {
    return await this.interestPaidTransferCreditAccountSelect.element(by.css('option:checked')).getText();
  }

  async interestAccruedDebitAccountSelectLastOption(): Promise<void> {
    await this.interestAccruedDebitAccountSelect.all(by.tagName('option')).last().click();
  }

  async interestAccruedDebitAccountSelectOption(option: string): Promise<void> {
    await this.interestAccruedDebitAccountSelect.sendKeys(option);
  }

  getInterestAccruedDebitAccountSelect(): ElementFinder {
    return this.interestAccruedDebitAccountSelect;
  }

  async getInterestAccruedDebitAccountSelectedOption(): Promise<string> {
    return await this.interestAccruedDebitAccountSelect.element(by.css('option:checked')).getText();
  }

  async interestAccruedCreditAccountSelectLastOption(): Promise<void> {
    await this.interestAccruedCreditAccountSelect.all(by.tagName('option')).last().click();
  }

  async interestAccruedCreditAccountSelectOption(option: string): Promise<void> {
    await this.interestAccruedCreditAccountSelect.sendKeys(option);
  }

  getInterestAccruedCreditAccountSelect(): ElementFinder {
    return this.interestAccruedCreditAccountSelect;
  }

  async getInterestAccruedCreditAccountSelectedOption(): Promise<string> {
    return await this.interestAccruedCreditAccountSelect.element(by.css('option:checked')).getText();
  }

  async leaseRecognitionDebitAccountSelectLastOption(): Promise<void> {
    await this.leaseRecognitionDebitAccountSelect.all(by.tagName('option')).last().click();
  }

  async leaseRecognitionDebitAccountSelectOption(option: string): Promise<void> {
    await this.leaseRecognitionDebitAccountSelect.sendKeys(option);
  }

  getLeaseRecognitionDebitAccountSelect(): ElementFinder {
    return this.leaseRecognitionDebitAccountSelect;
  }

  async getLeaseRecognitionDebitAccountSelectedOption(): Promise<string> {
    return await this.leaseRecognitionDebitAccountSelect.element(by.css('option:checked')).getText();
  }

  async leaseRecognitionCreditAccountSelectLastOption(): Promise<void> {
    await this.leaseRecognitionCreditAccountSelect.all(by.tagName('option')).last().click();
  }

  async leaseRecognitionCreditAccountSelectOption(option: string): Promise<void> {
    await this.leaseRecognitionCreditAccountSelect.sendKeys(option);
  }

  getLeaseRecognitionCreditAccountSelect(): ElementFinder {
    return this.leaseRecognitionCreditAccountSelect;
  }

  async getLeaseRecognitionCreditAccountSelectedOption(): Promise<string> {
    return await this.leaseRecognitionCreditAccountSelect.element(by.css('option:checked')).getText();
  }

  async leaseRepaymentDebitAccountSelectLastOption(): Promise<void> {
    await this.leaseRepaymentDebitAccountSelect.all(by.tagName('option')).last().click();
  }

  async leaseRepaymentDebitAccountSelectOption(option: string): Promise<void> {
    await this.leaseRepaymentDebitAccountSelect.sendKeys(option);
  }

  getLeaseRepaymentDebitAccountSelect(): ElementFinder {
    return this.leaseRepaymentDebitAccountSelect;
  }

  async getLeaseRepaymentDebitAccountSelectedOption(): Promise<string> {
    return await this.leaseRepaymentDebitAccountSelect.element(by.css('option:checked')).getText();
  }

  async leaseRepaymentCreditAccountSelectLastOption(): Promise<void> {
    await this.leaseRepaymentCreditAccountSelect.all(by.tagName('option')).last().click();
  }

  async leaseRepaymentCreditAccountSelectOption(option: string): Promise<void> {
    await this.leaseRepaymentCreditAccountSelect.sendKeys(option);
  }

  getLeaseRepaymentCreditAccountSelect(): ElementFinder {
    return this.leaseRepaymentCreditAccountSelect;
  }

  async getLeaseRepaymentCreditAccountSelectedOption(): Promise<string> {
    return await this.leaseRepaymentCreditAccountSelect.element(by.css('option:checked')).getText();
  }

  async rouRecognitionCreditAccountSelectLastOption(): Promise<void> {
    await this.rouRecognitionCreditAccountSelect.all(by.tagName('option')).last().click();
  }

  async rouRecognitionCreditAccountSelectOption(option: string): Promise<void> {
    await this.rouRecognitionCreditAccountSelect.sendKeys(option);
  }

  getRouRecognitionCreditAccountSelect(): ElementFinder {
    return this.rouRecognitionCreditAccountSelect;
  }

  async getRouRecognitionCreditAccountSelectedOption(): Promise<string> {
    return await this.rouRecognitionCreditAccountSelect.element(by.css('option:checked')).getText();
  }

  async rouRecognitionDebitAccountSelectLastOption(): Promise<void> {
    await this.rouRecognitionDebitAccountSelect.all(by.tagName('option')).last().click();
  }

  async rouRecognitionDebitAccountSelectOption(option: string): Promise<void> {
    await this.rouRecognitionDebitAccountSelect.sendKeys(option);
  }

  getRouRecognitionDebitAccountSelect(): ElementFinder {
    return this.rouRecognitionDebitAccountSelect;
  }

  async getRouRecognitionDebitAccountSelectedOption(): Promise<string> {
    return await this.rouRecognitionDebitAccountSelect.element(by.css('option:checked')).getText();
  }

  async assetCategorySelectLastOption(): Promise<void> {
    await this.assetCategorySelect.all(by.tagName('option')).last().click();
  }

  async assetCategorySelectOption(option: string): Promise<void> {
    await this.assetCategorySelect.sendKeys(option);
  }

  getAssetCategorySelect(): ElementFinder {
    return this.assetCategorySelect;
  }

  async getAssetCategorySelectedOption(): Promise<string> {
    return await this.assetCategorySelect.element(by.css('option:checked')).getText();
  }

  async serviceOutletSelectLastOption(): Promise<void> {
    await this.serviceOutletSelect.all(by.tagName('option')).last().click();
  }

  async serviceOutletSelectOption(option: string): Promise<void> {
    await this.serviceOutletSelect.sendKeys(option);
  }

  getServiceOutletSelect(): ElementFinder {
    return this.serviceOutletSelect;
  }

  async getServiceOutletSelectedOption(): Promise<string> {
    return await this.serviceOutletSelect.element(by.css('option:checked')).getText();
  }

  async mainDealerSelectLastOption(): Promise<void> {
    await this.mainDealerSelect.all(by.tagName('option')).last().click();
  }

  async mainDealerSelectOption(option: string): Promise<void> {
    await this.mainDealerSelect.sendKeys(option);
  }

  getMainDealerSelect(): ElementFinder {
    return this.mainDealerSelect;
  }

  async getMainDealerSelectedOption(): Promise<string> {
    return await this.mainDealerSelect.element(by.css('option:checked')).getText();
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

export class LeaseTemplateDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-leaseTemplate-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-leaseTemplate'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
