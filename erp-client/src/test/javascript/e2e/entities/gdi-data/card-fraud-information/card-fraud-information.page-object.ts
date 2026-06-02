import { element, by, ElementFinder } from 'protractor';

export class CardFraudInformationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-card-fraud-information div table .btn-danger'));
  title = element.all(by.css('jhi-card-fraud-information div h2#page-heading span')).first();
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

export class CardFraudInformationUpdatePage {
  pageTitle = element(by.id('jhi-card-fraud-information-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  totalNumberOfFraudIncidentsInput = element(by.id('field_totalNumberOfFraudIncidents'));
  valueOfFraudIncedentsInLCYInput = element(by.id('field_valueOfFraudIncedentsInLCY'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setReportingDateInput(reportingDate: string): Promise<void> {
    await this.reportingDateInput.sendKeys(reportingDate);
  }

  async getReportingDateInput(): Promise<string> {
    return await this.reportingDateInput.getAttribute('value');
  }

  async setTotalNumberOfFraudIncidentsInput(totalNumberOfFraudIncidents: string): Promise<void> {
    await this.totalNumberOfFraudIncidentsInput.sendKeys(totalNumberOfFraudIncidents);
  }

  async getTotalNumberOfFraudIncidentsInput(): Promise<string> {
    return await this.totalNumberOfFraudIncidentsInput.getAttribute('value');
  }

  async setValueOfFraudIncedentsInLCYInput(valueOfFraudIncedentsInLCY: string): Promise<void> {
    await this.valueOfFraudIncedentsInLCYInput.sendKeys(valueOfFraudIncedentsInLCY);
  }

  async getValueOfFraudIncedentsInLCYInput(): Promise<string> {
    return await this.valueOfFraudIncedentsInLCYInput.getAttribute('value');
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

export class CardFraudInformationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cardFraudInformation-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cardFraudInformation'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
