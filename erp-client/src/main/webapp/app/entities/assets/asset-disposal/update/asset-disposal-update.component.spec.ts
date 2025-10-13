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

import { AssetDisposalService } from '../service/asset-disposal.service';
import { IAssetDisposal, AssetDisposal } from '../asset-disposal.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';
import { AssetRegistrationService } from 'app/entities/assets/asset-registration/service/asset-registration.service';

import { AssetDisposalUpdateComponent } from './asset-disposal-update.component';

describe('AssetDisposal Management Update Component', () => {
  let comp: AssetDisposalUpdateComponent;
  let fixture: ComponentFixture<AssetDisposalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetDisposalService: AssetDisposalService;
  let applicationUserService: ApplicationUserService;
  let depreciationPeriodService: DepreciationPeriodService;
  let placeholderService: PlaceholderService;
  let assetRegistrationService: AssetRegistrationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AssetDisposalUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AssetDisposalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetDisposalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetDisposalService = TestBed.inject(AssetDisposalService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);
    placeholderService = TestBed.inject(PlaceholderService);
    assetRegistrationService = TestBed.inject(AssetRegistrationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const assetDisposal: IAssetDisposal = { id: 456 };
      const createdBy: IApplicationUser = { id: 28917 };
      assetDisposal.createdBy = createdBy;
      const modifiedBy: IApplicationUser = { id: 50178 };
      assetDisposal.modifiedBy = modifiedBy;
      const lastAccessedBy: IApplicationUser = { id: 88961 };
      assetDisposal.lastAccessedBy = lastAccessedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 4754 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [createdBy, modifiedBy, lastAccessedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetDisposal });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationPeriod query and add missing value', () => {
      const assetDisposal: IAssetDisposal = { id: 456 };
      const effectivePeriod: IDepreciationPeriod = { id: 91562 };
      assetDisposal.effectivePeriod = effectivePeriod;

      const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 56953 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationPeriodCollection })));
      const additionalDepreciationPeriods = [effectivePeriod];
      const expectedCollection: IDepreciationPeriod[] = [...additionalDepreciationPeriods, ...depreciationPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetDisposal });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationPeriodCollection,
        ...additionalDepreciationPeriods
      );
      expect(comp.depreciationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const assetDisposal: IAssetDisposal = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 45013 }];
      assetDisposal.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 8255 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetDisposal });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call assetDisposed query and add missing value', () => {
      const assetDisposal: IAssetDisposal = { id: 456 };
      const assetDisposed: IAssetRegistration = { id: 62242 };
      assetDisposal.assetDisposed = assetDisposed;

      const assetDisposedCollection: IAssetRegistration[] = [{ id: 19575 }];
      jest.spyOn(assetRegistrationService, 'query').mockReturnValue(of(new HttpResponse({ body: assetDisposedCollection })));
      const expectedCollection: IAssetRegistration[] = [assetDisposed, ...assetDisposedCollection];
      jest.spyOn(assetRegistrationService, 'addAssetRegistrationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetDisposal });
      comp.ngOnInit();

      expect(assetRegistrationService.query).toHaveBeenCalled();
      expect(assetRegistrationService.addAssetRegistrationToCollectionIfMissing).toHaveBeenCalledWith(
        assetDisposedCollection,
        assetDisposed
      );
      expect(comp.assetDisposedsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assetDisposal: IAssetDisposal = { id: 456 };
      const createdBy: IApplicationUser = { id: 57471 };
      assetDisposal.createdBy = createdBy;
      const modifiedBy: IApplicationUser = { id: 92310 };
      assetDisposal.modifiedBy = modifiedBy;
      const lastAccessedBy: IApplicationUser = { id: 52779 };
      assetDisposal.lastAccessedBy = lastAccessedBy;
      const effectivePeriod: IDepreciationPeriod = { id: 50712 };
      assetDisposal.effectivePeriod = effectivePeriod;
      const placeholders: IPlaceholder = { id: 94958 };
      assetDisposal.placeholders = [placeholders];
      const assetDisposed: IAssetRegistration = { id: 64054 };
      assetDisposal.assetDisposed = assetDisposed;

      activatedRoute.data = of({ assetDisposal });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetDisposal));
      expect(comp.applicationUsersSharedCollection).toContain(createdBy);
      expect(comp.applicationUsersSharedCollection).toContain(modifiedBy);
      expect(comp.applicationUsersSharedCollection).toContain(lastAccessedBy);
      expect(comp.depreciationPeriodsSharedCollection).toContain(effectivePeriod);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.assetDisposedsCollection).toContain(assetDisposed);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetDisposal>>();
      const assetDisposal = { id: 123 };
      jest.spyOn(assetDisposalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDisposal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetDisposal }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetDisposalService.update).toHaveBeenCalledWith(assetDisposal);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetDisposal>>();
      const assetDisposal = new AssetDisposal();
      jest.spyOn(assetDisposalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDisposal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetDisposal }));
      saveSubject.complete();

      // THEN
      expect(assetDisposalService.create).toHaveBeenCalledWith(assetDisposal);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetDisposal>>();
      const assetDisposal = { id: 123 };
      jest.spyOn(assetDisposalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDisposal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetDisposalService.update).toHaveBeenCalledWith(assetDisposal);
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

    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
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
