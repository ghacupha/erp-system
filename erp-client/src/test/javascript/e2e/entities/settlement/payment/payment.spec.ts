///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { PaymentComponentsPage, PaymentDeleteDialog, PaymentUpdatePage } from './payment.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Payment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentComponentsPage: PaymentComponentsPage;
  let paymentUpdatePage: PaymentUpdatePage;
  let paymentDeleteDialog: PaymentDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Payments', async () => {
    await navBarPage.goToEntity('payment');
    paymentComponentsPage = new PaymentComponentsPage();
    await browser.wait(ec.visibilityOf(paymentComponentsPage.title), 5000);
    expect(await paymentComponentsPage.getTitle()).to.eq('Payments');
    await browser.wait(ec.or(ec.visibilityOf(paymentComponentsPage.entities), ec.visibilityOf(paymentComponentsPage.noResult)), 1000);
  });

  it('should load create Payment page', async () => {
    await paymentComponentsPage.clickOnCreateButton();
    paymentUpdatePage = new PaymentUpdatePage();
    expect(await paymentUpdatePage.getPageTitle()).to.eq('Create or edit a Payment');
    await paymentUpdatePage.cancel();
  });

  it('should create and save Payments', async () => {
    const nbButtonsBeforeCreate = await paymentComponentsPage.countDeleteButtons();

    await paymentComponentsPage.clickOnCreateButton();

    await promise.all([
      paymentUpdatePage.setPaymentNumberInput('paymentNumber'),
      paymentUpdatePage.setPaymentDateInput('2000-12-31'),
      paymentUpdatePage.setInvoicedAmountInput('5'),
      paymentUpdatePage.setPaymentAmountInput('5'),
      paymentUpdatePage.setDescriptionInput('description'),
      paymentUpdatePage.settlementCurrencySelectLastOption(),
      paymentUpdatePage.setCalculationFileInput(absolutePath),
      paymentUpdatePage.setDealerNameInput('dealerName'),
      paymentUpdatePage.setPurchaseOrderNumberInput('purchaseOrderNumber'),
      paymentUpdatePage.setFileUploadTokenInput('fileUploadToken'),
      paymentUpdatePage.setCompilationTokenInput('compilationToken'),
      // paymentUpdatePage.paymentLabelSelectLastOption(),
      paymentUpdatePage.paymentCategorySelectLastOption(),
      // paymentUpdatePage.placeholderSelectLastOption(),
      paymentUpdatePage.paymentGroupSelectLastOption(),
    ]);

    await paymentUpdatePage.save();
    expect(await paymentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await paymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Payment', async () => {
    const nbButtonsBeforeDelete = await paymentComponentsPage.countDeleteButtons();
    await paymentComponentsPage.clickOnLastDeleteButton();

    paymentDeleteDialog = new PaymentDeleteDialog();
    expect(await paymentDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Payment?');
    await paymentDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(paymentComponentsPage.title), 5000);

    expect(await paymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
