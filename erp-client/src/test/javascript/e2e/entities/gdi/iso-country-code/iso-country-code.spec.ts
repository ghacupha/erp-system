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

import { IsoCountryCodeComponentsPage, IsoCountryCodeDeleteDialog, IsoCountryCodeUpdatePage } from './iso-country-code.page-object';

const expect = chai.expect;

describe('IsoCountryCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let isoCountryCodeComponentsPage: IsoCountryCodeComponentsPage;
  let isoCountryCodeUpdatePage: IsoCountryCodeUpdatePage;
  let isoCountryCodeDeleteDialog: IsoCountryCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load IsoCountryCodes', async () => {
    await navBarPage.goToEntity('iso-country-code');
    isoCountryCodeComponentsPage = new IsoCountryCodeComponentsPage();
    await browser.wait(ec.visibilityOf(isoCountryCodeComponentsPage.title), 5000);
    expect(await isoCountryCodeComponentsPage.getTitle()).to.eq('Iso Country Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(isoCountryCodeComponentsPage.entities), ec.visibilityOf(isoCountryCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create IsoCountryCode page', async () => {
    await isoCountryCodeComponentsPage.clickOnCreateButton();
    isoCountryCodeUpdatePage = new IsoCountryCodeUpdatePage();
    expect(await isoCountryCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Iso Country Code');
    await isoCountryCodeUpdatePage.cancel();
  });

  it('should create and save IsoCountryCodes', async () => {
    const nbButtonsBeforeCreate = await isoCountryCodeComponentsPage.countDeleteButtons();

    await isoCountryCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      isoCountryCodeUpdatePage.setCountryCodeInput('countryCode'),
      isoCountryCodeUpdatePage.setCountryDescriptionInput('countryDescription'),
      isoCountryCodeUpdatePage.setContinentCodeInput('continentCode'),
      isoCountryCodeUpdatePage.setContinentNameInput('continentName'),
      isoCountryCodeUpdatePage.setSubRegionInput('subRegion'),
    ]);

    await isoCountryCodeUpdatePage.save();
    expect(await isoCountryCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await isoCountryCodeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last IsoCountryCode', async () => {
    const nbButtonsBeforeDelete = await isoCountryCodeComponentsPage.countDeleteButtons();
    await isoCountryCodeComponentsPage.clickOnLastDeleteButton();

    isoCountryCodeDeleteDialog = new IsoCountryCodeDeleteDialog();
    expect(await isoCountryCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Iso Country Code?');
    await isoCountryCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(isoCountryCodeComponentsPage.title), 5000);

    expect(await isoCountryCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
