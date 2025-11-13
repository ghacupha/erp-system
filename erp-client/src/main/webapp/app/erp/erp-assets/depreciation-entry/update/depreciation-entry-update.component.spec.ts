///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { IAssetCategory } from '../../asset-category/asset-category.model';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DepreciationEntryService } from '../service/depreciation-entry.service';
import { IDepreciationEntry, DepreciationEntry } from '../depreciation-entry.model';

import { DepreciationEntryUpdateComponent } from './depreciation-entry-update.component';
import { IAssetRegistration } from '../../asset-registration/asset-registration.model';
import { IFiscalMonth } from '../../../erp-pages/fiscal-month/fiscal-month.model';
import { DepreciationPeriodService } from '../../depreciation-period/service/depreciation-period.service';
import { FiscalYearService } from '../../../erp-pages/fiscal-year/service/fiscal-year.service';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { IFiscalYear } from '../../../erp-pages/fiscal-year/fiscal-year.model';
import { IFiscalQuarter } from '../../../erp-pages/fiscal-quarter/fiscal-quarter.model';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { AssetRegistrationService } from '../../asset-registration/service/asset-registration.service';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';
import { IDepreciationMethod } from '../../depreciation-method/depreciation-method.model';
import { AssetCategoryService } from '../../asset-category/service/asset-category.service';
import { IDepreciationPeriod } from '../../depreciation-period/depreciation-period.model';
import { FiscalQuarterService } from '../../../erp-pages/fiscal-quarter/service/fiscal-quarter.service';
import { DepreciationMethodService } from '../../depreciation-method/service/depreciation-method.service';
import { DepreciationJobService } from '../../depreciation-job/service/depreciation-job.service';
import { IDepreciationBatchSequence } from '../../depreciation-batch-sequence/depreciation-batch-sequence.model';
import { IDepreciationJob } from '../../depreciation-job/depreciation-job.model';
import { DepreciationBatchSequenceService } from '../../depreciation-batch-sequence/service/depreciation-batch-sequence.service';

