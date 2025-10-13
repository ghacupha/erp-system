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

import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  PrepaymentCompilationRequestComponentsPage,
  PrepaymentCompilationRequestDeleteDialog,
  PrepaymentCompilationRequestUpdatePage,
} from './prepayment-compilation-request.page-object';

const expect = chai.expect;

describe('PrepaymentCompilationRequest e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentCompilationRequestComponentsPage: PrepaymentCompilationRequestComponentsPage;
  let prepaymentCompilationRequestUpdatePage: PrepaymentCompilationRequestUpdatePage;
  let prepaymentCompilationRequestDeleteDialog: PrepaymentCompilationRequestDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PrepaymentCompilationRequests', async () => {
    await navBarPage.goToEntity('prepayment-compilation-request');
    prepaymentCompilationRequestComponentsPage = new PrepaymentCompilationRequestComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentCompilationRequestComponentsPage.title), 5000);
    expect(await prepaymentCompilationRequestComponentsPage.getTitle()).to.eq('Prepayment Compilation Requests');
    await browser.wait(
      ec.or(
        ec.visibilityOf(prepaymentCompilationRequestComponentsPage.entities),
        ec.visibilityOf(prepaymentCompilationRequestComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create PrepaymentCompilationRequest page', async () => {
    await prepaymentCompilationRequestComponentsPage.clickOnCreateButton();
    prepaymentCompilationRequestUpdatePage = new PrepaymentCompilationRequestUpdatePage();
    expect(await prepaymentCompilationRequestUpdatePage.getPageTitle()).to.eq('Create or edit a Prepayment Compilation Request');
    await prepaymentCompilationRequestUpdatePage.cancel();
  });

  it('should create and save PrepaymentCompilationRequests', async () => {
    const nbButtonsBeforeCreate = await prepaymentCompilationRequestComponentsPage.countDeleteButtons();

    await prepaymentCompilationRequestComponentsPage.clickOnCreateButton();

    await promise.all([
      prepaymentCompilationRequestUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      prepaymentCompilationRequestUpdatePage.compilationStatusSelectLastOption(),
      prepaymentCompilationRequestUpdatePage.setItemsProcessedInput('5'),
      prepaymentCompilationRequestUpdatePage.setCompilationTokenInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      // prepaymentCompilationRequestUpdatePage.placeholderSelectLastOption(),
    ]);

    await prepaymentCompilationRequestUpdatePage.save();
    expect(await prepaymentCompilationRequestUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await prepaymentCompilationRequestComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last PrepaymentCompilationRequest', async () => {
    const nbButtonsBeforeDelete = await prepaymentCompilationRequestComponentsPage.countDeleteButtons();
    await prepaymentCompilationRequestComponentsPage.clickOnLastDeleteButton();

    prepaymentCompilationRequestDeleteDialog = new PrepaymentCompilationRequestDeleteDialog();
    expect(await prepaymentCompilationRequestDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Prepayment Compilation Request?'
    );
    await prepaymentCompilationRequestDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(prepaymentCompilationRequestComponentsPage.title), 5000);

    expect(await prepaymentCompilationRequestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
