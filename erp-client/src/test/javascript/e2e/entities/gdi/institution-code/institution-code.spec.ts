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

import { InstitutionCodeComponentsPage, InstitutionCodeDeleteDialog, InstitutionCodeUpdatePage } from './institution-code.page-object';

const expect = chai.expect;

describe('InstitutionCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let institutionCodeComponentsPage: InstitutionCodeComponentsPage;
  let institutionCodeUpdatePage: InstitutionCodeUpdatePage;
  let institutionCodeDeleteDialog: InstitutionCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InstitutionCodes', async () => {
    await navBarPage.goToEntity('institution-code');
    institutionCodeComponentsPage = new InstitutionCodeComponentsPage();
    await browser.wait(ec.visibilityOf(institutionCodeComponentsPage.title), 5000);
    expect(await institutionCodeComponentsPage.getTitle()).to.eq('Institution Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(institutionCodeComponentsPage.entities), ec.visibilityOf(institutionCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create InstitutionCode page', async () => {
    await institutionCodeComponentsPage.clickOnCreateButton();
    institutionCodeUpdatePage = new InstitutionCodeUpdatePage();
    expect(await institutionCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Institution Code');
    await institutionCodeUpdatePage.cancel();
  });

  it('should create and save InstitutionCodes', async () => {
    const nbButtonsBeforeCreate = await institutionCodeComponentsPage.countDeleteButtons();

    await institutionCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      institutionCodeUpdatePage.setInstitutionCodeInput('institutionCode'),
      institutionCodeUpdatePage.setInstitutionNameInput('institutionName'),
      institutionCodeUpdatePage.setShortNameInput('shortName'),
      institutionCodeUpdatePage.setCategoryInput('category'),
      institutionCodeUpdatePage.setInstitutionCategoryInput('institutionCategory'),
      institutionCodeUpdatePage.setInstitutionOwnershipInput('institutionOwnership'),
      institutionCodeUpdatePage.setDateLicensedInput('2000-12-31'),
      institutionCodeUpdatePage.setInstitutionStatusInput('institutionStatus'),
      // institutionCodeUpdatePage.placeholderSelectLastOption(),
    ]);

    await institutionCodeUpdatePage.save();
    expect(await institutionCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await institutionCodeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last InstitutionCode', async () => {
    const nbButtonsBeforeDelete = await institutionCodeComponentsPage.countDeleteButtons();
    await institutionCodeComponentsPage.clickOnLastDeleteButton();

    institutionCodeDeleteDialog = new InstitutionCodeDeleteDialog();
    expect(await institutionCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Institution Code?');
    await institutionCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(institutionCodeComponentsPage.title), 5000);

    expect(await institutionCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
