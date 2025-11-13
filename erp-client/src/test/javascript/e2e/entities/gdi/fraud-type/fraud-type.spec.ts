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

import { FraudTypeComponentsPage, FraudTypeDeleteDialog, FraudTypeUpdatePage } from './fraud-type.page-object';

const expect = chai.expect;

describe('FraudType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fraudTypeComponentsPage: FraudTypeComponentsPage;
  let fraudTypeUpdatePage: FraudTypeUpdatePage;
  let fraudTypeDeleteDialog: FraudTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FraudTypes', async () => {
    await navBarPage.goToEntity('fraud-type');
    fraudTypeComponentsPage = new FraudTypeComponentsPage();
    await browser.wait(ec.visibilityOf(fraudTypeComponentsPage.title), 5000);
    expect(await fraudTypeComponentsPage.getTitle()).to.eq('Fraud Types');
    await browser.wait(ec.or(ec.visibilityOf(fraudTypeComponentsPage.entities), ec.visibilityOf(fraudTypeComponentsPage.noResult)), 1000);
  });

  it('should load create FraudType page', async () => {
    await fraudTypeComponentsPage.clickOnCreateButton();
    fraudTypeUpdatePage = new FraudTypeUpdatePage();
    expect(await fraudTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Fraud Type');
    await fraudTypeUpdatePage.cancel();
  });

  it('should create and save FraudTypes', async () => {
    const nbButtonsBeforeCreate = await fraudTypeComponentsPage.countDeleteButtons();

    await fraudTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      fraudTypeUpdatePage.setFraudTypeCodeInput('fraudTypeCode'),
      fraudTypeUpdatePage.setFraudTypeInput('fraudType'),
      fraudTypeUpdatePage.setFraudTypeDetailsInput('fraudTypeDetails'),
    ]);

    await fraudTypeUpdatePage.save();
    expect(await fraudTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fraudTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last FraudType', async () => {
    const nbButtonsBeforeDelete = await fraudTypeComponentsPage.countDeleteButtons();
    await fraudTypeComponentsPage.clickOnLastDeleteButton();

    fraudTypeDeleteDialog = new FraudTypeDeleteDialog();
    expect(await fraudTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Fraud Type?');
    await fraudTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fraudTypeComponentsPage.title), 5000);

    expect(await fraudTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
