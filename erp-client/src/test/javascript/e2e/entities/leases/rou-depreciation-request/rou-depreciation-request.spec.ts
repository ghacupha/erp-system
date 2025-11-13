///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
  RouDepreciationRequestComponentsPage,
  RouDepreciationRequestDeleteDialog,
  RouDepreciationRequestUpdatePage,
} from './rou-depreciation-request.page-object';

const expect = chai.expect;

describe('RouDepreciationRequest e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouDepreciationRequestComponentsPage: RouDepreciationRequestComponentsPage;
  let rouDepreciationRequestUpdatePage: RouDepreciationRequestUpdatePage;
  let rouDepreciationRequestDeleteDialog: RouDepreciationRequestDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RouDepreciationRequests', async () => {
    await navBarPage.goToEntity('rou-depreciation-request');
    rouDepreciationRequestComponentsPage = new RouDepreciationRequestComponentsPage();
    await browser.wait(ec.visibilityOf(rouDepreciationRequestComponentsPage.title), 5000);
    expect(await rouDepreciationRequestComponentsPage.getTitle()).to.eq('Rou Depreciation Requests');
    await browser.wait(
      ec.or(ec.visibilityOf(rouDepreciationRequestComponentsPage.entities), ec.visibilityOf(rouDepreciationRequestComponentsPage.noResult)),
      1000
    );
  });

  it('should load create RouDepreciationRequest page', async () => {
    await rouDepreciationRequestComponentsPage.clickOnCreateButton();
    rouDepreciationRequestUpdatePage = new RouDepreciationRequestUpdatePage();
    expect(await rouDepreciationRequestUpdatePage.getPageTitle()).to.eq('Create or edit a Rou Depreciation Request');
    await rouDepreciationRequestUpdatePage.cancel();
  });

  it('should create and save RouDepreciationRequests', async () => {
    const nbButtonsBeforeCreate = await rouDepreciationRequestComponentsPage.countDeleteButtons();

    await rouDepreciationRequestComponentsPage.clickOnCreateButton();

    await promise.all([
      rouDepreciationRequestUpdatePage.setRequisitionIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      rouDepreciationRequestUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      rouDepreciationRequestUpdatePage.depreciationProcessStatusSelectLastOption(),
      rouDepreciationRequestUpdatePage.setNumberOfEnumeratedItemsInput('5'),
      rouDepreciationRequestUpdatePage.getInvalidatedInput().click(),
      rouDepreciationRequestUpdatePage.initiatedBySelectLastOption(),
    ]);

    await rouDepreciationRequestUpdatePage.save();
    expect(await rouDepreciationRequestUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await rouDepreciationRequestComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last RouDepreciationRequest', async () => {
    const nbButtonsBeforeDelete = await rouDepreciationRequestComponentsPage.countDeleteButtons();
    await rouDepreciationRequestComponentsPage.clickOnLastDeleteButton();

    rouDepreciationRequestDeleteDialog = new RouDepreciationRequestDeleteDialog();
    expect(await rouDepreciationRequestDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Rou Depreciation Request?'
    );
    await rouDepreciationRequestDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(rouDepreciationRequestComponentsPage.title), 5000);

    expect(await rouDepreciationRequestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
