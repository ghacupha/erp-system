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

import { InvoiceComponentsPage, InvoiceDeleteDialog, InvoiceUpdatePage } from './invoice.page-object';

const expect = chai.expect;

describe('Invoice e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let invoiceComponentsPage: InvoiceComponentsPage;
  let invoiceUpdatePage: InvoiceUpdatePage;
  let invoiceDeleteDialog: InvoiceDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
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
      invoiceUpdatePage.currencySelectLastOption(),
      invoiceUpdatePage.setPaymentReferenceInput('paymentReference'),
      invoiceUpdatePage.setDealerNameInput('dealerName'),
      invoiceUpdatePage.setFileUploadTokenInput('fileUploadToken'),
      invoiceUpdatePage.setCompilationTokenInput('compilationToken'),
      // invoiceUpdatePage.paymentLabelSelectLastOption(),
      // invoiceUpdatePage.placeholderSelectLastOption(),
    ]);

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
    await browser.wait(ec.visibilityOf(invoiceComponentsPage.title), 5000);

    expect(await invoiceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
