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

import { WorkInProgressTransferService } from '../service/work-in-progress-transfer.service';
import { IWorkInProgressTransfer, WorkInProgressTransfer } from '../work-in-progress-transfer.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { AssetCategoryService } from 'app/entities/assets/asset-category/service/asset-category.service';
import { IWorkInProgressRegistration } from 'app/entities/wip/work-in-progress-registration/work-in-progress-registration.model';
import { WorkInProgressRegistrationService } from 'app/entities/wip/work-in-progress-registration/service/work-in-progress-registration.service';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ServiceOutletService } from 'app/entities/system/service-outlet/service/service-outlet.service';
import { ISettlement } from 'app/entities/settlement/settlement/settlement.model';
import { SettlementService } from 'app/entities/settlement/settlement/service/settlement.service';
import { IWorkProjectRegister } from 'app/entities/wip/work-project-register/work-project-register.model';
import { WorkProjectRegisterService } from 'app/entities/wip/work-project-register/service/work-project-register.service';

import { WorkInProgressTransferUpdateComponent } from './work-in-progress-transfer-update.component';

describe('WorkInProgressTransfer Management Update Component', () => {
  let comp: WorkInProgressTransferUpdateComponent;
  let fixture: ComponentFixture<WorkInProgressTransferUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workInProgressTransferService: WorkInProgressTransferService;
  let placeholderService: PlaceholderService;
  let businessDocumentService: BusinessDocumentService;
  let assetCategoryService: AssetCategoryService;
  let workInProgressRegistrationService: WorkInProgressRegistrationService;
  let serviceOutletService: ServiceOutletService;
  let settlementService: SettlementService;
  let workProjectRegisterService: WorkProjectRegisterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [WorkInProgressTransferUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(WorkInProgressTransferUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkInProgressTransferUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workInProgressTransferService = TestBed.inject(WorkInProgressTransferService);
    placeholderService = TestBed.inject(PlaceholderService);
    businessDocumentService = TestBed.inject(BusinessDocumentService);
    assetCategoryService = TestBed.inject(AssetCategoryService);
    workInProgressRegistrationService = TestBed.inject(WorkInProgressRegistrationService);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    settlementService = TestBed.inject(SettlementService);
    workProjectRegisterService = TestBed.inject(WorkProjectRegisterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const workInProgressTransfer: IWorkInProgressTransfer = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 31211 }];
      workInProgressTransfer.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 13607 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessDocument query and add missing value', () => {
      const workInProgressTransfer: IWorkInProgressTransfer = { id: 456 };
      const businessDocuments: IBusinessDocument[] = [{ id: 60992 }];
      workInProgressTransfer.businessDocuments = businessDocuments;

      const businessDocumentCollection: IBusinessDocument[] = [{ id: 76661 }];
      jest.spyOn(businessDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: businessDocumentCollection })));
      const additionalBusinessDocuments = [...businessDocuments];
      const expectedCollection: IBusinessDocument[] = [...additionalBusinessDocuments, ...businessDocumentCollection];
      jest.spyOn(businessDocumentService, 'addBusinessDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      expect(businessDocumentService.query).toHaveBeenCalled();
      expect(businessDocumentService.addBusinessDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        businessDocumentCollection,
        ...additionalBusinessDocuments
      );
      expect(comp.businessDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetCategory query and add missing value', () => {
      const workInProgressTransfer: IWorkInProgressTransfer = { id: 456 };
      const assetCategory: IAssetCategory = { id: 17292 };
      workInProgressTransfer.assetCategory = assetCategory;

      const assetCategoryCollection: IAssetCategory[] = [{ id: 4346 }];
      jest.spyOn(assetCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCategoryCollection })));
      const additionalAssetCategories = [assetCategory];
      const expectedCollection: IAssetCategory[] = [...additionalAssetCategories, ...assetCategoryCollection];
      jest.spyOn(assetCategoryService, 'addAssetCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      expect(assetCategoryService.query).toHaveBeenCalled();
      expect(assetCategoryService.addAssetCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        assetCategoryCollection,
        ...additionalAssetCategories
      );
      expect(comp.assetCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call WorkInProgressRegistration query and add missing value', () => {
      const workInProgressTransfer: IWorkInProgressTransfer = { id: 456 };
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 39483 };
      workInProgressTransfer.workInProgressRegistration = workInProgressRegistration;

      const workInProgressRegistrationCollection: IWorkInProgressRegistration[] = [{ id: 62496 }];
      jest
        .spyOn(workInProgressRegistrationService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: workInProgressRegistrationCollection })));
      const additionalWorkInProgressRegistrations = [workInProgressRegistration];
      const expectedCollection: IWorkInProgressRegistration[] = [
        ...additionalWorkInProgressRegistrations,
        ...workInProgressRegistrationCollection,
      ];
      jest
        .spyOn(workInProgressRegistrationService, 'addWorkInProgressRegistrationToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      expect(workInProgressRegistrationService.query).toHaveBeenCalled();
      expect(workInProgressRegistrationService.addWorkInProgressRegistrationToCollectionIfMissing).toHaveBeenCalledWith(
        workInProgressRegistrationCollection,
        ...additionalWorkInProgressRegistrations
      );
      expect(comp.workInProgressRegistrationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceOutlet query and add missing value', () => {
      const workInProgressTransfer: IWorkInProgressTransfer = { id: 456 };
      const serviceOutlet: IServiceOutlet = { id: 28460 };
      workInProgressTransfer.serviceOutlet = serviceOutlet;

      const serviceOutletCollection: IServiceOutlet[] = [{ id: 90149 }];
      jest.spyOn(serviceOutletService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOutletCollection })));
      const additionalServiceOutlets = [serviceOutlet];
      const expectedCollection: IServiceOutlet[] = [...additionalServiceOutlets, ...serviceOutletCollection];
      jest.spyOn(serviceOutletService, 'addServiceOutletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      expect(serviceOutletService.query).toHaveBeenCalled();
      expect(serviceOutletService.addServiceOutletToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOutletCollection,
        ...additionalServiceOutlets
      );
      expect(comp.serviceOutletsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Settlement query and add missing value', () => {
      const workInProgressTransfer: IWorkInProgressTransfer = { id: 456 };
      const transferSettlement: ISettlement = { id: 35118 };
      workInProgressTransfer.transferSettlement = transferSettlement;
      const originalSettlement: ISettlement = { id: 99864 };
      workInProgressTransfer.originalSettlement = originalSettlement;

      const settlementCollection: ISettlement[] = [{ id: 55604 }];
      jest.spyOn(settlementService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCollection })));
      const additionalSettlements = [transferSettlement, originalSettlement];
      const expectedCollection: ISettlement[] = [...additionalSettlements, ...settlementCollection];
      jest.spyOn(settlementService, 'addSettlementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      expect(settlementService.query).toHaveBeenCalled();
      expect(settlementService.addSettlementToCollectionIfMissing).toHaveBeenCalledWith(settlementCollection, ...additionalSettlements);
      expect(comp.settlementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call WorkProjectRegister query and add missing value', () => {
      const workInProgressTransfer: IWorkInProgressTransfer = { id: 456 };
      const workProjectRegister: IWorkProjectRegister = { id: 71278 };
      workInProgressTransfer.workProjectRegister = workProjectRegister;

      const workProjectRegisterCollection: IWorkProjectRegister[] = [{ id: 46262 }];
      jest.spyOn(workProjectRegisterService, 'query').mockReturnValue(of(new HttpResponse({ body: workProjectRegisterCollection })));
      const additionalWorkProjectRegisters = [workProjectRegister];
      const expectedCollection: IWorkProjectRegister[] = [...additionalWorkProjectRegisters, ...workProjectRegisterCollection];
      jest.spyOn(workProjectRegisterService, 'addWorkProjectRegisterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      expect(workProjectRegisterService.query).toHaveBeenCalled();
      expect(workProjectRegisterService.addWorkProjectRegisterToCollectionIfMissing).toHaveBeenCalledWith(
        workProjectRegisterCollection,
        ...additionalWorkProjectRegisters
      );
      expect(comp.workProjectRegistersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workInProgressTransfer: IWorkInProgressTransfer = { id: 456 };
      const placeholders: IPlaceholder = { id: 49392 };
      workInProgressTransfer.placeholders = [placeholders];
      const businessDocuments: IBusinessDocument = { id: 93819 };
      workInProgressTransfer.businessDocuments = [businessDocuments];
      const assetCategory: IAssetCategory = { id: 67748 };
      workInProgressTransfer.assetCategory = assetCategory;
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 88361 };
      workInProgressTransfer.workInProgressRegistration = workInProgressRegistration;
      const serviceOutlet: IServiceOutlet = { id: 62154 };
      workInProgressTransfer.serviceOutlet = serviceOutlet;
      const transferSettlement: ISettlement = { id: 32459 };
      workInProgressTransfer.transferSettlement = transferSettlement;
      const originalSettlement: ISettlement = { id: 25102 };
      workInProgressTransfer.originalSettlement = originalSettlement;
      const workProjectRegister: IWorkProjectRegister = { id: 30830 };
      workInProgressTransfer.workProjectRegister = workProjectRegister;

      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(workInProgressTransfer));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.businessDocumentsSharedCollection).toContain(businessDocuments);
      expect(comp.assetCategoriesSharedCollection).toContain(assetCategory);
      expect(comp.workInProgressRegistrationsSharedCollection).toContain(workInProgressRegistration);
      expect(comp.serviceOutletsSharedCollection).toContain(serviceOutlet);
      expect(comp.settlementsSharedCollection).toContain(transferSettlement);
      expect(comp.settlementsSharedCollection).toContain(originalSettlement);
      expect(comp.workProjectRegistersSharedCollection).toContain(workProjectRegister);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkInProgressTransfer>>();
      const workInProgressTransfer = { id: 123 };
      jest.spyOn(workInProgressTransferService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workInProgressTransfer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(workInProgressTransferService.update).toHaveBeenCalledWith(workInProgressTransfer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkInProgressTransfer>>();
      const workInProgressTransfer = new WorkInProgressTransfer();
      jest.spyOn(workInProgressTransferService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workInProgressTransfer }));
      saveSubject.complete();

      // THEN
      expect(workInProgressTransferService.create).toHaveBeenCalledWith(workInProgressTransfer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkInProgressTransfer>>();
      const workInProgressTransfer = { id: 123 };
      jest.spyOn(workInProgressTransferService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workInProgressTransfer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workInProgressTransferService.update).toHaveBeenCalledWith(workInProgressTransfer);
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

    describe('trackBusinessDocumentById', () => {
      it('Should return tracked BusinessDocument primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBusinessDocumentById(0, entity);
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

    describe('trackWorkInProgressRegistrationById', () => {
      it('Should return tracked WorkInProgressRegistration primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWorkInProgressRegistrationById(0, entity);
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

    describe('trackSettlementById', () => {
      it('Should return tracked Settlement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSettlementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackWorkProjectRegisterById', () => {
      it('Should return tracked WorkProjectRegister primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWorkProjectRegisterById(0, entity);
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

    describe('getSelectedBusinessDocument', () => {
      it('Should return option if no BusinessDocument is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBusinessDocument(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected BusinessDocument for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBusinessDocument(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this BusinessDocument is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBusinessDocument(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
