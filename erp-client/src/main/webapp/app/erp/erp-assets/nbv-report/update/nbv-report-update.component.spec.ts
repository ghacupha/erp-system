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

import { IAssetCategory } from '../../asset-category/asset-category.model';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NbvReportService } from '../service/nbv-report.service';
import { INbvReport, NbvReport } from '../nbv-report.model';

import { NbvReportUpdateComponent } from './nbv-report-update.component';
import { DepreciationPeriodService } from '../../depreciation-period/service/depreciation-period.service';
import { AssetCategoryService } from '../../asset-category/service/asset-category.service';
import { IDepreciationPeriod } from '../../depreciation-period/depreciation-period.model';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';

describe('NbvReport Management Update Component', () => {
  let comp: NbvReportUpdateComponent;
  let fixture: ComponentFixture<NbvReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let nbvReportService: NbvReportService;
  let applicationUserService: ApplicationUserService;
  let depreciationPeriodService: DepreciationPeriodService;
  let serviceOutletService: ServiceOutletService;
  let assetCategoryService: AssetCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NbvReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(NbvReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NbvReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nbvReportService = TestBed.inject(NbvReportService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    assetCategoryService = TestBed.inject(AssetCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const nbvReport: INbvReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 61259 };
      nbvReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 75060 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nbvReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationPeriod query and add missing value', () => {
      const nbvReport: INbvReport = { id: 456 };
      const depreciationPeriod: IDepreciationPeriod = { id: 22124 };
      nbvReport.depreciationPeriod = depreciationPeriod;

      const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 49797 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationPeriodCollection })));
      const additionalDepreciationPeriods = [depreciationPeriod];
      const expectedCollection: IDepreciationPeriod[] = [...additionalDepreciationPeriods, ...depreciationPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nbvReport });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationPeriodCollection,
        ...additionalDepreciationPeriods
      );
      expect(comp.depreciationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceOutlet query and add missing value', () => {
      const nbvReport: INbvReport = { id: 456 };
      const serviceOutlet: IServiceOutlet = { id: 16603 };
      nbvReport.serviceOutlet = serviceOutlet;

      const serviceOutletCollection: IServiceOutlet[] = [{ id: 37271 }];
      jest.spyOn(serviceOutletService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOutletCollection })));
      const additionalServiceOutlets = [serviceOutlet];
      const expectedCollection: IServiceOutlet[] = [...additionalServiceOutlets, ...serviceOutletCollection];
      jest.spyOn(serviceOutletService, 'addServiceOutletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nbvReport });
      comp.ngOnInit();

      expect(serviceOutletService.query).toHaveBeenCalled();
      expect(serviceOutletService.addServiceOutletToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOutletCollection,
        ...additionalServiceOutlets
      );
      expect(comp.serviceOutletsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetCategory query and add missing value', () => {
      const nbvReport: INbvReport = { id: 456 };
      const assetCategory: IAssetCategory = { id: 1509 };
      nbvReport.assetCategory = assetCategory;

      const assetCategoryCollection: IAssetCategory[] = [{ id: 55599 }];
      jest.spyOn(assetCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCategoryCollection })));
      const additionalAssetCategories = [assetCategory];
      const expectedCollection: IAssetCategory[] = [...additionalAssetCategories, ...assetCategoryCollection];
      jest.spyOn(assetCategoryService, 'addAssetCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nbvReport });
      comp.ngOnInit();

      expect(assetCategoryService.query).toHaveBeenCalled();
      expect(assetCategoryService.addAssetCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        assetCategoryCollection,
        ...additionalAssetCategories
      );
      expect(comp.assetCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const nbvReport: INbvReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 91751 };
      nbvReport.requestedBy = requestedBy;
      const depreciationPeriod: IDepreciationPeriod = { id: 87076 };
      nbvReport.depreciationPeriod = depreciationPeriod;
      const serviceOutlet: IServiceOutlet = { id: 11351 };
      nbvReport.serviceOutlet = serviceOutlet;
      const assetCategory: IAssetCategory = { id: 71273 };
      nbvReport.assetCategory = assetCategory;

      activatedRoute.data = of({ nbvReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(nbvReport));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
      expect(comp.depreciationPeriodsSharedCollection).toContain(depreciationPeriod);
      expect(comp.serviceOutletsSharedCollection).toContain(serviceOutlet);
      expect(comp.assetCategoriesSharedCollection).toContain(assetCategory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NbvReport>>();
      const nbvReport = { id: 123 };
      jest.spyOn(nbvReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nbvReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nbvReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(nbvReportService.update).toHaveBeenCalledWith(nbvReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NbvReport>>();
      const nbvReport = new NbvReport();
      jest.spyOn(nbvReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nbvReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nbvReport }));
      saveSubject.complete();

      // THEN
      expect(nbvReportService.create).toHaveBeenCalledWith(nbvReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NbvReport>>();
      const nbvReport = { id: 123 };
      jest.spyOn(nbvReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nbvReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nbvReportService.update).toHaveBeenCalledWith(nbvReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
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
  });
});
