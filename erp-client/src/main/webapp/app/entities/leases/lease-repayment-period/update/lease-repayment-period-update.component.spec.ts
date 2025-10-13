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

import { LeaseRepaymentPeriodService } from '../service/lease-repayment-period.service';
import { ILeaseRepaymentPeriod, LeaseRepaymentPeriod } from '../lease-repayment-period.model';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/system/fiscal-month/service/fiscal-month.service';

import { LeaseRepaymentPeriodUpdateComponent } from './lease-repayment-period-update.component';

describe('LeaseRepaymentPeriod Management Update Component', () => {
  let comp: LeaseRepaymentPeriodUpdateComponent;
  let fixture: ComponentFixture<LeaseRepaymentPeriodUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaseRepaymentPeriodService: LeaseRepaymentPeriodService;
  let fiscalMonthService: FiscalMonthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseRepaymentPeriodUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LeaseRepaymentPeriodUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaseRepaymentPeriodUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaseRepaymentPeriodService = TestBed.inject(LeaseRepaymentPeriodService);
    fiscalMonthService = TestBed.inject(FiscalMonthService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call fiscalMonth query and add missing value', () => {
      const leaseRepaymentPeriod: ILeaseRepaymentPeriod = { id: 456 };
      const fiscalMonth: IFiscalMonth = { id: 68376 };
      leaseRepaymentPeriod.fiscalMonth = fiscalMonth;

      const fiscalMonthCollection: IFiscalMonth[] = [{ id: 16286 }];
      jest.spyOn(fiscalMonthService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalMonthCollection })));
      const expectedCollection: IFiscalMonth[] = [fiscalMonth, ...fiscalMonthCollection];
      jest.spyOn(fiscalMonthService, 'addFiscalMonthToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseRepaymentPeriod });
      comp.ngOnInit();

      expect(fiscalMonthService.query).toHaveBeenCalled();
      expect(fiscalMonthService.addFiscalMonthToCollectionIfMissing).toHaveBeenCalledWith(fiscalMonthCollection, fiscalMonth);
      expect(comp.fiscalMonthsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leaseRepaymentPeriod: ILeaseRepaymentPeriod = { id: 456 };
      const fiscalMonth: IFiscalMonth = { id: 37542 };
      leaseRepaymentPeriod.fiscalMonth = fiscalMonth;

      activatedRoute.data = of({ leaseRepaymentPeriod });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(leaseRepaymentPeriod));
      expect(comp.fiscalMonthsCollection).toContain(fiscalMonth);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseRepaymentPeriod>>();
      const leaseRepaymentPeriod = { id: 123 };
      jest.spyOn(leaseRepaymentPeriodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseRepaymentPeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseRepaymentPeriod }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaseRepaymentPeriodService.update).toHaveBeenCalledWith(leaseRepaymentPeriod);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseRepaymentPeriod>>();
      const leaseRepaymentPeriod = new LeaseRepaymentPeriod();
      jest.spyOn(leaseRepaymentPeriodService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseRepaymentPeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseRepaymentPeriod }));
      saveSubject.complete();

      // THEN
      expect(leaseRepaymentPeriodService.create).toHaveBeenCalledWith(leaseRepaymentPeriod);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseRepaymentPeriod>>();
      const leaseRepaymentPeriod = { id: 123 };
      jest.spyOn(leaseRepaymentPeriodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseRepaymentPeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaseRepaymentPeriodService.update).toHaveBeenCalledWith(leaseRepaymentPeriod);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFiscalMonthById', () => {
      it('Should return tracked FiscalMonth primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalMonthById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
