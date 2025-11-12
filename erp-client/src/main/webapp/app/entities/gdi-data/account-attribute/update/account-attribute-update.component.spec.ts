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

import { AccountAttributeService } from '../service/account-attribute.service';
import { IAccountAttribute, AccountAttribute } from '../account-attribute.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IAccountOwnershipType } from 'app/entities/gdi/account-ownership-type/account-ownership-type.model';
import { AccountOwnershipTypeService } from 'app/entities/gdi/account-ownership-type/service/account-ownership-type.service';

import { AccountAttributeUpdateComponent } from './account-attribute-update.component';

describe('AccountAttribute Management Update Component', () => {
  let comp: AccountAttributeUpdateComponent;
  let fixture: ComponentFixture<AccountAttributeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountAttributeService: AccountAttributeService;
  let institutionCodeService: InstitutionCodeService;
  let bankBranchCodeService: BankBranchCodeService;
  let accountOwnershipTypeService: AccountOwnershipTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AccountAttributeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AccountAttributeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountAttributeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountAttributeService = TestBed.inject(AccountAttributeService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    bankBranchCodeService = TestBed.inject(BankBranchCodeService);
    accountOwnershipTypeService = TestBed.inject(AccountOwnershipTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InstitutionCode query and add missing value', () => {
      const accountAttribute: IAccountAttribute = { id: 456 };
      const bankCode: IInstitutionCode = { id: 46315 };
      accountAttribute.bankCode = bankCode;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 30178 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [bankCode];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountAttribute });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankBranchCode query and add missing value', () => {
      const accountAttribute: IAccountAttribute = { id: 456 };
      const branchCode: IBankBranchCode = { id: 92534 };
      accountAttribute.branchCode = branchCode;

      const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 31821 }];
      jest.spyOn(bankBranchCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: bankBranchCodeCollection })));
      const additionalBankBranchCodes = [branchCode];
      const expectedCollection: IBankBranchCode[] = [...additionalBankBranchCodes, ...bankBranchCodeCollection];
      jest.spyOn(bankBranchCodeService, 'addBankBranchCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountAttribute });
      comp.ngOnInit();

      expect(bankBranchCodeService.query).toHaveBeenCalled();
      expect(bankBranchCodeService.addBankBranchCodeToCollectionIfMissing).toHaveBeenCalledWith(
        bankBranchCodeCollection,
        ...additionalBankBranchCodes
      );
      expect(comp.bankBranchCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountOwnershipType query and add missing value', () => {
      const accountAttribute: IAccountAttribute = { id: 456 };
      const accountOwnershipType: IAccountOwnershipType = { id: 48664 };
      accountAttribute.accountOwnershipType = accountOwnershipType;

      const accountOwnershipTypeCollection: IAccountOwnershipType[] = [{ id: 30485 }];
      jest.spyOn(accountOwnershipTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: accountOwnershipTypeCollection })));
      const additionalAccountOwnershipTypes = [accountOwnershipType];
      const expectedCollection: IAccountOwnershipType[] = [...additionalAccountOwnershipTypes, ...accountOwnershipTypeCollection];
      jest.spyOn(accountOwnershipTypeService, 'addAccountOwnershipTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountAttribute });
      comp.ngOnInit();

      expect(accountOwnershipTypeService.query).toHaveBeenCalled();
      expect(accountOwnershipTypeService.addAccountOwnershipTypeToCollectionIfMissing).toHaveBeenCalledWith(
        accountOwnershipTypeCollection,
        ...additionalAccountOwnershipTypes
      );
      expect(comp.accountOwnershipTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accountAttribute: IAccountAttribute = { id: 456 };
      const bankCode: IInstitutionCode = { id: 87278 };
      accountAttribute.bankCode = bankCode;
      const branchCode: IBankBranchCode = { id: 48656 };
      accountAttribute.branchCode = branchCode;
      const accountOwnershipType: IAccountOwnershipType = { id: 63759 };
      accountAttribute.accountOwnershipType = accountOwnershipType;

      activatedRoute.data = of({ accountAttribute });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(accountAttribute));
      expect(comp.institutionCodesSharedCollection).toContain(bankCode);
      expect(comp.bankBranchCodesSharedCollection).toContain(branchCode);
      expect(comp.accountOwnershipTypesSharedCollection).toContain(accountOwnershipType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountAttribute>>();
      const accountAttribute = { id: 123 };
      jest.spyOn(accountAttributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountAttribute }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountAttributeService.update).toHaveBeenCalledWith(accountAttribute);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountAttribute>>();
      const accountAttribute = new AccountAttribute();
      jest.spyOn(accountAttributeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountAttribute }));
      saveSubject.complete();

      // THEN
      expect(accountAttributeService.create).toHaveBeenCalledWith(accountAttribute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountAttribute>>();
      const accountAttribute = { id: 123 };
      jest.spyOn(accountAttributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountAttributeService.update).toHaveBeenCalledWith(accountAttribute);
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

    describe('trackAccountOwnershipTypeById', () => {
      it('Should return tracked AccountOwnershipType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAccountOwnershipTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
