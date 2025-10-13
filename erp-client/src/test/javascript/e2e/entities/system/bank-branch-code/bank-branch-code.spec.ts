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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { BankBranchCodeComponentsPage, BankBranchCodeDeleteDialog, BankBranchCodeUpdatePage } from './bank-branch-code.page-object';

const expect = chai.expect;

describe('BankBranchCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let bankBranchCodeComponentsPage: BankBranchCodeComponentsPage;
  let bankBranchCodeUpdatePage: BankBranchCodeUpdatePage;
  let bankBranchCodeDeleteDialog: BankBranchCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load BankBranchCodes', async () => {
    await navBarPage.goToEntity('bank-branch-code');
    bankBranchCodeComponentsPage = new BankBranchCodeComponentsPage();
    await browser.wait(ec.visibilityOf(bankBranchCodeComponentsPage.title), 5000);
    expect(await bankBranchCodeComponentsPage.getTitle()).to.eq('Bank Branch Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(bankBranchCodeComponentsPage.entities), ec.visibilityOf(bankBranchCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create BankBranchCode page', async () => {
    await bankBranchCodeComponentsPage.clickOnCreateButton();
    bankBranchCodeUpdatePage = new BankBranchCodeUpdatePage();
    expect(await bankBranchCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Bank Branch Code');
    await bankBranchCodeUpdatePage.cancel();
  });

  it('should create and save BankBranchCodes', async () => {
    const nbButtonsBeforeCreate = await bankBranchCodeComponentsPage.countDeleteButtons();

    await bankBranchCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      bankBranchCodeUpdatePage.setBankCodeInput('bankCode'),
      bankBranchCodeUpdatePage.setBankNameInput('bankName'),
      bankBranchCodeUpdatePage.setBranchCodeInput('branchCode'),
      bankBranchCodeUpdatePage.setBranchNameInput('branchName'),
      bankBranchCodeUpdatePage.setNotesInput('notes'),
      // bankBranchCodeUpdatePage.placeholderSelectLastOption(),
    ]);

    await bankBranchCodeUpdatePage.save();
    expect(await bankBranchCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await bankBranchCodeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last BankBranchCode', async () => {
    const nbButtonsBeforeDelete = await bankBranchCodeComponentsPage.countDeleteButtons();
    await bankBranchCodeComponentsPage.clickOnLastDeleteButton();

    bankBranchCodeDeleteDialog = new BankBranchCodeDeleteDialog();
    expect(await bankBranchCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Bank Branch Code?');
    await bankBranchCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(bankBranchCodeComponentsPage.title), 5000);

    expect(await bankBranchCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
