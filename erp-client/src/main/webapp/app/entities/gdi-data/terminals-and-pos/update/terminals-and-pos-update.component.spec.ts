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

import { TerminalsAndPOSService } from '../service/terminals-and-pos.service';
import { ITerminalsAndPOS, TerminalsAndPOS } from '../terminals-and-pos.model';
import { ITerminalTypes } from 'app/entities/gdi/terminal-types/terminal-types.model';
import { TerminalTypesService } from 'app/entities/gdi/terminal-types/service/terminal-types.service';
import { ITerminalFunctions } from 'app/entities/gdi/terminal-functions/terminal-functions.model';
import { TerminalFunctionsService } from 'app/entities/gdi/terminal-functions/service/terminal-functions.service';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { CountySubCountyCodeService } from 'app/entities/gdi-data/county-sub-county-code/service/county-sub-county-code.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';

import { TerminalsAndPOSUpdateComponent } from './terminals-and-pos-update.component';

describe('TerminalsAndPOS Management Update Component', () => {
  let comp: TerminalsAndPOSUpdateComponent;
  let fixture: ComponentFixture<TerminalsAndPOSUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let terminalsAndPOSService: TerminalsAndPOSService;
  let terminalTypesService: TerminalTypesService;
  let terminalFunctionsService: TerminalFunctionsService;
  let countySubCountyCodeService: CountySubCountyCodeService;
  let institutionCodeService: InstitutionCodeService;
  let bankBranchCodeService: BankBranchCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TerminalsAndPOSUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TerminalsAndPOSUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TerminalsAndPOSUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    terminalsAndPOSService = TestBed.inject(TerminalsAndPOSService);
    terminalTypesService = TestBed.inject(TerminalTypesService);
    terminalFunctionsService = TestBed.inject(TerminalFunctionsService);
    countySubCountyCodeService = TestBed.inject(CountySubCountyCodeService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    bankBranchCodeService = TestBed.inject(BankBranchCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TerminalTypes query and add missing value', () => {
      const terminalsAndPOS: ITerminalsAndPOS = { id: 456 };
      const terminalType: ITerminalTypes = { id: 63730 };
      terminalsAndPOS.terminalType = terminalType;

      const terminalTypesCollection: ITerminalTypes[] = [{ id: 53086 }];
      jest.spyOn(terminalTypesService, 'query').mockReturnValue(of(new HttpResponse({ body: terminalTypesCollection })));
      const additionalTerminalTypes = [terminalType];
      const expectedCollection: ITerminalTypes[] = [...additionalTerminalTypes, ...terminalTypesCollection];
      jest.spyOn(terminalTypesService, 'addTerminalTypesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ terminalsAndPOS });
      comp.ngOnInit();

      expect(terminalTypesService.query).toHaveBeenCalled();
      expect(terminalTypesService.addTerminalTypesToCollectionIfMissing).toHaveBeenCalledWith(
        terminalTypesCollection,
        ...additionalTerminalTypes
      );
      expect(comp.terminalTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TerminalFunctions query and add missing value', () => {
      const terminalsAndPOS: ITerminalsAndPOS = { id: 456 };
      const terminalFunctionality: ITerminalFunctions = { id: 69434 };
      terminalsAndPOS.terminalFunctionality = terminalFunctionality;

      const terminalFunctionsCollection: ITerminalFunctions[] = [{ id: 59792 }];
      jest.spyOn(terminalFunctionsService, 'query').mockReturnValue(of(new HttpResponse({ body: terminalFunctionsCollection })));
      const additionalTerminalFunctions = [terminalFunctionality];
      const expectedCollection: ITerminalFunctions[] = [...additionalTerminalFunctions, ...terminalFunctionsCollection];
      jest.spyOn(terminalFunctionsService, 'addTerminalFunctionsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ terminalsAndPOS });
      comp.ngOnInit();

      expect(terminalFunctionsService.query).toHaveBeenCalled();
      expect(terminalFunctionsService.addTerminalFunctionsToCollectionIfMissing).toHaveBeenCalledWith(
        terminalFunctionsCollection,
        ...additionalTerminalFunctions
      );
      expect(comp.terminalFunctionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CountySubCountyCode query and add missing value', () => {
      const terminalsAndPOS: ITerminalsAndPOS = { id: 456 };
      const physicalLocation: ICountySubCountyCode = { id: 8455 };
      terminalsAndPOS.physicalLocation = physicalLocation;

      const countySubCountyCodeCollection: ICountySubCountyCode[] = [{ id: 59731 }];
      jest.spyOn(countySubCountyCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: countySubCountyCodeCollection })));
      const additionalCountySubCountyCodes = [physicalLocation];
      const expectedCollection: ICountySubCountyCode[] = [...additionalCountySubCountyCodes, ...countySubCountyCodeCollection];
      jest.spyOn(countySubCountyCodeService, 'addCountySubCountyCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ terminalsAndPOS });
      comp.ngOnInit();

      expect(countySubCountyCodeService.query).toHaveBeenCalled();
      expect(countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing).toHaveBeenCalledWith(
        countySubCountyCodeCollection,
        ...additionalCountySubCountyCodes
      );
      expect(comp.countySubCountyCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InstitutionCode query and add missing value', () => {
      const terminalsAndPOS: ITerminalsAndPOS = { id: 456 };
      const bankId: IInstitutionCode = { id: 5803 };
      terminalsAndPOS.bankId = bankId;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 69432 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [bankId];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ terminalsAndPOS });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankBranchCode query and add missing value', () => {
      const terminalsAndPOS: ITerminalsAndPOS = { id: 456 };
      const branchId: IBankBranchCode = { id: 96926 };
      terminalsAndPOS.branchId = branchId;

      const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 58442 }];
      jest.spyOn(bankBranchCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: bankBranchCodeCollection })));
      const additionalBankBranchCodes = [branchId];
      const expectedCollection: IBankBranchCode[] = [...additionalBankBranchCodes, ...bankBranchCodeCollection];
      jest.spyOn(bankBranchCodeService, 'addBankBranchCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ terminalsAndPOS });
      comp.ngOnInit();

      expect(bankBranchCodeService.query).toHaveBeenCalled();
      expect(bankBranchCodeService.addBankBranchCodeToCollectionIfMissing).toHaveBeenCalledWith(
        bankBranchCodeCollection,
        ...additionalBankBranchCodes
      );
      expect(comp.bankBranchCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const terminalsAndPOS: ITerminalsAndPOS = { id: 456 };
      const terminalType: ITerminalTypes = { id: 71303 };
      terminalsAndPOS.terminalType = terminalType;
      const terminalFunctionality: ITerminalFunctions = { id: 27705 };
      terminalsAndPOS.terminalFunctionality = terminalFunctionality;
      const physicalLocation: ICountySubCountyCode = { id: 1637 };
      terminalsAndPOS.physicalLocation = physicalLocation;
      const bankId: IInstitutionCode = { id: 49472 };
      terminalsAndPOS.bankId = bankId;
      const branchId: IBankBranchCode = { id: 89679 };
      terminalsAndPOS.branchId = branchId;

      activatedRoute.data = of({ terminalsAndPOS });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(terminalsAndPOS));
      expect(comp.terminalTypesSharedCollection).toContain(terminalType);
      expect(comp.terminalFunctionsSharedCollection).toContain(terminalFunctionality);
      expect(comp.countySubCountyCodesSharedCollection).toContain(physicalLocation);
      expect(comp.institutionCodesSharedCollection).toContain(bankId);
      expect(comp.bankBranchCodesSharedCollection).toContain(branchId);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TerminalsAndPOS>>();
      const terminalsAndPOS = { id: 123 };
      jest.spyOn(terminalsAndPOSService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminalsAndPOS });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: terminalsAndPOS }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(terminalsAndPOSService.update).toHaveBeenCalledWith(terminalsAndPOS);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TerminalsAndPOS>>();
      const terminalsAndPOS = new TerminalsAndPOS();
      jest.spyOn(terminalsAndPOSService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminalsAndPOS });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: terminalsAndPOS }));
      saveSubject.complete();

      // THEN
      expect(terminalsAndPOSService.create).toHaveBeenCalledWith(terminalsAndPOS);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TerminalsAndPOS>>();
      const terminalsAndPOS = { id: 123 };
      jest.spyOn(terminalsAndPOSService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminalsAndPOS });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(terminalsAndPOSService.update).toHaveBeenCalledWith(terminalsAndPOS);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTerminalTypesById', () => {
      it('Should return tracked TerminalTypes primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTerminalTypesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTerminalFunctionsById', () => {
      it('Should return tracked TerminalFunctions primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTerminalFunctionsById(0, entity);
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
  });
});
