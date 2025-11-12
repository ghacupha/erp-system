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

import { SecurityTypeComponentsPage, SecurityTypeDeleteDialog, SecurityTypeUpdatePage } from './security-type.page-object';

const expect = chai.expect;

describe('SecurityType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let securityTypeComponentsPage: SecurityTypeComponentsPage;
  let securityTypeUpdatePage: SecurityTypeUpdatePage;
  let securityTypeDeleteDialog: SecurityTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SecurityTypes', async () => {
    await navBarPage.goToEntity('security-type');
    securityTypeComponentsPage = new SecurityTypeComponentsPage();
    await browser.wait(ec.visibilityOf(securityTypeComponentsPage.title), 5000);
    expect(await securityTypeComponentsPage.getTitle()).to.eq('Security Types');
    await browser.wait(
      ec.or(ec.visibilityOf(securityTypeComponentsPage.entities), ec.visibilityOf(securityTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SecurityType page', async () => {
    await securityTypeComponentsPage.clickOnCreateButton();
    securityTypeUpdatePage = new SecurityTypeUpdatePage();
    expect(await securityTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Security Type');
    await securityTypeUpdatePage.cancel();
  });

  it('should create and save SecurityTypes', async () => {
    const nbButtonsBeforeCreate = await securityTypeComponentsPage.countDeleteButtons();

    await securityTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      securityTypeUpdatePage.setSecurityTypeCodeInput('securityTypeCode'),
      securityTypeUpdatePage.setSecurityTypeInput('securityType'),
      securityTypeUpdatePage.setSecurityTypeDetailsInput('securityTypeDetails'),
      securityTypeUpdatePage.setSecurityTypeDescriptionInput('securityTypeDescription'),
    ]);

    await securityTypeUpdatePage.save();
    expect(await securityTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await securityTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SecurityType', async () => {
    const nbButtonsBeforeDelete = await securityTypeComponentsPage.countDeleteButtons();
    await securityTypeComponentsPage.clickOnLastDeleteButton();

    securityTypeDeleteDialog = new SecurityTypeDeleteDialog();
    expect(await securityTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Security Type?');
    await securityTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(securityTypeComponentsPage.title), 5000);

    expect(await securityTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
