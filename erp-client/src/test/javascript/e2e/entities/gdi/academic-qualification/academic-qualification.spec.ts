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
  AcademicQualificationComponentsPage,
  AcademicQualificationDeleteDialog,
  AcademicQualificationUpdatePage,
} from './academic-qualification.page-object';

const expect = chai.expect;

describe('AcademicQualification e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let academicQualificationComponentsPage: AcademicQualificationComponentsPage;
  let academicQualificationUpdatePage: AcademicQualificationUpdatePage;
  let academicQualificationDeleteDialog: AcademicQualificationDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AcademicQualifications', async () => {
    await navBarPage.goToEntity('academic-qualification');
    academicQualificationComponentsPage = new AcademicQualificationComponentsPage();
    await browser.wait(ec.visibilityOf(academicQualificationComponentsPage.title), 5000);
    expect(await academicQualificationComponentsPage.getTitle()).to.eq('Academic Qualifications');
    await browser.wait(
      ec.or(ec.visibilityOf(academicQualificationComponentsPage.entities), ec.visibilityOf(academicQualificationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AcademicQualification page', async () => {
    await academicQualificationComponentsPage.clickOnCreateButton();
    academicQualificationUpdatePage = new AcademicQualificationUpdatePage();
    expect(await academicQualificationUpdatePage.getPageTitle()).to.eq('Create or edit a Academic Qualification');
    await academicQualificationUpdatePage.cancel();
  });

  it('should create and save AcademicQualifications', async () => {
    const nbButtonsBeforeCreate = await academicQualificationComponentsPage.countDeleteButtons();

    await academicQualificationComponentsPage.clickOnCreateButton();

    await promise.all([
      academicQualificationUpdatePage.setAcademicQualificationsCodeInput('academicQualificationsCode'),
      academicQualificationUpdatePage.setAcademicQualificationTypeInput('academicQualificationType'),
      academicQualificationUpdatePage.setAcademicQualificationTypeDetailInput('academicQualificationTypeDetail'),
    ]);

    await academicQualificationUpdatePage.save();
    expect(await academicQualificationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await academicQualificationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last AcademicQualification', async () => {
    const nbButtonsBeforeDelete = await academicQualificationComponentsPage.countDeleteButtons();
    await academicQualificationComponentsPage.clickOnLastDeleteButton();

    academicQualificationDeleteDialog = new AcademicQualificationDeleteDialog();
    expect(await academicQualificationDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Academic Qualification?');
    await academicQualificationDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(academicQualificationComponentsPage.title), 5000);

    expect(await academicQualificationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
