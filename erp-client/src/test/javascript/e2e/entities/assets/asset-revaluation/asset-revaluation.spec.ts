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

import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AssetRevaluationComponentsPage,
  /* AssetRevaluationDeleteDialog, */
  AssetRevaluationUpdatePage,
} from './asset-revaluation.page-object';

const expect = chai.expect;

describe('AssetRevaluation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let assetRevaluationComponentsPage: AssetRevaluationComponentsPage;
  let assetRevaluationUpdatePage: AssetRevaluationUpdatePage;
  /* let assetRevaluationDeleteDialog: AssetRevaluationDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AssetRevaluations', async () => {
    await navBarPage.goToEntity('asset-revaluation');
    assetRevaluationComponentsPage = new AssetRevaluationComponentsPage();
    await browser.wait(ec.visibilityOf(assetRevaluationComponentsPage.title), 5000);
    expect(await assetRevaluationComponentsPage.getTitle()).to.eq('Asset Revaluations');
    await browser.wait(
      ec.or(ec.visibilityOf(assetRevaluationComponentsPage.entities), ec.visibilityOf(assetRevaluationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AssetRevaluation page', async () => {
    await assetRevaluationComponentsPage.clickOnCreateButton();
    assetRevaluationUpdatePage = new AssetRevaluationUpdatePage();
    expect(await assetRevaluationUpdatePage.getPageTitle()).to.eq('Create or edit a Asset Revaluation');
    await assetRevaluationUpdatePage.cancel();
  });

  /* it('should create and save AssetRevaluations', async () => {
        const nbButtonsBeforeCreate = await assetRevaluationComponentsPage.countDeleteButtons();

        await assetRevaluationComponentsPage.clickOnCreateButton();

        await promise.all([
            assetRevaluationUpdatePage.setDescriptionInput('description'),
            assetRevaluationUpdatePage.setDevaluationAmountInput('5'),
            assetRevaluationUpdatePage.setRevaluationDateInput('2000-12-31'),
            assetRevaluationUpdatePage.setRevaluationReferenceIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            assetRevaluationUpdatePage.setTimeOfCreationInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            assetRevaluationUpdatePage.revaluerSelectLastOption(),
            assetRevaluationUpdatePage.createdBySelectLastOption(),
            assetRevaluationUpdatePage.lastModifiedBySelectLastOption(),
            assetRevaluationUpdatePage.lastAccessedBySelectLastOption(),
            assetRevaluationUpdatePage.effectivePeriodSelectLastOption(),
            assetRevaluationUpdatePage.revaluedAssetSelectLastOption(),
            // assetRevaluationUpdatePage.placeholderSelectLastOption(),
        ]);

        await assetRevaluationUpdatePage.save();
        expect(await assetRevaluationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await assetRevaluationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AssetRevaluation', async () => {
        const nbButtonsBeforeDelete = await assetRevaluationComponentsPage.countDeleteButtons();
        await assetRevaluationComponentsPage.clickOnLastDeleteButton();

        assetRevaluationDeleteDialog = new AssetRevaluationDeleteDialog();
        expect(await assetRevaluationDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Asset Revaluation?');
        await assetRevaluationDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(assetRevaluationComponentsPage.title), 5000);

        expect(await assetRevaluationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
