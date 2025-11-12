///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
  BusinessSegmentTypesComponentsPage,
  BusinessSegmentTypesDeleteDialog,
  BusinessSegmentTypesUpdatePage,
} from './business-segment-types.page-object';

const expect = chai.expect;

describe('BusinessSegmentTypes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let businessSegmentTypesComponentsPage: BusinessSegmentTypesComponentsPage;
  let businessSegmentTypesUpdatePage: BusinessSegmentTypesUpdatePage;
  let businessSegmentTypesDeleteDialog: BusinessSegmentTypesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load BusinessSegmentTypes', async () => {
    await navBarPage.goToEntity('business-segment-types');
    businessSegmentTypesComponentsPage = new BusinessSegmentTypesComponentsPage();
    await browser.wait(ec.visibilityOf(businessSegmentTypesComponentsPage.title), 5000);
    expect(await businessSegmentTypesComponentsPage.getTitle()).to.eq('Business Segment Types');
    await browser.wait(
      ec.or(ec.visibilityOf(businessSegmentTypesComponentsPage.entities), ec.visibilityOf(businessSegmentTypesComponentsPage.noResult)),
      1000
    );
  });

  it('should load create BusinessSegmentTypes page', async () => {
    await businessSegmentTypesComponentsPage.clickOnCreateButton();
    businessSegmentTypesUpdatePage = new BusinessSegmentTypesUpdatePage();
    expect(await businessSegmentTypesUpdatePage.getPageTitle()).to.eq('Create or edit a Business Segment Types');
    await businessSegmentTypesUpdatePage.cancel();
  });

  it('should create and save BusinessSegmentTypes', async () => {
    const nbButtonsBeforeCreate = await businessSegmentTypesComponentsPage.countDeleteButtons();

    await businessSegmentTypesComponentsPage.clickOnCreateButton();

    await promise.all([
      businessSegmentTypesUpdatePage.setBusinessEconomicSegmentCodeInput('businessEconomicSegmentCode'),
      businessSegmentTypesUpdatePage.setBusinessEconomicSegmentInput('businessEconomicSegment'),
      businessSegmentTypesUpdatePage.setDetailsInput('details'),
    ]);

    await businessSegmentTypesUpdatePage.save();
    expect(await businessSegmentTypesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await businessSegmentTypesComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last BusinessSegmentTypes', async () => {
    const nbButtonsBeforeDelete = await businessSegmentTypesComponentsPage.countDeleteButtons();
    await businessSegmentTypesComponentsPage.clickOnLastDeleteButton();

    businessSegmentTypesDeleteDialog = new BusinessSegmentTypesDeleteDialog();
    expect(await businessSegmentTypesDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Business Segment Types?');
    await businessSegmentTypesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(businessSegmentTypesComponentsPage.title), 5000);

    expect(await businessSegmentTypesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
