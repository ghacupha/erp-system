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

import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SubCountyCodeService } from '../service/sub-county-code.service';
import { ISubCountyCode, SubCountyCode } from '../sub-county-code.model';

import { SubCountyCodeUpdateComponent } from './sub-county-code-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';

describe('SubCountyCode Management Update Component', () => {
  let comp: SubCountyCodeUpdateComponent;
  let fixture: ComponentFixture<SubCountyCodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let subCountyCodeService: SubCountyCodeService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SubCountyCodeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SubCountyCodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubCountyCodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    subCountyCodeService = TestBed.inject(SubCountyCodeService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const subCountyCode: ISubCountyCode = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 31232 }];
      subCountyCode.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 31150 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ subCountyCode });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const subCountyCode: ISubCountyCode = { id: 456 };
      const placeholders: IPlaceholder = { id: 87689 };
      subCountyCode.placeholders = [placeholders];

      activatedRoute.data = of({ subCountyCode });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(subCountyCode));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SubCountyCode>>();
      const subCountyCode = { id: 123 };
      jest.spyOn(subCountyCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subCountyCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subCountyCode }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(subCountyCodeService.update).toHaveBeenCalledWith(subCountyCode);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SubCountyCode>>();
      const subCountyCode = new SubCountyCode();
      jest.spyOn(subCountyCodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subCountyCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subCountyCode }));
      saveSubject.complete();

      // THEN
      expect(subCountyCodeService.create).toHaveBeenCalledWith(subCountyCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SubCountyCode>>();
      const subCountyCode = { id: 123 };
      jest.spyOn(subCountyCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subCountyCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(subCountyCodeService.update).toHaveBeenCalledWith(subCountyCode);
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
