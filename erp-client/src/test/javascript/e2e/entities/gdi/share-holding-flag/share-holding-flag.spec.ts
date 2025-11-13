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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ShareHoldingFlagComponentsPage, ShareHoldingFlagDeleteDialog, ShareHoldingFlagUpdatePage } from './share-holding-flag.page-object';

const expect = chai.expect;

describe('ShareHoldingFlag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let shareHoldingFlagComponentsPage: ShareHoldingFlagComponentsPage;
  let shareHoldingFlagUpdatePage: ShareHoldingFlagUpdatePage;
  let shareHoldingFlagDeleteDialog: ShareHoldingFlagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ShareHoldingFlags', async () => {
    await navBarPage.goToEntity('share-holding-flag');
    shareHoldingFlagComponentsPage = new ShareHoldingFlagComponentsPage();
    await browser.wait(ec.visibilityOf(shareHoldingFlagComponentsPage.title), 5000);
    expect(await shareHoldingFlagComponentsPage.getTitle()).to.eq('Share Holding Flags');
    await browser.wait(
      ec.or(ec.visibilityOf(shareHoldingFlagComponentsPage.entities), ec.visibilityOf(shareHoldingFlagComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ShareHoldingFlag page', async () => {
    await shareHoldingFlagComponentsPage.clickOnCreateButton();
    shareHoldingFlagUpdatePage = new ShareHoldingFlagUpdatePage();
    expect(await shareHoldingFlagUpdatePage.getPageTitle()).to.eq('Create or edit a Share Holding Flag');
    await shareHoldingFlagUpdatePage.cancel();
  });

  it('should create and save ShareHoldingFlags', async () => {
    const nbButtonsBeforeCreate = await shareHoldingFlagComponentsPage.countDeleteButtons();

    await shareHoldingFlagComponentsPage.clickOnCreateButton();

    await promise.all([
      shareHoldingFlagUpdatePage.shareholdingFlagTypeCodeSelectLastOption(),
      shareHoldingFlagUpdatePage.setShareholdingFlagTypeInput('shareholdingFlagType'),
      shareHoldingFlagUpdatePage.setShareholdingTypeDescriptionInput('shareholdingTypeDescription'),
    ]);

    await shareHoldingFlagUpdatePage.save();
    expect(await shareHoldingFlagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await shareHoldingFlagComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ShareHoldingFlag', async () => {
    const nbButtonsBeforeDelete = await shareHoldingFlagComponentsPage.countDeleteButtons();
    await shareHoldingFlagComponentsPage.clickOnLastDeleteButton();

    shareHoldingFlagDeleteDialog = new ShareHoldingFlagDeleteDialog();
    expect(await shareHoldingFlagDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Share Holding Flag?');
    await shareHoldingFlagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(shareHoldingFlagComponentsPage.title), 5000);

    expect(await shareHoldingFlagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
