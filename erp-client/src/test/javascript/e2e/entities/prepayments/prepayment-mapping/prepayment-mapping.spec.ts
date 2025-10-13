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
  PrepaymentMappingComponentsPage,
  PrepaymentMappingDeleteDialog,
  PrepaymentMappingUpdatePage,
} from './prepayment-mapping.page-object';

const expect = chai.expect;

describe('PrepaymentMapping e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentMappingComponentsPage: PrepaymentMappingComponentsPage;
  let prepaymentMappingUpdatePage: PrepaymentMappingUpdatePage;
  let prepaymentMappingDeleteDialog: PrepaymentMappingDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PrepaymentMappings', async () => {
    await navBarPage.goToEntity('prepayment-mapping');
    prepaymentMappingComponentsPage = new PrepaymentMappingComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentMappingComponentsPage.title), 5000);
    expect(await prepaymentMappingComponentsPage.getTitle()).to.eq('Prepayment Mappings');
    await browser.wait(
      ec.or(ec.visibilityOf(prepaymentMappingComponentsPage.entities), ec.visibilityOf(prepaymentMappingComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PrepaymentMapping page', async () => {
    await prepaymentMappingComponentsPage.clickOnCreateButton();
    prepaymentMappingUpdatePage = new PrepaymentMappingUpdatePage();
    expect(await prepaymentMappingUpdatePage.getPageTitle()).to.eq('Create or edit a Prepayment Mapping');
    await prepaymentMappingUpdatePage.cancel();
  });

  it('should create and save PrepaymentMappings', async () => {
    const nbButtonsBeforeCreate = await prepaymentMappingComponentsPage.countDeleteButtons();

    await prepaymentMappingComponentsPage.clickOnCreateButton();

    await promise.all([
      prepaymentMappingUpdatePage.setParameterKeyInput('parameterKey'),
      prepaymentMappingUpdatePage.setParameterGuidInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      prepaymentMappingUpdatePage.setParameterInput('parameter'),
      // prepaymentMappingUpdatePage.placeholderSelectLastOption(),
    ]);

    await prepaymentMappingUpdatePage.save();
    expect(await prepaymentMappingUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await prepaymentMappingComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last PrepaymentMapping', async () => {
    const nbButtonsBeforeDelete = await prepaymentMappingComponentsPage.countDeleteButtons();
    await prepaymentMappingComponentsPage.clickOnLastDeleteButton();

    prepaymentMappingDeleteDialog = new PrepaymentMappingDeleteDialog();
    expect(await prepaymentMappingDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Prepayment Mapping?');
    await prepaymentMappingDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(prepaymentMappingComponentsPage.title), 5000);

    expect(await prepaymentMappingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
