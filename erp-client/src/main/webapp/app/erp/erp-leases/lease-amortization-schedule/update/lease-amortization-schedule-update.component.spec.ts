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

import { LeaseLiabilityService } from '../../lease-liability/service/lease-liability.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeaseAmortizationScheduleService } from '../service/lease-amortization-schedule.service';
import { ILeaseAmortizationSchedule, LeaseAmortizationSchedule } from '../lease-amortization-schedule.model';

import { LeaseAmortizationScheduleUpdateComponent } from './lease-amortization-schedule-update.component';
import { ILeaseLiability } from '../../lease-liability/lease-liability.model';

describe('LeaseAmortizationSchedule Management Update Component', () => {
  let comp: LeaseAmortizationScheduleUpdateComponent;
  let fixture: ComponentFixture<LeaseAmortizationScheduleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaseAmortizationScheduleService: LeaseAmortizationScheduleService;
  let leaseLiabilityService: LeaseLiabilityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseAmortizationScheduleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LeaseAmortizationScheduleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaseAmortizationScheduleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaseAmortizationScheduleService = TestBed.inject(LeaseAmortizationScheduleService);
    leaseLiabilityService = TestBed.inject(LeaseLiabilityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call LeaseLiability query and add missing value', () => {
      const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 456 };
      const leaseLiability: ILeaseLiability = { id: 36475 };
      leaseAmortizationSchedule.leaseLiability = leaseLiability;

      const leaseLiabilityCollection: ILeaseLiability[] = [{ id: 51479 }];
      jest.spyOn(leaseLiabilityService, 'query').mockReturnValue(of(new HttpResponse({ body: leaseLiabilityCollection })));
      const additionalLeaseLiabilities = [leaseLiability];
      const expectedCollection: ILeaseLiability[] = [...additionalLeaseLiabilities, ...leaseLiabilityCollection];
      jest.spyOn(leaseLiabilityService, 'addLeaseLiabilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaseAmortizationSchedule });
      comp.ngOnInit();

      expect(leaseLiabilityService.query).toHaveBeenCalled();
      expect(leaseLiabilityService.addLeaseLiabilityToCollectionIfMissing).toHaveBeenCalledWith(
        leaseLiabilityCollection,
        ...additionalLeaseLiabilities
      );
      expect(comp.leaseLiabilitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leaseAmortizationSchedule: ILeaseAmortizationSchedule = { id: 456 };
      const leaseLiability: ILeaseLiability = { id: 94172 };
      leaseAmortizationSchedule.leaseLiability = leaseLiability;

      activatedRoute.data = of({ leaseAmortizationSchedule });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(leaseAmortizationSchedule));
      expect(comp.leaseLiabilitiesSharedCollection).toContain(leaseLiability);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseAmortizationSchedule>>();
      const leaseAmortizationSchedule = { id: 123 };
      jest.spyOn(leaseAmortizationScheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseAmortizationSchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseAmortizationSchedule }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaseAmortizationScheduleService.update).toHaveBeenCalledWith(leaseAmortizationSchedule);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseAmortizationSchedule>>();
      const leaseAmortizationSchedule = new LeaseAmortizationSchedule();
      jest.spyOn(leaseAmortizationScheduleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseAmortizationSchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaseAmortizationSchedule }));
      saveSubject.complete();

      // THEN
      expect(leaseAmortizationScheduleService.create).toHaveBeenCalledWith(leaseAmortizationSchedule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeaseAmortizationSchedule>>();
      const leaseAmortizationSchedule = { id: 123 };
      jest.spyOn(leaseAmortizationScheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaseAmortizationSchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaseAmortizationScheduleService.update).toHaveBeenCalledWith(leaseAmortizationSchedule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLeaseLiabilityById', () => {
      it('Should return tracked LeaseLiability primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLeaseLiabilityById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
