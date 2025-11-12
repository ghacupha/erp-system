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

import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NetBookValueEntryService } from '../service/net-book-value-entry.service';
import { INetBookValueEntry, NetBookValueEntry } from '../net-book-value-entry.model';

import { NetBookValueEntryUpdateComponent } from './net-book-value-entry-update.component';
import { IAssetRegistration } from '../../asset-registration/asset-registration.model';
import { IAssetCategory } from '../../asset-category/asset-category.model';
import { IFiscalMonth } from '../../../erp-pages/fiscal-month/fiscal-month.model';
import { DepreciationPeriodService } from '../../depreciation-period/service/depreciation-period.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { AssetRegistrationService } from '../../asset-registration/service/asset-registration.service';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';
import { IDepreciationMethod } from '../../depreciation-method/depreciation-method.model';
import { AssetCategoryService } from '../../asset-category/service/asset-category.service';
import { IDepreciationPeriod } from '../../depreciation-period/depreciation-period.model';
import { DepreciationMethodService } from '../../depreciation-method/service/depreciation-method.service';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';

describe('NetBookValueEntry Management Update Component', () => {
  let comp: NetBookValueEntryUpdateComponent;
  let fixture: ComponentFixture<NetBookValueEntryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let netBookValueEntryService: NetBookValueEntryService;
  let serviceOutletService: ServiceOutletService;
  let depreciationPeriodService: DepreciationPeriodService;
  let fiscalMonthService: FiscalMonthService;
  let depreciationMethodService: DepreciationMethodService;
  let assetRegistrationService: AssetRegistrationService;
  let assetCategoryService: AssetCategoryService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NetBookValueEntryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(NetBookValueEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NetBookValueEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    netBookValueEntryService = TestBed.inject(NetBookValueEntryService);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);
    fiscalMonthService = TestBed.inject(FiscalMonthService);
    depreciationMethodService = TestBed.inject(DepreciationMethodService);
    assetRegistrationService = TestBed.inject(AssetRegistrationService);
    assetCategoryService = TestBed.inject(AssetCategoryService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceOutlet query and add missing value', () => {
      const netBookValueEntry: INetBookValueEntry = { id: 456 };
      const serviceOutlet: IServiceOutlet = { id: 65777 };
      netBookValueEntry.serviceOutlet = serviceOutlet;

      const serviceOutletCollection: IServiceOutlet[] = [{ id: 7505 }];
      jest.spyOn(serviceOutletService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOutletCollection })));
      const additionalServiceOutlets = [serviceOutlet];
      const expectedCollection: IServiceOutlet[] = [...additionalServiceOutlets, ...serviceOutletCollection];
      jest.spyOn(serviceOutletService, 'addServiceOutletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      expect(serviceOutletService.query).toHaveBeenCalled();
      expect(serviceOutletService.addServiceOutletToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOutletCollection,
        ...additionalServiceOutlets
      );
      expect(comp.serviceOutletsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationPeriod query and add missing value', () => {
      const netBookValueEntry: INetBookValueEntry = { id: 456 };
      const depreciationPeriod: IDepreciationPeriod = { id: 62339 };
      netBookValueEntry.depreciationPeriod = depreciationPeriod;

      const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 64283 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationPeriodCollection })));
      const additionalDepreciationPeriods = [depreciationPeriod];
      const expectedCollection: IDepreciationPeriod[] = [...additionalDepreciationPeriods, ...depreciationPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationPeriodCollection,
        ...additionalDepreciationPeriods
      );
      expect(comp.depreciationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiscalMonth query and add missing value', () => {
      const netBookValueEntry: INetBookValueEntry = { id: 456 };
      const fiscalMonth: IFiscalMonth = { id: 8466 };
      netBookValueEntry.fiscalMonth = fiscalMonth;

      const fiscalMonthCollection: IFiscalMonth[] = [{ id: 26992 }];
      jest.spyOn(fiscalMonthService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalMonthCollection })));
      const additionalFiscalMonths = [fiscalMonth];
      const expectedCollection: IFiscalMonth[] = [...additionalFiscalMonths, ...fiscalMonthCollection];
      jest.spyOn(fiscalMonthService, 'addFiscalMonthToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      expect(fiscalMonthService.query).toHaveBeenCalled();
      expect(fiscalMonthService.addFiscalMonthToCollectionIfMissing).toHaveBeenCalledWith(fiscalMonthCollection, ...additionalFiscalMonths);
      expect(comp.fiscalMonthsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationMethod query and add missing value', () => {
      const netBookValueEntry: INetBookValueEntry = { id: 456 };
      const depreciationMethod: IDepreciationMethod = { id: 35901 };
      netBookValueEntry.depreciationMethod = depreciationMethod;

      const depreciationMethodCollection: IDepreciationMethod[] = [{ id: 62252 }];
      jest.spyOn(depreciationMethodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationMethodCollection })));
      const additionalDepreciationMethods = [depreciationMethod];
      const expectedCollection: IDepreciationMethod[] = [...additionalDepreciationMethods, ...depreciationMethodCollection];
      jest.spyOn(depreciationMethodService, 'addDepreciationMethodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      expect(depreciationMethodService.query).toHaveBeenCalled();
      expect(depreciationMethodService.addDepreciationMethodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationMethodCollection,
        ...additionalDepreciationMethods
      );
      expect(comp.depreciationMethodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetRegistration query and add missing value', () => {
      const netBookValueEntry: INetBookValueEntry = { id: 456 };
      const assetRegistration: IAssetRegistration = { id: 3865 };
      netBookValueEntry.assetRegistration = assetRegistration;

      const assetRegistrationCollection: IAssetRegistration[] = [{ id: 91529 }];
      jest.spyOn(assetRegistrationService, 'query').mockReturnValue(of(new HttpResponse({ body: assetRegistrationCollection })));
      const additionalAssetRegistrations = [assetRegistration];
      const expectedCollection: IAssetRegistration[] = [...additionalAssetRegistrations, ...assetRegistrationCollection];
      jest.spyOn(assetRegistrationService, 'addAssetRegistrationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      expect(assetRegistrationService.query).toHaveBeenCalled();
      expect(assetRegistrationService.addAssetRegistrationToCollectionIfMissing).toHaveBeenCalledWith(
        assetRegistrationCollection,
        ...additionalAssetRegistrations
      );
      expect(comp.assetRegistrationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetCategory query and add missing value', () => {
      const netBookValueEntry: INetBookValueEntry = { id: 456 };
      const assetCategory: IAssetCategory = { id: 15999 };
      netBookValueEntry.assetCategory = assetCategory;

      const assetCategoryCollection: IAssetCategory[] = [{ id: 86100 }];
      jest.spyOn(assetCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCategoryCollection })));
      const additionalAssetCategories = [assetCategory];
      const expectedCollection: IAssetCategory[] = [...additionalAssetCategories, ...assetCategoryCollection];
      jest.spyOn(assetCategoryService, 'addAssetCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      expect(assetCategoryService.query).toHaveBeenCalled();
      expect(assetCategoryService.addAssetCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        assetCategoryCollection,
        ...additionalAssetCategories
      );
      expect(comp.assetCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const netBookValueEntry: INetBookValueEntry = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 83955 }];
      netBookValueEntry.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 57449 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const netBookValueEntry: INetBookValueEntry = { id: 456 };
      const serviceOutlet: IServiceOutlet = { id: 1048 };
      netBookValueEntry.serviceOutlet = serviceOutlet;
      const depreciationPeriod: IDepreciationPeriod = { id: 59521 };
      netBookValueEntry.depreciationPeriod = depreciationPeriod;
      const fiscalMonth: IFiscalMonth = { id: 56931 };
      netBookValueEntry.fiscalMonth = fiscalMonth;
      const depreciationMethod: IDepreciationMethod = { id: 95641 };
      netBookValueEntry.depreciationMethod = depreciationMethod;
      const assetRegistration: IAssetRegistration = { id: 92522 };
      netBookValueEntry.assetRegistration = assetRegistration;
      const assetCategory: IAssetCategory = { id: 71290 };
      netBookValueEntry.assetCategory = assetCategory;
      const placeholders: IPlaceholder = { id: 27386 };
      netBookValueEntry.placeholders = [placeholders];

      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(netBookValueEntry));
      expect(comp.serviceOutletsSharedCollection).toContain(serviceOutlet);
      expect(comp.depreciationPeriodsSharedCollection).toContain(depreciationPeriod);
      expect(comp.fiscalMonthsSharedCollection).toContain(fiscalMonth);
      expect(comp.depreciationMethodsSharedCollection).toContain(depreciationMethod);
      expect(comp.assetRegistrationsSharedCollection).toContain(assetRegistration);
      expect(comp.assetCategoriesSharedCollection).toContain(assetCategory);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NetBookValueEntry>>();
      const netBookValueEntry = { id: 123 };
      jest.spyOn(netBookValueEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: netBookValueEntry }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(netBookValueEntryService.update).toHaveBeenCalledWith(netBookValueEntry);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NetBookValueEntry>>();
      const netBookValueEntry = new NetBookValueEntry();
      jest.spyOn(netBookValueEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: netBookValueEntry }));
      saveSubject.complete();

      // THEN
      expect(netBookValueEntryService.create).toHaveBeenCalledWith(netBookValueEntry);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NetBookValueEntry>>();
      const netBookValueEntry = { id: 123 };
      jest.spyOn(netBookValueEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ netBookValueEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(netBookValueEntryService.update).toHaveBeenCalledWith(netBookValueEntry);
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

    describe('trackAssetCategoryById', () => {
      it('Should return tracked AssetCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssetCategoryById(0, entity);
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
