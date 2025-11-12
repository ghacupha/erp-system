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

import { SecurityTenureComponentsPage, SecurityTenureDeleteDialog, SecurityTenureUpdatePage } from './security-tenure.page-object';

const expect = chai.expect;

describe('SecurityTenure e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let securityTenureComponentsPage: SecurityTenureComponentsPage;
  let securityTenureUpdatePage: SecurityTenureUpdatePage;
  let securityTenureDeleteDialog: SecurityTenureDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SecurityTenures', async () => {
    await navBarPage.goToEntity('security-tenure');
    securityTenureComponentsPage = new SecurityTenureComponentsPage();
    await browser.wait(ec.visibilityOf(securityTenureComponentsPage.title), 5000);
    expect(await securityTenureComponentsPage.getTitle()).to.eq('Security Tenures');
    await browser.wait(
      ec.or(ec.visibilityOf(securityTenureComponentsPage.entities), ec.visibilityOf(securityTenureComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SecurityTenure page', async () => {
    await securityTenureComponentsPage.clickOnCreateButton();
    securityTenureUpdatePage = new SecurityTenureUpdatePage();
    expect(await securityTenureUpdatePage.getPageTitle()).to.eq('Create or edit a Security Tenure');
    await securityTenureUpdatePage.cancel();
  });

  it('should create and save SecurityTenures', async () => {
    const nbButtonsBeforeCreate = await securityTenureComponentsPage.countDeleteButtons();

    await securityTenureComponentsPage.clickOnCreateButton();

    await promise.all([
      securityTenureUpdatePage.setSecurityTenureCodeInput('securityTenureCode'),
      securityTenureUpdatePage.setSecurityTenureTypeInput('securityTenureType'),
      securityTenureUpdatePage.setSecurityTenureDetailsInput('securityTenureDetails'),
    ]);

    await securityTenureUpdatePage.save();
    expect(await securityTenureUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await securityTenureComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last SecurityTenure', async () => {
    const nbButtonsBeforeDelete = await securityTenureComponentsPage.countDeleteButtons();
    await securityTenureComponentsPage.clickOnLastDeleteButton();

    securityTenureDeleteDialog = new SecurityTenureDeleteDialog();
    expect(await securityTenureDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Security Tenure?');
    await securityTenureDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(securityTenureComponentsPage.title), 5000);

    expect(await securityTenureComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
