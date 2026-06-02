import { element, by, ElementFinder } from 'protractor';

export class ReasonsForBouncedChequeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-reasons-for-bounced-cheque div table .btn-danger'));
  title = element.all(by.css('jhi-reasons-for-bounced-cheque div h2#page-heading span')).first();
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

export class ReasonsForBouncedChequeUpdatePage {
  pageTitle = element(by.id('jhi-reasons-for-bounced-cheque-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  bouncedChequeReasonsTypeCodeInput = element(by.id('field_bouncedChequeReasonsTypeCode'));
  bouncedChequeReasonsTypeInput = element(by.id('field_bouncedChequeReasonsType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setBouncedChequeReasonsTypeCodeInput(bouncedChequeReasonsTypeCode: string): Promise<void> {
    await this.bouncedChequeReasonsTypeCodeInput.sendKeys(bouncedChequeReasonsTypeCode);
  }

  async getBouncedChequeReasonsTypeCodeInput(): Promise<string> {
    return await this.bouncedChequeReasonsTypeCodeInput.getAttribute('value');
  }

  async setBouncedChequeReasonsTypeInput(bouncedChequeReasonsType: string): Promise<void> {
    await this.bouncedChequeReasonsTypeInput.sendKeys(bouncedChequeReasonsType);
  }

  async getBouncedChequeReasonsTypeInput(): Promise<string> {
    return await this.bouncedChequeReasonsTypeInput.getAttribute('value');
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

export class ReasonsForBouncedChequeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-reasonsForBouncedCheque-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-reasonsForBouncedCheque'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
