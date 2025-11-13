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
  LeaseModelMetadataComponentsPage,
  /* LeaseModelMetadataDeleteDialog, */
  LeaseModelMetadataUpdatePage,
} from './lease-model-metadata.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('LeaseModelMetadata e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseModelMetadataComponentsPage: LeaseModelMetadataComponentsPage;
  let leaseModelMetadataUpdatePage: LeaseModelMetadataUpdatePage;
  /* let leaseModelMetadataDeleteDialog: LeaseModelMetadataDeleteDialog; */
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

  it('should load LeaseModelMetadata', async () => {
    await navBarPage.goToEntity('lease-model-metadata');
    leaseModelMetadataComponentsPage = new LeaseModelMetadataComponentsPage();
    await browser.wait(ec.visibilityOf(leaseModelMetadataComponentsPage.title), 5000);
    expect(await leaseModelMetadataComponentsPage.getTitle()).to.eq('Lease Model Metadata');
    await browser.wait(
      ec.or(ec.visibilityOf(leaseModelMetadataComponentsPage.entities), ec.visibilityOf(leaseModelMetadataComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeaseModelMetadata page', async () => {
    await leaseModelMetadataComponentsPage.clickOnCreateButton();
    leaseModelMetadataUpdatePage = new LeaseModelMetadataUpdatePage();
    expect(await leaseModelMetadataUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Model Metadata');
    await leaseModelMetadataUpdatePage.cancel();
  });

  /* it('should create and save LeaseModelMetadata', async () => {
        const nbButtonsBeforeCreate = await leaseModelMetadataComponentsPage.countDeleteButtons();

        await leaseModelMetadataComponentsPage.clickOnCreateButton();

        await promise.all([
            leaseModelMetadataUpdatePage.setModelTitleInput('modelTitle'),
            leaseModelMetadataUpdatePage.setModelVersionInput('5'),
            leaseModelMetadataUpdatePage.setDescriptionInput('description'),
            leaseModelMetadataUpdatePage.setModelNotesInput(absolutePath),
            leaseModelMetadataUpdatePage.setAnnualDiscountingRateInput('5'),
            leaseModelMetadataUpdatePage.setCommencementDateInput('2000-12-31'),
            leaseModelMetadataUpdatePage.setTerminalDateInput('2000-12-31'),
            leaseModelMetadataUpdatePage.setTotalReportingPeriodsInput('5'),
            leaseModelMetadataUpdatePage.setReportingPeriodsPerYearInput('5'),
            leaseModelMetadataUpdatePage.setSettlementPeriodsPerYearInput('5'),
            leaseModelMetadataUpdatePage.setInitialLiabilityAmountInput('5'),
            leaseModelMetadataUpdatePage.setInitialROUAmountInput('5'),
            leaseModelMetadataUpdatePage.setTotalDepreciationPeriodsInput('5'),
            // leaseModelMetadataUpdatePage.placeholderSelectLastOption(),
            // leaseModelMetadataUpdatePage.leaseMappingSelectLastOption(),
            leaseModelMetadataUpdatePage.leaseContractSelectLastOption(),
            leaseModelMetadataUpdatePage.predecessorSelectLastOption(),
            leaseModelMetadataUpdatePage.liabilityCurrencySelectLastOption(),
            leaseModelMetadataUpdatePage.rouAssetCurrencySelectLastOption(),
            leaseModelMetadataUpdatePage.modelAttachmentsSelectLastOption(),
            leaseModelMetadataUpdatePage.securityClearanceSelectLastOption(),
            leaseModelMetadataUpdatePage.leaseLiabilityAccountSelectLastOption(),
            leaseModelMetadataUpdatePage.interestPayableAccountSelectLastOption(),
            leaseModelMetadataUpdatePage.interestExpenseAccountSelectLastOption(),
            leaseModelMetadataUpdatePage.rouAssetAccountSelectLastOption(),
            leaseModelMetadataUpdatePage.rouDepreciationAccountSelectLastOption(),
            leaseModelMetadataUpdatePage.accruedDepreciationAccountSelectLastOption(),
        ]);

        await leaseModelMetadataUpdatePage.save();
        expect(await leaseModelMetadataUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leaseModelMetadataComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeaseModelMetadata', async () => {
        const nbButtonsBeforeDelete = await leaseModelMetadataComponentsPage.countDeleteButtons();
        await leaseModelMetadataComponentsPage.clickOnLastDeleteButton();

        leaseModelMetadataDeleteDialog = new LeaseModelMetadataDeleteDialog();
        expect(await leaseModelMetadataDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Model Metadata?');
        await leaseModelMetadataDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leaseModelMetadataComponentsPage.title), 5000);

        expect(await leaseModelMetadataComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
