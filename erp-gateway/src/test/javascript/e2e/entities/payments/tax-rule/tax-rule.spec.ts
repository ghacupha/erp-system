///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { TaxRuleComponentsPage, TaxRuleDeleteDialog, TaxRuleUpdatePage } from './tax-rule.page-object';

const expect = chai.expect;

describe('TaxRule e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let taxRuleComponentsPage: TaxRuleComponentsPage;
  let taxRuleUpdatePage: TaxRuleUpdatePage;
  let taxRuleDeleteDialog: TaxRuleDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TaxRules', async () => {
    await navBarPage.goToEntity('tax-rule');
    taxRuleComponentsPage = new TaxRuleComponentsPage();
    await browser.wait(ec.visibilityOf(taxRuleComponentsPage.title), 5000);
    expect(await taxRuleComponentsPage.getTitle()).to.eq('Tax Rules');
    await browser.wait(ec.or(ec.visibilityOf(taxRuleComponentsPage.entities), ec.visibilityOf(taxRuleComponentsPage.noResult)), 1000);
  });

  it('should load create TaxRule page', async () => {
    await taxRuleComponentsPage.clickOnCreateButton();
    taxRuleUpdatePage = new TaxRuleUpdatePage();
    expect(await taxRuleUpdatePage.getPageTitle()).to.eq('Create or edit a Tax Rule');
    await taxRuleUpdatePage.cancel();
  });

  it('should create and save TaxRules', async () => {
    const nbButtonsBeforeCreate = await taxRuleComponentsPage.countDeleteButtons();

    await taxRuleComponentsPage.clickOnCreateButton();

    await promise.all([
      taxRuleUpdatePage.setPaymentNumberInput('paymentNumber'),
      taxRuleUpdatePage.setPaymentDateInput('2000-12-31'),
      taxRuleUpdatePage.setTelcoExciseDutyInput('5'),
      taxRuleUpdatePage.setValueAddedTaxInput('5'),
      taxRuleUpdatePage.setWithholdingVATInput('5'),
      taxRuleUpdatePage.setWithholdingTaxConsultancyInput('5'),
      taxRuleUpdatePage.setWithholdingTaxRentInput('5'),
      taxRuleUpdatePage.setCateringLevyInput('5'),
      taxRuleUpdatePage.setServiceChargeInput('5'),
      taxRuleUpdatePage.setWithholdingTaxImportedServiceInput('5'),
    ]);

    expect(await taxRuleUpdatePage.getPaymentNumberInput()).to.eq(
      'paymentNumber',
      'Expected PaymentNumber value to be equals to paymentNumber'
    );
    expect(await taxRuleUpdatePage.getPaymentDateInput()).to.eq('2000-12-31', 'Expected paymentDate value to be equals to 2000-12-31');
    expect(await taxRuleUpdatePage.getTelcoExciseDutyInput()).to.eq('5', 'Expected telcoExciseDuty value to be equals to 5');
    expect(await taxRuleUpdatePage.getValueAddedTaxInput()).to.eq('5', 'Expected valueAddedTax value to be equals to 5');
    expect(await taxRuleUpdatePage.getWithholdingVATInput()).to.eq('5', 'Expected withholdingVAT value to be equals to 5');
    expect(await taxRuleUpdatePage.getWithholdingTaxConsultancyInput()).to.eq(
      '5',
      'Expected withholdingTaxConsultancy value to be equals to 5'
    );
    expect(await taxRuleUpdatePage.getWithholdingTaxRentInput()).to.eq('5', 'Expected withholdingTaxRent value to be equals to 5');
    expect(await taxRuleUpdatePage.getCateringLevyInput()).to.eq('5', 'Expected cateringLevy value to be equals to 5');
    expect(await taxRuleUpdatePage.getServiceChargeInput()).to.eq('5', 'Expected serviceCharge value to be equals to 5');
    expect(await taxRuleUpdatePage.getWithholdingTaxImportedServiceInput()).to.eq(
      '5',
      'Expected withholdingTaxImportedService value to be equals to 5'
    );

    await taxRuleUpdatePage.save();
    expect(await taxRuleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await taxRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last TaxRule', async () => {
    const nbButtonsBeforeDelete = await taxRuleComponentsPage.countDeleteButtons();
    await taxRuleComponentsPage.clickOnLastDeleteButton();

    taxRuleDeleteDialog = new TaxRuleDeleteDialog();
    expect(await taxRuleDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Tax Rule?');
    await taxRuleDeleteDialog.clickOnConfirmButton();

    expect(await taxRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