describe('DepreciationEntry Management Update Component', () => {
  let comp: DepreciationEntryUpdateComponent, fixture: ComponentFixture<DepreciationEntryUpdateComponent>,
    activatedRoute: ActivatedRoute, depreciationEntryService: DepreciationEntryService,
    serviceOutletService: ServiceOutletService, assetCategoryService: AssetCategoryService,
    depreciationMethodService: DepreciationMethodService, assetRegistrationService: AssetRegistrationService,
    depreciationPeriodService: DepreciationPeriodService, fiscalMonthService: FiscalMonthService,
    fiscalQuarterService: FiscalQuarterService, fiscalYearService: FiscalYearService,
    depreciationJobService: DepreciationJobService, depreciationBatchSequenceService: DepreciationBatchSequenceService;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DepreciationEntryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DepreciationEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepreciationEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    depreciationEntryService = TestBed.inject(DepreciationEntryService);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    assetCategoryService = TestBed.inject(AssetCategoryService);
    depreciationMethodService = TestBed.inject(DepreciationMethodService);
    assetRegistrationService = TestBed.inject(AssetRegistrationService);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);
    fiscalMonthService = TestBed.inject(FiscalMonthService);
    fiscalQuarterService = TestBed.inject(FiscalQuarterService);
    fiscalYearService = TestBed.inject(FiscalYearService);
    depreciationJobService = TestBed.inject(DepreciationJobService);
    depreciationBatchSequenceService = TestBed.inject(DepreciationBatchSequenceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceOutlet query and add missing value', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const serviceOutlet: IServiceOutlet = { id: 21191 };
      depreciationEntry.serviceOutlet = serviceOutlet;

      const serviceOutletCollection: IServiceOutlet[] = [{ id: 55202 }];
      jest.spyOn(serviceOutletService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOutletCollection })));
      const additionalServiceOutlets = [serviceOutlet];
      const expectedCollection: IServiceOutlet[] = [...additionalServiceOutlets, ...serviceOutletCollection];
      jest.spyOn(serviceOutletService, 'addServiceOutletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(serviceOutletService.query).toHaveBeenCalled();
      expect(serviceOutletService.addServiceOutletToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOutletCollection,
        ...additionalServiceOutlets
      );
      expect(comp.serviceOutletsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetCategory query and add missing value', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const assetCategory: IAssetCategory = { id: 51749 };
      depreciationEntry.assetCategory = assetCategory;

      const assetCategoryCollection: IAssetCategory[] = [{ id: 33276 }];
      jest.spyOn(assetCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCategoryCollection })));
      const additionalAssetCategories = [assetCategory];
      const expectedCollection: IAssetCategory[] = [...additionalAssetCategories, ...assetCategoryCollection];
      jest.spyOn(assetCategoryService, 'addAssetCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(assetCategoryService.query).toHaveBeenCalled();
      expect(assetCategoryService.addAssetCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        assetCategoryCollection,
        ...additionalAssetCategories
      );
      expect(comp.assetCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationMethod query and add missing value', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const depreciationMethod: IDepreciationMethod = { id: 44142 };
      depreciationEntry.depreciationMethod = depreciationMethod;

      const depreciationMethodCollection: IDepreciationMethod[] = [{ id: 36499 }];
      jest.spyOn(depreciationMethodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationMethodCollection })));
      const additionalDepreciationMethods = [depreciationMethod];
      const expectedCollection: IDepreciationMethod[] = [...additionalDepreciationMethods, ...depreciationMethodCollection];
      jest.spyOn(depreciationMethodService, 'addDepreciationMethodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(depreciationMethodService.query).toHaveBeenCalled();
      expect(depreciationMethodService.addDepreciationMethodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationMethodCollection,
        ...additionalDepreciationMethods
      );
      expect(comp.depreciationMethodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetRegistration query and add missing value', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const assetRegistration: IAssetRegistration = { id: 63740 };
      depreciationEntry.assetRegistration = assetRegistration;

      const assetRegistrationCollection: IAssetRegistration[] = [{ id: 76323 }];
      jest.spyOn(assetRegistrationService, 'query').mockReturnValue(of(new HttpResponse({ body: assetRegistrationCollection })));
      const additionalAssetRegistrations = [assetRegistration];
      const expectedCollection: IAssetRegistration[] = [...additionalAssetRegistrations, ...assetRegistrationCollection];
      jest.spyOn(assetRegistrationService, 'addAssetRegistrationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(assetRegistrationService.query).toHaveBeenCalled();
      expect(assetRegistrationService.addAssetRegistrationToCollectionIfMissing).toHaveBeenCalledWith(
        assetRegistrationCollection,
        ...additionalAssetRegistrations
      );
      expect(comp.assetRegistrationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationPeriod query and add missing value', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const depreciationPeriod: IDepreciationPeriod = { id: 90040 };
      depreciationEntry.depreciationPeriod = depreciationPeriod;

      const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 44998 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationPeriodCollection })));
      const additionalDepreciationPeriods = [depreciationPeriod];
      const expectedCollection: IDepreciationPeriod[] = [...additionalDepreciationPeriods, ...depreciationPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationPeriodCollection,
        ...additionalDepreciationPeriods
      );
      expect(comp.depreciationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiscalMonth query and add missing value', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const fiscalMonth: IFiscalMonth = { id: 6064 };
      depreciationEntry.fiscalMonth = fiscalMonth;

      const fiscalMonthCollection: IFiscalMonth[] = [{ id: 95496 }];
      jest.spyOn(fiscalMonthService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalMonthCollection })));
      const additionalFiscalMonths = [fiscalMonth];
      const expectedCollection: IFiscalMonth[] = [...additionalFiscalMonths, ...fiscalMonthCollection];
      jest.spyOn(fiscalMonthService, 'addFiscalMonthToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(fiscalMonthService.query).toHaveBeenCalled();
      expect(fiscalMonthService.addFiscalMonthToCollectionIfMissing).toHaveBeenCalledWith(fiscalMonthCollection, ...additionalFiscalMonths);
      expect(comp.fiscalMonthsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiscalQuarter query and add missing value', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const fiscalQuarter: IFiscalQuarter = { id: 96846 };
      depreciationEntry.fiscalQuarter = fiscalQuarter;

      const fiscalQuarterCollection: IFiscalQuarter[] = [{ id: 1999 }];
      jest.spyOn(fiscalQuarterService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalQuarterCollection })));
      const additionalFiscalQuarters = [fiscalQuarter];
      const expectedCollection: IFiscalQuarter[] = [...additionalFiscalQuarters, ...fiscalQuarterCollection];
      jest.spyOn(fiscalQuarterService, 'addFiscalQuarterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(fiscalQuarterService.query).toHaveBeenCalled();
      expect(fiscalQuarterService.addFiscalQuarterToCollectionIfMissing).toHaveBeenCalledWith(
        fiscalQuarterCollection,
        ...additionalFiscalQuarters
      );
      expect(comp.fiscalQuartersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiscalYear query and add missing value', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const fiscalYear: IFiscalYear = { id: 97911 };
      depreciationEntry.fiscalYear = fiscalYear;

      const fiscalYearCollection: IFiscalYear[] = [{ id: 17190 }];
      jest.spyOn(fiscalYearService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalYearCollection })));
      const additionalFiscalYears = [fiscalYear];
      const expectedCollection: IFiscalYear[] = [...additionalFiscalYears, ...fiscalYearCollection];
      jest.spyOn(fiscalYearService, 'addFiscalYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(fiscalYearService.query).toHaveBeenCalled();
      expect(fiscalYearService.addFiscalYearToCollectionIfMissing).toHaveBeenCalledWith(fiscalYearCollection, ...additionalFiscalYears);
      expect(comp.fiscalYearsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationJob query and add missing value', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const depreciationJob: IDepreciationJob = { id: 13222 };
      depreciationEntry.depreciationJob = depreciationJob;

      const depreciationJobCollection: IDepreciationJob[] = [{ id: 77330 }];
      jest.spyOn(depreciationJobService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationJobCollection })));
      const additionalDepreciationJobs = [depreciationJob];
      const expectedCollection: IDepreciationJob[] = [...additionalDepreciationJobs, ...depreciationJobCollection];
      jest.spyOn(depreciationJobService, 'addDepreciationJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(depreciationJobService.query).toHaveBeenCalled();
      expect(depreciationJobService.addDepreciationJobToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationJobCollection,
        ...additionalDepreciationJobs
      );
      expect(comp.depreciationJobsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationBatchSequence query and add missing value', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const depreciationBatchSequence: IDepreciationBatchSequence = { id: 46370 };
      depreciationEntry.depreciationBatchSequence = depreciationBatchSequence;

      const depreciationBatchSequenceCollection: IDepreciationBatchSequence[] = [{ id: 32758 }];
      jest
        .spyOn(depreciationBatchSequenceService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: depreciationBatchSequenceCollection })));
      const additionalDepreciationBatchSequences = [depreciationBatchSequence];
      const expectedCollection: IDepreciationBatchSequence[] = [
        ...additionalDepreciationBatchSequences,
        ...depreciationBatchSequenceCollection,
      ];
      jest.spyOn(depreciationBatchSequenceService, 'addDepreciationBatchSequenceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(depreciationBatchSequenceService.query).toHaveBeenCalled();
      expect(depreciationBatchSequenceService.addDepreciationBatchSequenceToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationBatchSequenceCollection,
        ...additionalDepreciationBatchSequences
      );
      expect(comp.depreciationBatchSequencesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const depreciationEntry: IDepreciationEntry = { id: 456 };
      const serviceOutlet: IServiceOutlet = { id: 27372 };
      depreciationEntry.serviceOutlet = serviceOutlet;
      const assetCategory: IAssetCategory = { id: 71584 };
      depreciationEntry.assetCategory = assetCategory;
      const depreciationMethod: IDepreciationMethod = { id: 97895 };
      depreciationEntry.depreciationMethod = depreciationMethod;
      const assetRegistration: IAssetRegistration = { id: 77189 };
      depreciationEntry.assetRegistration = assetRegistration;
      const depreciationPeriod: IDepreciationPeriod = { id: 21186 };
      depreciationEntry.depreciationPeriod = depreciationPeriod;
      const fiscalMonth: IFiscalMonth = { id: 25241 };
      depreciationEntry.fiscalMonth = fiscalMonth;
      const fiscalQuarter: IFiscalQuarter = { id: 47326 };
      depreciationEntry.fiscalQuarter = fiscalQuarter;
      const fiscalYear: IFiscalYear = { id: 14983 };
      depreciationEntry.fiscalYear = fiscalYear;
      const depreciationJob: IDepreciationJob = { id: 29177 };
      depreciationEntry.depreciationJob = depreciationJob;
      const depreciationBatchSequence: IDepreciationBatchSequence = { id: 21118 };
      depreciationEntry.depreciationBatchSequence = depreciationBatchSequence;

      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(depreciationEntry));
      expect(comp.serviceOutletsSharedCollection).toContain(serviceOutlet);
      expect(comp.assetCategoriesSharedCollection).toContain(assetCategory);
      expect(comp.depreciationMethodsSharedCollection).toContain(depreciationMethod);
      expect(comp.assetRegistrationsSharedCollection).toContain(assetRegistration);
      expect(comp.depreciationPeriodsSharedCollection).toContain(depreciationPeriod);
      expect(comp.fiscalMonthsSharedCollection).toContain(fiscalMonth);
      expect(comp.fiscalQuartersSharedCollection).toContain(fiscalQuarter);
      expect(comp.fiscalYearsSharedCollection).toContain(fiscalYear);
      expect(comp.depreciationJobsSharedCollection).toContain(depreciationJob);
      expect(comp.depreciationBatchSequencesSharedCollection).toContain(depreciationBatchSequence);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationEntry>>();
      const depreciationEntry = { id: 123 };
      jest.spyOn(depreciationEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depreciationEntry }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(depreciationEntryService.update).toHaveBeenCalledWith(depreciationEntry);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationEntry>>();
      const depreciationEntry = new DepreciationEntry();
      jest.spyOn(depreciationEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depreciationEntry }));
      saveSubject.complete();

      // THEN
      expect(depreciationEntryService.create).toHaveBeenCalledWith(depreciationEntry);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DepreciationEntry>>();
      const depreciationEntry = { id: 123 };
      jest.spyOn(depreciationEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depreciationEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(depreciationEntryService.update).toHaveBeenCalledWith(depreciationEntry);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackServiceOutletById', () => {
      it('Should return tracked ServiceOutlet primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackServiceOutletById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAssetCategoryById', () => {
      it('Should return tracked AssetCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssetCategoryById(0, entity);
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

    describe('trackAssetRegistrationById', () => {
      it('Should return tracked AssetRegistration primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssetRegistrationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDepreciationPeriodById', () => {
      it('Should return tracked DepreciationPeriod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepreciationPeriodById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFiscalMonthById', () => {
      it('Should return tracked FiscalMonth primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalMonthById(0, entity);
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

    describe('trackFiscalYearById', () => {
      it('Should return tracked FiscalYear primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalYearById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDepreciationJobById', () => {
      it('Should return tracked DepreciationJob primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepreciationJobById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDepreciationBatchSequenceById', () => {
      it('Should return tracked DepreciationBatchSequence primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepreciationBatchSequenceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
