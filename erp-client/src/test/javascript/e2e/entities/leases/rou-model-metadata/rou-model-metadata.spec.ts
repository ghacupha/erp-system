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
  RouModelMetadataComponentsPage,
  /* RouModelMetadataDeleteDialog, */
  RouModelMetadataUpdatePage,
} from './rou-model-metadata.page-object';

const expect = chai.expect;

describe('RouModelMetadata e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouModelMetadataComponentsPage: RouModelMetadataComponentsPage;
  let rouModelMetadataUpdatePage: RouModelMetadataUpdatePage;
  /* let rouModelMetadataDeleteDialog: RouModelMetadataDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RouModelMetadata', async () => {
    await navBarPage.goToEntity('rou-model-metadata');
    rouModelMetadataComponentsPage = new RouModelMetadataComponentsPage();
    await browser.wait(ec.visibilityOf(rouModelMetadataComponentsPage.title), 5000);
    expect(await rouModelMetadataComponentsPage.getTitle()).to.eq('Rou Model Metadata');
    await browser.wait(
      ec.or(ec.visibilityOf(rouModelMetadataComponentsPage.entities), ec.visibilityOf(rouModelMetadataComponentsPage.noResult)),
      1000
    );
  });

  it('should load create RouModelMetadata page', async () => {
    await rouModelMetadataComponentsPage.clickOnCreateButton();
    rouModelMetadataUpdatePage = new RouModelMetadataUpdatePage();
    expect(await rouModelMetadataUpdatePage.getPageTitle()).to.eq('Create or edit a Rou Model Metadata');
    await rouModelMetadataUpdatePage.cancel();
  });

  /* it('should create and save RouModelMetadata', async () => {
        const nbButtonsBeforeCreate = await rouModelMetadataComponentsPage.countDeleteButtons();

        await rouModelMetadataComponentsPage.clickOnCreateButton();

        await promise.all([
            rouModelMetadataUpdatePage.setModelTitleInput('modelTitle'),
            rouModelMetadataUpdatePage.setModelVersionInput('5'),
            rouModelMetadataUpdatePage.setDescriptionInput('description'),
            rouModelMetadataUpdatePage.setLeaseTermPeriodsInput('5'),
            rouModelMetadataUpdatePage.setLeaseAmountInput('5'),
            rouModelMetadataUpdatePage.setRouModelReferenceInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouModelMetadataUpdatePage.setCommencementDateInput('2000-12-31'),
            rouModelMetadataUpdatePage.setExpirationDateInput('2000-12-31'),
            rouModelMetadataUpdatePage.getHasBeenFullyAmortisedInput().click(),
            rouModelMetadataUpdatePage.getHasBeenDecommissionedInput().click(),
            rouModelMetadataUpdatePage.ifrs16LeaseContractSelectLastOption(),
            rouModelMetadataUpdatePage.assetAccountSelectLastOption(),
            rouModelMetadataUpdatePage.depreciationAccountSelectLastOption(),
            rouModelMetadataUpdatePage.accruedDepreciationAccountSelectLastOption(),
            rouModelMetadataUpdatePage.assetCategorySelectLastOption(),
            // rouModelMetadataUpdatePage.documentAttachmentsSelectLastOption(),
        ]);

        await rouModelMetadataUpdatePage.save();
        expect(await rouModelMetadataUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await rouModelMetadataComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last RouModelMetadata', async () => {
        const nbButtonsBeforeDelete = await rouModelMetadataComponentsPage.countDeleteButtons();
        await rouModelMetadataComponentsPage.clickOnLastDeleteButton();

        rouModelMetadataDeleteDialog = new RouModelMetadataDeleteDialog();
        expect(await rouModelMetadataDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Rou Model Metadata?');
        await rouModelMetadataDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(rouModelMetadataComponentsPage.title), 5000);

        expect(await rouModelMetadataComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
