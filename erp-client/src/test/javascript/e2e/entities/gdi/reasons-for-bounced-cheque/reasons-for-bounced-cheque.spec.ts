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

import {
  ReasonsForBouncedChequeComponentsPage,
  ReasonsForBouncedChequeDeleteDialog,
  ReasonsForBouncedChequeUpdatePage,
} from './reasons-for-bounced-cheque.page-object';

const expect = chai.expect;

describe('ReasonsForBouncedCheque e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reasonsForBouncedChequeComponentsPage: ReasonsForBouncedChequeComponentsPage;
  let reasonsForBouncedChequeUpdatePage: ReasonsForBouncedChequeUpdatePage;
  let reasonsForBouncedChequeDeleteDialog: ReasonsForBouncedChequeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ReasonsForBouncedCheques', async () => {
    await navBarPage.goToEntity('reasons-for-bounced-cheque');
    reasonsForBouncedChequeComponentsPage = new ReasonsForBouncedChequeComponentsPage();
    await browser.wait(ec.visibilityOf(reasonsForBouncedChequeComponentsPage.title), 5000);
    expect(await reasonsForBouncedChequeComponentsPage.getTitle()).to.eq('Reasons For Bounced Cheques');
    await browser.wait(
      ec.or(
        ec.visibilityOf(reasonsForBouncedChequeComponentsPage.entities),
        ec.visibilityOf(reasonsForBouncedChequeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create ReasonsForBouncedCheque page', async () => {
    await reasonsForBouncedChequeComponentsPage.clickOnCreateButton();
    reasonsForBouncedChequeUpdatePage = new ReasonsForBouncedChequeUpdatePage();
    expect(await reasonsForBouncedChequeUpdatePage.getPageTitle()).to.eq('Create or edit a Reasons For Bounced Cheque');
    await reasonsForBouncedChequeUpdatePage.cancel();
  });

  it('should create and save ReasonsForBouncedCheques', async () => {
    const nbButtonsBeforeCreate = await reasonsForBouncedChequeComponentsPage.countDeleteButtons();

    await reasonsForBouncedChequeComponentsPage.clickOnCreateButton();

    await promise.all([
      reasonsForBouncedChequeUpdatePage.setBouncedChequeReasonsTypeCodeInput('bouncedChequeReasonsTypeCode'),
      reasonsForBouncedChequeUpdatePage.setBouncedChequeReasonsTypeInput('bouncedChequeReasonsType'),
    ]);

    await reasonsForBouncedChequeUpdatePage.save();
    expect(await reasonsForBouncedChequeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await reasonsForBouncedChequeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ReasonsForBouncedCheque', async () => {
    const nbButtonsBeforeDelete = await reasonsForBouncedChequeComponentsPage.countDeleteButtons();
    await reasonsForBouncedChequeComponentsPage.clickOnLastDeleteButton();

    reasonsForBouncedChequeDeleteDialog = new ReasonsForBouncedChequeDeleteDialog();
    expect(await reasonsForBouncedChequeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Reasons For Bounced Cheque?'
    );
    await reasonsForBouncedChequeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(reasonsForBouncedChequeComponentsPage.title), 5000);

    expect(await reasonsForBouncedChequeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
