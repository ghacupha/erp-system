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
  SourceRemittancePurposeTypeComponentsPage,
  SourceRemittancePurposeTypeDeleteDialog,
  SourceRemittancePurposeTypeUpdatePage,
} from './source-remittance-purpose-type.page-object';

const expect = chai.expect;

describe('SourceRemittancePurposeType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sourceRemittancePurposeTypeComponentsPage: SourceRemittancePurposeTypeComponentsPage;
  let sourceRemittancePurposeTypeUpdatePage: SourceRemittancePurposeTypeUpdatePage;
  let sourceRemittancePurposeTypeDeleteDialog: SourceRemittancePurposeTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SourceRemittancePurposeTypes', async () => {
    await navBarPage.goToEntity('source-remittance-purpose-type');
    sourceRemittancePurposeTypeComponentsPage = new SourceRemittancePurposeTypeComponentsPage();
    await browser.wait(ec.visibilityOf(sourceRemittancePurposeTypeComponentsPage.title), 5000);
    expect(await sourceRemittancePurposeTypeComponentsPage.getTitle()).to.eq('Source Remittance Purpose Types');
    await browser.wait(
      ec.or(
        ec.visibilityOf(sourceRemittancePurposeTypeComponentsPage.entities),
        ec.visibilityOf(sourceRemittancePurposeTypeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create SourceRemittancePurposeType page', async () => {
    await sourceRemittancePurposeTypeComponentsPage.clickOnCreateButton();
    sourceRemittancePurposeTypeUpdatePage = new SourceRemittancePurposeTypeUpdatePage();
    expect(await sourceRemittancePurposeTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Source Remittance Purpose Type');
    await sourceRemittancePurposeTypeUpdatePage.cancel();
  });

  it('should create and save SourceRemittancePurposeTypes', async () => {
    const nbButtonsBeforeCreate = await sourceRemittancePurposeTypeComponentsPage.countDeleteButtons();

    await sourceRemittancePurposeTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      sourceRemittancePurposeTypeUpdatePage.setSourceOrPurposeTypeCodeInput('sourceOrPurposeTypeCode'),
      sourceRemittancePurposeTypeUpdatePage.sourceOrPurposeOfRemittanceFlagSelectLastOption(),
      sourceRemittancePurposeTypeUpdatePage.setSourceOrPurposeOfRemittanceTypeInput('sourceOrPurposeOfRemittanceType'),
      sourceRemittancePurposeTypeUpdatePage.setRemittancePurposeTypeDetailsInput('remittancePurposeTypeDetails'),
    ]);

    await sourceRemittancePurposeTypeUpdatePage.save();
    expect(await sourceRemittancePurposeTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sourceRemittancePurposeTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last SourceRemittancePurposeType', async () => {
    const nbButtonsBeforeDelete = await sourceRemittancePurposeTypeComponentsPage.countDeleteButtons();
    await sourceRemittancePurposeTypeComponentsPage.clickOnLastDeleteButton();

    sourceRemittancePurposeTypeDeleteDialog = new SourceRemittancePurposeTypeDeleteDialog();
    expect(await sourceRemittancePurposeTypeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Source Remittance Purpose Type?'
    );
    await sourceRemittancePurposeTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sourceRemittancePurposeTypeComponentsPage.title), 5000);

    expect(await sourceRemittancePurposeTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
