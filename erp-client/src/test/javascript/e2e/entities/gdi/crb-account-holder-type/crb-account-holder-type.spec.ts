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
  CrbAccountHolderTypeComponentsPage,
  CrbAccountHolderTypeDeleteDialog,
  CrbAccountHolderTypeUpdatePage,
} from './crb-account-holder-type.page-object';

const expect = chai.expect;

describe('CrbAccountHolderType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbAccountHolderTypeComponentsPage: CrbAccountHolderTypeComponentsPage;
  let crbAccountHolderTypeUpdatePage: CrbAccountHolderTypeUpdatePage;
  let crbAccountHolderTypeDeleteDialog: CrbAccountHolderTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbAccountHolderTypes', async () => {
    await navBarPage.goToEntity('crb-account-holder-type');
    crbAccountHolderTypeComponentsPage = new CrbAccountHolderTypeComponentsPage();
    await browser.wait(ec.visibilityOf(crbAccountHolderTypeComponentsPage.title), 5000);
    expect(await crbAccountHolderTypeComponentsPage.getTitle()).to.eq('Crb Account Holder Types');
    await browser.wait(
      ec.or(ec.visibilityOf(crbAccountHolderTypeComponentsPage.entities), ec.visibilityOf(crbAccountHolderTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbAccountHolderType page', async () => {
    await crbAccountHolderTypeComponentsPage.clickOnCreateButton();
    crbAccountHolderTypeUpdatePage = new CrbAccountHolderTypeUpdatePage();
    expect(await crbAccountHolderTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Account Holder Type');
    await crbAccountHolderTypeUpdatePage.cancel();
  });

  it('should create and save CrbAccountHolderTypes', async () => {
    const nbButtonsBeforeCreate = await crbAccountHolderTypeComponentsPage.countDeleteButtons();

    await crbAccountHolderTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbAccountHolderTypeUpdatePage.setAccountHolderCategoryTypeCodeInput('accountHolderCategoryTypeCode'),
      crbAccountHolderTypeUpdatePage.setAccountHolderCategoryTypeInput('accountHolderCategoryType'),
    ]);

    await crbAccountHolderTypeUpdatePage.save();
    expect(await crbAccountHolderTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbAccountHolderTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbAccountHolderType', async () => {
    const nbButtonsBeforeDelete = await crbAccountHolderTypeComponentsPage.countDeleteButtons();
    await crbAccountHolderTypeComponentsPage.clickOnLastDeleteButton();

    crbAccountHolderTypeDeleteDialog = new CrbAccountHolderTypeDeleteDialog();
    expect(await crbAccountHolderTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crb Account Holder Type?');
    await crbAccountHolderTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbAccountHolderTypeComponentsPage.title), 5000);

    expect(await crbAccountHolderTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
