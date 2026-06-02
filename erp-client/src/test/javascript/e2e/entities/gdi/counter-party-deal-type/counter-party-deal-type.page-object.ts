import { element, by, ElementFinder } from 'protractor';

export class CounterPartyDealTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-counter-party-deal-type div table .btn-danger'));
  title = element.all(by.css('jhi-counter-party-deal-type div h2#page-heading span')).first();
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

export class CounterPartyDealTypeUpdatePage {
  pageTitle = element(by.id('jhi-counter-party-deal-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  counterpartyDealCodeInput = element(by.id('field_counterpartyDealCode'));
  counterpartyDealTypeDetailsInput = element(by.id('field_counterpartyDealTypeDetails'));
  counterpartyDealTypeDescriptionInput = element(by.id('field_counterpartyDealTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCounterpartyDealCodeInput(counterpartyDealCode: string): Promise<void> {
    await this.counterpartyDealCodeInput.sendKeys(counterpartyDealCode);
  }

  async getCounterpartyDealCodeInput(): Promise<string> {
    return await this.counterpartyDealCodeInput.getAttribute('value');
  }

  async setCounterpartyDealTypeDetailsInput(counterpartyDealTypeDetails: string): Promise<void> {
    await this.counterpartyDealTypeDetailsInput.sendKeys(counterpartyDealTypeDetails);
  }

  async getCounterpartyDealTypeDetailsInput(): Promise<string> {
    return await this.counterpartyDealTypeDetailsInput.getAttribute('value');
  }

  async setCounterpartyDealTypeDescriptionInput(counterpartyDealTypeDescription: string): Promise<void> {
    await this.counterpartyDealTypeDescriptionInput.sendKeys(counterpartyDealTypeDescription);
  }

  async getCounterpartyDealTypeDescriptionInput(): Promise<string> {
    return await this.counterpartyDealTypeDescriptionInput.getAttribute('value');
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

export class CounterPartyDealTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-counterPartyDealType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-counterPartyDealType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
