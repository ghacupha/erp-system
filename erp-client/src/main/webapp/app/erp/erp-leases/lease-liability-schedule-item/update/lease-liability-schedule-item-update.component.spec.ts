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

import { LeaseLiabilityScheduleItemService } from '../service/lease-liability-schedule-item.service';
import { ILeaseLiabilityScheduleItem, LeaseLiabilityScheduleItem } from '../lease-liability-schedule-item.model';

import { LeaseLiabilityScheduleItemUpdateComponent } from './lease-liability-schedule-item-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IFRS16LeaseContractService } from '../../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { ILeaseLiability } from '../../lease-liability/lease-liability.model';
import { LeaseLiabilityService } from '../../lease-liability/service/lease-liability.service';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { LeaseAmortizationScheduleService } from '../../lease-amortization-schedule/service/lease-amortization-schedule.service';
import { ILeaseRepaymentPeriod } from '../../lease-repayment-period/lease-repayment-period.model';
import { IIFRS16LeaseContract } from '../../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { LeaseRepaymentPeriodService } from '../../lease-repayment-period/service/lease-repayment-period.service';
import { ILeaseAmortizationSchedule } from '../../lease-amortization-schedule/lease-amortization-schedule.model';
import { LeaseLiabilityCompilationService } from '../../lease-liability-compilation/service/lease-liability-compilation.service';
import { ILeaseLiabilityCompilation } from '../../lease-liability-compilation/lease-liability-compilation.model';

