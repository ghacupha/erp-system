import { element, by, ElementFinder } from 'protractor';

export class CrbSubscriptionStatusTypeCodeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-crb-subscription-status-type-code div table .btn-danger'));
  title = element.all(by.css('jhi-crb-subscription-status-type-code div h2#page-heading span')).first();
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

export class CrbSubscriptionStatusTypeCodeUpdatePage {
  pageTitle = element(by.id('jhi-crb-subscription-status-type-code-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  subscriptionStatusTypeCodeInput = element(by.id('field_subscriptionStatusTypeCode'));
  subscriptionStatusTypeInput = element(by.id('field_subscriptionStatusType'));
  subscriptionStatusTypeDescriptionInput = element(by.id('field_subscriptionStatusTypeDescription'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSubscriptionStatusTypeCodeInput(subscriptionStatusTypeCode: string): Promise<void> {
    await this.subscriptionStatusTypeCodeInput.sendKeys(subscriptionStatusTypeCode);
  }

  async getSubscriptionStatusTypeCodeInput(): Promise<string> {
    return await this.subscriptionStatusTypeCodeInput.getAttribute('value');
  }

  async setSubscriptionStatusTypeInput(subscriptionStatusType: string): Promise<void> {
    await this.subscriptionStatusTypeInput.sendKeys(subscriptionStatusType);
  }

  async getSubscriptionStatusTypeInput(): Promise<string> {
    return await this.subscriptionStatusTypeInput.getAttribute('value');
  }

  async setSubscriptionStatusTypeDescriptionInput(subscriptionStatusTypeDescription: string): Promise<void> {
    await this.subscriptionStatusTypeDescriptionInput.sendKeys(subscriptionStatusTypeDescription);
  }

  async getSubscriptionStatusTypeDescriptionInput(): Promise<string> {
    return await this.subscriptionStatusTypeDescriptionInput.getAttribute('value');
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

export class CrbSubscriptionStatusTypeCodeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-crbSubscriptionStatusTypeCode-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-crbSubscriptionStatusTypeCode'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
