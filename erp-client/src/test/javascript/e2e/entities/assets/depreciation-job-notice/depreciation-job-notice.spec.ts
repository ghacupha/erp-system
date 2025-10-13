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

import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  DepreciationJobNoticeComponentsPage,
  DepreciationJobNoticeDeleteDialog,
  DepreciationJobNoticeUpdatePage,
} from './depreciation-job-notice.page-object';

const expect = chai.expect;

describe('DepreciationJobNotice e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let depreciationJobNoticeComponentsPage: DepreciationJobNoticeComponentsPage;
  let depreciationJobNoticeUpdatePage: DepreciationJobNoticeUpdatePage;
  let depreciationJobNoticeDeleteDialog: DepreciationJobNoticeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DepreciationJobNotices', async () => {
    await navBarPage.goToEntity('depreciation-job-notice');
    depreciationJobNoticeComponentsPage = new DepreciationJobNoticeComponentsPage();
    await browser.wait(ec.visibilityOf(depreciationJobNoticeComponentsPage.title), 5000);
    expect(await depreciationJobNoticeComponentsPage.getTitle()).to.eq('Depreciation Job Notices');
    await browser.wait(
      ec.or(ec.visibilityOf(depreciationJobNoticeComponentsPage.entities), ec.visibilityOf(depreciationJobNoticeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DepreciationJobNotice page', async () => {
    await depreciationJobNoticeComponentsPage.clickOnCreateButton();
    depreciationJobNoticeUpdatePage = new DepreciationJobNoticeUpdatePage();
    expect(await depreciationJobNoticeUpdatePage.getPageTitle()).to.eq('Create or edit a Depreciation Job Notice');
    await depreciationJobNoticeUpdatePage.cancel();
  });

  it('should create and save DepreciationJobNotices', async () => {
    const nbButtonsBeforeCreate = await depreciationJobNoticeComponentsPage.countDeleteButtons();

    await depreciationJobNoticeComponentsPage.clickOnCreateButton();

    await promise.all([
      depreciationJobNoticeUpdatePage.setEventNarrativeInput('eventNarrative'),
      depreciationJobNoticeUpdatePage.setEventTimeStampInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      depreciationJobNoticeUpdatePage.depreciationNoticeStatusSelectLastOption(),
      depreciationJobNoticeUpdatePage.setSourceModuleInput('sourceModule'),
      depreciationJobNoticeUpdatePage.setSourceEntityInput('sourceEntity'),
      depreciationJobNoticeUpdatePage.setErrorCodeInput('errorCode'),
      depreciationJobNoticeUpdatePage.setErrorMessageInput('errorMessage'),
      depreciationJobNoticeUpdatePage.setUserActionInput('userAction'),
      depreciationJobNoticeUpdatePage.setTechnicalDetailsInput('technicalDetails'),
      depreciationJobNoticeUpdatePage.depreciationJobSelectLastOption(),
      depreciationJobNoticeUpdatePage.depreciationBatchSequenceSelectLastOption(),
      depreciationJobNoticeUpdatePage.depreciationPeriodSelectLastOption(),
      // depreciationJobNoticeUpdatePage.placeholderSelectLastOption(),
      // depreciationJobNoticeUpdatePage.universallyUniqueMappingSelectLastOption(),
      depreciationJobNoticeUpdatePage.superintendedSelectLastOption(),
    ]);

    await depreciationJobNoticeUpdatePage.save();
    expect(await depreciationJobNoticeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await depreciationJobNoticeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last DepreciationJobNotice', async () => {
    const nbButtonsBeforeDelete = await depreciationJobNoticeComponentsPage.countDeleteButtons();
    await depreciationJobNoticeComponentsPage.clickOnLastDeleteButton();

    depreciationJobNoticeDeleteDialog = new DepreciationJobNoticeDeleteDialog();
    expect(await depreciationJobNoticeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Depreciation Job Notice?');
    await depreciationJobNoticeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(depreciationJobNoticeComponentsPage.title), 5000);

    expect(await depreciationJobNoticeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
