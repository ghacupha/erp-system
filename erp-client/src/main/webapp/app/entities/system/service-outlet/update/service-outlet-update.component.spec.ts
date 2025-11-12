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

import { ServiceOutletService } from '../service/service-outlet.service';
import { IServiceOutlet, ServiceOutlet } from '../service-outlet.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IOutletType } from 'app/entities/system/outlet-type/outlet-type.model';
import { OutletTypeService } from 'app/entities/system/outlet-type/service/outlet-type.service';
import { IOutletStatus } from 'app/entities/system/outlet-status/outlet-status.model';
import { OutletStatusService } from 'app/entities/system/outlet-status/service/outlet-status.service';
import { ICountyCode } from 'app/entities/system/county-code/county-code.model';
import { CountyCodeService } from 'app/entities/system/county-code/service/county-code.service';

import { ServiceOutletUpdateComponent } from './service-outlet-update.component';

describe('ServiceOutlet Management Update Component', () => {
  let comp: ServiceOutletUpdateComponent;
  let fixture: ComponentFixture<ServiceOutletUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceOutletService: ServiceOutletService;
  let placeholderService: PlaceholderService;
  let bankBranchCodeService: BankBranchCodeService;
  let outletTypeService: OutletTypeService;
  let outletStatusService: OutletStatusService;
  let countyCodeService: CountyCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ServiceOutletUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ServiceOutletUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceOutletUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    placeholderService = TestBed.inject(PlaceholderService);
    bankBranchCodeService = TestBed.inject(BankBranchCodeService);
    outletTypeService = TestBed.inject(OutletTypeService);
    outletStatusService = TestBed.inject(OutletStatusService);
    countyCodeService = TestBed.inject(CountyCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const serviceOutlet: IServiceOutlet = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 89142 }];
      serviceOutlet.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 81803 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceOutlet });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankBranchCode query and add missing value', () => {
      const serviceOutlet: IServiceOutlet = { id: 456 };
      const bankCode: IBankBranchCode = { id: 54950 };
      serviceOutlet.bankCode = bankCode;

      const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 64159 }];
      jest.spyOn(bankBranchCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: bankBranchCodeCollection })));
      const additionalBankBranchCodes = [bankCode];
      const expectedCollection: IBankBranchCode[] = [...additionalBankBranchCodes, ...bankBranchCodeCollection];
      jest.spyOn(bankBranchCodeService, 'addBankBranchCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceOutlet });
      comp.ngOnInit();

      expect(bankBranchCodeService.query).toHaveBeenCalled();
      expect(bankBranchCodeService.addBankBranchCodeToCollectionIfMissing).toHaveBeenCalledWith(
        bankBranchCodeCollection,
        ...additionalBankBranchCodes
      );
      expect(comp.bankBranchCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OutletType query and add missing value', () => {
      const serviceOutlet: IServiceOutlet = { id: 456 };
      const outletType: IOutletType = { id: 37040 };
      serviceOutlet.outletType = outletType;

      const outletTypeCollection: IOutletType[] = [{ id: 37080 }];
      jest.spyOn(outletTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: outletTypeCollection })));
      const additionalOutletTypes = [outletType];
      const expectedCollection: IOutletType[] = [...additionalOutletTypes, ...outletTypeCollection];
      jest.spyOn(outletTypeService, 'addOutletTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceOutlet });
      comp.ngOnInit();

      expect(outletTypeService.query).toHaveBeenCalled();
      expect(outletTypeService.addOutletTypeToCollectionIfMissing).toHaveBeenCalledWith(outletTypeCollection, ...additionalOutletTypes);
      expect(comp.outletTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OutletStatus query and add missing value', () => {
      const serviceOutlet: IServiceOutlet = { id: 456 };
      const outletStatus: IOutletStatus = { id: 70154 };
      serviceOutlet.outletStatus = outletStatus;

      const outletStatusCollection: IOutletStatus[] = [{ id: 24966 }];
      jest.spyOn(outletStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: outletStatusCollection })));
      const additionalOutletStatuses = [outletStatus];
      const expectedCollection: IOutletStatus[] = [...additionalOutletStatuses, ...outletStatusCollection];
      jest.spyOn(outletStatusService, 'addOutletStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceOutlet });
      comp.ngOnInit();

      expect(outletStatusService.query).toHaveBeenCalled();
      expect(outletStatusService.addOutletStatusToCollectionIfMissing).toHaveBeenCalledWith(
        outletStatusCollection,
        ...additionalOutletStatuses
      );
      expect(comp.outletStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CountyCode query and add missing value', () => {
      const serviceOutlet: IServiceOutlet = { id: 456 };
      const countyName: ICountyCode = { id: 84099 };
      serviceOutlet.countyName = countyName;
      const subCountyName: ICountyCode = { id: 48030 };
      serviceOutlet.subCountyName = subCountyName;

      const countyCodeCollection: ICountyCode[] = [{ id: 4997 }];
      jest.spyOn(countyCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: countyCodeCollection })));
      const additionalCountyCodes = [countyName, subCountyName];
      const expectedCollection: ICountyCode[] = [...additionalCountyCodes, ...countyCodeCollection];
      jest.spyOn(countyCodeService, 'addCountyCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceOutlet });
      comp.ngOnInit();

      expect(countyCodeService.query).toHaveBeenCalled();
      expect(countyCodeService.addCountyCodeToCollectionIfMissing).toHaveBeenCalledWith(countyCodeCollection, ...additionalCountyCodes);
      expect(comp.countyCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const serviceOutlet: IServiceOutlet = { id: 456 };
      const placeholders: IPlaceholder = { id: 52019 };
      serviceOutlet.placeholders = [placeholders];
      const bankCode: IBankBranchCode = { id: 35169 };
      serviceOutlet.bankCode = bankCode;
      const outletType: IOutletType = { id: 93566 };
      serviceOutlet.outletType = outletType;
      const outletStatus: IOutletStatus = { id: 84573 };
      serviceOutlet.outletStatus = outletStatus;
      const countyName: ICountyCode = { id: 14785 };
      serviceOutlet.countyName = countyName;
      const subCountyName: ICountyCode = { id: 17876 };
      serviceOutlet.subCountyName = subCountyName;

      activatedRoute.data = of({ serviceOutlet });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(serviceOutlet));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.bankBranchCodesSharedCollection).toContain(bankCode);
      expect(comp.outletTypesSharedCollection).toContain(outletType);
      expect(comp.outletStatusesSharedCollection).toContain(outletStatus);
      expect(comp.countyCodesSharedCollection).toContain(countyName);
      expect(comp.countyCodesSharedCollection).toContain(subCountyName);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ServiceOutlet>>();
      const serviceOutlet = { id: 123 };
      jest.spyOn(serviceOutletService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceOutlet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceOutlet }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceOutletService.update).toHaveBeenCalledWith(serviceOutlet);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ServiceOutlet>>();
      const serviceOutlet = new ServiceOutlet();
      jest.spyOn(serviceOutletService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceOutlet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceOutlet }));
      saveSubject.complete();

      // THEN
      expect(serviceOutletService.create).toHaveBeenCalledWith(serviceOutlet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ServiceOutlet>>();
      const serviceOutlet = { id: 123 };
      jest.spyOn(serviceOutletService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceOutlet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceOutletService.update).toHaveBeenCalledWith(serviceOutlet);
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

    describe('trackBankBranchCodeById', () => {
      it('Should return tracked BankBranchCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBankBranchCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackOutletTypeById', () => {
      it('Should return tracked OutletType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOutletTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackOutletStatusById', () => {
      it('Should return tracked OutletStatus primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOutletStatusById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCountyCodeById', () => {
      it('Should return tracked CountyCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCountyCodeById(0, entity);
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
