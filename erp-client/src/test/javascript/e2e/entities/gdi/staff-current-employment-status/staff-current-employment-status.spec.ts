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
  StaffCurrentEmploymentStatusComponentsPage,
  StaffCurrentEmploymentStatusDeleteDialog,
  StaffCurrentEmploymentStatusUpdatePage,
} from './staff-current-employment-status.page-object';

const expect = chai.expect;

describe('StaffCurrentEmploymentStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let staffCurrentEmploymentStatusComponentsPage: StaffCurrentEmploymentStatusComponentsPage;
  let staffCurrentEmploymentStatusUpdatePage: StaffCurrentEmploymentStatusUpdatePage;
  let staffCurrentEmploymentStatusDeleteDialog: StaffCurrentEmploymentStatusDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load StaffCurrentEmploymentStatuses', async () => {
    await navBarPage.goToEntity('staff-current-employment-status');
    staffCurrentEmploymentStatusComponentsPage = new StaffCurrentEmploymentStatusComponentsPage();
    await browser.wait(ec.visibilityOf(staffCurrentEmploymentStatusComponentsPage.title), 5000);
    expect(await staffCurrentEmploymentStatusComponentsPage.getTitle()).to.eq('Staff Current Employment Statuses');
    await browser.wait(
      ec.or(
        ec.visibilityOf(staffCurrentEmploymentStatusComponentsPage.entities),
        ec.visibilityOf(staffCurrentEmploymentStatusComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create StaffCurrentEmploymentStatus page', async () => {
    await staffCurrentEmploymentStatusComponentsPage.clickOnCreateButton();
    staffCurrentEmploymentStatusUpdatePage = new StaffCurrentEmploymentStatusUpdatePage();
    expect(await staffCurrentEmploymentStatusUpdatePage.getPageTitle()).to.eq('Create or edit a Staff Current Employment Status');
    await staffCurrentEmploymentStatusUpdatePage.cancel();
  });

  it('should create and save StaffCurrentEmploymentStatuses', async () => {
    const nbButtonsBeforeCreate = await staffCurrentEmploymentStatusComponentsPage.countDeleteButtons();

    await staffCurrentEmploymentStatusComponentsPage.clickOnCreateButton();

    await promise.all([
      staffCurrentEmploymentStatusUpdatePage.setStaffCurrentEmploymentStatusTypeCodeInput('staffCurrentEmploymentStatusTypeCode'),
      staffCurrentEmploymentStatusUpdatePage.setStaffCurrentEmploymentStatusTypeInput('staffCurrentEmploymentStatusType'),
      staffCurrentEmploymentStatusUpdatePage.setStaffCurrentEmploymentStatusTypeDetailsInput('staffCurrentEmploymentStatusTypeDetails'),
    ]);

    await staffCurrentEmploymentStatusUpdatePage.save();
    expect(await staffCurrentEmploymentStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await staffCurrentEmploymentStatusComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last StaffCurrentEmploymentStatus', async () => {
    const nbButtonsBeforeDelete = await staffCurrentEmploymentStatusComponentsPage.countDeleteButtons();
    await staffCurrentEmploymentStatusComponentsPage.clickOnLastDeleteButton();

    staffCurrentEmploymentStatusDeleteDialog = new StaffCurrentEmploymentStatusDeleteDialog();
    expect(await staffCurrentEmploymentStatusDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Staff Current Employment Status?'
    );
    await staffCurrentEmploymentStatusDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(staffCurrentEmploymentStatusComponentsPage.title), 5000);

    expect(await staffCurrentEmploymentStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
