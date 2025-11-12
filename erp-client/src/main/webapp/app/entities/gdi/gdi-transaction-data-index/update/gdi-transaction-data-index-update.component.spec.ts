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

import { GdiTransactionDataIndexService } from '../service/gdi-transaction-data-index.service';
import { IGdiTransactionDataIndex, GdiTransactionDataIndex } from '../gdi-transaction-data-index.model';
import { IGdiMasterDataIndex } from 'app/entities/gdi/gdi-master-data-index/gdi-master-data-index.model';
import { GdiMasterDataIndexService } from 'app/entities/gdi/gdi-master-data-index/service/gdi-master-data-index.service';

import { GdiTransactionDataIndexUpdateComponent } from './gdi-transaction-data-index-update.component';

describe('GdiTransactionDataIndex Management Update Component', () => {
  let comp: GdiTransactionDataIndexUpdateComponent;
  let fixture: ComponentFixture<GdiTransactionDataIndexUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gdiTransactionDataIndexService: GdiTransactionDataIndexService;
  let gdiMasterDataIndexService: GdiMasterDataIndexService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GdiTransactionDataIndexUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(GdiTransactionDataIndexUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GdiTransactionDataIndexUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gdiTransactionDataIndexService = TestBed.inject(GdiTransactionDataIndexService);
    gdiMasterDataIndexService = TestBed.inject(GdiMasterDataIndexService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call GdiMasterDataIndex query and add missing value', () => {
      const gdiTransactionDataIndex: IGdiTransactionDataIndex = { id: 456 };
      const masterDataItems: IGdiMasterDataIndex[] = [{ id: 72602 }];
      gdiTransactionDataIndex.masterDataItems = masterDataItems;

      const gdiMasterDataIndexCollection: IGdiMasterDataIndex[] = [{ id: 24767 }];
      jest.spyOn(gdiMasterDataIndexService, 'query').mockReturnValue(of(new HttpResponse({ body: gdiMasterDataIndexCollection })));
      const additionalGdiMasterDataIndices = [...masterDataItems];
      const expectedCollection: IGdiMasterDataIndex[] = [...additionalGdiMasterDataIndices, ...gdiMasterDataIndexCollection];
      jest.spyOn(gdiMasterDataIndexService, 'addGdiMasterDataIndexToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gdiTransactionDataIndex });
      comp.ngOnInit();

      expect(gdiMasterDataIndexService.query).toHaveBeenCalled();
      expect(gdiMasterDataIndexService.addGdiMasterDataIndexToCollectionIfMissing).toHaveBeenCalledWith(
        gdiMasterDataIndexCollection,
        ...additionalGdiMasterDataIndices
      );
      expect(comp.gdiMasterDataIndicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gdiTransactionDataIndex: IGdiTransactionDataIndex = { id: 456 };
      const masterDataItems: IGdiMasterDataIndex = { id: 81067 };
      gdiTransactionDataIndex.masterDataItems = [masterDataItems];

      activatedRoute.data = of({ gdiTransactionDataIndex });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(gdiTransactionDataIndex));
      expect(comp.gdiMasterDataIndicesSharedCollection).toContain(masterDataItems);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GdiTransactionDataIndex>>();
      const gdiTransactionDataIndex = { id: 123 };
      jest.spyOn(gdiTransactionDataIndexService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gdiTransactionDataIndex });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gdiTransactionDataIndex }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(gdiTransactionDataIndexService.update).toHaveBeenCalledWith(gdiTransactionDataIndex);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GdiTransactionDataIndex>>();
      const gdiTransactionDataIndex = new GdiTransactionDataIndex();
      jest.spyOn(gdiTransactionDataIndexService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gdiTransactionDataIndex });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gdiTransactionDataIndex }));
      saveSubject.complete();

      // THEN
      expect(gdiTransactionDataIndexService.create).toHaveBeenCalledWith(gdiTransactionDataIndex);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GdiTransactionDataIndex>>();
      const gdiTransactionDataIndex = { id: 123 };
      jest.spyOn(gdiTransactionDataIndexService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gdiTransactionDataIndex });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gdiTransactionDataIndexService.update).toHaveBeenCalledWith(gdiTransactionDataIndex);
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

  describe('Getting selected relationships', () => {
    describe('getSelectedGdiMasterDataIndex', () => {
      it('Should return option if no GdiMasterDataIndex is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedGdiMasterDataIndex(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected GdiMasterDataIndex for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedGdiMasterDataIndex(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this GdiMasterDataIndex is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedGdiMasterDataIndex(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
