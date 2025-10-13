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

import { AgentBankingActivityService } from '../service/agent-banking-activity.service';
import { IAgentBankingActivity, AgentBankingActivity } from '../agent-banking-activity.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IBankTransactionType } from 'app/entities/gdi/bank-transaction-type/bank-transaction-type.model';
import { BankTransactionTypeService } from 'app/entities/gdi/bank-transaction-type/service/bank-transaction-type.service';

import { AgentBankingActivityUpdateComponent } from './agent-banking-activity-update.component';

describe('AgentBankingActivity Management Update Component', () => {
  let comp: AgentBankingActivityUpdateComponent;
  let fixture: ComponentFixture<AgentBankingActivityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agentBankingActivityService: AgentBankingActivityService;
  let institutionCodeService: InstitutionCodeService;
  let bankBranchCodeService: BankBranchCodeService;
  let bankTransactionTypeService: BankTransactionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AgentBankingActivityUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AgentBankingActivityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgentBankingActivityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agentBankingActivityService = TestBed.inject(AgentBankingActivityService);
    institutionCodeService = TestBed.inject(InstitutionCodeService);
    bankBranchCodeService = TestBed.inject(BankBranchCodeService);
    bankTransactionTypeService = TestBed.inject(BankTransactionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InstitutionCode query and add missing value', () => {
      const agentBankingActivity: IAgentBankingActivity = { id: 456 };
      const bankCode: IInstitutionCode = { id: 68792 };
      agentBankingActivity.bankCode = bankCode;

      const institutionCodeCollection: IInstitutionCode[] = [{ id: 26448 }];
      jest.spyOn(institutionCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCodeCollection })));
      const additionalInstitutionCodes = [bankCode];
      const expectedCollection: IInstitutionCode[] = [...additionalInstitutionCodes, ...institutionCodeCollection];
      jest.spyOn(institutionCodeService, 'addInstitutionCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agentBankingActivity });
      comp.ngOnInit();

      expect(institutionCodeService.query).toHaveBeenCalled();
      expect(institutionCodeService.addInstitutionCodeToCollectionIfMissing).toHaveBeenCalledWith(
        institutionCodeCollection,
        ...additionalInstitutionCodes
      );
      expect(comp.institutionCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankBranchCode query and add missing value', () => {
      const agentBankingActivity: IAgentBankingActivity = { id: 456 };
      const branchCode: IBankBranchCode = { id: 59795 };
      agentBankingActivity.branchCode = branchCode;

      const bankBranchCodeCollection: IBankBranchCode[] = [{ id: 52180 }];
      jest.spyOn(bankBranchCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: bankBranchCodeCollection })));
      const additionalBankBranchCodes = [branchCode];
      const expectedCollection: IBankBranchCode[] = [...additionalBankBranchCodes, ...bankBranchCodeCollection];
      jest.spyOn(bankBranchCodeService, 'addBankBranchCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agentBankingActivity });
      comp.ngOnInit();

      expect(bankBranchCodeService.query).toHaveBeenCalled();
      expect(bankBranchCodeService.addBankBranchCodeToCollectionIfMissing).toHaveBeenCalledWith(
        bankBranchCodeCollection,
        ...additionalBankBranchCodes
      );
      expect(comp.bankBranchCodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankTransactionType query and add missing value', () => {
      const agentBankingActivity: IAgentBankingActivity = { id: 456 };
      const transactionType: IBankTransactionType = { id: 54370 };
      agentBankingActivity.transactionType = transactionType;

      const bankTransactionTypeCollection: IBankTransactionType[] = [{ id: 52342 }];
      jest.spyOn(bankTransactionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: bankTransactionTypeCollection })));
      const additionalBankTransactionTypes = [transactionType];
      const expectedCollection: IBankTransactionType[] = [...additionalBankTransactionTypes, ...bankTransactionTypeCollection];
      jest.spyOn(bankTransactionTypeService, 'addBankTransactionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agentBankingActivity });
      comp.ngOnInit();

      expect(bankTransactionTypeService.query).toHaveBeenCalled();
      expect(bankTransactionTypeService.addBankTransactionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        bankTransactionTypeCollection,
        ...additionalBankTransactionTypes
      );
      expect(comp.bankTransactionTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const agentBankingActivity: IAgentBankingActivity = { id: 456 };
      const bankCode: IInstitutionCode = { id: 37815 };
      agentBankingActivity.bankCode = bankCode;
      const branchCode: IBankBranchCode = { id: 54375 };
      agentBankingActivity.branchCode = branchCode;
      const transactionType: IBankTransactionType = { id: 8765 };
      agentBankingActivity.transactionType = transactionType;

      activatedRoute.data = of({ agentBankingActivity });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(agentBankingActivity));
      expect(comp.institutionCodesSharedCollection).toContain(bankCode);
      expect(comp.bankBranchCodesSharedCollection).toContain(branchCode);
      expect(comp.bankTransactionTypesSharedCollection).toContain(transactionType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AgentBankingActivity>>();
      const agentBankingActivity = { id: 123 };
      jest.spyOn(agentBankingActivityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agentBankingActivity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agentBankingActivity }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(agentBankingActivityService.update).toHaveBeenCalledWith(agentBankingActivity);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AgentBankingActivity>>();
      const agentBankingActivity = new AgentBankingActivity();
      jest.spyOn(agentBankingActivityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agentBankingActivity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agentBankingActivity }));
      saveSubject.complete();

      // THEN
      expect(agentBankingActivityService.create).toHaveBeenCalledWith(agentBankingActivity);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AgentBankingActivity>>();
      const agentBankingActivity = { id: 123 };
      jest.spyOn(agentBankingActivityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agentBankingActivity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agentBankingActivityService.update).toHaveBeenCalledWith(agentBankingActivity);
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

    describe('trackBankTransactionTypeById', () => {
      it('Should return tracked BankTransactionType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBankTransactionTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
