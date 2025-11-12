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
  AcquiringIssuingFlagComponentsPage,
  AcquiringIssuingFlagDeleteDialog,
  AcquiringIssuingFlagUpdatePage,
} from './acquiring-issuing-flag.page-object';

const expect = chai.expect;

describe('AcquiringIssuingFlag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let acquiringIssuingFlagComponentsPage: AcquiringIssuingFlagComponentsPage;
  let acquiringIssuingFlagUpdatePage: AcquiringIssuingFlagUpdatePage;
  let acquiringIssuingFlagDeleteDialog: AcquiringIssuingFlagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AcquiringIssuingFlags', async () => {
    await navBarPage.goToEntity('acquiring-issuing-flag');
    acquiringIssuingFlagComponentsPage = new AcquiringIssuingFlagComponentsPage();
    await browser.wait(ec.visibilityOf(acquiringIssuingFlagComponentsPage.title), 5000);
    expect(await acquiringIssuingFlagComponentsPage.getTitle()).to.eq('Acquiring Issuing Flags');
    await browser.wait(
      ec.or(ec.visibilityOf(acquiringIssuingFlagComponentsPage.entities), ec.visibilityOf(acquiringIssuingFlagComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AcquiringIssuingFlag page', async () => {
    await acquiringIssuingFlagComponentsPage.clickOnCreateButton();
    acquiringIssuingFlagUpdatePage = new AcquiringIssuingFlagUpdatePage();
    expect(await acquiringIssuingFlagUpdatePage.getPageTitle()).to.eq('Create or edit a Acquiring Issuing Flag');
    await acquiringIssuingFlagUpdatePage.cancel();
  });

  it('should create and save AcquiringIssuingFlags', async () => {
    const nbButtonsBeforeCreate = await acquiringIssuingFlagComponentsPage.countDeleteButtons();

    await acquiringIssuingFlagComponentsPage.clickOnCreateButton();

    await promise.all([
      acquiringIssuingFlagUpdatePage.setCardAcquiringIssuingFlagCodeInput('cardAcquiringIssuingFlagCode'),
      acquiringIssuingFlagUpdatePage.setCardAcquiringIssuingDescriptionInput('cardAcquiringIssuingDescription'),
      acquiringIssuingFlagUpdatePage.setCardAcquiringIssuingDetailsInput('cardAcquiringIssuingDetails'),
    ]);

    await acquiringIssuingFlagUpdatePage.save();
    expect(await acquiringIssuingFlagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await acquiringIssuingFlagComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last AcquiringIssuingFlag', async () => {
    const nbButtonsBeforeDelete = await acquiringIssuingFlagComponentsPage.countDeleteButtons();
    await acquiringIssuingFlagComponentsPage.clickOnLastDeleteButton();

    acquiringIssuingFlagDeleteDialog = new AcquiringIssuingFlagDeleteDialog();
    expect(await acquiringIssuingFlagDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Acquiring Issuing Flag?');
    await acquiringIssuingFlagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(acquiringIssuingFlagComponentsPage.title), 5000);

    expect(await acquiringIssuingFlagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
