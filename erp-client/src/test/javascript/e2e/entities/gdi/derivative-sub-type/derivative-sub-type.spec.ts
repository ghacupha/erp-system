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

import {
  DerivativeSubTypeComponentsPage,
  DerivativeSubTypeDeleteDialog,
  DerivativeSubTypeUpdatePage,
} from './derivative-sub-type.page-object';

const expect = chai.expect;

describe('DerivativeSubType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let derivativeSubTypeComponentsPage: DerivativeSubTypeComponentsPage;
  let derivativeSubTypeUpdatePage: DerivativeSubTypeUpdatePage;
  let derivativeSubTypeDeleteDialog: DerivativeSubTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DerivativeSubTypes', async () => {
    await navBarPage.goToEntity('derivative-sub-type');
    derivativeSubTypeComponentsPage = new DerivativeSubTypeComponentsPage();
    await browser.wait(ec.visibilityOf(derivativeSubTypeComponentsPage.title), 5000);
    expect(await derivativeSubTypeComponentsPage.getTitle()).to.eq('Derivative Sub Types');
    await browser.wait(
      ec.or(ec.visibilityOf(derivativeSubTypeComponentsPage.entities), ec.visibilityOf(derivativeSubTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DerivativeSubType page', async () => {
    await derivativeSubTypeComponentsPage.clickOnCreateButton();
    derivativeSubTypeUpdatePage = new DerivativeSubTypeUpdatePage();
    expect(await derivativeSubTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Derivative Sub Type');
    await derivativeSubTypeUpdatePage.cancel();
  });

  it('should create and save DerivativeSubTypes', async () => {
    const nbButtonsBeforeCreate = await derivativeSubTypeComponentsPage.countDeleteButtons();

    await derivativeSubTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      derivativeSubTypeUpdatePage.setFinancialDerivativeSubTypeCodeInput('financialDerivativeSubTypeCode'),
      derivativeSubTypeUpdatePage.setFinancialDerivativeSubTyeInput('financialDerivativeSubTye'),
      derivativeSubTypeUpdatePage.setFinancialDerivativeSubtypeDetailsInput('financialDerivativeSubtypeDetails'),
    ]);

    await derivativeSubTypeUpdatePage.save();
    expect(await derivativeSubTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await derivativeSubTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last DerivativeSubType', async () => {
    const nbButtonsBeforeDelete = await derivativeSubTypeComponentsPage.countDeleteButtons();
    await derivativeSubTypeComponentsPage.clickOnLastDeleteButton();

    derivativeSubTypeDeleteDialog = new DerivativeSubTypeDeleteDialog();
    expect(await derivativeSubTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Derivative Sub Type?');
    await derivativeSubTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(derivativeSubTypeComponentsPage.title), 5000);

    expect(await derivativeSubTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
