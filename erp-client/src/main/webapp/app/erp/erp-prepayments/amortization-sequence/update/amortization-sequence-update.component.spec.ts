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

import { AmortizationSequenceService } from '../service/amortization-sequence.service';
import { IAmortizationSequence, AmortizationSequence } from '../amortization-sequence.model';

import { AmortizationSequenceUpdateComponent } from './amortization-sequence-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IAmortizationRecurrence } from '../../amortization-recurrence/amortization-recurrence.model';
import { IPrepaymentMapping } from '../../prepayment-mapping/prepayment-mapping.model';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { AmortizationRecurrenceService } from '../../amortization-recurrence/service/amortization-recurrence.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { PrepaymentMappingService } from '../../prepayment-mapping/service/prepayment-mapping.service';
import { IPrepaymentAccount } from '../../prepayment-account/prepayment-account.model';

describe('AmortizationSequence Management Update Component', () => {
  let comp: AmortizationSequenceUpdateComponent;
  let fixture: ComponentFixture<AmortizationSequenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let amortizationSequenceService: AmortizationSequenceService;
  let prepaymentAccountService: PrepaymentAccountService;
  let amortizationRecurrenceService: AmortizationRecurrenceService;
  let placeholderService: PlaceholderService;
  let prepaymentMappingService: PrepaymentMappingService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AmortizationSequenceUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AmortizationSequenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AmortizationSequenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    amortizationSequenceService = TestBed.inject(AmortizationSequenceService);
    prepaymentAccountService = TestBed.inject(PrepaymentAccountService);
    amortizationRecurrenceService = TestBed.inject(AmortizationRecurrenceService);
    placeholderService = TestBed.inject(PlaceholderService);
    prepaymentMappingService = TestBed.inject(PrepaymentMappingService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PrepaymentAccount query and add missing value', () => {
      const amortizationSequence: IAmortizationSequence = { id: 456 };
      const prepaymentAccount: IPrepaymentAccount = { id: 76107 };
      amortizationSequence.prepaymentAccount = prepaymentAccount;

      const prepaymentAccountCollection: IPrepaymentAccount[] = [{ id: 44742 }];
      jest.spyOn(prepaymentAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: prepaymentAccountCollection })));
      const additionalPrepaymentAccounts = [prepaymentAccount];
      const expectedCollection: IPrepaymentAccount[] = [...additionalPrepaymentAccounts, ...prepaymentAccountCollection];
      jest.spyOn(prepaymentAccountService, 'addPrepaymentAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationSequence });
      comp.ngOnInit();

      expect(prepaymentAccountService.query).toHaveBeenCalled();
      expect(prepaymentAccountService.addPrepaymentAccountToCollectionIfMissing).toHaveBeenCalledWith(
        prepaymentAccountCollection,
        ...additionalPrepaymentAccounts
      );
      expect(comp.prepaymentAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AmortizationRecurrence query and add missing value', () => {
      const amortizationSequence: IAmortizationSequence = { id: 456 };
      const amortizationRecurrence: IAmortizationRecurrence = { id: 9151 };
      amortizationSequence.amortizationRecurrence = amortizationRecurrence;

      const amortizationRecurrenceCollection: IAmortizationRecurrence[] = [{ id: 33652 }];
      jest.spyOn(amortizationRecurrenceService, 'query').mockReturnValue(of(new HttpResponse({ body: amortizationRecurrenceCollection })));
      const additionalAmortizationRecurrences = [amortizationRecurrence];
      const expectedCollection: IAmortizationRecurrence[] = [...additionalAmortizationRecurrences, ...amortizationRecurrenceCollection];
      jest.spyOn(amortizationRecurrenceService, 'addAmortizationRecurrenceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationSequence });
      comp.ngOnInit();

      expect(amortizationRecurrenceService.query).toHaveBeenCalled();
      expect(amortizationRecurrenceService.addAmortizationRecurrenceToCollectionIfMissing).toHaveBeenCalledWith(
        amortizationRecurrenceCollection,
        ...additionalAmortizationRecurrences
      );
      expect(comp.amortizationRecurrencesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const amortizationSequence: IAmortizationSequence = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 64564 }];
      amortizationSequence.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 12372 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationSequence });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PrepaymentMapping query and add missing value', () => {
      const amortizationSequence: IAmortizationSequence = { id: 456 };
      const prepaymentMappings: IPrepaymentMapping[] = [{ id: 37255 }];
      amortizationSequence.prepaymentMappings = prepaymentMappings;

      const prepaymentMappingCollection: IPrepaymentMapping[] = [{ id: 64405 }];
      jest.spyOn(prepaymentMappingService, 'query').mockReturnValue(of(new HttpResponse({ body: prepaymentMappingCollection })));
      const additionalPrepaymentMappings = [...prepaymentMappings];
      const expectedCollection: IPrepaymentMapping[] = [...additionalPrepaymentMappings, ...prepaymentMappingCollection];
      jest.spyOn(prepaymentMappingService, 'addPrepaymentMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationSequence });
      comp.ngOnInit();

      expect(prepaymentMappingService.query).toHaveBeenCalled();
      expect(prepaymentMappingService.addPrepaymentMappingToCollectionIfMissing).toHaveBeenCalledWith(
        prepaymentMappingCollection,
        ...additionalPrepaymentMappings
      );
      expect(comp.prepaymentMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const amortizationSequence: IAmortizationSequence = { id: 456 };
      const applicationParameters: IUniversallyUniqueMapping[] = [{ id: 79767 }];
      amortizationSequence.applicationParameters = applicationParameters;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 41560 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...applicationParameters];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amortizationSequence });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const amortizationSequence: IAmortizationSequence = { id: 456 };
      const prepaymentAccount: IPrepaymentAccount = { id: 14544 };
      amortizationSequence.prepaymentAccount = prepaymentAccount;
      const amortizationRecurrence: IAmortizationRecurrence = { id: 5922 };
      amortizationSequence.amortizationRecurrence = amortizationRecurrence;
      const placeholders: IPlaceholder = { id: 33594 };
      amortizationSequence.placeholders = [placeholders];
      const prepaymentMappings: IPrepaymentMapping = { id: 71269 };
      amortizationSequence.prepaymentMappings = [prepaymentMappings];
      const applicationParameters: IUniversallyUniqueMapping = { id: 50876 };
      amortizationSequence.applicationParameters = [applicationParameters];

      activatedRoute.data = of({ amortizationSequence });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(amortizationSequence));
      expect(comp.prepaymentAccountsSharedCollection).toContain(prepaymentAccount);
      expect(comp.amortizationRecurrencesSharedCollection).toContain(amortizationRecurrence);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.prepaymentMappingsSharedCollection).toContain(prepaymentMappings);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(applicationParameters);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationSequence>>();
      const amortizationSequence = { id: 123 };
      jest.spyOn(amortizationSequenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationSequence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amortizationSequence }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(amortizationSequenceService.update).toHaveBeenCalledWith(amortizationSequence);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationSequence>>();
      const amortizationSequence = new AmortizationSequence();
      jest.spyOn(amortizationSequenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationSequence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amortizationSequence }));
      saveSubject.complete();

      // THEN
      expect(amortizationSequenceService.create).toHaveBeenCalledWith(amortizationSequence);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmortizationSequence>>();
      const amortizationSequence = { id: 123 };
      jest.spyOn(amortizationSequenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amortizationSequence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(amortizationSequenceService.update).toHaveBeenCalledWith(amortizationSequence);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPrepaymentAccountById', () => {
      it('Should return tracked PrepaymentAccount primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPrepaymentAccountById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAmortizationRecurrenceById', () => {
      it('Should return tracked AmortizationRecurrence primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAmortizationRecurrenceById(0, entity);
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
