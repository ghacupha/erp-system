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

import { PrepaymentAccountService } from '../../prepayment-account/service/prepayment-account.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AmortizationRecurrenceService } from '../service/amortization-recurrence.service';
import { IAmortizationRecurrence, AmortizationRecurrence } from '../amortization-recurrence.model';

import { AmortizationRecurrenceUpdateComponent } from './amortization-recurrence-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IDepreciationMethod } from '../../../erp-assets/depreciation-method/depreciation-method.model';
import { IPrepaymentMapping } from '../../prepayment-mapping/prepayment-mapping.model';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { PrepaymentMappingService } from '../../prepayment-mapping/service/prepayment-mapping.service';
import { DepreciationMethodService } from '../../../erp-assets/depreciation-method/service/depreciation-method.service';
import { IPrepaymentAccount } from '../../prepayment-account/prepayment-account.model';

describe('AmortizationRecurrence Management Update Component', () => {
  let comp: AmortizationRecurrenceUpdateComponent;
  let fixture: ComponentFixture<AmortizationRecurrenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let amortizationRecurrenceService: AmortizationRecurrenceService;
  let placeholderService: PlaceholderService;
  let prepaymentMappingService: PrepaymentMappingService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;
  let depreciationMethodService: DepreciationMethodService;
  let prepaymentAccountService: PrepaymentAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AmortizationRecurrenceUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AmortizationRecurrenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AmortizationRecurrenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    amortizationRecurrenceService = TestBed.inject(AmortizationRecurrenceService);
    placeholderService = TestBed.inject(PlaceholderService);
    prepaymentMappingService = TestBed.inject(PrepaymentMappingService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);
    depreciationMethodService = TestBed.inject(DepreciationMethodService);
    prepaymentAccountService = TestBed.inject(PrepaymentAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const amortizationRecurrence: IAmortizationRecurrence = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 74567 }];
      amortizationRecurrence.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 41997 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationRecurrence });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PrepaymentMapping query and add missing value', () => {
      const amortizationRecurrence: IAmortizationRecurrence = { id: 456 };
      const parameters: IPrepaymentMapping[] = [{ id: 2360 }];
      amortizationRecurrence.parameters = parameters;

      const prepaymentMappingCollection: IPrepaymentMapping[] = [{ id: 84779 }];
      jest.spyOn(prepaymentMappingService, 'query').mockReturnValue(of(new HttpResponse({ body: prepaymentMappingCollection })));
      const additionalPrepaymentMappings = [...parameters];
      const expectedCollection: IPrepaymentMapping[] = [...additionalPrepaymentMappings, ...prepaymentMappingCollection];
      jest.spyOn(prepaymentMappingService, 'addPrepaymentMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationRecurrence });
      comp.ngOnInit();

      expect(prepaymentMappingService.query).toHaveBeenCalled();
      expect(prepaymentMappingService.addPrepaymentMappingToCollectionIfMissing).toHaveBeenCalledWith(
        prepaymentMappingCollection,
        ...additionalPrepaymentMappings
      );
      expect(comp.prepaymentMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const amortizationRecurrence: IAmortizationRecurrence = { id: 456 };
      const applicationParameters: IUniversallyUniqueMapping[] = [{ id: 41774 }];
      amortizationRecurrence.applicationParameters = applicationParameters;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 65020 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...applicationParameters];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationRecurrence });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationMethod query and add missing value', () => {
      const amortizationRecurrence: IAmortizationRecurrence = { id: 456 };
      const depreciationMethod: IDepreciationMethod = { id: 23241 };
      amortizationRecurrence.depreciationMethod = depreciationMethod;

      const depreciationMethodCollection: IDepreciationMethod[] = [{ id: 6981 }];
      jest.spyOn(depreciationMethodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationMethodCollection })));
      const additionalDepreciationMethods = [depreciationMethod];
      const expectedCollection: IDepreciationMethod[] = [...additionalDepreciationMethods, ...depreciationMethodCollection];
      jest.spyOn(depreciationMethodService, 'addDepreciationMethodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationRecurrence });
      comp.ngOnInit();

      expect(depreciationMethodService.query).toHaveBeenCalled();
      expect(depreciationMethodService.addDepreciationMethodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationMethodCollection,
        ...additionalDepreciationMethods
      );
      expect(comp.depreciationMethodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PrepaymentAccount query and add missing value', () => {
      const amortizationRecurrence: IAmortizationRecurrence = { id: 456 };
      const prepaymentAccount: IPrepaymentAccount = { id: 73479 };
      amortizationRecurrence.prepaymentAccount = prepaymentAccount;

      const prepaymentAccountCollection: IPrepaymentAccount[] = [{ id: 11038 }];
      jest.spyOn(prepaymentAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: prepaymentAccountCollection })));
      const additionalPrepaymentAccounts = [prepaymentAccount];
      const expectedCollection: IPrepaymentAccount[] = [...additionalPrepaymentAccounts, ...prepaymentAccountCollection];
      jest.spyOn(prepaymentAccountService, 'addPrepaymentAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationRecurrence });
      comp.ngOnInit();

      expect(prepaymentAccountService.query).toHaveBeenCalled();
      expect(prepaymentAccountService.addPrepaymentAccountToCollectionIfMissing).toHaveBeenCalledWith(
        prepaymentAccountCollection,
        ...additionalPrepaymentAccounts
      );
      expect(comp.prepaymentAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const amortizationRecurrence: IAmortizationRecurrence = { id: 456 };
      const placeholders: IPlaceholder = { id: 30107 };
      amortizationRecurrence.placeholders = [placeholders];
      const parameters: IPrepaymentMapping = { id: 65218 };
      amortizationRecurrence.parameters = [parameters];
      const applicationParameters: IUniversallyUniqueMapping = { id: 6202 };
      amortizationRecurrence.applicationParameters = [applicationParameters];
      const depreciationMethod: IDepreciationMethod = { id: 6325 };
      amortizationRecurrence.depreciationMethod = depreciationMethod;
      const prepaymentAccount: IPrepaymentAccount = { id: 26896 };
      amortizationRecurrence.prepaymentAccount = prepaymentAccount;

      activatedRoute.data = of({ amortizationRecurrence });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(amortizationRecurrence));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.prepaymentMappingsSharedCollection).toContain(parameters);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(applicationParameters);
      expect(comp.depreciationMethodsSharedCollection).toContain(depreciationMethod);
      expect(comp.prepaymentAccountsSharedCollection).toContain(prepaymentAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationRecurrence>>();
      const amortizationRecurrence = { id: 123 };
      jest.spyOn(amortizationRecurrenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationRecurrence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amortizationRecurrence }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(amortizationRecurrenceService.update).toHaveBeenCalledWith(amortizationRecurrence);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationRecurrence>>();
      const amortizationRecurrence = new AmortizationRecurrence();
      jest.spyOn(amortizationRecurrenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationRecurrence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amortizationRecurrence }));
      saveSubject.complete();

      // THEN
      // TODO expect(amortizationRecurrenceService.create).toHaveBeenCalledWith(amortizationRecurrence);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationRecurrence>>();
      const amortizationRecurrence = { id: 123 };
      jest.spyOn(amortizationRecurrenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationRecurrence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(amortizationRecurrenceService.update).toHaveBeenCalledWith(amortizationRecurrence);
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

    describe('trackPrepaymentMappingById', () => {
      it('Should return tracked PrepaymentMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPrepaymentMappingById(0, entity);
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

    describe('trackDepreciationMethodById', () => {
      it('Should return tracked DepreciationMethod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepreciationMethodById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPrepaymentAccountById', () => {
      it('Should return tracked PrepaymentAccount primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPrepaymentAccountById(0, entity);
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

    describe('getSelectedPrepaymentMapping', () => {
      it('Should return option if no PrepaymentMapping is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPrepaymentMapping(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected PrepaymentMapping for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPrepaymentMapping(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this PrepaymentMapping is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPrepaymentMapping(option, [selected]);
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
