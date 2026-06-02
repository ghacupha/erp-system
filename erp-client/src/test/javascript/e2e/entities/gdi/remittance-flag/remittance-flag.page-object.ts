import { element, by, ElementFinder } from 'protractor';

export class RemittanceFlagComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-remittance-flag div table .btn-danger'));
  title = element.all(by.css('jhi-remittance-flag div h2#page-heading span')).first();
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

export class RemittanceFlagUpdatePage {
  pageTitle = element(by.id('jhi-remittance-flag-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  remittanceTypeFlagSelect = element(by.id('field_remittanceTypeFlag'));
  remittanceTypeSelect = element(by.id('field_remittanceType'));
  remittanceTypeDetailsInput = element(by.id('field_remittanceTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setRemittanceTypeFlagSelect(remittanceTypeFlag: string): Promise<void> {
    await this.remittanceTypeFlagSelect.sendKeys(remittanceTypeFlag);
  }

  async getRemittanceTypeFlagSelect(): Promise<string> {
    return await this.remittanceTypeFlagSelect.element(by.css('option:checked')).getText();
  }

  async remittanceTypeFlagSelectLastOption(): Promise<void> {
    await this.remittanceTypeFlagSelect.all(by.tagName('option')).last().click();
  }

  async setRemittanceTypeSelect(remittanceType: string): Promise<void> {
    await this.remittanceTypeSelect.sendKeys(remittanceType);
  }

  async getRemittanceTypeSelect(): Promise<string> {
    return await this.remittanceTypeSelect.element(by.css('option:checked')).getText();
  }

  async remittanceTypeSelectLastOption(): Promise<void> {
    await this.remittanceTypeSelect.all(by.tagName('option')).last().click();
  }

  async setRemittanceTypeDetailsInput(remittanceTypeDetails: string): Promise<void> {
    await this.remittanceTypeDetailsInput.sendKeys(remittanceTypeDetails);
  }

  async getRemittanceTypeDetailsInput(): Promise<string> {
    return await this.remittanceTypeDetailsInput.getAttribute('value');
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

export class RemittanceFlagDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-remittanceFlag-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-remittanceFlag'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
