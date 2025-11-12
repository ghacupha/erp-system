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
  FxTransactionTypeComponentsPage,
  FxTransactionTypeDeleteDialog,
  FxTransactionTypeUpdatePage,
} from './fx-transaction-type.page-object';

const expect = chai.expect;

describe('FxTransactionType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fxTransactionTypeComponentsPage: FxTransactionTypeComponentsPage;
  let fxTransactionTypeUpdatePage: FxTransactionTypeUpdatePage;
  let fxTransactionTypeDeleteDialog: FxTransactionTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FxTransactionTypes', async () => {
    await navBarPage.goToEntity('fx-transaction-type');
    fxTransactionTypeComponentsPage = new FxTransactionTypeComponentsPage();
    await browser.wait(ec.visibilityOf(fxTransactionTypeComponentsPage.title), 5000);
    expect(await fxTransactionTypeComponentsPage.getTitle()).to.eq('Fx Transaction Types');
    await browser.wait(
      ec.or(ec.visibilityOf(fxTransactionTypeComponentsPage.entities), ec.visibilityOf(fxTransactionTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FxTransactionType page', async () => {
    await fxTransactionTypeComponentsPage.clickOnCreateButton();
    fxTransactionTypeUpdatePage = new FxTransactionTypeUpdatePage();
    expect(await fxTransactionTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Fx Transaction Type');
    await fxTransactionTypeUpdatePage.cancel();
  });

  it('should create and save FxTransactionTypes', async () => {
    const nbButtonsBeforeCreate = await fxTransactionTypeComponentsPage.countDeleteButtons();

    await fxTransactionTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      fxTransactionTypeUpdatePage.setFxTransactionTypeCodeInput('fxTransactionTypeCode'),
      fxTransactionTypeUpdatePage.setFxTransactionTypeInput('fxTransactionType'),
      fxTransactionTypeUpdatePage.setFxTransactionTypeDescriptionInput('fxTransactionTypeDescription'),
    ]);

    await fxTransactionTypeUpdatePage.save();
    expect(await fxTransactionTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fxTransactionTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FxTransactionType', async () => {
    const nbButtonsBeforeDelete = await fxTransactionTypeComponentsPage.countDeleteButtons();
    await fxTransactionTypeComponentsPage.clickOnLastDeleteButton();

    fxTransactionTypeDeleteDialog = new FxTransactionTypeDeleteDialog();
    expect(await fxTransactionTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Fx Transaction Type?');
    await fxTransactionTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fxTransactionTypeComponentsPage.title), 5000);

    expect(await fxTransactionTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
