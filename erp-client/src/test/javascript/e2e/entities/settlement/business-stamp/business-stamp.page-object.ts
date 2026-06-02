import { element, by, ElementFinder } from 'protractor';

export class BusinessStampComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-business-stamp div table .btn-danger'));
  title = element.all(by.css('jhi-business-stamp div h2#page-heading span')).first();
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

export class BusinessStampUpdatePage {
  pageTitle = element(by.id('jhi-business-stamp-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  stampDateInput = element(by.id('field_stampDate'));
  purposeInput = element(by.id('field_purpose'));
  detailsInput = element(by.id('field_details'));
  remarksInput = element(by.id('field_remarks'));

  stampHolderSelect = element(by.id('field_stampHolder'));
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

  async setStampDateInput(stampDate: string): Promise<void> {
    await this.stampDateInput.sendKeys(stampDate);
  }

  async getStampDateInput(): Promise<string> {
    return await this.stampDateInput.getAttribute('value');
  }

  async setPurposeInput(purpose: string): Promise<void> {
    await this.purposeInput.sendKeys(purpose);
  }

  async getPurposeInput(): Promise<string> {
    return await this.purposeInput.getAttribute('value');
  }

  async setDetailsInput(details: string): Promise<void> {
    await this.detailsInput.sendKeys(details);
  }

  async getDetailsInput(): Promise<string> {
    return await this.detailsInput.getAttribute('value');
  }

  async setRemarksInput(remarks: string): Promise<void> {
    await this.remarksInput.sendKeys(remarks);
  }

  async getRemarksInput(): Promise<string> {
    return await this.remarksInput.getAttribute('value');
  }

  async stampHolderSelectLastOption(): Promise<void> {
    await this.stampHolderSelect.all(by.tagName('option')).last().click();
  }

  async stampHolderSelectOption(option: string): Promise<void> {
    await this.stampHolderSelect.sendKeys(option);
  }

  getStampHolderSelect(): ElementFinder {
    return this.stampHolderSelect;
  }

  async getStampHolderSelectedOption(): Promise<string> {
    return await this.stampHolderSelect.element(by.css('option:checked')).getText();
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

export class BusinessStampDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-businessStamp-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-businessStamp'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
