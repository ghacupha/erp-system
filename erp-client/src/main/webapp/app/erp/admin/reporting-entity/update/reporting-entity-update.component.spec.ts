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

import { ReportingEntityService } from '../service/reporting-entity.service';
import { IReportingEntity, ReportingEntity } from '../reporting-entity.model';

import { ReportingEntityUpdateComponent } from './reporting-entity-update.component';
import { ITransactionAccount } from '../../../erp-accounts/transaction-account/transaction-account.model';
import { SettlementCurrencyService } from '../../../erp-settlements/settlement-currency/service/settlement-currency.service';
import { TransactionAccountService } from '../../../erp-accounts/transaction-account/service/transaction-account.service';
import { ISettlementCurrency } from '../../../erp-settlements/settlement-currency/settlement-currency.model';

describe('ReportingEntity Management Update Component', () => {
  let comp: ReportingEntityUpdateComponent;
  let fixture: ComponentFixture<ReportingEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportingEntityService: ReportingEntityService;
  let settlementCurrencyService: SettlementCurrencyService;
  let transactionAccountService: TransactionAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ReportingEntityUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ReportingEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportingEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportingEntityService = TestBed.inject(ReportingEntityService);
    settlementCurrencyService = TestBed.inject(SettlementCurrencyService);
    transactionAccountService = TestBed.inject(TransactionAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call reportingCurrency query and add missing value', () => {
      const reportingEntity: IReportingEntity = { id: 456 };
      const reportingCurrency: ISettlementCurrency = { id: 69411 };
      reportingEntity.reportingCurrency = reportingCurrency;

      const reportingCurrencyCollection: ISettlementCurrency[] = [{ id: 42452 }];
      jest.spyOn(settlementCurrencyService, 'query').mockReturnValue(of(new HttpResponse({ body: reportingCurrencyCollection })));
      const expectedCollection: ISettlementCurrency[] = [reportingCurrency, ...reportingCurrencyCollection];
      jest.spyOn(settlementCurrencyService, 'addSettlementCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportingEntity });
      comp.ngOnInit();

      expect(settlementCurrencyService.query).toHaveBeenCalled();
      expect(settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        reportingCurrencyCollection,
        reportingCurrency
      );
      expect(comp.reportingCurrenciesCollection).toEqual(expectedCollection);
    });

    it('Should call retainedEarningsAccount query and add missing value', () => {
      const reportingEntity: IReportingEntity = { id: 456 };
      const retainedEarningsAccount: ITransactionAccount = { id: 78590 };
      reportingEntity.retainedEarningsAccount = retainedEarningsAccount;

      const retainedEarningsAccountCollection: ITransactionAccount[] = [{ id: 81291 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: retainedEarningsAccountCollection })));
      const expectedCollection: ITransactionAccount[] = [retainedEarningsAccount, ...retainedEarningsAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportingEntity });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        retainedEarningsAccountCollection,
        retainedEarningsAccount
      );
      expect(comp.retainedEarningsAccountsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportingEntity: IReportingEntity = { id: 456 };
      const reportingCurrency: ISettlementCurrency = { id: 49638 };
      reportingEntity.reportingCurrency = reportingCurrency;
      const retainedEarningsAccount: ITransactionAccount = { id: 75433 };
      reportingEntity.retainedEarningsAccount = retainedEarningsAccount;

      activatedRoute.data = of({ reportingEntity });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(reportingEntity));
      expect(comp.reportingCurrenciesCollection).toContain(reportingCurrency);
      expect(comp.retainedEarningsAccountsCollection).toContain(retainedEarningsAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportingEntity>>();
      const reportingEntity = { id: 123 };
      jest.spyOn(reportingEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingEntity }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportingEntityService.update).toHaveBeenCalledWith(reportingEntity);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportingEntity>>();
      const reportingEntity = new ReportingEntity();
      jest.spyOn(reportingEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingEntity }));
      saveSubject.complete();

      // THEN
      expect(reportingEntityService.create).toHaveBeenCalledWith(reportingEntity);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportingEntity>>();
      const reportingEntity = { id: 123 };
      jest.spyOn(reportingEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportingEntityService.update).toHaveBeenCalledWith(reportingEntity);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSettlementCurrencyById', () => {
      it('Should return tracked SettlementCurrency primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSettlementCurrencyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTransactionAccountById', () => {
      it('Should return tracked TransactionAccount primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransactionAccountById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
