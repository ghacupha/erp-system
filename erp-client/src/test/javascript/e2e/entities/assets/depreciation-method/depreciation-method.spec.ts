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
  DepreciationMethodComponentsPage,
  DepreciationMethodDeleteDialog,
  DepreciationMethodUpdatePage,
} from './depreciation-method.page-object';

const expect = chai.expect;

describe('DepreciationMethod e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let depreciationMethodComponentsPage: DepreciationMethodComponentsPage;
  let depreciationMethodUpdatePage: DepreciationMethodUpdatePage;
  let depreciationMethodDeleteDialog: DepreciationMethodDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DepreciationMethods', async () => {
    await navBarPage.goToEntity('depreciation-method');
    depreciationMethodComponentsPage = new DepreciationMethodComponentsPage();
    await browser.wait(ec.visibilityOf(depreciationMethodComponentsPage.title), 5000);
    expect(await depreciationMethodComponentsPage.getTitle()).to.eq('Depreciation Methods');
    await browser.wait(
      ec.or(ec.visibilityOf(depreciationMethodComponentsPage.entities), ec.visibilityOf(depreciationMethodComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DepreciationMethod page', async () => {
    await depreciationMethodComponentsPage.clickOnCreateButton();
    depreciationMethodUpdatePage = new DepreciationMethodUpdatePage();
    expect(await depreciationMethodUpdatePage.getPageTitle()).to.eq('Create or edit a Depreciation Method');
    await depreciationMethodUpdatePage.cancel();
  });

  it('should create and save DepreciationMethods', async () => {
    const nbButtonsBeforeCreate = await depreciationMethodComponentsPage.countDeleteButtons();

    await depreciationMethodComponentsPage.clickOnCreateButton();

    await promise.all([
      depreciationMethodUpdatePage.setDepreciationMethodNameInput('depreciationMethodName'),
      depreciationMethodUpdatePage.setDescriptionInput('description'),
      depreciationMethodUpdatePage.depreciationTypeSelectLastOption(),
      depreciationMethodUpdatePage.setRemarksInput('remarks'),
      // depreciationMethodUpdatePage.placeholderSelectLastOption(),
    ]);

    await depreciationMethodUpdatePage.save();
    expect(await depreciationMethodUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await depreciationMethodComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last DepreciationMethod', async () => {
    const nbButtonsBeforeDelete = await depreciationMethodComponentsPage.countDeleteButtons();
    await depreciationMethodComponentsPage.clickOnLastDeleteButton();

    depreciationMethodDeleteDialog = new DepreciationMethodDeleteDialog();
    expect(await depreciationMethodDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Depreciation Method?');
    await depreciationMethodDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(depreciationMethodComponentsPage.title), 5000);

    expect(await depreciationMethodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
