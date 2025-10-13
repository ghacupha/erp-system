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
  CrbCreditFacilityTypeComponentsPage,
  CrbCreditFacilityTypeDeleteDialog,
  CrbCreditFacilityTypeUpdatePage,
} from './crb-credit-facility-type.page-object';

const expect = chai.expect;

describe('CrbCreditFacilityType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbCreditFacilityTypeComponentsPage: CrbCreditFacilityTypeComponentsPage;
  let crbCreditFacilityTypeUpdatePage: CrbCreditFacilityTypeUpdatePage;
  let crbCreditFacilityTypeDeleteDialog: CrbCreditFacilityTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbCreditFacilityTypes', async () => {
    await navBarPage.goToEntity('crb-credit-facility-type');
    crbCreditFacilityTypeComponentsPage = new CrbCreditFacilityTypeComponentsPage();
    await browser.wait(ec.visibilityOf(crbCreditFacilityTypeComponentsPage.title), 5000);
    expect(await crbCreditFacilityTypeComponentsPage.getTitle()).to.eq('Crb Credit Facility Types');
    await browser.wait(
      ec.or(ec.visibilityOf(crbCreditFacilityTypeComponentsPage.entities), ec.visibilityOf(crbCreditFacilityTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbCreditFacilityType page', async () => {
    await crbCreditFacilityTypeComponentsPage.clickOnCreateButton();
    crbCreditFacilityTypeUpdatePage = new CrbCreditFacilityTypeUpdatePage();
    expect(await crbCreditFacilityTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Credit Facility Type');
    await crbCreditFacilityTypeUpdatePage.cancel();
  });

  it('should create and save CrbCreditFacilityTypes', async () => {
    const nbButtonsBeforeCreate = await crbCreditFacilityTypeComponentsPage.countDeleteButtons();

    await crbCreditFacilityTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbCreditFacilityTypeUpdatePage.setCreditFacilityTypeCodeInput('creditFacilityTypeCode'),
      crbCreditFacilityTypeUpdatePage.setCreditFacilityTypeInput('creditFacilityType'),
      crbCreditFacilityTypeUpdatePage.setCreditFacilityDescriptionInput('creditFacilityDescription'),
    ]);

    await crbCreditFacilityTypeUpdatePage.save();
    expect(await crbCreditFacilityTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbCreditFacilityTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbCreditFacilityType', async () => {
    const nbButtonsBeforeDelete = await crbCreditFacilityTypeComponentsPage.countDeleteButtons();
    await crbCreditFacilityTypeComponentsPage.clickOnLastDeleteButton();

    crbCreditFacilityTypeDeleteDialog = new CrbCreditFacilityTypeDeleteDialog();
    expect(await crbCreditFacilityTypeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb Credit Facility Type?'
    );
    await crbCreditFacilityTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbCreditFacilityTypeComponentsPage.title), 5000);

    expect(await crbCreditFacilityTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
