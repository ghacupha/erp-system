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
  FxTransactionRateTypeComponentsPage,
  FxTransactionRateTypeDeleteDialog,
  FxTransactionRateTypeUpdatePage,
} from './fx-transaction-rate-type.page-object';

const expect = chai.expect;

describe('FxTransactionRateType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fxTransactionRateTypeComponentsPage: FxTransactionRateTypeComponentsPage;
  let fxTransactionRateTypeUpdatePage: FxTransactionRateTypeUpdatePage;
  let fxTransactionRateTypeDeleteDialog: FxTransactionRateTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FxTransactionRateTypes', async () => {
    await navBarPage.goToEntity('fx-transaction-rate-type');
    fxTransactionRateTypeComponentsPage = new FxTransactionRateTypeComponentsPage();
    await browser.wait(ec.visibilityOf(fxTransactionRateTypeComponentsPage.title), 5000);
    expect(await fxTransactionRateTypeComponentsPage.getTitle()).to.eq('Fx Transaction Rate Types');
    await browser.wait(
      ec.or(ec.visibilityOf(fxTransactionRateTypeComponentsPage.entities), ec.visibilityOf(fxTransactionRateTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FxTransactionRateType page', async () => {
    await fxTransactionRateTypeComponentsPage.clickOnCreateButton();
    fxTransactionRateTypeUpdatePage = new FxTransactionRateTypeUpdatePage();
    expect(await fxTransactionRateTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Fx Transaction Rate Type');
    await fxTransactionRateTypeUpdatePage.cancel();
  });

  it('should create and save FxTransactionRateTypes', async () => {
    const nbButtonsBeforeCreate = await fxTransactionRateTypeComponentsPage.countDeleteButtons();

    await fxTransactionRateTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      fxTransactionRateTypeUpdatePage.setFxTransactionRateTypeCodeInput('fxTransactionRateTypeCode'),
      fxTransactionRateTypeUpdatePage.setFxTransactionRateTypeInput('fxTransactionRateType'),
      fxTransactionRateTypeUpdatePage.setFxTransactionRateTypeDetailsInput('fxTransactionRateTypeDetails'),
    ]);

    await fxTransactionRateTypeUpdatePage.save();
    expect(await fxTransactionRateTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fxTransactionRateTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FxTransactionRateType', async () => {
    const nbButtonsBeforeDelete = await fxTransactionRateTypeComponentsPage.countDeleteButtons();
    await fxTransactionRateTypeComponentsPage.clickOnLastDeleteButton();

    fxTransactionRateTypeDeleteDialog = new FxTransactionRateTypeDeleteDialog();
    expect(await fxTransactionRateTypeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Fx Transaction Rate Type?'
    );
    await fxTransactionRateTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fxTransactionRateTypeComponentsPage.title), 5000);

    expect(await fxTransactionRateTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
