import { element, by, ElementFinder } from 'protractor';

export class IsoCurrencyCodeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-iso-currency-code div table .btn-danger'));
  title = element.all(by.css('jhi-iso-currency-code div h2#page-heading span')).first();
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

export class IsoCurrencyCodeUpdatePage {
  pageTitle = element(by.id('jhi-iso-currency-code-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  alphabeticCodeInput = element(by.id('field_alphabeticCode'));
  numericCodeInput = element(by.id('field_numericCode'));
  minorUnitInput = element(by.id('field_minorUnit'));
  currencyInput = element(by.id('field_currency'));
  countryInput = element(by.id('field_country'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAlphabeticCodeInput(alphabeticCode: string): Promise<void> {
    await this.alphabeticCodeInput.sendKeys(alphabeticCode);
  }

  async getAlphabeticCodeInput(): Promise<string> {
    return await this.alphabeticCodeInput.getAttribute('value');
  }

  async setNumericCodeInput(numericCode: string): Promise<void> {
    await this.numericCodeInput.sendKeys(numericCode);
  }

  async getNumericCodeInput(): Promise<string> {
    return await this.numericCodeInput.getAttribute('value');
  }

  async setMinorUnitInput(minorUnit: string): Promise<void> {
    await this.minorUnitInput.sendKeys(minorUnit);
  }

  async getMinorUnitInput(): Promise<string> {
    return await this.minorUnitInput.getAttribute('value');
  }

  async setCurrencyInput(currency: string): Promise<void> {
    await this.currencyInput.sendKeys(currency);
  }

  async getCurrencyInput(): Promise<string> {
    return await this.currencyInput.getAttribute('value');
  }

  async setCountryInput(country: string): Promise<void> {
    await this.countryInput.sendKeys(country);
  }

  async getCountryInput(): Promise<string> {
    return await this.countryInput.getAttribute('value');
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

export class IsoCurrencyCodeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-isoCurrencyCode-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-isoCurrencyCode'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
