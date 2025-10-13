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
  TACompilationRequestComponentsPage,
  TACompilationRequestDeleteDialog,
  TACompilationRequestUpdatePage,
} from './ta-compilation-request.page-object';

const expect = chai.expect;

describe('TACompilationRequest e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let tACompilationRequestComponentsPage: TACompilationRequestComponentsPage;
  let tACompilationRequestUpdatePage: TACompilationRequestUpdatePage;
  let tACompilationRequestDeleteDialog: TACompilationRequestDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TACompilationRequests', async () => {
    await navBarPage.goToEntity('ta-compilation-request');
    tACompilationRequestComponentsPage = new TACompilationRequestComponentsPage();
    await browser.wait(ec.visibilityOf(tACompilationRequestComponentsPage.title), 5000);
    expect(await tACompilationRequestComponentsPage.getTitle()).to.eq('TA Compilation Requests');
    await browser.wait(
      ec.or(ec.visibilityOf(tACompilationRequestComponentsPage.entities), ec.visibilityOf(tACompilationRequestComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TACompilationRequest page', async () => {
    await tACompilationRequestComponentsPage.clickOnCreateButton();
    tACompilationRequestUpdatePage = new TACompilationRequestUpdatePage();
    expect(await tACompilationRequestUpdatePage.getPageTitle()).to.eq('Create or edit a TA Compilation Request');
    await tACompilationRequestUpdatePage.cancel();
  });

  it('should create and save TACompilationRequests', async () => {
    const nbButtonsBeforeCreate = await tACompilationRequestComponentsPage.countDeleteButtons();

    await tACompilationRequestComponentsPage.clickOnCreateButton();

    await promise.all([
      tACompilationRequestUpdatePage.setRequisitionIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      tACompilationRequestUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      tACompilationRequestUpdatePage.compilationProcessStatusSelectLastOption(),
      tACompilationRequestUpdatePage.setNumberOfEnumeratedItemsInput('5'),
      tACompilationRequestUpdatePage.setBatchJobIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      tACompilationRequestUpdatePage.setCompilationTimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      tACompilationRequestUpdatePage.getInvalidatedInput().click(),
      tACompilationRequestUpdatePage.initiatedBySelectLastOption(),
    ]);

    await tACompilationRequestUpdatePage.save();
    expect(await tACompilationRequestUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await tACompilationRequestComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last TACompilationRequest', async () => {
    const nbButtonsBeforeDelete = await tACompilationRequestComponentsPage.countDeleteButtons();
    await tACompilationRequestComponentsPage.clickOnLastDeleteButton();

    tACompilationRequestDeleteDialog = new TACompilationRequestDeleteDialog();
    expect(await tACompilationRequestDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this TA Compilation Request?');
    await tACompilationRequestDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(tACompilationRequestComponentsPage.title), 5000);

    expect(await tACompilationRequestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
