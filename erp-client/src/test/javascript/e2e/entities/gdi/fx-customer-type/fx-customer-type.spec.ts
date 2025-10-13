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

import { FxCustomerTypeComponentsPage, FxCustomerTypeDeleteDialog, FxCustomerTypeUpdatePage } from './fx-customer-type.page-object';

const expect = chai.expect;

describe('FxCustomerType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fxCustomerTypeComponentsPage: FxCustomerTypeComponentsPage;
  let fxCustomerTypeUpdatePage: FxCustomerTypeUpdatePage;
  let fxCustomerTypeDeleteDialog: FxCustomerTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FxCustomerTypes', async () => {
    await navBarPage.goToEntity('fx-customer-type');
    fxCustomerTypeComponentsPage = new FxCustomerTypeComponentsPage();
    await browser.wait(ec.visibilityOf(fxCustomerTypeComponentsPage.title), 5000);
    expect(await fxCustomerTypeComponentsPage.getTitle()).to.eq('Fx Customer Types');
    await browser.wait(
      ec.or(ec.visibilityOf(fxCustomerTypeComponentsPage.entities), ec.visibilityOf(fxCustomerTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FxCustomerType page', async () => {
    await fxCustomerTypeComponentsPage.clickOnCreateButton();
    fxCustomerTypeUpdatePage = new FxCustomerTypeUpdatePage();
    expect(await fxCustomerTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Fx Customer Type');
    await fxCustomerTypeUpdatePage.cancel();
  });

  it('should create and save FxCustomerTypes', async () => {
    const nbButtonsBeforeCreate = await fxCustomerTypeComponentsPage.countDeleteButtons();

    await fxCustomerTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      fxCustomerTypeUpdatePage.setForeignExchangeCustomerTypeCodeInput('foreignExchangeCustomerTypeCode'),
      fxCustomerTypeUpdatePage.setForeignCustomerTypeInput('foreignCustomerType'),
    ]);

    await fxCustomerTypeUpdatePage.save();
    expect(await fxCustomerTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fxCustomerTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FxCustomerType', async () => {
    const nbButtonsBeforeDelete = await fxCustomerTypeComponentsPage.countDeleteButtons();
    await fxCustomerTypeComponentsPage.clickOnLastDeleteButton();

    fxCustomerTypeDeleteDialog = new FxCustomerTypeDeleteDialog();
    expect(await fxCustomerTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Fx Customer Type?');
    await fxCustomerTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fxCustomerTypeComponentsPage.title), 5000);

    expect(await fxCustomerTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
