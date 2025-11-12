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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AccountAttributeMetadataService } from '../service/account-attribute-metadata.service';
import { IAccountAttributeMetadata, AccountAttributeMetadata } from '../account-attribute-metadata.model';
import { IGdiMasterDataIndex } from 'app/entities/gdi/gdi-master-data-index/gdi-master-data-index.model';
import { GdiMasterDataIndexService } from 'app/entities/gdi/gdi-master-data-index/service/gdi-master-data-index.service';

import { AccountAttributeMetadataUpdateComponent } from './account-attribute-metadata-update.component';

describe('AccountAttributeMetadata Management Update Component', () => {
  let comp: AccountAttributeMetadataUpdateComponent;
  let fixture: ComponentFixture<AccountAttributeMetadataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountAttributeMetadataService: AccountAttributeMetadataService;
  let gdiMasterDataIndexService: GdiMasterDataIndexService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AccountAttributeMetadataUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AccountAttributeMetadataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountAttributeMetadataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountAttributeMetadataService = TestBed.inject(AccountAttributeMetadataService);
    gdiMasterDataIndexService = TestBed.inject(GdiMasterDataIndexService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call GdiMasterDataIndex query and add missing value', () => {
      const accountAttributeMetadata: IAccountAttributeMetadata = { id: 456 };
      const standardInputTemplate: IGdiMasterDataIndex = { id: 12415 };
      accountAttributeMetadata.standardInputTemplate = standardInputTemplate;

      const gdiMasterDataIndexCollection: IGdiMasterDataIndex[] = [{ id: 19138 }];
      jest.spyOn(gdiMasterDataIndexService, 'query').mockReturnValue(of(new HttpResponse({ body: gdiMasterDataIndexCollection })));
      const additionalGdiMasterDataIndices = [standardInputTemplate];
      const expectedCollection: IGdiMasterDataIndex[] = [...additionalGdiMasterDataIndices, ...gdiMasterDataIndexCollection];
      jest.spyOn(gdiMasterDataIndexService, 'addGdiMasterDataIndexToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountAttributeMetadata });
      comp.ngOnInit();

      expect(gdiMasterDataIndexService.query).toHaveBeenCalled();
      expect(gdiMasterDataIndexService.addGdiMasterDataIndexToCollectionIfMissing).toHaveBeenCalledWith(
        gdiMasterDataIndexCollection,
        ...additionalGdiMasterDataIndices
      );
      expect(comp.gdiMasterDataIndicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accountAttributeMetadata: IAccountAttributeMetadata = { id: 456 };
      const standardInputTemplate: IGdiMasterDataIndex = { id: 93126 };
      accountAttributeMetadata.standardInputTemplate = standardInputTemplate;

      activatedRoute.data = of({ accountAttributeMetadata });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(accountAttributeMetadata));
      expect(comp.gdiMasterDataIndicesSharedCollection).toContain(standardInputTemplate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountAttributeMetadata>>();
      const accountAttributeMetadata = { id: 123 };
      jest.spyOn(accountAttributeMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountAttributeMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountAttributeMetadata }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountAttributeMetadataService.update).toHaveBeenCalledWith(accountAttributeMetadata);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountAttributeMetadata>>();
      const accountAttributeMetadata = new AccountAttributeMetadata();
      jest.spyOn(accountAttributeMetadataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountAttributeMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountAttributeMetadata }));
      saveSubject.complete();

      // THEN
      expect(accountAttributeMetadataService.create).toHaveBeenCalledWith(accountAttributeMetadata);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountAttributeMetadata>>();
      const accountAttributeMetadata = { id: 123 };
      jest.spyOn(accountAttributeMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountAttributeMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountAttributeMetadataService.update).toHaveBeenCalledWith(accountAttributeMetadata);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackGdiMasterDataIndexById', () => {
      it('Should return tracked GdiMasterDataIndex primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGdiMasterDataIndexById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
