import { element, by, ElementFinder } from 'protractor';

export class OutletStatusComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-outlet-status div table .btn-danger'));
  title = element.all(by.css('jhi-outlet-status div h2#page-heading span')).first();
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

export class OutletStatusUpdatePage {
  pageTitle = element(by.id('jhi-outlet-status-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  branchStatusTypeCodeInput = element(by.id('field_branchStatusTypeCode'));
  branchStatusTypeSelect = element(by.id('field_branchStatusType'));
  branchStatusTypeDescriptionInput = element(by.id('field_branchStatusTypeDescription'));

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

  async setBranchStatusTypeCodeInput(branchStatusTypeCode: string): Promise<void> {
    await this.branchStatusTypeCodeInput.sendKeys(branchStatusTypeCode);
  }

  async getBranchStatusTypeCodeInput(): Promise<string> {
    return await this.branchStatusTypeCodeInput.getAttribute('value');
  }

  async setBranchStatusTypeSelect(branchStatusType: string): Promise<void> {
    await this.branchStatusTypeSelect.sendKeys(branchStatusType);
  }

  async getBranchStatusTypeSelect(): Promise<string> {
    return await this.branchStatusTypeSelect.element(by.css('option:checked')).getText();
  }

  async branchStatusTypeSelectLastOption(): Promise<void> {
    await this.branchStatusTypeSelect.all(by.tagName('option')).last().click();
  }

  async setBranchStatusTypeDescriptionInput(branchStatusTypeDescription: string): Promise<void> {
    await this.branchStatusTypeDescriptionInput.sendKeys(branchStatusTypeDescription);
  }

  async getBranchStatusTypeDescriptionInput(): Promise<string> {
    return await this.branchStatusTypeDescriptionInput.getAttribute('value');
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

export class OutletStatusDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-outletStatus-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-outletStatus'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
