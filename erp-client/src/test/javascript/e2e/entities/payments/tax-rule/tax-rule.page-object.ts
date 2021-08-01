import { element, by, ElementFinder } from 'protractor';

export class TaxRuleComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-tax-rule div table .btn-danger'));
  title = element.all(by.css('jhi-tax-rule div h2#page-heading span')).first();
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

export class TaxRuleUpdatePage {
  pageTitle = element(by.id('jhi-tax-rule-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  paymentNumberInput = element(by.id('field_paymentNumber'));
  paymentDateInput = element(by.id('field_paymentDate'));
  telcoExciseDutyInput = element(by.id('field_telcoExciseDuty'));
  valueAddedTaxInput = element(by.id('field_valueAddedTax'));
  withholdingVATInput = element(by.id('field_withholdingVAT'));
  withholdingTaxConsultancyInput = element(by.id('field_withholdingTaxConsultancy'));
  withholdingTaxRentInput = element(by.id('field_withholdingTaxRent'));
  cateringLevyInput = element(by.id('field_cateringLevy'));
  serviceChargeInput = element(by.id('field_serviceCharge'));
  withholdingTaxImportedServiceInput = element(by.id('field_withholdingTaxImportedService'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setPaymentNumberInput(paymentNumber: string): Promise<void> {
    await this.paymentNumberInput.sendKeys(paymentNumber);
  }

  async getPaymentNumberInput(): Promise<string> {
    return await this.paymentNumberInput.getAttribute('value');
  }

  async setPaymentDateInput(paymentDate: string): Promise<void> {
    await this.paymentDateInput.sendKeys(paymentDate);
  }

  async getPaymentDateInput(): Promise<string> {
    return await this.paymentDateInput.getAttribute('value');
  }

  async setTelcoExciseDutyInput(telcoExciseDuty: string): Promise<void> {
    await this.telcoExciseDutyInput.sendKeys(telcoExciseDuty);
  }

  async getTelcoExciseDutyInput(): Promise<string> {
    return await this.telcoExciseDutyInput.getAttribute('value');
  }

  async setValueAddedTaxInput(valueAddedTax: string): Promise<void> {
    await this.valueAddedTaxInput.sendKeys(valueAddedTax);
  }

  async getValueAddedTaxInput(): Promise<string> {
    return await this.valueAddedTaxInput.getAttribute('value');
  }

  async setWithholdingVATInput(withholdingVAT: string): Promise<void> {
    await this.withholdingVATInput.sendKeys(withholdingVAT);
  }

  async getWithholdingVATInput(): Promise<string> {
    return await this.withholdingVATInput.getAttribute('value');
  }

  async setWithholdingTaxConsultancyInput(withholdingTaxConsultancy: string): Promise<void> {
    await this.withholdingTaxConsultancyInput.sendKeys(withholdingTaxConsultancy);
  }

  async getWithholdingTaxConsultancyInput(): Promise<string> {
    return await this.withholdingTaxConsultancyInput.getAttribute('value');
  }

  async setWithholdingTaxRentInput(withholdingTaxRent: string): Promise<void> {
    await this.withholdingTaxRentInput.sendKeys(withholdingTaxRent);
  }

  async getWithholdingTaxRentInput(): Promise<string> {
    return await this.withholdingTaxRentInput.getAttribute('value');
  }

  async setCateringLevyInput(cateringLevy: string): Promise<void> {
    await this.cateringLevyInput.sendKeys(cateringLevy);
  }

  async getCateringLevyInput(): Promise<string> {
    return await this.cateringLevyInput.getAttribute('value');
  }

  async setServiceChargeInput(serviceCharge: string): Promise<void> {
    await this.serviceChargeInput.sendKeys(serviceCharge);
  }

  async getServiceChargeInput(): Promise<string> {
    return await this.serviceChargeInput.getAttribute('value');
  }

  async setWithholdingTaxImportedServiceInput(withholdingTaxImportedService: string): Promise<void> {
    await this.withholdingTaxImportedServiceInput.sendKeys(withholdingTaxImportedService);
  }

  async getWithholdingTaxImportedServiceInput(): Promise<string> {
    return await this.withholdingTaxImportedServiceInput.getAttribute('value');
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

export class TaxRuleDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-taxRule-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-taxRule'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
