import { element, by, ElementFinder } from 'protractor';

export class InstitutionContactDetailsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-institution-contact-details div table .btn-danger'));
  title = element.all(by.css('jhi-institution-contact-details div h2#page-heading span')).first();
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

export class InstitutionContactDetailsUpdatePage {
  pageTitle = element(by.id('jhi-institution-contact-details-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  entityIdInput = element(by.id('field_entityId'));
  entityNameInput = element(by.id('field_entityName'));
  contactTypeInput = element(by.id('field_contactType'));
  contactLevelInput = element(by.id('field_contactLevel'));
  contactValueInput = element(by.id('field_contactValue'));
  contactNameInput = element(by.id('field_contactName'));
  contactDesignationInput = element(by.id('field_contactDesignation'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setEntityIdInput(entityId: string): Promise<void> {
    await this.entityIdInput.sendKeys(entityId);
  }

  async getEntityIdInput(): Promise<string> {
    return await this.entityIdInput.getAttribute('value');
  }

  async setEntityNameInput(entityName: string): Promise<void> {
    await this.entityNameInput.sendKeys(entityName);
  }

  async getEntityNameInput(): Promise<string> {
    return await this.entityNameInput.getAttribute('value');
  }

  async setContactTypeInput(contactType: string): Promise<void> {
    await this.contactTypeInput.sendKeys(contactType);
  }

  async getContactTypeInput(): Promise<string> {
    return await this.contactTypeInput.getAttribute('value');
  }

  async setContactLevelInput(contactLevel: string): Promise<void> {
    await this.contactLevelInput.sendKeys(contactLevel);
  }

  async getContactLevelInput(): Promise<string> {
    return await this.contactLevelInput.getAttribute('value');
  }

  async setContactValueInput(contactValue: string): Promise<void> {
    await this.contactValueInput.sendKeys(contactValue);
  }

  async getContactValueInput(): Promise<string> {
    return await this.contactValueInput.getAttribute('value');
  }

  async setContactNameInput(contactName: string): Promise<void> {
    await this.contactNameInput.sendKeys(contactName);
  }

  async getContactNameInput(): Promise<string> {
    return await this.contactNameInput.getAttribute('value');
  }

  async setContactDesignationInput(contactDesignation: string): Promise<void> {
    await this.contactDesignationInput.sendKeys(contactDesignation);
  }

  async getContactDesignationInput(): Promise<string> {
    return await this.contactDesignationInput.getAttribute('value');
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

export class InstitutionContactDetailsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-institutionContactDetails-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-institutionContactDetails'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
