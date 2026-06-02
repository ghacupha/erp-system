import { element, by, ElementFinder } from 'protractor';

export class ProfessionalQualificationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-professional-qualification div table .btn-danger'));
  title = element.all(by.css('jhi-professional-qualification div h2#page-heading span')).first();
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

export class ProfessionalQualificationUpdatePage {
  pageTitle = element(by.id('jhi-professional-qualification-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  professionalQualificationsCodeInput = element(by.id('field_professionalQualificationsCode'));
  professionalQualificationsTypeInput = element(by.id('field_professionalQualificationsType'));
  professionalQualificationsDetailsInput = element(by.id('field_professionalQualificationsDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setProfessionalQualificationsCodeInput(professionalQualificationsCode: string): Promise<void> {
    await this.professionalQualificationsCodeInput.sendKeys(professionalQualificationsCode);
  }

  async getProfessionalQualificationsCodeInput(): Promise<string> {
    return await this.professionalQualificationsCodeInput.getAttribute('value');
  }

  async setProfessionalQualificationsTypeInput(professionalQualificationsType: string): Promise<void> {
    await this.professionalQualificationsTypeInput.sendKeys(professionalQualificationsType);
  }

  async getProfessionalQualificationsTypeInput(): Promise<string> {
    return await this.professionalQualificationsTypeInput.getAttribute('value');
  }

  async setProfessionalQualificationsDetailsInput(professionalQualificationsDetails: string): Promise<void> {
    await this.professionalQualificationsDetailsInput.sendKeys(professionalQualificationsDetails);
  }

  async getProfessionalQualificationsDetailsInput(): Promise<string> {
    return await this.professionalQualificationsDetailsInput.getAttribute('value');
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

export class ProfessionalQualificationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-professionalQualification-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-professionalQualification'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
