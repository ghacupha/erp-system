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

import { OutletTypeComponentsPage, OutletTypeDeleteDialog, OutletTypeUpdatePage } from './outlet-type.page-object';

const expect = chai.expect;

describe('OutletType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let outletTypeComponentsPage: OutletTypeComponentsPage;
  let outletTypeUpdatePage: OutletTypeUpdatePage;
  let outletTypeDeleteDialog: OutletTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load OutletTypes', async () => {
    await navBarPage.goToEntity('outlet-type');
    outletTypeComponentsPage = new OutletTypeComponentsPage();
    await browser.wait(ec.visibilityOf(outletTypeComponentsPage.title), 5000);
    expect(await outletTypeComponentsPage.getTitle()).to.eq('Outlet Types');
    await browser.wait(ec.or(ec.visibilityOf(outletTypeComponentsPage.entities), ec.visibilityOf(outletTypeComponentsPage.noResult)), 1000);
  });

  it('should load create OutletType page', async () => {
    await outletTypeComponentsPage.clickOnCreateButton();
    outletTypeUpdatePage = new OutletTypeUpdatePage();
    expect(await outletTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Outlet Type');
    await outletTypeUpdatePage.cancel();
  });

  it('should create and save OutletTypes', async () => {
    const nbButtonsBeforeCreate = await outletTypeComponentsPage.countDeleteButtons();

    await outletTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      outletTypeUpdatePage.setOutletTypeCodeInput('outletTypeCode'),
      outletTypeUpdatePage.setOutletTypeInput('outletType'),
      outletTypeUpdatePage.setOutletTypeDetailsInput('outletTypeDetails'),
      // outletTypeUpdatePage.placeholderSelectLastOption(),
    ]);

    await outletTypeUpdatePage.save();
    expect(await outletTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await outletTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last OutletType', async () => {
    const nbButtonsBeforeDelete = await outletTypeComponentsPage.countDeleteButtons();
    await outletTypeComponentsPage.clickOnLastDeleteButton();

    outletTypeDeleteDialog = new OutletTypeDeleteDialog();
    expect(await outletTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Outlet Type?');
    await outletTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(outletTypeComponentsPage.title), 5000);

    expect(await outletTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
