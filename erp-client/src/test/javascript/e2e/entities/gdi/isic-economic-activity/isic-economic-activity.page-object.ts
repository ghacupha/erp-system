import { element, by, ElementFinder } from 'protractor';

export class IsicEconomicActivityComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-isic-economic-activity div table .btn-danger'));
  title = element.all(by.css('jhi-isic-economic-activity div h2#page-heading span')).first();
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

export class IsicEconomicActivityUpdatePage {
  pageTitle = element(by.id('jhi-isic-economic-activity-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  businessEconomicActivityCodeInput = element(by.id('field_businessEconomicActivityCode'));
  sectionInput = element(by.id('field_section'));
  sectionLabelInput = element(by.id('field_sectionLabel'));
  divisionInput = element(by.id('field_division'));
  divisionLabelInput = element(by.id('field_divisionLabel'));
  groupCodeInput = element(by.id('field_groupCode'));
  groupLabelInput = element(by.id('field_groupLabel'));
  classCodeInput = element(by.id('field_classCode'));
  businessEconomicActivityTypeInput = element(by.id('field_businessEconomicActivityType'));
  businessEconomicActivityTypeDescriptionInput = element(by.id('field_businessEconomicActivityTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setBusinessEconomicActivityCodeInput(businessEconomicActivityCode: string): Promise<void> {
    await this.businessEconomicActivityCodeInput.sendKeys(businessEconomicActivityCode);
  }

  async getBusinessEconomicActivityCodeInput(): Promise<string> {
    return await this.businessEconomicActivityCodeInput.getAttribute('value');
  }

  async setSectionInput(section: string): Promise<void> {
    await this.sectionInput.sendKeys(section);
  }

  async getSectionInput(): Promise<string> {
    return await this.sectionInput.getAttribute('value');
  }

  async setSectionLabelInput(sectionLabel: string): Promise<void> {
    await this.sectionLabelInput.sendKeys(sectionLabel);
  }

  async getSectionLabelInput(): Promise<string> {
    return await this.sectionLabelInput.getAttribute('value');
  }

  async setDivisionInput(division: string): Promise<void> {
    await this.divisionInput.sendKeys(division);
  }

  async getDivisionInput(): Promise<string> {
    return await this.divisionInput.getAttribute('value');
  }

  async setDivisionLabelInput(divisionLabel: string): Promise<void> {
    await this.divisionLabelInput.sendKeys(divisionLabel);
  }

  async getDivisionLabelInput(): Promise<string> {
    return await this.divisionLabelInput.getAttribute('value');
  }

  async setGroupCodeInput(groupCode: string): Promise<void> {
    await this.groupCodeInput.sendKeys(groupCode);
  }

  async getGroupCodeInput(): Promise<string> {
    return await this.groupCodeInput.getAttribute('value');
  }

  async setGroupLabelInput(groupLabel: string): Promise<void> {
    await this.groupLabelInput.sendKeys(groupLabel);
  }

  async getGroupLabelInput(): Promise<string> {
    return await this.groupLabelInput.getAttribute('value');
  }

  async setClassCodeInput(classCode: string): Promise<void> {
    await this.classCodeInput.sendKeys(classCode);
  }

  async getClassCodeInput(): Promise<string> {
    return await this.classCodeInput.getAttribute('value');
  }

  async setBusinessEconomicActivityTypeInput(businessEconomicActivityType: string): Promise<void> {
    await this.businessEconomicActivityTypeInput.sendKeys(businessEconomicActivityType);
  }

  async getBusinessEconomicActivityTypeInput(): Promise<string> {
    return await this.businessEconomicActivityTypeInput.getAttribute('value');
  }

  async setBusinessEconomicActivityTypeDescriptionInput(businessEconomicActivityTypeDescription: string): Promise<void> {
    await this.businessEconomicActivityTypeDescriptionInput.sendKeys(businessEconomicActivityTypeDescription);
  }

  async getBusinessEconomicActivityTypeDescriptionInput(): Promise<string> {
    return await this.businessEconomicActivityTypeDescriptionInput.getAttribute('value');
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

export class IsicEconomicActivityDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-isicEconomicActivity-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-isicEconomicActivity'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
