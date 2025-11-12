///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { CreditNoteComponentsPage, CreditNoteDeleteDialog, CreditNoteUpdatePage } from './credit-note.page-object';

const expect = chai.expect;

describe('CreditNote e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let creditNoteComponentsPage: CreditNoteComponentsPage;
  let creditNoteUpdatePage: CreditNoteUpdatePage;
  let creditNoteDeleteDialog: CreditNoteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CreditNotes', async () => {
    await navBarPage.goToEntity('credit-note');
    creditNoteComponentsPage = new CreditNoteComponentsPage();
    await browser.wait(ec.visibilityOf(creditNoteComponentsPage.title), 5000);
    expect(await creditNoteComponentsPage.getTitle()).to.eq('Credit Notes');
    await browser.wait(ec.or(ec.visibilityOf(creditNoteComponentsPage.entities), ec.visibilityOf(creditNoteComponentsPage.noResult)), 1000);
  });

  it('should load create CreditNote page', async () => {
    await creditNoteComponentsPage.clickOnCreateButton();
    creditNoteUpdatePage = new CreditNoteUpdatePage();
    expect(await creditNoteUpdatePage.getPageTitle()).to.eq('Create or edit a Credit Note');
    await creditNoteUpdatePage.cancel();
  });

  it('should create and save CreditNotes', async () => {
    const nbButtonsBeforeCreate = await creditNoteComponentsPage.countDeleteButtons();

    await creditNoteComponentsPage.clickOnCreateButton();

    await promise.all([
      creditNoteUpdatePage.setCreditNumberInput('creditNumber'),
      creditNoteUpdatePage.setCreditNoteDateInput('2000-12-31'),
      creditNoteUpdatePage.setCreditAmountInput('5'),
      creditNoteUpdatePage.setRemarksInput('remarks'),
      // creditNoteUpdatePage.purchaseOrdersSelectLastOption(),
      // creditNoteUpdatePage.invoicesSelectLastOption(),
      // creditNoteUpdatePage.paymentLabelSelectLastOption(),
      // creditNoteUpdatePage.placeholderSelectLastOption(),
      creditNoteUpdatePage.settlementCurrencySelectLastOption(),
    ]);

    await creditNoteUpdatePage.save();
    expect(await creditNoteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await creditNoteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CreditNote', async () => {
    const nbButtonsBeforeDelete = await creditNoteComponentsPage.countDeleteButtons();
    await creditNoteComponentsPage.clickOnLastDeleteButton();

    creditNoteDeleteDialog = new CreditNoteDeleteDialog();
    expect(await creditNoteDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Credit Note?');
    await creditNoteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(creditNoteComponentsPage.title), 5000);

    expect(await creditNoteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
