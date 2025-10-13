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
  CrbSourceOfInformationTypeComponentsPage,
  CrbSourceOfInformationTypeDeleteDialog,
  CrbSourceOfInformationTypeUpdatePage,
} from './crb-source-of-information-type.page-object';

const expect = chai.expect;

describe('CrbSourceOfInformationType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbSourceOfInformationTypeComponentsPage: CrbSourceOfInformationTypeComponentsPage;
  let crbSourceOfInformationTypeUpdatePage: CrbSourceOfInformationTypeUpdatePage;
  let crbSourceOfInformationTypeDeleteDialog: CrbSourceOfInformationTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbSourceOfInformationTypes', async () => {
    await navBarPage.goToEntity('crb-source-of-information-type');
    crbSourceOfInformationTypeComponentsPage = new CrbSourceOfInformationTypeComponentsPage();
    await browser.wait(ec.visibilityOf(crbSourceOfInformationTypeComponentsPage.title), 5000);
    expect(await crbSourceOfInformationTypeComponentsPage.getTitle()).to.eq('Crb Source Of Information Types');
    await browser.wait(
      ec.or(
        ec.visibilityOf(crbSourceOfInformationTypeComponentsPage.entities),
        ec.visibilityOf(crbSourceOfInformationTypeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CrbSourceOfInformationType page', async () => {
    await crbSourceOfInformationTypeComponentsPage.clickOnCreateButton();
    crbSourceOfInformationTypeUpdatePage = new CrbSourceOfInformationTypeUpdatePage();
    expect(await crbSourceOfInformationTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Source Of Information Type');
    await crbSourceOfInformationTypeUpdatePage.cancel();
  });

  it('should create and save CrbSourceOfInformationTypes', async () => {
    const nbButtonsBeforeCreate = await crbSourceOfInformationTypeComponentsPage.countDeleteButtons();

    await crbSourceOfInformationTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbSourceOfInformationTypeUpdatePage.setSourceOfInformationTypeCodeInput('sourceOfInformationTypeCode'),
      crbSourceOfInformationTypeUpdatePage.setSourceOfInformationTypeDescriptionInput('sourceOfInformationTypeDescription'),
    ]);

    await crbSourceOfInformationTypeUpdatePage.save();
    expect(await crbSourceOfInformationTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbSourceOfInformationTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbSourceOfInformationType', async () => {
    const nbButtonsBeforeDelete = await crbSourceOfInformationTypeComponentsPage.countDeleteButtons();
    await crbSourceOfInformationTypeComponentsPage.clickOnLastDeleteButton();

    crbSourceOfInformationTypeDeleteDialog = new CrbSourceOfInformationTypeDeleteDialog();
    expect(await crbSourceOfInformationTypeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb Source Of Information Type?'
    );
    await crbSourceOfInformationTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbSourceOfInformationTypeComponentsPage.title), 5000);

    expect(await crbSourceOfInformationTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
