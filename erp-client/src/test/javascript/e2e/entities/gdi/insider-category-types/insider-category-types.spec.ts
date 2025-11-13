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

import {
  InsiderCategoryTypesComponentsPage,
  InsiderCategoryTypesDeleteDialog,
  InsiderCategoryTypesUpdatePage,
} from './insider-category-types.page-object';

const expect = chai.expect;

describe('InsiderCategoryTypes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let insiderCategoryTypesComponentsPage: InsiderCategoryTypesComponentsPage;
  let insiderCategoryTypesUpdatePage: InsiderCategoryTypesUpdatePage;
  let insiderCategoryTypesDeleteDialog: InsiderCategoryTypesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InsiderCategoryTypes', async () => {
    await navBarPage.goToEntity('insider-category-types');
    insiderCategoryTypesComponentsPage = new InsiderCategoryTypesComponentsPage();
    await browser.wait(ec.visibilityOf(insiderCategoryTypesComponentsPage.title), 5000);
    expect(await insiderCategoryTypesComponentsPage.getTitle()).to.eq('Insider Category Types');
    await browser.wait(
      ec.or(ec.visibilityOf(insiderCategoryTypesComponentsPage.entities), ec.visibilityOf(insiderCategoryTypesComponentsPage.noResult)),
      1000
    );
  });

  it('should load create InsiderCategoryTypes page', async () => {
    await insiderCategoryTypesComponentsPage.clickOnCreateButton();
    insiderCategoryTypesUpdatePage = new InsiderCategoryTypesUpdatePage();
    expect(await insiderCategoryTypesUpdatePage.getPageTitle()).to.eq('Create or edit a Insider Category Types');
    await insiderCategoryTypesUpdatePage.cancel();
  });

  it('should create and save InsiderCategoryTypes', async () => {
    const nbButtonsBeforeCreate = await insiderCategoryTypesComponentsPage.countDeleteButtons();

    await insiderCategoryTypesComponentsPage.clickOnCreateButton();

    await promise.all([
      insiderCategoryTypesUpdatePage.setInsiderCategoryTypeCodeInput('insiderCategoryTypeCode'),
      insiderCategoryTypesUpdatePage.setInsiderCategoryTypeDetailInput('insiderCategoryTypeDetail'),
      insiderCategoryTypesUpdatePage.setInsiderCategoryDescriptionInput('insiderCategoryDescription'),
    ]);

    await insiderCategoryTypesUpdatePage.save();
    expect(await insiderCategoryTypesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await insiderCategoryTypesComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last InsiderCategoryTypes', async () => {
    const nbButtonsBeforeDelete = await insiderCategoryTypesComponentsPage.countDeleteButtons();
    await insiderCategoryTypesComponentsPage.clickOnLastDeleteButton();

    insiderCategoryTypesDeleteDialog = new InsiderCategoryTypesDeleteDialog();
    expect(await insiderCategoryTypesDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Insider Category Types?');
    await insiderCategoryTypesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(insiderCategoryTypesComponentsPage.title), 5000);

    expect(await insiderCategoryTypesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
