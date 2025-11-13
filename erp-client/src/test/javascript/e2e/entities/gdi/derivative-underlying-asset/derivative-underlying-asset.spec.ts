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
  DerivativeUnderlyingAssetComponentsPage,
  DerivativeUnderlyingAssetDeleteDialog,
  DerivativeUnderlyingAssetUpdatePage,
} from './derivative-underlying-asset.page-object';

const expect = chai.expect;

describe('DerivativeUnderlyingAsset e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let derivativeUnderlyingAssetComponentsPage: DerivativeUnderlyingAssetComponentsPage;
  let derivativeUnderlyingAssetUpdatePage: DerivativeUnderlyingAssetUpdatePage;
  let derivativeUnderlyingAssetDeleteDialog: DerivativeUnderlyingAssetDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DerivativeUnderlyingAssets', async () => {
    await navBarPage.goToEntity('derivative-underlying-asset');
    derivativeUnderlyingAssetComponentsPage = new DerivativeUnderlyingAssetComponentsPage();
    await browser.wait(ec.visibilityOf(derivativeUnderlyingAssetComponentsPage.title), 5000);
    expect(await derivativeUnderlyingAssetComponentsPage.getTitle()).to.eq('Derivative Underlying Assets');
    await browser.wait(
      ec.or(
        ec.visibilityOf(derivativeUnderlyingAssetComponentsPage.entities),
        ec.visibilityOf(derivativeUnderlyingAssetComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create DerivativeUnderlyingAsset page', async () => {
    await derivativeUnderlyingAssetComponentsPage.clickOnCreateButton();
    derivativeUnderlyingAssetUpdatePage = new DerivativeUnderlyingAssetUpdatePage();
    expect(await derivativeUnderlyingAssetUpdatePage.getPageTitle()).to.eq('Create or edit a Derivative Underlying Asset');
    await derivativeUnderlyingAssetUpdatePage.cancel();
  });

  it('should create and save DerivativeUnderlyingAssets', async () => {
    const nbButtonsBeforeCreate = await derivativeUnderlyingAssetComponentsPage.countDeleteButtons();

    await derivativeUnderlyingAssetComponentsPage.clickOnCreateButton();

    await promise.all([
      derivativeUnderlyingAssetUpdatePage.setDerivativeUnderlyingAssetTypeCodeInput('derivativeUnderlyingAssetTypeCode'),
      derivativeUnderlyingAssetUpdatePage.setFinancialDerivativeUnderlyingAssetTypeInput('financialDerivativeUnderlyingAssetType'),
      derivativeUnderlyingAssetUpdatePage.setDerivativeUnderlyingAssetTypeDetailsInput('derivativeUnderlyingAssetTypeDetails'),
    ]);

    await derivativeUnderlyingAssetUpdatePage.save();
    expect(await derivativeUnderlyingAssetUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await derivativeUnderlyingAssetComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last DerivativeUnderlyingAsset', async () => {
    const nbButtonsBeforeDelete = await derivativeUnderlyingAssetComponentsPage.countDeleteButtons();
    await derivativeUnderlyingAssetComponentsPage.clickOnLastDeleteButton();

    derivativeUnderlyingAssetDeleteDialog = new DerivativeUnderlyingAssetDeleteDialog();
    expect(await derivativeUnderlyingAssetDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Derivative Underlying Asset?'
    );
    await derivativeUnderlyingAssetDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(derivativeUnderlyingAssetComponentsPage.title), 5000);

    expect(await derivativeUnderlyingAssetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
