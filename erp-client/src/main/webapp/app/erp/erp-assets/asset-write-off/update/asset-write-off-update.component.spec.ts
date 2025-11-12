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

import { AssetWriteOffService } from '../service/asset-write-off.service';
import { IAssetWriteOff, AssetWriteOff } from '../asset-write-off.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';
import { AssetRegistrationService } from 'app/entities/assets/asset-registration/service/asset-registration.service';

import { AssetWriteOffUpdateComponent } from './asset-write-off-update.component';

describe('AssetWriteOff Management Update Component', () => {
  let comp: AssetWriteOffUpdateComponent;
  let fixture: ComponentFixture<AssetWriteOffUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetWriteOffService: AssetWriteOffService;
  let applicationUserService: ApplicationUserService;
  let depreciationPeriodService: DepreciationPeriodService;
  let placeholderService: PlaceholderService;
  let assetRegistrationService: AssetRegistrationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AssetWriteOffUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AssetWriteOffUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetWriteOffUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetWriteOffService = TestBed.inject(AssetWriteOffService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    depreciationPeriodService = TestBed.inject(DepreciationPeriodService);
    placeholderService = TestBed.inject(PlaceholderService);
    assetRegistrationService = TestBed.inject(AssetRegistrationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const assetWriteOff: IAssetWriteOff = { id: 456 };
      const createdBy: IApplicationUser = { id: 78593 };
      assetWriteOff.createdBy = createdBy;
      const modifiedBy: IApplicationUser = { id: 55442 };
      assetWriteOff.modifiedBy = modifiedBy;
      const lastAccessedBy: IApplicationUser = { id: 14859 };
      assetWriteOff.lastAccessedBy = lastAccessedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 24038 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [createdBy, modifiedBy, lastAccessedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetWriteOff });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DepreciationPeriod query and add missing value', () => {
      const assetWriteOff: IAssetWriteOff = { id: 456 };
      const effectivePeriod: IDepreciationPeriod = { id: 96343 };
      assetWriteOff.effectivePeriod = effectivePeriod;

      const depreciationPeriodCollection: IDepreciationPeriod[] = [{ id: 87220 }];
      jest.spyOn(depreciationPeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationPeriodCollection })));
      const additionalDepreciationPeriods = [effectivePeriod];
      const expectedCollection: IDepreciationPeriod[] = [...additionalDepreciationPeriods, ...depreciationPeriodCollection];
      jest.spyOn(depreciationPeriodService, 'addDepreciationPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetWriteOff });
      comp.ngOnInit();

      expect(depreciationPeriodService.query).toHaveBeenCalled();
      expect(depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationPeriodCollection,
        ...additionalDepreciationPeriods
      );
      expect(comp.depreciationPeriodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const assetWriteOff: IAssetWriteOff = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 51252 }];
      assetWriteOff.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 19993 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetWriteOff });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call assetWrittenOff query and add missing value', () => {
      const assetWriteOff: IAssetWriteOff = { id: 456 };
      const assetWrittenOff: IAssetRegistration = { id: 51761 };
      assetWriteOff.assetWrittenOff = assetWrittenOff;

      const assetWrittenOffCollection: IAssetRegistration[] = [{ id: 71873 }];
      jest.spyOn(assetRegistrationService, 'query').mockReturnValue(of(new HttpResponse({ body: assetWrittenOffCollection })));
      const expectedCollection: IAssetRegistration[] = [assetWrittenOff, ...assetWrittenOffCollection];
      jest.spyOn(assetRegistrationService, 'addAssetRegistrationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetWriteOff });
      comp.ngOnInit();

      expect(assetRegistrationService.query).toHaveBeenCalled();
      expect(assetRegistrationService.addAssetRegistrationToCollectionIfMissing).toHaveBeenCalledWith(
        assetWrittenOffCollection,
        assetWrittenOff
      );
      expect(comp.assetWrittenOffsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assetWriteOff: IAssetWriteOff = { id: 456 };
      const createdBy: IApplicationUser = { id: 86170 };
      assetWriteOff.createdBy = createdBy;
      const modifiedBy: IApplicationUser = { id: 9345 };
      assetWriteOff.modifiedBy = modifiedBy;
      const lastAccessedBy: IApplicationUser = { id: 1729 };
      assetWriteOff.lastAccessedBy = lastAccessedBy;
      const effectivePeriod: IDepreciationPeriod = { id: 79821 };
      assetWriteOff.effectivePeriod = effectivePeriod;
      const placeholders: IPlaceholder = { id: 59287 };
      assetWriteOff.placeholders = [placeholders];
      const assetWrittenOff: IAssetRegistration = { id: 45640 };
      assetWriteOff.assetWrittenOff = assetWrittenOff;

      activatedRoute.data = of({ assetWriteOff });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetWriteOff));
      expect(comp.applicationUsersSharedCollection).toContain(createdBy);
      expect(comp.applicationUsersSharedCollection).toContain(modifiedBy);
      expect(comp.applicationUsersSharedCollection).toContain(lastAccessedBy);
      expect(comp.depreciationPeriodsSharedCollection).toContain(effectivePeriod);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.assetWrittenOffsCollection).toContain(assetWrittenOff);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetWriteOff>>();
      const assetWriteOff = { id: 123 };
      jest.spyOn(assetWriteOffService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetWriteOff });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetWriteOff }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetWriteOffService.update).toHaveBeenCalledWith(assetWriteOff);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetWriteOff>>();
      const assetWriteOff = new AssetWriteOff();
      jest.spyOn(assetWriteOffService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetWriteOff });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetWriteOff }));
      saveSubject.complete();

      // THEN
      expect(assetWriteOffService.create).toHaveBeenCalledWith(assetWriteOff);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetWriteOff>>();
      const assetWriteOff = { id: 123 };
      jest.spyOn(assetWriteOffService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetWriteOff });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetWriteOffService.update).toHaveBeenCalledWith(assetWriteOff);
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
