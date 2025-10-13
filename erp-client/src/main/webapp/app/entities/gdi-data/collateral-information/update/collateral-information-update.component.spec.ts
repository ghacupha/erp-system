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

import { CollateralInformationService } from '../service/collateral-information.service';
import { ICollateralInformation, CollateralInformation } from '../collateral-information.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { ICollateralType } from 'app/entities/gdi/collateral-type/collateral-type.model';
import { CollateralTypeService } from 'app/entities/gdi/collateral-type/service/collateral-type.service';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { CountySubCountyCodeService } from 'app/entities/gdi-data/county-sub-county-code/service/county-sub-county-code.service';

import { CollateralInformationUpdateComponent } from './collateral-information-update.component';

describe('CollateralInformation Management Update Component', () => {
  let comp: CollateralInformationUpdateComponent;
  let fixture: ComponentFixture<CollateralInformationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let collateralInformationService: CollateralInformationService;
  let institutionCodeService: InstitutionCodeService;
  let bankBranchCodeService: BankBranchCodeService;
  let collateralTypeService: CollateralTypeService;
  let countySubCountyCodeService: CountySubCountyCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CollateralInformationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CollateralInformationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CollateralInformationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    collateralInformationService = TestBed.inject(CollateralInformationService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    bankBranchCodeService = TestBed.inject(BankBranchCodeService);
    collateralTypeService = TestBed.inject(CollateralTypeService);
    countySubCountyCodeService = TestBed.inject(CountySubCountyCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InstitutionCode query and add missing value', () => {
      const collateralInformation: ICollateralInformation = { id: 456 };
      const bankCode: IInstitutionCode = { id: 48614 };
      collateralInformation.bankCode = bankCode;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 8114 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [bankCode];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ collateralInformation });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankBranchCode query and add missing value', () => {
      const collateralInformation: ICollateralInformation = { id: 456 };
      const branchCode: IBankBranchCode = { id: 86719 };
      collateralInformation.branchCode = branchCode;

      const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 26757 }];
      jest.spyOn(bankBranchCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: bankBranchCodeCollection })));
      const additionalBankBranchCodes = [branchCode];
      const expectedCollection: IBankBranchCode[] = [...additionalBankBranchCodes, ...bankBranchCodeCollection];
      jest.spyOn(bankBranchCodeService, 'addBankBranchCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ collateralInformation });
      comp.ngOnInit();

      expect(bankBranchCodeService.query).toHaveBeenCalled();
      expect(bankBranchCodeService.addBankBranchCodeToCollectionIfMissing).toHaveBeenCalledWith(
        bankBranchCodeCollection,
        ...additionalBankBranchCodes
      );
      expect(comp.bankBranchCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CollateralType query and add missing value', () => {
      const collateralInformation: ICollateralInformation = { id: 456 };
      const collateralType: ICollateralType = { id: 43407 };
      collateralInformation.collateralType = collateralType;

      const collateralTypeCollection: ICollateralType[] = [{ id: 66940 }];
      jest.spyOn(collateralTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: collateralTypeCollection })));
      const additionalCollateralTypes = [collateralType];
      const expectedCollection: ICollateralType[] = [...additionalCollateralTypes, ...collateralTypeCollection];
      jest.spyOn(collateralTypeService, 'addCollateralTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ collateralInformation });
      comp.ngOnInit();

      expect(collateralTypeService.query).toHaveBeenCalled();
      expect(collateralTypeService.addCollateralTypeToCollectionIfMissing).toHaveBeenCalledWith(
        collateralTypeCollection,
        ...additionalCollateralTypes
      );
      expect(comp.collateralTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CountySubCountyCode query and add missing value', () => {
      const collateralInformation: ICollateralInformation = { id: 456 };
      const countyCode: ICountySubCountyCode = { id: 52616 };
      collateralInformation.countyCode = countyCode;

      const countySubCountyCodeCollection: ICountySubCountyCode[] = [{ id: 59676 }];
      jest.spyOn(countySubCountyCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: countySubCountyCodeCollection })));
      const additionalCountySubCountyCodes = [countyCode];
      const expectedCollection: ICountySubCountyCode[] = [...additionalCountySubCountyCodes, ...countySubCountyCodeCollection];
      jest.spyOn(countySubCountyCodeService, 'addCountySubCountyCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ collateralInformation });
      comp.ngOnInit();

      expect(countySubCountyCodeService.query).toHaveBeenCalled();
      expect(countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing).toHaveBeenCalledWith(
        countySubCountyCodeCollection,
        ...additionalCountySubCountyCodes
      );
      expect(comp.countySubCountyCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const collateralInformation: ICollateralInformation = { id: 456 };
      const bankCode: IInstitutionCode = { id: 39046 };
      collateralInformation.bankCode = bankCode;
      const branchCode: IBankBranchCode = { id: 35142 };
      collateralInformation.branchCode = branchCode;
      const collateralType: ICollateralType = { id: 15253 };
      collateralInformation.collateralType = collateralType;
      const countyCode: ICountySubCountyCode = { id: 4461 };
      collateralInformation.countyCode = countyCode;

      activatedRoute.data = of({ collateralInformation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(collateralInformation));
      expect(comp.institutionCodesSharedCollection).toContain(bankCode);
      expect(comp.bankBranchCodesSharedCollection).toContain(branchCode);
      expect(comp.collateralTypesSharedCollection).toContain(collateralType);
      expect(comp.countySubCountyCodesSharedCollection).toContain(countyCode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollateralInformation>>();
      const collateralInformation = { id: 123 };
      jest.spyOn(collateralInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collateralInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collateralInformation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(collateralInformationService.update).toHaveBeenCalledWith(collateralInformation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollateralInformation>>();
      const collateralInformation = new CollateralInformation();
      jest.spyOn(collateralInformationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collateralInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collateralInformation }));
      saveSubject.complete();

      // THEN
      expect(collateralInformationService.create).toHaveBeenCalledWith(collateralInformation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollateralInformation>>();
      const collateralInformation = { id: 123 };
      jest.spyOn(collateralInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collateralInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(collateralInformationService.update).toHaveBeenCalledWith(collateralInformation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
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

    describe('trackCollateralTypeById', () => {
      it('Should return tracked CollateralType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCollateralTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCountySubCountyCodeById', () => {
      it('Should return tracked CountySubCountyCode primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCountySubCountyCodeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
