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

import { MfbBranchCodeComponentsPage, MfbBranchCodeDeleteDialog, MfbBranchCodeUpdatePage } from './mfb-branch-code.page-object';

const expect = chai.expect;

describe('MfbBranchCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let mfbBranchCodeComponentsPage: MfbBranchCodeComponentsPage;
  let mfbBranchCodeUpdatePage: MfbBranchCodeUpdatePage;
  let mfbBranchCodeDeleteDialog: MfbBranchCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MfbBranchCodes', async () => {
    await navBarPage.goToEntity('mfb-branch-code');
    mfbBranchCodeComponentsPage = new MfbBranchCodeComponentsPage();
    await browser.wait(ec.visibilityOf(mfbBranchCodeComponentsPage.title), 5000);
    expect(await mfbBranchCodeComponentsPage.getTitle()).to.eq('Mfb Branch Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(mfbBranchCodeComponentsPage.entities), ec.visibilityOf(mfbBranchCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create MfbBranchCode page', async () => {
    await mfbBranchCodeComponentsPage.clickOnCreateButton();
    mfbBranchCodeUpdatePage = new MfbBranchCodeUpdatePage();
    expect(await mfbBranchCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Mfb Branch Code');
    await mfbBranchCodeUpdatePage.cancel();
  });

  it('should create and save MfbBranchCodes', async () => {
    const nbButtonsBeforeCreate = await mfbBranchCodeComponentsPage.countDeleteButtons();

    await mfbBranchCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      mfbBranchCodeUpdatePage.setBankCodeInput('bankCode'),
      mfbBranchCodeUpdatePage.setBankNameInput('bankName'),
      mfbBranchCodeUpdatePage.setBranchCodeInput('branchCode'),
      mfbBranchCodeUpdatePage.setBranchNameInput('branchName'),
      // mfbBranchCodeUpdatePage.placeholderSelectLastOption(),
    ]);

    await mfbBranchCodeUpdatePage.save();
    expect(await mfbBranchCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await mfbBranchCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last MfbBranchCode', async () => {
    const nbButtonsBeforeDelete = await mfbBranchCodeComponentsPage.countDeleteButtons();
    await mfbBranchCodeComponentsPage.clickOnLastDeleteButton();

    mfbBranchCodeDeleteDialog = new MfbBranchCodeDeleteDialog();
    expect(await mfbBranchCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Mfb Branch Code?');
    await mfbBranchCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(mfbBranchCodeComponentsPage.title), 5000);

    expect(await mfbBranchCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
