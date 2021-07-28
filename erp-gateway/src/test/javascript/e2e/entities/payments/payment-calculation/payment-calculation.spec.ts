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

import {
  PaymentCalculationComponentsPage,
  PaymentCalculationDeleteDialog,
  PaymentCalculationUpdatePage,
} from './payment-calculation.page-object';

const expect = chai.expect;

describe('PaymentCalculation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentCalculationComponentsPage: PaymentCalculationComponentsPage;
  let paymentCalculationUpdatePage: PaymentCalculationUpdatePage;
  let paymentCalculationDeleteDialog: PaymentCalculationDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PaymentCalculations', async () => {
    await navBarPage.goToEntity('payment-calculation');
    paymentCalculationComponentsPage = new PaymentCalculationComponentsPage();
    await browser.wait(ec.visibilityOf(paymentCalculationComponentsPage.title), 5000);
    expect(await paymentCalculationComponentsPage.getTitle()).to.eq('Payment Calculations');
    await browser.wait(
      ec.or(ec.visibilityOf(paymentCalculationComponentsPage.entities), ec.visibilityOf(paymentCalculationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PaymentCalculation page', async () => {
    await paymentCalculationComponentsPage.clickOnCreateButton();
    paymentCalculationUpdatePage = new PaymentCalculationUpdatePage();
    expect(await paymentCalculationUpdatePage.getPageTitle()).to.eq('Create or edit a Payment Calculation');
    await paymentCalculationUpdatePage.cancel();
  });

  it('should create and save PaymentCalculations', async () => {
    const nbButtonsBeforeCreate = await paymentCalculationComponentsPage.countDeleteButtons();

    await paymentCalculationComponentsPage.clickOnCreateButton();

    await promise.all([
      paymentCalculationUpdatePage.setPaymentNumberInput('paymentNumber'),
      paymentCalculationUpdatePage.setPaymentDateInput('2000-12-31'),
      paymentCalculationUpdatePage.setPaymentCategoryInput('paymentCategory'),
      paymentCalculationUpdatePage.setPaymentExpenseInput('5'),
      paymentCalculationUpdatePage.setWithholdingVATInput('5'),
      paymentCalculationUpdatePage.setWithholdingTaxInput('5'),
      paymentCalculationUpdatePage.setPaymentAmountInput('5'),
    ]);

    expect(await paymentCalculationUpdatePage.getPaymentNumberInput()).to.eq(
      'paymentNumber',
      'Expected PaymentNumber value to be equals to paymentNumber'
    );
    expect(await paymentCalculationUpdatePage.getPaymentDateInput()).to.eq(
      '2000-12-31',
      'Expected paymentDate value to be equals to 2000-12-31'
    );
    expect(await paymentCalculationUpdatePage.getPaymentCategoryInput()).to.eq(
      'paymentCategory',
      'Expected PaymentCategory value to be equals to paymentCategory'
    );
    expect(await paymentCalculationUpdatePage.getPaymentExpenseInput()).to.eq('5', 'Expected paymentExpense value to be equals to 5');
    expect(await paymentCalculationUpdatePage.getWithholdingVATInput()).to.eq('5', 'Expected withholdingVAT value to be equals to 5');
    expect(await paymentCalculationUpdatePage.getWithholdingTaxInput()).to.eq('5', 'Expected withholdingTax value to be equals to 5');
    expect(await paymentCalculationUpdatePage.getPaymentAmountInput()).to.eq('5', 'Expected paymentAmount value to be equals to 5');

    await paymentCalculationUpdatePage.save();
    expect(await paymentCalculationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await paymentCalculationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last PaymentCalculation', async () => {
    const nbButtonsBeforeDelete = await paymentCalculationComponentsPage.countDeleteButtons();
    await paymentCalculationComponentsPage.clickOnLastDeleteButton();

    paymentCalculationDeleteDialog = new PaymentCalculationDeleteDialog();
    expect(await paymentCalculationDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Payment Calculation?');
    await paymentCalculationDeleteDialog.clickOnConfirmButton();

    expect(await paymentCalculationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
