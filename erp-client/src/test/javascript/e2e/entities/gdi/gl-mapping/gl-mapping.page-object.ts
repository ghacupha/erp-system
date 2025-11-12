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

import { element, by, ElementFinder } from 'protractor';

export class GlMappingComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-gl-mapping div table .btn-danger'));
  title = element.all(by.css('jhi-gl-mapping div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class GlMappingUpdatePage {
  pageTitle = element(by.id('jhi-gl-mapping-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  subGLCodeInput = element(by.id('field_subGLCode'));
  subGLDescriptionInput = element(by.id('field_subGLDescription'));
  mainGLCodeInput = element(by.id('field_mainGLCode'));
  mainGLDescriptionInput = element(by.id('field_mainGLDescription'));
  glTypeInput = element(by.id('field_glType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSubGLCodeInput(subGLCode: string): Promise<void> {
    await this.subGLCodeInput.sendKeys(subGLCode);
  }

  async getSubGLCodeInput(): Promise<string> {
    return await this.subGLCodeInput.getAttribute('value');
  }

  async setSubGLDescriptionInput(subGLDescription: string): Promise<void> {
    await this.subGLDescriptionInput.sendKeys(subGLDescription);
  }

  async getSubGLDescriptionInput(): Promise<string> {
    return await this.subGLDescriptionInput.getAttribute('value');
  }

  async setMainGLCodeInput(mainGLCode: string): Promise<void> {
    await this.mainGLCodeInput.sendKeys(mainGLCode);
  }

  async getMainGLCodeInput(): Promise<string> {
    return await this.mainGLCodeInput.getAttribute('value');
  }

  async setMainGLDescriptionInput(mainGLDescription: string): Promise<void> {
    await this.mainGLDescriptionInput.sendKeys(mainGLDescription);
  }

  async getMainGLDescriptionInput(): Promise<string> {
    return await this.mainGLDescriptionInput.getAttribute('value');
  }

  async setGlTypeInput(glType: string): Promise<void> {
    await this.glTypeInput.sendKeys(glType);
  }

  async getGlTypeInput(): Promise<string> {
    return await this.glTypeInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class GlMappingDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-glMapping-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-glMapping'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
