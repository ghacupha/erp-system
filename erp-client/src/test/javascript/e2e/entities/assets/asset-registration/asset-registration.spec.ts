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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AssetRegistrationComponentsPage,
  /* AssetRegistrationDeleteDialog, */
  AssetRegistrationUpdatePage,
} from './asset-registration.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('AssetRegistration e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let assetRegistrationComponentsPage: AssetRegistrationComponentsPage;
  let assetRegistrationUpdatePage: AssetRegistrationUpdatePage;
  /* let assetRegistrationDeleteDialog: AssetRegistrationDeleteDialog; */
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AssetRegistrations', async () => {
    await navBarPage.goToEntity('asset-registration');
    assetRegistrationComponentsPage = new AssetRegistrationComponentsPage();
    await browser.wait(ec.visibilityOf(assetRegistrationComponentsPage.title), 5000);
    expect(await assetRegistrationComponentsPage.getTitle()).to.eq('Asset Registrations');
    await browser.wait(
      ec.or(ec.visibilityOf(assetRegistrationComponentsPage.entities), ec.visibilityOf(assetRegistrationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AssetRegistration page', async () => {
    await assetRegistrationComponentsPage.clickOnCreateButton();
    assetRegistrationUpdatePage = new AssetRegistrationUpdatePage();
    expect(await assetRegistrationUpdatePage.getPageTitle()).to.eq('Create or edit a Asset Registration');
    await assetRegistrationUpdatePage.cancel();
  });

  /* it('should create and save AssetRegistrations', async () => {
        const nbButtonsBeforeCreate = await assetRegistrationComponentsPage.countDeleteButtons();

        await assetRegistrationComponentsPage.clickOnCreateButton();

        await promise.all([
            assetRegistrationUpdatePage.setAssetNumberInput('assetNumber'),
            assetRegistrationUpdatePage.setAssetTagInput('assetTag'),
            assetRegistrationUpdatePage.setAssetDetailsInput('assetDetails'),
            assetRegistrationUpdatePage.setAssetCostInput('5'),
            assetRegistrationUpdatePage.setCommentsInput(absolutePath),
            assetRegistrationUpdatePage.setModelNumberInput('modelNumber'),
            assetRegistrationUpdatePage.setSerialNumberInput('serialNumber'),
            assetRegistrationUpdatePage.setRemarksInput('remarks'),
            assetRegistrationUpdatePage.setCapitalizationDateInput('2000-12-31'),
            assetRegistrationUpdatePage.setHistoricalCostInput('5'),
            assetRegistrationUpdatePage.setRegistrationDateInput('2000-12-31'),
            // assetRegistrationUpdatePage.placeholderSelectLastOption(),
            // assetRegistrationUpdatePage.paymentInvoicesSelectLastOption(),
            // assetRegistrationUpdatePage.otherRelatedServiceOutletsSelectLastOption(),
            // assetRegistrationUpdatePage.otherRelatedSettlementsSelectLastOption(),
            assetRegistrationUpdatePage.assetCategorySelectLastOption(),
            // assetRegistrationUpdatePage.purchaseOrderSelectLastOption(),
            // assetRegistrationUpdatePage.deliveryNoteSelectLastOption(),
            // assetRegistrationUpdatePage.jobSheetSelectLastOption(),
            assetRegistrationUpdatePage.dealerSelectLastOption(),
            // assetRegistrationUpdatePage.designatedUsersSelectLastOption(),
            assetRegistrationUpdatePage.settlementCurrencySelectLastOption(),
            // assetRegistrationUpdatePage.businessDocumentSelectLastOption(),
            // assetRegistrationUpdatePage.assetWarrantySelectLastOption(),
            // assetRegistrationUpdatePage.universallyUniqueMappingSelectLastOption(),
            // assetRegistrationUpdatePage.assetAccessorySelectLastOption(),
            assetRegistrationUpdatePage.mainServiceOutletSelectLastOption(),
            assetRegistrationUpdatePage.acquiringTransactionSelectLastOption(),
        ]);

        await assetRegistrationUpdatePage.save();
        expect(await assetRegistrationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await assetRegistrationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AssetRegistration', async () => {
        const nbButtonsBeforeDelete = await assetRegistrationComponentsPage.countDeleteButtons();
        await assetRegistrationComponentsPage.clickOnLastDeleteButton();

        assetRegistrationDeleteDialog = new AssetRegistrationDeleteDialog();
        expect(await assetRegistrationDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Asset Registration?');
        await assetRegistrationDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(assetRegistrationComponentsPage.title), 5000);

        expect(await assetRegistrationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
