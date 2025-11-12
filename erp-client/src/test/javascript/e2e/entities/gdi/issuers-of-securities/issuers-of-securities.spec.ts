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

import {
  IssuersOfSecuritiesComponentsPage,
  IssuersOfSecuritiesDeleteDialog,
  IssuersOfSecuritiesUpdatePage,
} from './issuers-of-securities.page-object';

const expect = chai.expect;

describe('IssuersOfSecurities e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let issuersOfSecuritiesComponentsPage: IssuersOfSecuritiesComponentsPage;
  let issuersOfSecuritiesUpdatePage: IssuersOfSecuritiesUpdatePage;
  let issuersOfSecuritiesDeleteDialog: IssuersOfSecuritiesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load IssuersOfSecurities', async () => {
    await navBarPage.goToEntity('issuers-of-securities');
    issuersOfSecuritiesComponentsPage = new IssuersOfSecuritiesComponentsPage();
    await browser.wait(ec.visibilityOf(issuersOfSecuritiesComponentsPage.title), 5000);
    expect(await issuersOfSecuritiesComponentsPage.getTitle()).to.eq('Issuers Of Securities');
    await browser.wait(
      ec.or(ec.visibilityOf(issuersOfSecuritiesComponentsPage.entities), ec.visibilityOf(issuersOfSecuritiesComponentsPage.noResult)),
      1000
    );
  });

  it('should load create IssuersOfSecurities page', async () => {
    await issuersOfSecuritiesComponentsPage.clickOnCreateButton();
    issuersOfSecuritiesUpdatePage = new IssuersOfSecuritiesUpdatePage();
    expect(await issuersOfSecuritiesUpdatePage.getPageTitle()).to.eq('Create or edit a Issuers Of Securities');
    await issuersOfSecuritiesUpdatePage.cancel();
  });

  it('should create and save IssuersOfSecurities', async () => {
    const nbButtonsBeforeCreate = await issuersOfSecuritiesComponentsPage.countDeleteButtons();

    await issuersOfSecuritiesComponentsPage.clickOnCreateButton();

    await promise.all([
      issuersOfSecuritiesUpdatePage.setIssuerOfSecuritiesCodeInput('issuerOfSecuritiesCode'),
      issuersOfSecuritiesUpdatePage.setIssuerOfSecuritiesInput('issuerOfSecurities'),
      issuersOfSecuritiesUpdatePage.setIssuerOfSecuritiesDescriptionInput('issuerOfSecuritiesDescription'),
    ]);

    await issuersOfSecuritiesUpdatePage.save();
    expect(await issuersOfSecuritiesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await issuersOfSecuritiesComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last IssuersOfSecurities', async () => {
    const nbButtonsBeforeDelete = await issuersOfSecuritiesComponentsPage.countDeleteButtons();
    await issuersOfSecuritiesComponentsPage.clickOnLastDeleteButton();

    issuersOfSecuritiesDeleteDialog = new IssuersOfSecuritiesDeleteDialog();
    expect(await issuersOfSecuritiesDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Issuers Of Securities?');
    await issuersOfSecuritiesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(issuersOfSecuritiesComponentsPage.title), 5000);

    expect(await issuersOfSecuritiesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
