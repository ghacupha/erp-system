import { element, by, ElementFinder } from 'protractor';

export class ChartOfAccountsCodeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-chart-of-accounts-code div table .btn-danger'));
  title = element.all(by.css('jhi-chart-of-accounts-code div h2#page-heading span')).first();
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

export class ChartOfAccountsCodeUpdatePage {
  pageTitle = element(by.id('jhi-chart-of-accounts-code-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  chartOfAccountsCodeInput = element(by.id('field_chartOfAccountsCode'));
  chartOfAccountsClassInput = element(by.id('field_chartOfAccountsClass'));
  descriptionInput = element(by.id('field_description'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setChartOfAccountsCodeInput(chartOfAccountsCode: string): Promise<void> {
    await this.chartOfAccountsCodeInput.sendKeys(chartOfAccountsCode);
  }

  async getChartOfAccountsCodeInput(): Promise<string> {
    return await this.chartOfAccountsCodeInput.getAttribute('value');
  }

  async setChartOfAccountsClassInput(chartOfAccountsClass: string): Promise<void> {
    await this.chartOfAccountsClassInput.sendKeys(chartOfAccountsClass);
  }

  async getChartOfAccountsClassInput(): Promise<string> {
    return await this.chartOfAccountsClassInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
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

export class ChartOfAccountsCodeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-chartOfAccountsCode-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-chartOfAccountsCode'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
