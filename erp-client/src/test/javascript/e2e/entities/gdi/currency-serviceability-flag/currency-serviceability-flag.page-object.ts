import { element, by, ElementFinder } from 'protractor';

export class CurrencyServiceabilityFlagComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-currency-serviceability-flag div table .btn-danger'));
  title = element.all(by.css('jhi-currency-serviceability-flag div h2#page-heading span')).first();
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

export class CurrencyServiceabilityFlagUpdatePage {
  pageTitle = element(by.id('jhi-currency-serviceability-flag-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  currencyServiceabilityFlagSelect = element(by.id('field_currencyServiceabilityFlag'));
  currencyServiceabilitySelect = element(by.id('field_currencyServiceability'));
  currencyServiceabilityFlagDetailsInput = element(by.id('field_currencyServiceabilityFlagDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCurrencyServiceabilityFlagSelect(currencyServiceabilityFlag: string): Promise<void> {
    await this.currencyServiceabilityFlagSelect.sendKeys(currencyServiceabilityFlag);
  }

  async getCurrencyServiceabilityFlagSelect(): Promise<string> {
    return await this.currencyServiceabilityFlagSelect.element(by.css('option:checked')).getText();
  }

  async currencyServiceabilityFlagSelectLastOption(): Promise<void> {
    await this.currencyServiceabilityFlagSelect.all(by.tagName('option')).last().click();
  }

  async setCurrencyServiceabilitySelect(currencyServiceability: string): Promise<void> {
    await this.currencyServiceabilitySelect.sendKeys(currencyServiceability);
  }

  async getCurrencyServiceabilitySelect(): Promise<string> {
    return await this.currencyServiceabilitySelect.element(by.css('option:checked')).getText();
  }

  async currencyServiceabilitySelectLastOption(): Promise<void> {
    await this.currencyServiceabilitySelect.all(by.tagName('option')).last().click();
  }

  async setCurrencyServiceabilityFlagDetailsInput(currencyServiceabilityFlagDetails: string): Promise<void> {
    await this.currencyServiceabilityFlagDetailsInput.sendKeys(currencyServiceabilityFlagDetails);
  }

  async getCurrencyServiceabilityFlagDetailsInput(): Promise<string> {
    return await this.currencyServiceabilityFlagDetailsInput.getAttribute('value');
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

export class CurrencyServiceabilityFlagDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-currencyServiceabilityFlag-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-currencyServiceabilityFlag'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
