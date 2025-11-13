///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { LeasePaymentService } from '../service/lease-payment.service';
import { ILeasePayment, LeasePayment } from '../lease-payment.model';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';

import { LeasePaymentUpdateComponent } from './lease-payment-update.component';

describe('LeasePayment Management Update Component', () => {
  let comp: LeasePaymentUpdateComponent;
  let fixture: ComponentFixture<LeasePaymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leasePaymentService: LeasePaymentService;
  let iFRS16LeaseContractService: IFRS16LeaseContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeasePaymentUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LeasePaymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeasePaymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leasePaymentService = TestBed.inject(LeasePaymentService);
    iFRS16LeaseContractService = TestBed.inject(IFRS16LeaseContractService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call IFRS16LeaseContract query and add missing value', () => {
      const leasePayment: ILeasePayment = { id: 456 };
      const leaseContract: IIFRS16LeaseContract = { id: 74388 };
      leasePayment.leaseContract = leaseContract;

      const iFRS16LeaseContractCollection: IIFRS16LeaseContract[] = [{ id: 96106 }];
      jest.spyOn(iFRS16LeaseContractService, 'query').mockReturnValue(of(new HttpResponse({ body: iFRS16LeaseContractCollection })));
      const additionalIFRS16LeaseContracts = [leaseContract];
      const expectedCollection: IIFRS16LeaseContract[] = [...additionalIFRS16LeaseContracts, ...iFRS16LeaseContractCollection];
      jest.spyOn(iFRS16LeaseContractService, 'addIFRS16LeaseContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leasePayment });
      comp.ngOnInit();

      expect(iFRS16LeaseContractService.query).toHaveBeenCalled();
      expect(iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing).toHaveBeenCalledWith(
        iFRS16LeaseContractCollection,
        ...additionalIFRS16LeaseContracts
      );
      expect(comp.iFRS16LeaseContractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leasePayment: ILeasePayment = { id: 456 };
      const leaseContract: IIFRS16LeaseContract = { id: 33887 };
      leasePayment.leaseContract = leaseContract;

      activatedRoute.data = of({ leasePayment });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(leasePayment));
      expect(comp.iFRS16LeaseContractsSharedCollection).toContain(leaseContract);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeasePayment>>();
      const leasePayment = { id: 123 };
      jest.spyOn(leasePaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leasePayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leasePayment }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(leasePaymentService.update).toHaveBeenCalledWith(leasePayment);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeasePayment>>();
      const leasePayment = new LeasePayment();
      jest.spyOn(leasePaymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leasePayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leasePayment }));
      saveSubject.complete();

      // THEN
      expect(leasePaymentService.create).toHaveBeenCalledWith(leasePayment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LeasePayment>>();
      const leasePayment = { id: 123 };
      jest.spyOn(leasePaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leasePayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leasePaymentService.update).toHaveBeenCalledWith(leasePayment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackIFRS16LeaseContractById', () => {
      it('Should return tracked IFRS16LeaseContract primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIFRS16LeaseContractById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
