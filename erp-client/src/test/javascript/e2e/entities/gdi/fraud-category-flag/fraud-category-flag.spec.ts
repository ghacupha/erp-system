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
  FraudCategoryFlagComponentsPage,
  FraudCategoryFlagDeleteDialog,
  FraudCategoryFlagUpdatePage,
} from './fraud-category-flag.page-object';

const expect = chai.expect;

describe('FraudCategoryFlag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fraudCategoryFlagComponentsPage: FraudCategoryFlagComponentsPage;
  let fraudCategoryFlagUpdatePage: FraudCategoryFlagUpdatePage;
  let fraudCategoryFlagDeleteDialog: FraudCategoryFlagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FraudCategoryFlags', async () => {
    await navBarPage.goToEntity('fraud-category-flag');
    fraudCategoryFlagComponentsPage = new FraudCategoryFlagComponentsPage();
    await browser.wait(ec.visibilityOf(fraudCategoryFlagComponentsPage.title), 5000);
    expect(await fraudCategoryFlagComponentsPage.getTitle()).to.eq('Fraud Category Flags');
    await browser.wait(
      ec.or(ec.visibilityOf(fraudCategoryFlagComponentsPage.entities), ec.visibilityOf(fraudCategoryFlagComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FraudCategoryFlag page', async () => {
    await fraudCategoryFlagComponentsPage.clickOnCreateButton();
    fraudCategoryFlagUpdatePage = new FraudCategoryFlagUpdatePage();
    expect(await fraudCategoryFlagUpdatePage.getPageTitle()).to.eq('Create or edit a Fraud Category Flag');
    await fraudCategoryFlagUpdatePage.cancel();
  });

  it('should create and save FraudCategoryFlags', async () => {
    const nbButtonsBeforeCreate = await fraudCategoryFlagComponentsPage.countDeleteButtons();

    await fraudCategoryFlagComponentsPage.clickOnCreateButton();

    await promise.all([
      fraudCategoryFlagUpdatePage.fraudCategoryFlagSelectLastOption(),
      fraudCategoryFlagUpdatePage.setFraudCategoryTypeDetailsInput('fraudCategoryTypeDetails'),
    ]);

    await fraudCategoryFlagUpdatePage.save();
    expect(await fraudCategoryFlagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fraudCategoryFlagComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FraudCategoryFlag', async () => {
    const nbButtonsBeforeDelete = await fraudCategoryFlagComponentsPage.countDeleteButtons();
    await fraudCategoryFlagComponentsPage.clickOnLastDeleteButton();

    fraudCategoryFlagDeleteDialog = new FraudCategoryFlagDeleteDialog();
    expect(await fraudCategoryFlagDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Fraud Category Flag?');
    await fraudCategoryFlagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fraudCategoryFlagComponentsPage.title), 5000);

    expect(await fraudCategoryFlagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
