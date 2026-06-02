import { element, by, ElementFinder } from 'protractor';

export class AnticipatedMaturityPerioodComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-anticipated-maturity-periood div table .btn-danger'));
  title = element.all(by.css('jhi-anticipated-maturity-periood div h2#page-heading span')).first();
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

export class AnticipatedMaturityPerioodUpdatePage {
  pageTitle = element(by.id('jhi-anticipated-maturity-periood-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  anticipatedMaturityTenorCodeInput = element(by.id('field_anticipatedMaturityTenorCode'));
  aniticipatedMaturityTenorTypeInput = element(by.id('field_aniticipatedMaturityTenorType'));
  anticipatedMaturityTenorDetailsInput = element(by.id('field_anticipatedMaturityTenorDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAnticipatedMaturityTenorCodeInput(anticipatedMaturityTenorCode: string): Promise<void> {
    await this.anticipatedMaturityTenorCodeInput.sendKeys(anticipatedMaturityTenorCode);
  }

  async getAnticipatedMaturityTenorCodeInput(): Promise<string> {
    return await this.anticipatedMaturityTenorCodeInput.getAttribute('value');
  }

  async setAniticipatedMaturityTenorTypeInput(aniticipatedMaturityTenorType: string): Promise<void> {
    await this.aniticipatedMaturityTenorTypeInput.sendKeys(aniticipatedMaturityTenorType);
  }

  async getAniticipatedMaturityTenorTypeInput(): Promise<string> {
    return await this.aniticipatedMaturityTenorTypeInput.getAttribute('value');
  }

  async setAnticipatedMaturityTenorDetailsInput(anticipatedMaturityTenorDetails: string): Promise<void> {
    await this.anticipatedMaturityTenorDetailsInput.sendKeys(anticipatedMaturityTenorDetails);
  }

  async getAnticipatedMaturityTenorDetailsInput(): Promise<string> {
    return await this.anticipatedMaturityTenorDetailsInput.getAttribute('value');
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

export class AnticipatedMaturityPerioodDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-anticipatedMaturityPeriood-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-anticipatedMaturityPeriood'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
