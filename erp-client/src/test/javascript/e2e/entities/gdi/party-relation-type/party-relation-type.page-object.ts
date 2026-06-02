import { element, by, ElementFinder } from 'protractor';

export class PartyRelationTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-party-relation-type div table .btn-danger'));
  title = element.all(by.css('jhi-party-relation-type div h2#page-heading span')).first();
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

export class PartyRelationTypeUpdatePage {
  pageTitle = element(by.id('jhi-party-relation-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  partyRelationTypeCodeInput = element(by.id('field_partyRelationTypeCode'));
  partyRelationTypeInput = element(by.id('field_partyRelationType'));
  partyRelationTypeDescriptionInput = element(by.id('field_partyRelationTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setPartyRelationTypeCodeInput(partyRelationTypeCode: string): Promise<void> {
    await this.partyRelationTypeCodeInput.sendKeys(partyRelationTypeCode);
  }

  async getPartyRelationTypeCodeInput(): Promise<string> {
    return await this.partyRelationTypeCodeInput.getAttribute('value');
  }

  async setPartyRelationTypeInput(partyRelationType: string): Promise<void> {
    await this.partyRelationTypeInput.sendKeys(partyRelationType);
  }

  async getPartyRelationTypeInput(): Promise<string> {
    return await this.partyRelationTypeInput.getAttribute('value');
  }

  async setPartyRelationTypeDescriptionInput(partyRelationTypeDescription: string): Promise<void> {
    await this.partyRelationTypeDescriptionInput.sendKeys(partyRelationTypeDescription);
  }

  async getPartyRelationTypeDescriptionInput(): Promise<string> {
    return await this.partyRelationTypeDescriptionInput.getAttribute('value');
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

export class PartyRelationTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-partyRelationType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-partyRelationType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
