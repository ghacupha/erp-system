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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AssetRevaluationService } from '../service/asset-revaluation.service';
import { IAssetRevaluation, AssetRevaluation } from '../asset-revaluation.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { DealerService } from 'app/entities/people/dealer/service/dealer.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';
import { AssetRegistrationService } from 'app/entities/assets/asset-registration/service/asset-registration.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

import { AssetRevaluationUpdateComponent } from './asset-revaluation-update.component';

describe('AssetRevaluation Management Update Component', () => {
  let comp: AssetRevaluationUpdateComponent;
  let fixture: ComponentFixture<AssetRevaluationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetRevaluationService: AssetRevaluationService;
  let dealerService: DealerService;
  let applicationUserService: ApplicationUserService;
  let depreciationPeriodService: DepreciationPeriodService;
  let assetRegistrationService: AssetRegistrationService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AssetRevaluationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AssetRevaluationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetRevaluationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetRevaluationService = TestBed.inject(AssetRevaluationService);
    dealerService = TestBed.inject(DealerService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);
    assetRegistrationService = TestBed.inject(AssetRegistrationService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dealer query and add missing value', () => {
      const assetRevaluation: IAssetRevaluation = { id: 456 };
      const revaluer: IDealer = { id: 56693 };
      assetRevaluation.revaluer = revaluer;

      const dealerCollection: IDealer[] = [{ id: 71950 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [revaluer];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetRevaluation });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const assetRevaluation: IAssetRevaluation = { id: 456 };
      const createdBy: IApplicationUser = { id: 57302 };
      assetRevaluation.createdBy = createdBy;
      const lastModifiedBy: IApplicationUser = { id: 39433 };
      assetRevaluation.lastModifiedBy = lastModifiedBy;
      const lastAccessedBy: IApplicationUser = { id: 90556 };
      assetRevaluation.lastAccessedBy = lastAccessedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 48355 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [createdBy, lastModifiedBy, lastAccessedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetRevaluation });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationPeriod query and add missing value', () => {
      const assetRevaluation: IAssetRevaluation = { id: 456 };
      const effectivePeriod: IDepreciationPeriod = { id: 3868 };
      assetRevaluation.effectivePeriod = effectivePeriod;

      const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 17677 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationPeriodCollection })));
      const additionalDepreciationPeriods = [effectivePeriod];
      const expectedCollection: IDepreciationPeriod[] = [...additionalDepreciationPeriods, ...depreciationPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetRevaluation });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationPeriodCollection,
        ...additionalDepreciationPeriods
      );
      expect(comp.depreciationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetRegistration query and add missing value', () => {
      const assetRevaluation: IAssetRevaluation = { id: 456 };
      const revaluedAsset: IAssetRegistration = { id: 29128 };
      assetRevaluation.revaluedAsset = revaluedAsset;

      const assetRegistrationCollection: IAssetRegistration[] = [{ id: 96364 }];
      jest.spyOn(assetRegistrationService, 'query').mockReturnValue(of(new HttpResponse({ body: assetRegistrationCollection })));
      const additionalAssetRegistrations = [revaluedAsset];
      const expectedCollection: IAssetRegistration[] = [...additionalAssetRegistrations, ...assetRegistrationCollection];
      jest.spyOn(assetRegistrationService, 'addAssetRegistrationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetRevaluation });
      comp.ngOnInit();

      expect(assetRegistrationService.query).toHaveBeenCalled();
      expect(assetRegistrationService.addAssetRegistrationToCollectionIfMissing).toHaveBeenCalledWith(
        assetRegistrationCollection,
        ...additionalAssetRegistrations
      );
      expect(comp.assetRegistrationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const assetRevaluation: IAssetRevaluation = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 63528 }];
      assetRevaluation.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 4773 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetRevaluation });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assetRevaluation: IAssetRevaluation = { id: 456 };
      const revaluer: IDealer = { id: 63311 };
      assetRevaluation.revaluer = revaluer;
      const createdBy: IApplicationUser = { id: 17732 };
      assetRevaluation.createdBy = createdBy;
      const lastModifiedBy: IApplicationUser = { id: 91364 };
      assetRevaluation.lastModifiedBy = lastModifiedBy;
      const lastAccessedBy: IApplicationUser = { id: 91868 };
      assetRevaluation.lastAccessedBy = lastAccessedBy;
      const effectivePeriod: IDepreciationPeriod = { id: 46487 };
      assetRevaluation.effectivePeriod = effectivePeriod;
      const revaluedAsset: IAssetRegistration = { id: 54307 };
      assetRevaluation.revaluedAsset = revaluedAsset;
      const placeholders: IPlaceholder = { id: 38262 };
      assetRevaluation.placeholders = [placeholders];

      activatedRoute.data = of({ assetRevaluation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetRevaluation));
      expect(comp.dealersSharedCollection).toContain(revaluer);
      expect(comp.applicationUsersSharedCollection).toContain(createdBy);
      expect(comp.applicationUsersSharedCollection).toContain(lastModifiedBy);
      expect(comp.applicationUsersSharedCollection).toContain(lastAccessedBy);
      expect(comp.depreciationPeriodsSharedCollection).toContain(effectivePeriod);
      expect(comp.assetRegistrationsSharedCollection).toContain(revaluedAsset);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetRevaluation>>();
      const assetRevaluation = { id: 123 };
      jest.spyOn(assetRevaluationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetRevaluation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetRevaluation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetRevaluationService.update).toHaveBeenCalledWith(assetRevaluation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetRevaluation>>();
      const assetRevaluation = new AssetRevaluation();
      jest.spyOn(assetRevaluationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetRevaluation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetRevaluation }));
      saveSubject.complete();

      // THEN
      expect(assetRevaluationService.create).toHaveBeenCalledWith(assetRevaluation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetRevaluation>>();
      const assetRevaluation = { id: 123 };
      jest.spyOn(assetRevaluationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetRevaluation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetRevaluationService.update).toHaveBeenCalledWith(assetRevaluation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDealerById', () => {
      it('Should return tracked Dealer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealerById(0, entity);
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
