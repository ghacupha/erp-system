import { element, by, ElementFinder } from 'protractor';

export class SecurityClearanceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-security-clearance div table .btn-danger'));
  title = element.all(by.css('jhi-security-clearance div h2#page-heading span')).first();
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

export class SecurityClearanceUpdatePage {
  pageTitle = element(by.id('jhi-security-clearance-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  clearanceLevelInput = element(by.id('field_clearanceLevel'));

  grantedClearancesSelect = element(by.id('field_grantedClearances'));
  placeholderSelect = element(by.id('field_placeholder'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setClearanceLevelInput(clearanceLevel: string): Promise<void> {
    await this.clearanceLevelInput.sendKeys(clearanceLevel);
  }

  async getClearanceLevelInput(): Promise<string> {
    return await this.clearanceLevelInput.getAttribute('value');
  }

  async grantedClearancesSelectLastOption(): Promise<void> {
    await this.grantedClearancesSelect.all(by.tagName('option')).last().click();
  }

  async grantedClearancesSelectOption(option: string): Promise<void> {
    await this.grantedClearancesSelect.sendKeys(option);
  }

  getGrantedClearancesSelect(): ElementFinder {
    return this.grantedClearancesSelect;
  }

  async getGrantedClearancesSelectedOption(): Promise<string> {
    return await this.grantedClearancesSelect.element(by.css('option:checked')).getText();
  }

  async placeholderSelectLastOption(): Promise<void> {
    await this.placeholderSelect.all(by.tagName('option')).last().click();
  }

  async placeholderSelectOption(option: string): Promise<void> {
    await this.placeholderSelect.sendKeys(option);
  }

  getPlaceholderSelect(): ElementFinder {
    return this.placeholderSelect;
  }

  async getPlaceholderSelectedOption(): Promise<string> {
    return await this.placeholderSelect.element(by.css('option:checked')).getText();
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

export class SecurityClearanceDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-securityClearance-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-securityClearance'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
