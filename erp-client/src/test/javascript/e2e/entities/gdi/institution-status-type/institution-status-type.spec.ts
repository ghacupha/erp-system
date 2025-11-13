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
  InstitutionStatusTypeComponentsPage,
  InstitutionStatusTypeDeleteDialog,
  InstitutionStatusTypeUpdatePage,
} from './institution-status-type.page-object';

const expect = chai.expect;

describe('InstitutionStatusType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let institutionStatusTypeComponentsPage: InstitutionStatusTypeComponentsPage;
  let institutionStatusTypeUpdatePage: InstitutionStatusTypeUpdatePage;
  let institutionStatusTypeDeleteDialog: InstitutionStatusTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InstitutionStatusTypes', async () => {
    await navBarPage.goToEntity('institution-status-type');
    institutionStatusTypeComponentsPage = new InstitutionStatusTypeComponentsPage();
    await browser.wait(ec.visibilityOf(institutionStatusTypeComponentsPage.title), 5000);
    expect(await institutionStatusTypeComponentsPage.getTitle()).to.eq('Institution Status Types');
    await browser.wait(
      ec.or(ec.visibilityOf(institutionStatusTypeComponentsPage.entities), ec.visibilityOf(institutionStatusTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create InstitutionStatusType page', async () => {
    await institutionStatusTypeComponentsPage.clickOnCreateButton();
    institutionStatusTypeUpdatePage = new InstitutionStatusTypeUpdatePage();
    expect(await institutionStatusTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Institution Status Type');
    await institutionStatusTypeUpdatePage.cancel();
  });

  it('should create and save InstitutionStatusTypes', async () => {
    const nbButtonsBeforeCreate = await institutionStatusTypeComponentsPage.countDeleteButtons();

    await institutionStatusTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      institutionStatusTypeUpdatePage.setInstitutionStatusCodeInput('institutionStatusCode'),
      institutionStatusTypeUpdatePage.setInstitutionStatusTypeInput('institutionStatusType'),
      institutionStatusTypeUpdatePage.setInsitutionStatusTypeDescriptionInput('insitutionStatusTypeDescription'),
    ]);

    await institutionStatusTypeUpdatePage.save();
    expect(await institutionStatusTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await institutionStatusTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last InstitutionStatusType', async () => {
    const nbButtonsBeforeDelete = await institutionStatusTypeComponentsPage.countDeleteButtons();
    await institutionStatusTypeComponentsPage.clickOnLastDeleteButton();

    institutionStatusTypeDeleteDialog = new InstitutionStatusTypeDeleteDialog();
    expect(await institutionStatusTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Institution Status Type?');
    await institutionStatusTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(institutionStatusTypeComponentsPage.title), 5000);

    expect(await institutionStatusTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
