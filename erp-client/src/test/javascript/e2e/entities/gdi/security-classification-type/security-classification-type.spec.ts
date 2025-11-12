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
  SecurityClassificationTypeComponentsPage,
  SecurityClassificationTypeDeleteDialog,
  SecurityClassificationTypeUpdatePage,
} from './security-classification-type.page-object';

const expect = chai.expect;

describe('SecurityClassificationType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let securityClassificationTypeComponentsPage: SecurityClassificationTypeComponentsPage;
  let securityClassificationTypeUpdatePage: SecurityClassificationTypeUpdatePage;
  let securityClassificationTypeDeleteDialog: SecurityClassificationTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SecurityClassificationTypes', async () => {
    await navBarPage.goToEntity('security-classification-type');
    securityClassificationTypeComponentsPage = new SecurityClassificationTypeComponentsPage();
    await browser.wait(ec.visibilityOf(securityClassificationTypeComponentsPage.title), 5000);
    expect(await securityClassificationTypeComponentsPage.getTitle()).to.eq('Security Classification Types');
    await browser.wait(
      ec.or(
        ec.visibilityOf(securityClassificationTypeComponentsPage.entities),
        ec.visibilityOf(securityClassificationTypeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create SecurityClassificationType page', async () => {
    await securityClassificationTypeComponentsPage.clickOnCreateButton();
    securityClassificationTypeUpdatePage = new SecurityClassificationTypeUpdatePage();
    expect(await securityClassificationTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Security Classification Type');
    await securityClassificationTypeUpdatePage.cancel();
  });

  it('should create and save SecurityClassificationTypes', async () => {
    const nbButtonsBeforeCreate = await securityClassificationTypeComponentsPage.countDeleteButtons();

    await securityClassificationTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      securityClassificationTypeUpdatePage.setSecurityClassificationTypeCodeInput('securityClassificationTypeCode'),
      securityClassificationTypeUpdatePage.setSecurityClassificationTypeInput('securityClassificationType'),
      securityClassificationTypeUpdatePage.setSecurityClassificationDetailsInput('securityClassificationDetails'),
    ]);

    await securityClassificationTypeUpdatePage.save();
    expect(await securityClassificationTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await securityClassificationTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last SecurityClassificationType', async () => {
    const nbButtonsBeforeDelete = await securityClassificationTypeComponentsPage.countDeleteButtons();
    await securityClassificationTypeComponentsPage.clickOnLastDeleteButton();

    securityClassificationTypeDeleteDialog = new SecurityClassificationTypeDeleteDialog();
    expect(await securityClassificationTypeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Security Classification Type?'
    );
    await securityClassificationTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(securityClassificationTypeComponentsPage.title), 5000);

    expect(await securityClassificationTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
