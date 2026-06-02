import { element, by, ElementFinder } from 'protractor';

export class LoanRestructureItemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-loan-restructure-item div table .btn-danger'));
  title = element.all(by.css('jhi-loan-restructure-item div h2#page-heading span')).first();
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

export class LoanRestructureItemUpdatePage {
  pageTitle = element(by.id('jhi-loan-restructure-item-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  loanRestructureItemCodeInput = element(by.id('field_loanRestructureItemCode'));
  loanRestructureItemTypeInput = element(by.id('field_loanRestructureItemType'));
  loanRestructureItemDetailsInput = element(by.id('field_loanRestructureItemDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setLoanRestructureItemCodeInput(loanRestructureItemCode: string): Promise<void> {
    await this.loanRestructureItemCodeInput.sendKeys(loanRestructureItemCode);
  }

  async getLoanRestructureItemCodeInput(): Promise<string> {
    return await this.loanRestructureItemCodeInput.getAttribute('value');
  }

  async setLoanRestructureItemTypeInput(loanRestructureItemType: string): Promise<void> {
    await this.loanRestructureItemTypeInput.sendKeys(loanRestructureItemType);
  }

  async getLoanRestructureItemTypeInput(): Promise<string> {
    return await this.loanRestructureItemTypeInput.getAttribute('value');
  }

  async setLoanRestructureItemDetailsInput(loanRestructureItemDetails: string): Promise<void> {
    await this.loanRestructureItemDetailsInput.sendKeys(loanRestructureItemDetails);
  }

  async getLoanRestructureItemDetailsInput(): Promise<string> {
    return await this.loanRestructureItemDetailsInput.getAttribute('value');
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

export class LoanRestructureItemDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-loanRestructureItem-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-loanRestructureItem'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
