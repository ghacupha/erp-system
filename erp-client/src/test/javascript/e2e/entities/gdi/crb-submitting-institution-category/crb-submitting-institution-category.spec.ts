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
  CrbSubmittingInstitutionCategoryComponentsPage,
  CrbSubmittingInstitutionCategoryDeleteDialog,
  CrbSubmittingInstitutionCategoryUpdatePage,
} from './crb-submitting-institution-category.page-object';

const expect = chai.expect;

describe('CrbSubmittingInstitutionCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbSubmittingInstitutionCategoryComponentsPage: CrbSubmittingInstitutionCategoryComponentsPage;
  let crbSubmittingInstitutionCategoryUpdatePage: CrbSubmittingInstitutionCategoryUpdatePage;
  let crbSubmittingInstitutionCategoryDeleteDialog: CrbSubmittingInstitutionCategoryDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbSubmittingInstitutionCategories', async () => {
    await navBarPage.goToEntity('crb-submitting-institution-category');
    crbSubmittingInstitutionCategoryComponentsPage = new CrbSubmittingInstitutionCategoryComponentsPage();
    await browser.wait(ec.visibilityOf(crbSubmittingInstitutionCategoryComponentsPage.title), 5000);
    expect(await crbSubmittingInstitutionCategoryComponentsPage.getTitle()).to.eq('Crb Submitting Institution Categories');
    await browser.wait(
      ec.or(
        ec.visibilityOf(crbSubmittingInstitutionCategoryComponentsPage.entities),
        ec.visibilityOf(crbSubmittingInstitutionCategoryComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CrbSubmittingInstitutionCategory page', async () => {
    await crbSubmittingInstitutionCategoryComponentsPage.clickOnCreateButton();
    crbSubmittingInstitutionCategoryUpdatePage = new CrbSubmittingInstitutionCategoryUpdatePage();
    expect(await crbSubmittingInstitutionCategoryUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Submitting Institution Category');
    await crbSubmittingInstitutionCategoryUpdatePage.cancel();
  });

  it('should create and save CrbSubmittingInstitutionCategories', async () => {
    const nbButtonsBeforeCreate = await crbSubmittingInstitutionCategoryComponentsPage.countDeleteButtons();

    await crbSubmittingInstitutionCategoryComponentsPage.clickOnCreateButton();

    await promise.all([
      crbSubmittingInstitutionCategoryUpdatePage.setSubmittingInstitutionCategoryTypeCodeInput('submittingInstitutionCategoryTypeCode'),
      crbSubmittingInstitutionCategoryUpdatePage.setSubmittingInstitutionCategoryTypeInput('submittingInstitutionCategoryType'),
      crbSubmittingInstitutionCategoryUpdatePage.setSubmittingInstitutionCategoryDetailsInput('submittingInstitutionCategoryDetails'),
    ]);

    await crbSubmittingInstitutionCategoryUpdatePage.save();
    expect(await crbSubmittingInstitutionCategoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbSubmittingInstitutionCategoryComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbSubmittingInstitutionCategory', async () => {
    const nbButtonsBeforeDelete = await crbSubmittingInstitutionCategoryComponentsPage.countDeleteButtons();
    await crbSubmittingInstitutionCategoryComponentsPage.clickOnLastDeleteButton();

    crbSubmittingInstitutionCategoryDeleteDialog = new CrbSubmittingInstitutionCategoryDeleteDialog();
    expect(await crbSubmittingInstitutionCategoryDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb Submitting Institution Category?'
    );
    await crbSubmittingInstitutionCategoryDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbSubmittingInstitutionCategoryComponentsPage.title), 5000);

    expect(await crbSubmittingInstitutionCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
