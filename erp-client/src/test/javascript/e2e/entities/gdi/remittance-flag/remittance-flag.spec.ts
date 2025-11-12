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

import { RemittanceFlagComponentsPage, RemittanceFlagDeleteDialog, RemittanceFlagUpdatePage } from './remittance-flag.page-object';

const expect = chai.expect;

describe('RemittanceFlag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let remittanceFlagComponentsPage: RemittanceFlagComponentsPage;
  let remittanceFlagUpdatePage: RemittanceFlagUpdatePage;
  let remittanceFlagDeleteDialog: RemittanceFlagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RemittanceFlags', async () => {
    await navBarPage.goToEntity('remittance-flag');
    remittanceFlagComponentsPage = new RemittanceFlagComponentsPage();
    await browser.wait(ec.visibilityOf(remittanceFlagComponentsPage.title), 5000);
    expect(await remittanceFlagComponentsPage.getTitle()).to.eq('Remittance Flags');
    await browser.wait(
      ec.or(ec.visibilityOf(remittanceFlagComponentsPage.entities), ec.visibilityOf(remittanceFlagComponentsPage.noResult)),
      1000
    );
  });

  it('should load create RemittanceFlag page', async () => {
    await remittanceFlagComponentsPage.clickOnCreateButton();
    remittanceFlagUpdatePage = new RemittanceFlagUpdatePage();
    expect(await remittanceFlagUpdatePage.getPageTitle()).to.eq('Create or edit a Remittance Flag');
    await remittanceFlagUpdatePage.cancel();
  });

  it('should create and save RemittanceFlags', async () => {
    const nbButtonsBeforeCreate = await remittanceFlagComponentsPage.countDeleteButtons();

    await remittanceFlagComponentsPage.clickOnCreateButton();

    await promise.all([
      remittanceFlagUpdatePage.remittanceTypeFlagSelectLastOption(),
      remittanceFlagUpdatePage.remittanceTypeSelectLastOption(),
      remittanceFlagUpdatePage.setRemittanceTypeDetailsInput('remittanceTypeDetails'),
    ]);

    await remittanceFlagUpdatePage.save();
    expect(await remittanceFlagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await remittanceFlagComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last RemittanceFlag', async () => {
    const nbButtonsBeforeDelete = await remittanceFlagComponentsPage.countDeleteButtons();
    await remittanceFlagComponentsPage.clickOnLastDeleteButton();

    remittanceFlagDeleteDialog = new RemittanceFlagDeleteDialog();
    expect(await remittanceFlagDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Remittance Flag?');
    await remittanceFlagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(remittanceFlagComponentsPage.title), 5000);

    expect(await remittanceFlagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
