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
  AssetWarrantyComponentsPage,
  /* AssetWarrantyDeleteDialog, */
  AssetWarrantyUpdatePage,
} from './asset-warranty.page-object';

const expect = chai.expect;

describe('AssetWarranty e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let assetWarrantyComponentsPage: AssetWarrantyComponentsPage;
  let assetWarrantyUpdatePage: AssetWarrantyUpdatePage;
  /* let assetWarrantyDeleteDialog: AssetWarrantyDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AssetWarranties', async () => {
    await navBarPage.goToEntity('asset-warranty');
    assetWarrantyComponentsPage = new AssetWarrantyComponentsPage();
    await browser.wait(ec.visibilityOf(assetWarrantyComponentsPage.title), 5000);
    expect(await assetWarrantyComponentsPage.getTitle()).to.eq('Asset Warranties');
    await browser.wait(
      ec.or(ec.visibilityOf(assetWarrantyComponentsPage.entities), ec.visibilityOf(assetWarrantyComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AssetWarranty page', async () => {
    await assetWarrantyComponentsPage.clickOnCreateButton();
    assetWarrantyUpdatePage = new AssetWarrantyUpdatePage();
    expect(await assetWarrantyUpdatePage.getPageTitle()).to.eq('Create or edit a Asset Warranty');
    await assetWarrantyUpdatePage.cancel();
  });

  /* it('should create and save AssetWarranties', async () => {
        const nbButtonsBeforeCreate = await assetWarrantyComponentsPage.countDeleteButtons();

        await assetWarrantyComponentsPage.clickOnCreateButton();

        await promise.all([
            assetWarrantyUpdatePage.setAssetTagInput('assetTag'),
            assetWarrantyUpdatePage.setDescriptionInput('description'),
            assetWarrantyUpdatePage.setModelNumberInput('modelNumber'),
            assetWarrantyUpdatePage.setSerialNumberInput('serialNumber'),
            assetWarrantyUpdatePage.setExpiryDateInput('2000-12-31'),
            // assetWarrantyUpdatePage.placeholderSelectLastOption(),
            // assetWarrantyUpdatePage.universallyUniqueMappingSelectLastOption(),
            assetWarrantyUpdatePage.dealerSelectLastOption(),
            // assetWarrantyUpdatePage.warrantyAttachmentSelectLastOption(),
        ]);

        await assetWarrantyUpdatePage.save();
        expect(await assetWarrantyUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await assetWarrantyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AssetWarranty', async () => {
        const nbButtonsBeforeDelete = await assetWarrantyComponentsPage.countDeleteButtons();
        await assetWarrantyComponentsPage.clickOnLastDeleteButton();

        assetWarrantyDeleteDialog = new AssetWarrantyDeleteDialog();
        expect(await assetWarrantyDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Asset Warranty?');
        await assetWarrantyDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(assetWarrantyComponentsPage.title), 5000);

        expect(await assetWarrantyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
