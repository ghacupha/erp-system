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

import { InvoiceComponentsPage, InvoiceDeleteDialog, InvoiceUpdatePage } from './invoice.page-object';

const expect = chai.expect;

describe('Invoice e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let invoiceComponentsPage: InvoiceComponentsPage;
  let invoiceUpdatePage: InvoiceUpdatePage;
  let invoiceDeleteDialog: InvoiceDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Invoices', async () => {
    await navBarPage.goToEntity('invoice');
    invoiceComponentsPage = new InvoiceComponentsPage();
    await browser.wait(ec.visibilityOf(invoiceComponentsPage.title), 5000);
    expect(await invoiceComponentsPage.getTitle()).to.eq('Invoices');
    await browser.wait(ec.or(ec.visibilityOf(invoiceComponentsPage.entities), ec.visibilityOf(invoiceComponentsPage.noResult)), 1000);
  });

  it('should load create Invoice page', async () => {
    await invoiceComponentsPage.clickOnCreateButton();
    invoiceUpdatePage = new InvoiceUpdatePage();
    expect(await invoiceUpdatePage.getPageTitle()).to.eq('Create or edit a Invoice');
    await invoiceUpdatePage.cancel();
  });

  it('should create and save Invoices', async () => {
    const nbButtonsBeforeCreate = await invoiceComponentsPage.countDeleteButtons();

    await invoiceComponentsPage.clickOnCreateButton();

    await promise.all([
      invoiceUpdatePage.setInvoiceNumberInput('invoiceNumber'),
      invoiceUpdatePage.setInvoiceDateInput('2000-12-31'),
      invoiceUpdatePage.setInvoiceAmountInput('5'),
      invoiceUpdatePage.setPaymentCategoryInput('paymentCategory'),
      invoiceUpdatePage.setDealerNameInput('dealerName'),
      invoiceUpdatePage.paymentSelectLastOption(),
      invoiceUpdatePage.dealerSelectLastOption(),
    ]);

    expect(await invoiceUpdatePage.getInvoiceNumberInput()).to.eq(
      'invoiceNumber',
      'Expected InvoiceNumber value to be equals to invoiceNumber'
    );
    expect(await invoiceUpdatePage.getInvoiceDateInput()).to.eq('2000-12-31', 'Expected invoiceDate value to be equals to 2000-12-31');
    expect(await invoiceUpdatePage.getInvoiceAmountInput()).to.eq('5', 'Expected invoiceAmount value to be equals to 5');
    expect(await invoiceUpdatePage.getPaymentCategoryInput()).to.eq(
      'paymentCategory',
      'Expected PaymentCategory value to be equals to paymentCategory'
    );
    expect(await invoiceUpdatePage.getDealerNameInput()).to.eq('dealerName', 'Expected DealerName value to be equals to dealerName');

    await invoiceUpdatePage.save();
    expect(await invoiceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await invoiceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Invoice', async () => {
    const nbButtonsBeforeDelete = await invoiceComponentsPage.countDeleteButtons();
    await invoiceComponentsPage.clickOnLastDeleteButton();

    invoiceDeleteDialog = new InvoiceDeleteDialog();
    expect(await invoiceDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Invoice?');
    await invoiceDeleteDialog.clickOnConfirmButton();

    expect(await invoiceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
