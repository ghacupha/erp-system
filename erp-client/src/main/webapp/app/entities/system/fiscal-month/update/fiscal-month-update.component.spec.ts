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

import { FiscalMonthService } from '../service/fiscal-month.service';
import { IFiscalMonth, FiscalMonth } from '../fiscal-month.model';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';
import { FiscalYearService } from 'app/entities/system/fiscal-year/service/fiscal-year.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { IFiscalQuarter } from 'app/entities/system/fiscal-quarter/fiscal-quarter.model';
import { FiscalQuarterService } from 'app/entities/system/fiscal-quarter/service/fiscal-quarter.service';

import { FiscalMonthUpdateComponent } from './fiscal-month-update.component';

describe('FiscalMonth Management Update Component', () => {
  let comp: FiscalMonthUpdateComponent;
  let fixture: ComponentFixture<FiscalMonthUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fiscalMonthService: FiscalMonthService;
  let fiscalYearService: FiscalYearService;
  let placeholderService: PlaceholderService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;
  let fiscalQuarterService: FiscalQuarterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FiscalMonthUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FiscalMonthUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FiscalMonthUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fiscalMonthService = TestBed.inject(FiscalMonthService);
    fiscalYearService = TestBed.inject(FiscalYearService);
    placeholderService = TestBed.inject(PlaceholderService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);
    fiscalQuarterService = TestBed.inject(FiscalQuarterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FiscalYear query and add missing value', () => {
      const fiscalMonth: IFiscalMonth = { id: 456 };
      const fiscalYear: IFiscalYear = { id: 50742 };
      fiscalMonth.fiscalYear = fiscalYear;

      const fiscalYearCollection: IFiscalYear[] = [{ id: 7476 }];
      jest.spyOn(fiscalYearService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalYearCollection })));
      const additionalFiscalYears = [fiscalYear];
      const expectedCollection: IFiscalYear[] = [...additionalFiscalYears, ...fiscalYearCollection];
      jest.spyOn(fiscalYearService, 'addFiscalYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      expect(fiscalYearService.query).toHaveBeenCalled();
      expect(fiscalYearService.addFiscalYearToCollectionIfMissing).toHaveBeenCalledWith(fiscalYearCollection, ...additionalFiscalYears);
      expect(comp.fiscalYearsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const fiscalMonth: IFiscalMonth = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 51444 }];
      fiscalMonth.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 35444 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const fiscalMonth: IFiscalMonth = { id: 456 };
      const universallyUniqueMappings: IUniversallyUniqueMapping[] = [{ id: 178 }];
      fiscalMonth.universallyUniqueMappings = universallyUniqueMappings;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 87257 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...universallyUniqueMappings];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiscalQuarter query and add missing value', () => {
      const fiscalMonth: IFiscalMonth = { id: 456 };
      const fiscalQuarter: IFiscalQuarter = { id: 49157 };
      fiscalMonth.fiscalQuarter = fiscalQuarter;

      const fiscalQuarterCollection: IFiscalQuarter[] = [{ id: 49551 }];
      jest.spyOn(fiscalQuarterService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalQuarterCollection })));
      const additionalFiscalQuarters = [fiscalQuarter];
      const expectedCollection: IFiscalQuarter[] = [...additionalFiscalQuarters, ...fiscalQuarterCollection];
      jest.spyOn(fiscalQuarterService, 'addFiscalQuarterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      expect(fiscalQuarterService.query).toHaveBeenCalled();
      expect(fiscalQuarterService.addFiscalQuarterToCollectionIfMissing).toHaveBeenCalledWith(
        fiscalQuarterCollection,
        ...additionalFiscalQuarters
      );
      expect(comp.fiscalQuartersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fiscalMonth: IFiscalMonth = { id: 456 };
      const fiscalYear: IFiscalYear = { id: 17337 };
      fiscalMonth.fiscalYear = fiscalYear;
      const placeholders: IPlaceholder = { id: 96095 };
      fiscalMonth.placeholders = [placeholders];
      const universallyUniqueMappings: IUniversallyUniqueMapping = { id: 63395 };
      fiscalMonth.universallyUniqueMappings = [universallyUniqueMappings];
      const fiscalQuarter: IFiscalQuarter = { id: 53256 };
      fiscalMonth.fiscalQuarter = fiscalQuarter;

      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fiscalMonth));
      expect(comp.fiscalYearsSharedCollection).toContain(fiscalYear);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(universallyUniqueMappings);
      expect(comp.fiscalQuartersSharedCollection).toContain(fiscalQuarter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FiscalMonth>>();
      const fiscalMonth = { id: 123 };
      jest.spyOn(fiscalMonthService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fiscalMonth }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fiscalMonthService.update).toHaveBeenCalledWith(fiscalMonth);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FiscalMonth>>();
      const fiscalMonth = new FiscalMonth();
      jest.spyOn(fiscalMonthService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fiscalMonth }));
      saveSubject.complete();

      // THEN
      expect(fiscalMonthService.create).toHaveBeenCalledWith(fiscalMonth);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FiscalMonth>>();
      const fiscalMonth = { id: 123 };
      jest.spyOn(fiscalMonthService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fiscalMonth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fiscalMonthService.update).toHaveBeenCalledWith(fiscalMonth);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFiscalYearById', () => {
      it('Should return tracked FiscalYear primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalYearById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUniversallyUniqueMappingById', () => {
      it('Should return tracked UniversallyUniqueMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUniversallyUniqueMappingById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFiscalQuarterById', () => {
      it('Should return tracked FiscalQuarter primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalQuarterById(0, entity);
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

    describe('getSelectedUniversallyUniqueMapping', () => {
      it('Should return option if no UniversallyUniqueMapping is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUniversallyUniqueMapping(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected UniversallyUniqueMapping for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this UniversallyUniqueMapping is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
