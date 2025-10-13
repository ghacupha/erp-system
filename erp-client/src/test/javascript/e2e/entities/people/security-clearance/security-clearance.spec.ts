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

import {
  SecurityClearanceComponentsPage,
  SecurityClearanceDeleteDialog,
  SecurityClearanceUpdatePage,
} from './security-clearance.page-object';

const expect = chai.expect;

describe('SecurityClearance e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let securityClearanceComponentsPage: SecurityClearanceComponentsPage;
  let securityClearanceUpdatePage: SecurityClearanceUpdatePage;
  let securityClearanceDeleteDialog: SecurityClearanceDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SecurityClearances', async () => {
    await navBarPage.goToEntity('security-clearance');
    securityClearanceComponentsPage = new SecurityClearanceComponentsPage();
    await browser.wait(ec.visibilityOf(securityClearanceComponentsPage.title), 5000);
    expect(await securityClearanceComponentsPage.getTitle()).to.eq('Security Clearances');
    await browser.wait(
      ec.or(ec.visibilityOf(securityClearanceComponentsPage.entities), ec.visibilityOf(securityClearanceComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SecurityClearance page', async () => {
    await securityClearanceComponentsPage.clickOnCreateButton();
    securityClearanceUpdatePage = new SecurityClearanceUpdatePage();
    expect(await securityClearanceUpdatePage.getPageTitle()).to.eq('Create or edit a Security Clearance');
    await securityClearanceUpdatePage.cancel();
  });

  it('should create and save SecurityClearances', async () => {
    const nbButtonsBeforeCreate = await securityClearanceComponentsPage.countDeleteButtons();

    await securityClearanceComponentsPage.clickOnCreateButton();

    await promise.all([
      securityClearanceUpdatePage.setClearanceLevelInput('clearanceLevel'),
      // securityClearanceUpdatePage.grantedClearancesSelectLastOption(),
      // securityClearanceUpdatePage.placeholderSelectLastOption(),
    ]);

    await securityClearanceUpdatePage.save();
    expect(await securityClearanceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await securityClearanceComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last SecurityClearance', async () => {
    const nbButtonsBeforeDelete = await securityClearanceComponentsPage.countDeleteButtons();
    await securityClearanceComponentsPage.clickOnLastDeleteButton();

    securityClearanceDeleteDialog = new SecurityClearanceDeleteDialog();
    expect(await securityClearanceDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Security Clearance?');
    await securityClearanceDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(securityClearanceComponentsPage.title), 5000);

    expect(await securityClearanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
