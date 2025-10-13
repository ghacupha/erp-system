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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AssetDisposalComponentsPage,
  /* AssetDisposalDeleteDialog, */
  AssetDisposalUpdatePage,
} from './asset-disposal.page-object';

const expect = chai.expect;

describe('AssetDisposal e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let assetDisposalComponentsPage: AssetDisposalComponentsPage;
  let assetDisposalUpdatePage: AssetDisposalUpdatePage;
  /* let assetDisposalDeleteDialog: AssetDisposalDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AssetDisposals', async () => {
    await navBarPage.goToEntity('asset-disposal');
    assetDisposalComponentsPage = new AssetDisposalComponentsPage();
    await browser.wait(ec.visibilityOf(assetDisposalComponentsPage.title), 5000);
    expect(await assetDisposalComponentsPage.getTitle()).to.eq('Asset Disposals');
    await browser.wait(
      ec.or(ec.visibilityOf(assetDisposalComponentsPage.entities), ec.visibilityOf(assetDisposalComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AssetDisposal page', async () => {
    await assetDisposalComponentsPage.clickOnCreateButton();
    assetDisposalUpdatePage = new AssetDisposalUpdatePage();
    expect(await assetDisposalUpdatePage.getPageTitle()).to.eq('Create or edit a Asset Disposal');
    await assetDisposalUpdatePage.cancel();
  });

  /* it('should create and save AssetDisposals', async () => {
        const nbButtonsBeforeCreate = await assetDisposalComponentsPage.countDeleteButtons();

        await assetDisposalComponentsPage.clickOnCreateButton();

        await promise.all([
            assetDisposalUpdatePage.setAssetDisposalReferenceInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            assetDisposalUpdatePage.setDescriptionInput('description'),
            assetDisposalUpdatePage.setAssetCostInput('5'),
            assetDisposalUpdatePage.setHistoricalCostInput('5'),
            assetDisposalUpdatePage.setAccruedDepreciationInput('5'),
            assetDisposalUpdatePage.setNetBookValueInput('5'),
            assetDisposalUpdatePage.setDecommissioningDateInput('2000-12-31'),
            assetDisposalUpdatePage.setDisposalDateInput('2000-12-31'),
            assetDisposalUpdatePage.getDormantInput().click(),
            assetDisposalUpdatePage.createdBySelectLastOption(),
            assetDisposalUpdatePage.modifiedBySelectLastOption(),
            assetDisposalUpdatePage.lastAccessedBySelectLastOption(),
            assetDisposalUpdatePage.effectivePeriodSelectLastOption(),
            // assetDisposalUpdatePage.placeholderSelectLastOption(),
            assetDisposalUpdatePage.assetDisposedSelectLastOption(),
        ]);

        await assetDisposalUpdatePage.save();
        expect(await assetDisposalUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await assetDisposalComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AssetDisposal', async () => {
        const nbButtonsBeforeDelete = await assetDisposalComponentsPage.countDeleteButtons();
        await assetDisposalComponentsPage.clickOnLastDeleteButton();

        assetDisposalDeleteDialog = new AssetDisposalDeleteDialog();
        expect(await assetDisposalDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Asset Disposal?');
        await assetDisposalDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(assetDisposalComponentsPage.title), 5000);

        expect(await assetDisposalComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
