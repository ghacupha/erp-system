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

import { SubCountyCodeComponentsPage, SubCountyCodeDeleteDialog, SubCountyCodeUpdatePage } from './sub-county-code.page-object';

const expect = chai.expect;

describe('SubCountyCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let subCountyCodeComponentsPage: SubCountyCodeComponentsPage;
  let subCountyCodeUpdatePage: SubCountyCodeUpdatePage;
  let subCountyCodeDeleteDialog: SubCountyCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SubCountyCodes', async () => {
    await navBarPage.goToEntity('sub-county-code');
    subCountyCodeComponentsPage = new SubCountyCodeComponentsPage();
    await browser.wait(ec.visibilityOf(subCountyCodeComponentsPage.title), 5000);
    expect(await subCountyCodeComponentsPage.getTitle()).to.eq('Sub County Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(subCountyCodeComponentsPage.entities), ec.visibilityOf(subCountyCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SubCountyCode page', async () => {
    await subCountyCodeComponentsPage.clickOnCreateButton();
    subCountyCodeUpdatePage = new SubCountyCodeUpdatePage();
    expect(await subCountyCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Sub County Code');
    await subCountyCodeUpdatePage.cancel();
  });

  it('should create and save SubCountyCodes', async () => {
    const nbButtonsBeforeCreate = await subCountyCodeComponentsPage.countDeleteButtons();

    await subCountyCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      subCountyCodeUpdatePage.setCountyCodeInput('countyCode'),
      subCountyCodeUpdatePage.setCountyNameInput('countyName'),
      subCountyCodeUpdatePage.setSubCountyCodeInput('subCountyCode'),
      subCountyCodeUpdatePage.setSubCountyNameInput('subCountyName'),
      // subCountyCodeUpdatePage.placeholderSelectLastOption(),
    ]);

    await subCountyCodeUpdatePage.save();
    expect(await subCountyCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await subCountyCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SubCountyCode', async () => {
    const nbButtonsBeforeDelete = await subCountyCodeComponentsPage.countDeleteButtons();
    await subCountyCodeComponentsPage.clickOnLastDeleteButton();

    subCountyCodeDeleteDialog = new SubCountyCodeDeleteDialog();
    expect(await subCountyCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sub County Code?');
    await subCountyCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(subCountyCodeComponentsPage.title), 5000);

    expect(await subCountyCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
