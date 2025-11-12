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

import { ParticularsOfOutletService } from '../service/particulars-of-outlet.service';
import { IParticularsOfOutlet, ParticularsOfOutlet } from '../particulars-of-outlet.model';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { CountySubCountyCodeService } from 'app/entities/gdi-data/county-sub-county-code/service/county-sub-county-code.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IOutletType } from 'app/entities/system/outlet-type/outlet-type.model';
import { OutletTypeService } from 'app/entities/system/outlet-type/service/outlet-type.service';
import { IOutletStatus } from 'app/entities/system/outlet-status/outlet-status.model';
import { OutletStatusService } from 'app/entities/system/outlet-status/service/outlet-status.service';

import { ParticularsOfOutletUpdateComponent } from './particulars-of-outlet-update.component';

describe('ParticularsOfOutlet Management Update Component', () => {
  let comp: ParticularsOfOutletUpdateComponent;
  let fixture: ComponentFixture<ParticularsOfOutletUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let particularsOfOutletService: ParticularsOfOutletService;
  let countySubCountyCodeService: CountySubCountyCodeService;
  let institutionCodeService: InstitutionCodeService;
  let bankBranchCodeService: BankBranchCodeService;
  let outletTypeService: OutletTypeService;
  let outletStatusService: OutletStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ParticularsOfOutletUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ParticularsOfOutletUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParticularsOfOutletUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    particularsOfOutletService = TestBed.inject(ParticularsOfOutletService);
    countySubCountyCodeService = TestBed.inject(CountySubCountyCodeService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    bankBranchCodeService = TestBed.inject(BankBranchCodeService);
    outletTypeService = TestBed.inject(OutletTypeService);
    outletStatusService = TestBed.inject(OutletStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CountySubCountyCode query and add missing value', () => {
      const particularsOfOutlet: IParticularsOfOutlet = { id: 456 };
      const subCountyCode: ICountySubCountyCode = { id: 63976 };
      particularsOfOutlet.subCountyCode = subCountyCode;

      const countySubCountyCodeCollection: ICountySubCountyCode[] = [{ id: 33680 }];
      jest.spyOn(countySubCountyCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: countySubCountyCodeCollection })));
      const additionalCountySubCountyCodes = [subCountyCode];
      const expectedCollection: ICountySubCountyCode[] = [...additionalCountySubCountyCodes, ...countySubCountyCodeCollection];
      jest.spyOn(countySubCountyCodeService, 'addCountySubCountyCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ particularsOfOutlet });
      comp.ngOnInit();

      expect(countySubCountyCodeService.query).toHaveBeenCalled();
      expect(countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing).toHaveBeenCalledWith(
        countySubCountyCodeCollection,
        ...additionalCountySubCountyCodes
      );
      expect(comp.countySubCountyCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InstitutionCode query and add missing value', () => {
      const particularsOfOutlet: IParticularsOfOutlet = { id: 456 };
      const bankCode: IInstitutionCode = { id: 70973 };
      particularsOfOutlet.bankCode = bankCode;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 76989 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [bankCode];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ particularsOfOutlet });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankBranchCode query and add missing value', () => {
      const particularsOfOutlet: IParticularsOfOutlet = { id: 456 };
      const outletId: IBankBranchCode = { id: 48970 };
      particularsOfOutlet.outletId = outletId;

      const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 26027 }];
      jest.spyOn(bankBranchCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: bankBranchCodeCollection })));
      const additionalBankBranchCodes = [outletId];
      const expectedCollection: IBankBranchCode[] = [...additionalBankBranchCodes, ...bankBranchCodeCollection];
      jest.spyOn(bankBranchCodeService, 'addBankBranchCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ particularsOfOutlet });
      comp.ngOnInit();

      expect(bankBranchCodeService.query).toHaveBeenCalled();
      expect(bankBranchCodeService.addBankBranchCodeToCollectionIfMissing).toHaveBeenCalledWith(
        bankBranchCodeCollection,
        ...additionalBankBranchCodes
      );
      expect(comp.bankBranchCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OutletType query and add missing value', () => {
      const particularsOfOutlet: IParticularsOfOutlet = { id: 456 };
      const typeOfOutlet: IOutletType = { id: 96106 };
      particularsOfOutlet.typeOfOutlet = typeOfOutlet;

      const outletTypeCollection: IOutletType[] = [{ id: 63175 }];
      jest.spyOn(outletTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: outletTypeCollection })));
      const additionalOutletTypes = [typeOfOutlet];
      const expectedCollection: IOutletType[] = [...additionalOutletTypes, ...outletTypeCollection];
      jest.spyOn(outletTypeService, 'addOutletTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ particularsOfOutlet });
      comp.ngOnInit();

      expect(outletTypeService.query).toHaveBeenCalled();
      expect(outletTypeService.addOutletTypeToCollectionIfMissing).toHaveBeenCalledWith(outletTypeCollection, ...additionalOutletTypes);
      expect(comp.outletTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OutletStatus query and add missing value', () => {
      const particularsOfOutlet: IParticularsOfOutlet = { id: 456 };
      const outletStatus: IOutletStatus = { id: 1356 };
      particularsOfOutlet.outletStatus = outletStatus;

      const outletStatusCollection: IOutletStatus[] = [{ id: 90390 }];
      jest.spyOn(outletStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: outletStatusCollection })));
      const additionalOutletStatuses = [outletStatus];
      const expectedCollection: IOutletStatus[] = [...additionalOutletStatuses, ...outletStatusCollection];
      jest.spyOn(outletStatusService, 'addOutletStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ particularsOfOutlet });
      comp.ngOnInit();

      expect(outletStatusService.query).toHaveBeenCalled();
      expect(outletStatusService.addOutletStatusToCollectionIfMissing).toHaveBeenCalledWith(
        outletStatusCollection,
        ...additionalOutletStatuses
      );
      expect(comp.outletStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const particularsOfOutlet: IParticularsOfOutlet = { id: 456 };
      const subCountyCode: ICountySubCountyCode = { id: 52223 };
      particularsOfOutlet.subCountyCode = subCountyCode;
      const bankCode: IInstitutionCode = { id: 52328 };
      particularsOfOutlet.bankCode = bankCode;
      const outletId: IBankBranchCode = { id: 47931 };
      particularsOfOutlet.outletId = outletId;
      const typeOfOutlet: IOutletType = { id: 85887 };
      particularsOfOutlet.typeOfOutlet = typeOfOutlet;
      const outletStatus: IOutletStatus = { id: 92708 };
      particularsOfOutlet.outletStatus = outletStatus;

      activatedRoute.data = of({ particularsOfOutlet });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(particularsOfOutlet));
      expect(comp.countySubCountyCodesSharedCollection).toContain(subCountyCode);
      expect(comp.institutionCodesSharedCollection).toContain(bankCode);
      expect(comp.bankBranchCodesSharedCollection).toContain(outletId);
      expect(comp.outletTypesSharedCollection).toContain(typeOfOutlet);
      expect(comp.outletStatusesSharedCollection).toContain(outletStatus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ParticularsOfOutlet>>();
      const particularsOfOutlet = { id: 123 };
      jest.spyOn(particularsOfOutletService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ particularsOfOutlet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: particularsOfOutlet }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(particularsOfOutletService.update).toHaveBeenCalledWith(particularsOfOutlet);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ParticularsOfOutlet>>();
      const particularsOfOutlet = new ParticularsOfOutlet();
      jest.spyOn(particularsOfOutletService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ particularsOfOutlet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: particularsOfOutlet }));
      saveSubject.complete();

      // THEN
      expect(particularsOfOutletService.create).toHaveBeenCalledWith(particularsOfOutlet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ParticularsOfOutlet>>();
      const particularsOfOutlet = { id: 123 };
      jest.spyOn(particularsOfOutletService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ particularsOfOutlet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(particularsOfOutletService.update).toHaveBeenCalledWith(particularsOfOutlet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCountySubCountyCodeById', () => {
      it('Should return tracked CountySubCountyCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCountySubCountyCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInstitutionCodeById', () => {
      it('Should return tracked InstitutionCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInstitutionCodeById(0, entity);
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
  });
});
