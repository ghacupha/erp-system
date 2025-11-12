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

import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FixedAssetAcquisitionService } from '../service/fixed-asset-acquisition.service';
import { IFixedAssetAcquisition, FixedAssetAcquisition } from '../fixed-asset-acquisition.model';

import { FixedAssetAcquisitionUpdateComponent } from './fixed-asset-acquisition-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';

describe('FixedAssetAcquisition Management Update Component', () => {
  let comp: FixedAssetAcquisitionUpdateComponent;
  let fixture: ComponentFixture<FixedAssetAcquisitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fixedAssetAcquisitionService: FixedAssetAcquisitionService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FixedAssetAcquisitionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FixedAssetAcquisitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FixedAssetAcquisitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fixedAssetAcquisitionService = TestBed.inject(FixedAssetAcquisitionService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const fixedAssetAcquisition: IFixedAssetAcquisition = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 43365 }];
      fixedAssetAcquisition.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 61260 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fixedAssetAcquisition });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fixedAssetAcquisition: IFixedAssetAcquisition = { id: 456 };
      const placeholders: IPlaceholder = { id: 67851 };
      fixedAssetAcquisition.placeholders = [placeholders];

      activatedRoute.data = of({ fixedAssetAcquisition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fixedAssetAcquisition));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FixedAssetAcquisition>>();
      const fixedAssetAcquisition = { id: 123 };
      jest.spyOn(fixedAssetAcquisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fixedAssetAcquisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fixedAssetAcquisition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fixedAssetAcquisitionService.update).toHaveBeenCalledWith(fixedAssetAcquisition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FixedAssetAcquisition>>();
      const fixedAssetAcquisition = new FixedAssetAcquisition();
      jest.spyOn(fixedAssetAcquisitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fixedAssetAcquisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fixedAssetAcquisition }));
      saveSubject.complete();

      // THEN
      expect(fixedAssetAcquisitionService.create).toHaveBeenCalledWith(fixedAssetAcquisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FixedAssetAcquisition>>();
      const fixedAssetAcquisition = { id: 123 };
      jest.spyOn(fixedAssetAcquisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fixedAssetAcquisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fixedAssetAcquisitionService.update).toHaveBeenCalledWith(fixedAssetAcquisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedPlaceholder', () => {
      it('Should return option if no Placeholder is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPlaceholder(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Placeholder for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Placeholder is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
