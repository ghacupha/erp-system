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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CrbAgingBandsComponentsPage, CrbAgingBandsDeleteDialog, CrbAgingBandsUpdatePage } from './crb-aging-bands.page-object';

const expect = chai.expect;

describe('CrbAgingBands e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbAgingBandsComponentsPage: CrbAgingBandsComponentsPage;
  let crbAgingBandsUpdatePage: CrbAgingBandsUpdatePage;
  let crbAgingBandsDeleteDialog: CrbAgingBandsDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbAgingBands', async () => {
    await navBarPage.goToEntity('crb-aging-bands');
    crbAgingBandsComponentsPage = new CrbAgingBandsComponentsPage();
    await browser.wait(ec.visibilityOf(crbAgingBandsComponentsPage.title), 5000);
    expect(await crbAgingBandsComponentsPage.getTitle()).to.eq('Crb Aging Bands');
    await browser.wait(
      ec.or(ec.visibilityOf(crbAgingBandsComponentsPage.entities), ec.visibilityOf(crbAgingBandsComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbAgingBands page', async () => {
    await crbAgingBandsComponentsPage.clickOnCreateButton();
    crbAgingBandsUpdatePage = new CrbAgingBandsUpdatePage();
    expect(await crbAgingBandsUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Aging Bands');
    await crbAgingBandsUpdatePage.cancel();
  });

  it('should create and save CrbAgingBands', async () => {
    const nbButtonsBeforeCreate = await crbAgingBandsComponentsPage.countDeleteButtons();

    await crbAgingBandsComponentsPage.clickOnCreateButton();

    await promise.all([
      crbAgingBandsUpdatePage.setAgingBandCategoryCodeInput('agingBandCategoryCode'),
      crbAgingBandsUpdatePage.setAgingBandCategoryInput('agingBandCategory'),
      crbAgingBandsUpdatePage.setAgingBandCategoryDetailsInput('agingBandCategoryDetails'),
    ]);

    await crbAgingBandsUpdatePage.save();
    expect(await crbAgingBandsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbAgingBandsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CrbAgingBands', async () => {
    const nbButtonsBeforeDelete = await crbAgingBandsComponentsPage.countDeleteButtons();
    await crbAgingBandsComponentsPage.clickOnLastDeleteButton();

    crbAgingBandsDeleteDialog = new CrbAgingBandsDeleteDialog();
    expect(await crbAgingBandsDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crb Aging Bands?');
    await crbAgingBandsDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbAgingBandsComponentsPage.title), 5000);

    expect(await crbAgingBandsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
