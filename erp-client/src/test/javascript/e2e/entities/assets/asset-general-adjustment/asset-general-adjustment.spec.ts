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
  AssetGeneralAdjustmentComponentsPage,
  /* AssetGeneralAdjustmentDeleteDialog, */
  AssetGeneralAdjustmentUpdatePage,
} from './asset-general-adjustment.page-object';

const expect = chai.expect;

describe('AssetGeneralAdjustment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let assetGeneralAdjustmentComponentsPage: AssetGeneralAdjustmentComponentsPage;
  let assetGeneralAdjustmentUpdatePage: AssetGeneralAdjustmentUpdatePage;
  /* let assetGeneralAdjustmentDeleteDialog: AssetGeneralAdjustmentDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AssetGeneralAdjustments', async () => {
    await navBarPage.goToEntity('asset-general-adjustment');
    assetGeneralAdjustmentComponentsPage = new AssetGeneralAdjustmentComponentsPage();
    await browser.wait(ec.visibilityOf(assetGeneralAdjustmentComponentsPage.title), 5000);
    expect(await assetGeneralAdjustmentComponentsPage.getTitle()).to.eq('Asset General Adjustments');
    await browser.wait(
      ec.or(ec.visibilityOf(assetGeneralAdjustmentComponentsPage.entities), ec.visibilityOf(assetGeneralAdjustmentComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AssetGeneralAdjustment page', async () => {
    await assetGeneralAdjustmentComponentsPage.clickOnCreateButton();
    assetGeneralAdjustmentUpdatePage = new AssetGeneralAdjustmentUpdatePage();
    expect(await assetGeneralAdjustmentUpdatePage.getPageTitle()).to.eq('Create or edit a Asset General Adjustment');
    await assetGeneralAdjustmentUpdatePage.cancel();
  });

  /* it('should create and save AssetGeneralAdjustments', async () => {
        const nbButtonsBeforeCreate = await assetGeneralAdjustmentComponentsPage.countDeleteButtons();

        await assetGeneralAdjustmentComponentsPage.clickOnCreateButton();

        await promise.all([
            assetGeneralAdjustmentUpdatePage.setDescriptionInput('description'),
            assetGeneralAdjustmentUpdatePage.setDevaluationAmountInput('5'),
            assetGeneralAdjustmentUpdatePage.setAdjustmentDateInput('2000-12-31'),
            assetGeneralAdjustmentUpdatePage.setTimeOfCreationInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            assetGeneralAdjustmentUpdatePage.setAdjustmentReferenceIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            assetGeneralAdjustmentUpdatePage.effectivePeriodSelectLastOption(),
            assetGeneralAdjustmentUpdatePage.assetRegistrationSelectLastOption(),
            assetGeneralAdjustmentUpdatePage.createdBySelectLastOption(),
            assetGeneralAdjustmentUpdatePage.lastModifiedBySelectLastOption(),
            assetGeneralAdjustmentUpdatePage.lastAccessedBySelectLastOption(),
            assetGeneralAdjustmentUpdatePage.placeholderSelectLastOption(),
        ]);

        await assetGeneralAdjustmentUpdatePage.save();
        expect(await assetGeneralAdjustmentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await assetGeneralAdjustmentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AssetGeneralAdjustment', async () => {
        const nbButtonsBeforeDelete = await assetGeneralAdjustmentComponentsPage.countDeleteButtons();
        await assetGeneralAdjustmentComponentsPage.clickOnLastDeleteButton();

        assetGeneralAdjustmentDeleteDialog = new AssetGeneralAdjustmentDeleteDialog();
        expect(await assetGeneralAdjustmentDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Asset General Adjustment?');
        await assetGeneralAdjustmentDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(assetGeneralAdjustmentComponentsPage.title), 5000);

        expect(await assetGeneralAdjustmentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
