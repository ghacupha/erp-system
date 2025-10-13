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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AssetWriteOffComponentsPage,
  /* AssetWriteOffDeleteDialog, */
  AssetWriteOffUpdatePage,
} from './asset-write-off.page-object';

const expect = chai.expect;

describe('AssetWriteOff e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let assetWriteOffComponentsPage: AssetWriteOffComponentsPage;
  let assetWriteOffUpdatePage: AssetWriteOffUpdatePage;
  /* let assetWriteOffDeleteDialog: AssetWriteOffDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AssetWriteOffs', async () => {
    await navBarPage.goToEntity('asset-write-off');
    assetWriteOffComponentsPage = new AssetWriteOffComponentsPage();
    await browser.wait(ec.visibilityOf(assetWriteOffComponentsPage.title), 5000);
    expect(await assetWriteOffComponentsPage.getTitle()).to.eq('Asset Write Offs');
    await browser.wait(
      ec.or(ec.visibilityOf(assetWriteOffComponentsPage.entities), ec.visibilityOf(assetWriteOffComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AssetWriteOff page', async () => {
    await assetWriteOffComponentsPage.clickOnCreateButton();
    assetWriteOffUpdatePage = new AssetWriteOffUpdatePage();
    expect(await assetWriteOffUpdatePage.getPageTitle()).to.eq('Create or edit a Asset Write Off');
    await assetWriteOffUpdatePage.cancel();
  });

  /* it('should create and save AssetWriteOffs', async () => {
        const nbButtonsBeforeCreate = await assetWriteOffComponentsPage.countDeleteButtons();

        await assetWriteOffComponentsPage.clickOnCreateButton();

        await promise.all([
            assetWriteOffUpdatePage.setDescriptionInput('description'),
            assetWriteOffUpdatePage.setWriteOffAmountInput('5'),
            assetWriteOffUpdatePage.setWriteOffDateInput('2000-12-31'),
            assetWriteOffUpdatePage.setWriteOffReferenceIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            assetWriteOffUpdatePage.createdBySelectLastOption(),
            assetWriteOffUpdatePage.modifiedBySelectLastOption(),
            assetWriteOffUpdatePage.lastAccessedBySelectLastOption(),
            assetWriteOffUpdatePage.effectivePeriodSelectLastOption(),
            // assetWriteOffUpdatePage.placeholderSelectLastOption(),
            assetWriteOffUpdatePage.assetWrittenOffSelectLastOption(),
        ]);

        await assetWriteOffUpdatePage.save();
        expect(await assetWriteOffUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await assetWriteOffComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AssetWriteOff', async () => {
        const nbButtonsBeforeDelete = await assetWriteOffComponentsPage.countDeleteButtons();
        await assetWriteOffComponentsPage.clickOnLastDeleteButton();

        assetWriteOffDeleteDialog = new AssetWriteOffDeleteDialog();
        expect(await assetWriteOffDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Asset Write Off?');
        await assetWriteOffDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(assetWriteOffComponentsPage.title), 5000);

        expect(await assetWriteOffComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
