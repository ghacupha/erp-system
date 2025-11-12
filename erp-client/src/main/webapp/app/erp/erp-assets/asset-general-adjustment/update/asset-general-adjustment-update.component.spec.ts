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

import { IAssetRegistration } from '../../asset-registration/asset-registration.model';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AssetGeneralAdjustmentService } from '../service/asset-general-adjustment.service';
import { IAssetGeneralAdjustment, AssetGeneralAdjustment } from '../asset-general-adjustment.model';

import { AssetGeneralAdjustmentUpdateComponent } from './asset-general-adjustment-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { AssetRegistrationService } from '../../asset-registration/service/asset-registration.service';
import { DepreciationPeriodService } from '../../depreciation-period/service/depreciation-period.service';
import { IDepreciationPeriod } from '../../depreciation-period/depreciation-period.model';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

describe('AssetGeneralAdjustment Management Update Component', () => {
  let comp: AssetGeneralAdjustmentUpdateComponent;
  let fixture: ComponentFixture<AssetGeneralAdjustmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetGeneralAdjustmentService: AssetGeneralAdjustmentService;
  let depreciationPeriodService: DepreciationPeriodService;
  let assetRegistrationService: AssetRegistrationService;
  let applicationUserService: ApplicationUserService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AssetGeneralAdjustmentUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AssetGeneralAdjustmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetGeneralAdjustmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetGeneralAdjustmentService = TestBed.inject(AssetGeneralAdjustmentService);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);
    assetRegistrationService = TestBed.inject(AssetRegistrationService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DepreciationPeriod query and add missing value', () => {
      const assetGeneralAdjustment: IAssetGeneralAdjustment = { id: 456 };
      const effectivePeriod: IDepreciationPeriod = { id: 85767 };
      assetGeneralAdjustment.effectivePeriod = effectivePeriod;

      const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 98603 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationPeriodCollection })));
      const additionalDepreciationPeriods = [effectivePeriod];
      const expectedCollection: IDepreciationPeriod[] = [...additionalDepreciationPeriods, ...depreciationPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetGeneralAdjustment });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationPeriodCollection,
        ...additionalDepreciationPeriods
      );
      expect(comp.depreciationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetRegistration query and add missing value', () => {
      const assetGeneralAdjustment: IAssetGeneralAdjustment = { id: 456 };
      const assetRegistration: IAssetRegistration = { id: 94873 };
      assetGeneralAdjustment.assetRegistration = assetRegistration;

      const assetRegistrationCollection: IAssetRegistration[] = [{ id: 83436 }];
      jest.spyOn(assetRegistrationService, 'query').mockReturnValue(of(new HttpResponse({ body: assetRegistrationCollection })));
      const additionalAssetRegistrations = [assetRegistration];
      const expectedCollection: IAssetRegistration[] = [...additionalAssetRegistrations, ...assetRegistrationCollection];
      jest.spyOn(assetRegistrationService, 'addAssetRegistrationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetGeneralAdjustment });
      comp.ngOnInit();

      expect(assetRegistrationService.query).toHaveBeenCalled();
      expect(assetRegistrationService.addAssetRegistrationToCollectionIfMissing).toHaveBeenCalledWith(
        assetRegistrationCollection,
        ...additionalAssetRegistrations
      );
      expect(comp.assetRegistrationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const assetGeneralAdjustment: IAssetGeneralAdjustment = { id: 456 };
      const createdBy: IApplicationUser = { id: 27335 };
      assetGeneralAdjustment.createdBy = createdBy;
      const lastModifiedBy: IApplicationUser = { id: 50360 };
      assetGeneralAdjustment.lastModifiedBy = lastModifiedBy;
      const lastAccessedBy: IApplicationUser = { id: 52036 };
      assetGeneralAdjustment.lastAccessedBy = lastAccessedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 93723 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [createdBy, lastModifiedBy, lastAccessedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetGeneralAdjustment });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const assetGeneralAdjustment: IAssetGeneralAdjustment = { id: 456 };
      const placeholder: IPlaceholder = { id: 63115 };
      assetGeneralAdjustment.placeholder = placeholder;

      const placeholderCollection: IPlaceholder[] = [{ id: 6822 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [placeholder];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetGeneralAdjustment });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assetGeneralAdjustment: IAssetGeneralAdjustment = { id: 456 };
      const effectivePeriod: IDepreciationPeriod = { id: 51952 };
      assetGeneralAdjustment.effectivePeriod = effectivePeriod;
      const assetRegistration: IAssetRegistration = { id: 54666 };
      assetGeneralAdjustment.assetRegistration = assetRegistration;
      const createdBy: IApplicationUser = { id: 62991 };
      assetGeneralAdjustment.createdBy = createdBy;
      const lastModifiedBy: IApplicationUser = { id: 62703 };
      assetGeneralAdjustment.lastModifiedBy = lastModifiedBy;
      const lastAccessedBy: IApplicationUser = { id: 28218 };
      assetGeneralAdjustment.lastAccessedBy = lastAccessedBy;
      const placeholder: IPlaceholder = { id: 28444 };
      assetGeneralAdjustment.placeholder = placeholder;

      activatedRoute.data = of({ assetGeneralAdjustment });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetGeneralAdjustment));
      expect(comp.depreciationPeriodsSharedCollection).toContain(effectivePeriod);
      expect(comp.assetRegistrationsSharedCollection).toContain(assetRegistration);
      expect(comp.applicationUsersSharedCollection).toContain(createdBy);
      expect(comp.applicationUsersSharedCollection).toContain(lastModifiedBy);
      expect(comp.applicationUsersSharedCollection).toContain(lastAccessedBy);
      expect(comp.placeholdersSharedCollection).toContain(placeholder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetGeneralAdjustment>>();
      const assetGeneralAdjustment = { id: 123 };
      jest.spyOn(assetGeneralAdjustmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetGeneralAdjustment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetGeneralAdjustment }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetGeneralAdjustmentService.update).toHaveBeenCalledWith(assetGeneralAdjustment);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetGeneralAdjustment>>();
      const assetGeneralAdjustment = new AssetGeneralAdjustment();
      jest.spyOn(assetGeneralAdjustmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetGeneralAdjustment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetGeneralAdjustment }));
      saveSubject.complete();

      // THEN
      expect(assetGeneralAdjustmentService.create).toHaveBeenCalledWith(assetGeneralAdjustment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetGeneralAdjustment>>();
      const assetGeneralAdjustment = { id: 123 };
      jest.spyOn(assetGeneralAdjustmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetGeneralAdjustment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetGeneralAdjustmentService.update).toHaveBeenCalledWith(assetGeneralAdjustment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDepreciationPeriodById', () => {
      it('Should return tracked DepreciationPeriod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepreciationPeriodById(0, entity);
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

    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
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
});