describe('LeaseLiabilityScheduleItem Management Update Component', () => {
  let comp: LeaseLiabilityScheduleItemUpdateComponent;
  let fixture: ComponentFixture<LeaseLiabilityScheduleItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaseLiabilityScheduleItemService: LeaseLiabilityScheduleItemService;
  let placeholderService: PlaceholderService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;
  let leaseAmortizationScheduleService: LeaseAmortizationScheduleService;
  let iFRS16LeaseContractService: IFRS16LeaseContractService;
  let leaseLiabilityService: LeaseLiabilityService;
  let leaseRepaymentPeriodService: LeaseRepaymentPeriodService;
  let leaseLiabilityCompilationService: LeaseLiabilityCompilationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseLiabilityScheduleItemUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LeaseLiabilityScheduleItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaseLiabilityScheduleItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaseLiabilityScheduleItemService = TestBed.inject(LeaseLiabilityScheduleItemService);
    placeholderService = TestBed.inject(PlaceholderService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);
    leaseAmortizationScheduleService = TestBed.inject(LeaseAmortizationScheduleService);
    iFRS16LeaseContractService = TestBed.inject(IFRS16LeaseContractService);
    leaseLiabilityService = TestBed.inject(LeaseLiabilityService);
    leaseRepaymentPeriodService = TestBed.inject(LeaseRepaymentPeriodService);
    leaseLiabilityCompilationService = TestBed.inject(LeaseLiabilityCompilationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 70734 }];
      leaseLiabilityScheduleItem.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 78261 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem = { id: 456 };
      const universallyUniqueMappings: IUniversallyUniqueMapping[] = [{ id: 66061 }];
      leaseLiabilityScheduleItem.universallyUniqueMappings = universallyUniqueMappings;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 25704 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...universallyUniqueMappings];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LeaseAmortizationSchedule query and add missing value', () => {
      const leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem = { id: 456 };
      const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 95924 };
      leaseLiabilityScheduleItem.leaseAmortizationSchedule = leaseAmortizationSchedule;

      const leaseAmortizationScheduleCollection: ILeaseAmortizationSchedule[] = [{ id: 59618 }];
      jest
        .spyOn(leaseAmortizationScheduleService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: leaseAmortizationScheduleCollection })));
      const additionalLeaseAmortizationSchedules = [leaseAmortizationSchedule];
      const expectedCollection: ILeaseAmortizationSchedule[] = [
        ...additionalLeaseAmortizationSchedules,
        ...leaseAmortizationScheduleCollection,
      ];
      jest.spyOn(leaseAmortizationScheduleService, 'addLeaseAmortizationScheduleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      expect(leaseAmortizationScheduleService.query).toHaveBeenCalled();
      expect(leaseAmortizationScheduleService.addLeaseAmortizationScheduleToCollectionIfMissing).toHaveBeenCalledWith(
        leaseAmortizationScheduleCollection,
        ...additionalLeaseAmortizationSchedules
      );
      expect(comp.leaseAmortizationSchedulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call IFRS16LeaseContract query and add missing value', () => {
      const leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem = { id: 456 };
      const leaseContract: IIFRS16LeaseContract = { id: 86522 };
      leaseLiabilityScheduleItem.leaseContract = leaseContract;

      const iFRS16LeaseContractCollection: IIFRS16LeaseContract[] = [{ id: 21528 }];
      jest.spyOn(iFRS16LeaseContractService, 'query').mockReturnValue(of(new HttpResponse({ body: iFRS16LeaseContractCollection })));
      const additionalIFRS16LeaseContracts = [leaseContract];
      const expectedCollection: IIFRS16LeaseContract[] = [...additionalIFRS16LeaseContracts, ...iFRS16LeaseContractCollection];
      jest.spyOn(iFRS16LeaseContractService, 'addIFRS16LeaseContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      expect(iFRS16LeaseContractService.query).toHaveBeenCalled();
      expect(iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing).toHaveBeenCalledWith(
        iFRS16LeaseContractCollection,
        ...additionalIFRS16LeaseContracts
      );
      expect(comp.iFRS16LeaseContractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LeaseLiability query and add missing value', () => {
      const leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem = { id: 456 };
      const leaseLiability: ILeaseLiability = { id: 15224 };
      leaseLiabilityScheduleItem.leaseLiability = leaseLiability;

      const leaseLiabilityCollection: ILeaseLiability[] = [{ id: 2776 }];
      jest.spyOn(leaseLiabilityService, 'query').mockReturnValue(of(new HttpResponse({ body: leaseLiabilityCollection })));
      const additionalLeaseLiabilities = [leaseLiability];
      const expectedCollection: ILeaseLiability[] = [...additionalLeaseLiabilities, ...leaseLiabilityCollection];
      jest.spyOn(leaseLiabilityService, 'addLeaseLiabilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      expect(leaseLiabilityService.query).toHaveBeenCalled();
      expect(leaseLiabilityService.addLeaseLiabilityToCollectionIfMissing).toHaveBeenCalledWith(
        leaseLiabilityCollection,
        ...additionalLeaseLiabilities
      );
      expect(comp.leaseLiabilitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LeaseRepaymentPeriod query and add missing value', () => {
      const leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem = { id: 456 };
      const leasePeriod: ILeaseRepaymentPeriod = { id: 59988 };
      leaseLiabilityScheduleItem.leasePeriod = leasePeriod;

      const leaseRepaymentPeriodCollection: ILeaseRepaymentPeriod[] = [{ id: 5838 }];
      jest.spyOn(leaseRepaymentPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: leaseRepaymentPeriodCollection })));
      const additionalLeaseRepaymentPeriods = [leasePeriod];
      const expectedCollection: ILeaseRepaymentPeriod[] = [...additionalLeaseRepaymentPeriods, ...leaseRepaymentPeriodCollection];
      jest.spyOn(leaseRepaymentPeriodService, 'addLeaseRepaymentPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      expect(leaseRepaymentPeriodService.query).toHaveBeenCalled();
      expect(leaseRepaymentPeriodService.addLeaseRepaymentPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        leaseRepaymentPeriodCollection,
        ...additionalLeaseRepaymentPeriods
      );
      expect(comp.leaseRepaymentPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LeaseLiabilityCompilation query and add missing value', () => {
      const leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem = { id: 456 };
      const compilation: ILeaseLiabilityCompilation = { id: 99887 };
      leaseLiabilityScheduleItem.compilation = compilation;

      const compilationCollection: ILeaseLiabilityCompilation[] = [{ id: 11223 }];
      jest.spyOn(leaseLiabilityCompilationService, 'query').mockReturnValue(of(new HttpResponse({ body: compilationCollection })));
      const additionalCompilations = [compilation];
      const expectedCollection: ILeaseLiabilityCompilation[] = [...additionalCompilations, ...compilationCollection];
      jest
        .spyOn(leaseLiabilityCompilationService, 'addLeaseLiabilityCompilationToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      expect(leaseLiabilityCompilationService.query).toHaveBeenCalled();
      expect(leaseLiabilityCompilationService.addLeaseLiabilityCompilationToCollectionIfMissing).toHaveBeenCalledWith(
        compilationCollection,
        ...additionalCompilations
      );
      expect(comp.leaseLiabilityCompilationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem = { id: 456 };
      const placeholders: IPlaceholder = { id: 79718 };
      leaseLiabilityScheduleItem.placeholders = [placeholders];
      const universallyUniqueMappings: IUniversallyUniqueMapping = { id: 69682 };
      leaseLiabilityScheduleItem.universallyUniqueMappings = [universallyUniqueMappings];
      const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 74255 };
      leaseLiabilityScheduleItem.leaseAmortizationSchedule = leaseAmortizationSchedule;
      const leaseContract: IIFRS16LeaseContract = { id: 62876 };
      leaseLiabilityScheduleItem.leaseContract = leaseContract;
      const leaseLiability: ILeaseLiability = { id: 42385 };
      leaseLiabilityScheduleItem.leaseLiability = leaseLiability;
      const leasePeriod: ILeaseRepaymentPeriod = { id: 36966 };
      leaseLiabilityScheduleItem.leasePeriod = leasePeriod;
      const compilation: ILeaseLiabilityCompilation = { id: 887766 };
      leaseLiabilityScheduleItem.compilation = compilation;
      leaseLiabilityScheduleItem.active = true;

      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(leaseLiabilityScheduleItem));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(universallyUniqueMappings);
      expect(comp.leaseAmortizationSchedulesSharedCollection).toContain(leaseAmortizationSchedule);
      expect(comp.iFRS16LeaseContractsSharedCollection).toContain(leaseContract);
      expect(comp.leaseLiabilitiesSharedCollection).toContain(leaseLiability);
      expect(comp.leaseRepaymentPeriodsSharedCollection).toContain(leasePeriod);
      expect(comp.leaseLiabilityCompilationsSharedCollection).toContain(compilation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityScheduleItem>>();
      const leaseLiabilityScheduleItem = { id: 123 };
      jest.spyOn(leaseLiabilityScheduleItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseLiabilityScheduleItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaseLiabilityScheduleItemService.update).toHaveBeenCalledWith(leaseLiabilityScheduleItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityScheduleItem>>();
      const leaseLiabilityScheduleItem = new LeaseLiabilityScheduleItem();
      jest.spyOn(leaseLiabilityScheduleItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseLiabilityScheduleItem }));
      saveSubject.complete();

      // THEN
      expect(leaseLiabilityScheduleItemService.create).toHaveBeenCalledWith(leaseLiabilityScheduleItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseLiabilityScheduleItem>>();
      const leaseLiabilityScheduleItem = { id: 123 };
      jest.spyOn(leaseLiabilityScheduleItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseLiabilityScheduleItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaseLiabilityScheduleItemService.update).toHaveBeenCalledWith(leaseLiabilityScheduleItem);
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

    describe('trackUniversallyUniqueMappingById', () => {
      it('Should return tracked UniversallyUniqueMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUniversallyUniqueMappingById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLeaseAmortizationScheduleById', () => {
      it('Should return tracked LeaseAmortizationSchedule primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLeaseAmortizationScheduleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackIFRS16LeaseContractById', () => {
      it('Should return tracked IFRS16LeaseContract primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIFRS16LeaseContractById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLeaseLiabilityById', () => {
      it('Should return tracked LeaseLiability primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLeaseLiabilityById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLeaseRepaymentPeriodById', () => {
      it('Should return tracked LeaseRepaymentPeriod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLeaseRepaymentPeriodById(0, entity);
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
