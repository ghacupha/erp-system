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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PrepaymentCompilationRequestService } from '../service/prepayment-compilation-request.service';
import { IPrepaymentCompilationRequest, PrepaymentCompilationRequest } from '../prepayment-compilation-request.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

import { PrepaymentCompilationRequestUpdateComponent } from './prepayment-compilation-request-update.component';

describe('PrepaymentCompilationRequest Management Update Component', () => {
  let comp: PrepaymentCompilationRequestUpdateComponent;
  let fixture: ComponentFixture<PrepaymentCompilationRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prepaymentCompilationRequestService: PrepaymentCompilationRequestService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PrepaymentCompilationRequestUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PrepaymentCompilationRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrepaymentCompilationRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prepaymentCompilationRequestService = TestBed.inject(PrepaymentCompilationRequestService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const prepaymentCompilationRequest: IPrepaymentCompilationRequest = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 29314 }];
      prepaymentCompilationRequest.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 63213 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentCompilationRequest });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prepaymentCompilationRequest: IPrepaymentCompilationRequest = { id: 456 };
      const placeholders: IPlaceholder = { id: 43863 };
      prepaymentCompilationRequest.placeholders = [placeholders];

      activatedRoute.data = of({ prepaymentCompilationRequest });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(prepaymentCompilationRequest));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentCompilationRequest>>();
      const prepaymentCompilationRequest = { id: 123 };
      jest.spyOn(prepaymentCompilationRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentCompilationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prepaymentCompilationRequest }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(prepaymentCompilationRequestService.update).toHaveBeenCalledWith(prepaymentCompilationRequest);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentCompilationRequest>>();
      const prepaymentCompilationRequest = new PrepaymentCompilationRequest();
      jest.spyOn(prepaymentCompilationRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentCompilationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prepaymentCompilationRequest }));
      saveSubject.complete();

      // THEN
      expect(prepaymentCompilationRequestService.create).toHaveBeenCalledWith(prepaymentCompilationRequest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentCompilationRequest>>();
      const prepaymentCompilationRequest = { id: 123 };
      jest.spyOn(prepaymentCompilationRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentCompilationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prepaymentCompilationRequestService.update).toHaveBeenCalledWith(prepaymentCompilationRequest);
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
