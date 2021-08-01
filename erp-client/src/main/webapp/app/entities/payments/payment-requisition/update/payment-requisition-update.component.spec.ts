jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentRequisitionService } from '../service/payment-requisition.service';
import { IPaymentRequisition, PaymentRequisition } from '../payment-requisition.model';

import { PaymentRequisitionUpdateComponent } from './payment-requisition-update.component';

describe('Component Tests', () => {
  describe('PaymentRequisition Management Update Component', () => {
    let comp: PaymentRequisitionUpdateComponent;
    let fixture: ComponentFixture<PaymentRequisitionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentRequisitionService: PaymentRequisitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentRequisitionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PaymentRequisitionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentRequisitionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentRequisitionService = TestBed.inject(PaymentRequisitionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const paymentRequisition: IPaymentRequisition = { id: 456 };

        activatedRoute.data = of({ paymentRequisition });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paymentRequisition));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentRequisition>>();
        const paymentRequisition = { id: 123 };
        jest.spyOn(paymentRequisitionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentRequisition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentRequisition }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(paymentRequisitionService.update).toHaveBeenCalledWith(paymentRequisition);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentRequisition>>();
        const paymentRequisition = new PaymentRequisition();
        jest.spyOn(paymentRequisitionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentRequisition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentRequisition }));
        saveSubject.complete();

        // THEN
        expect(paymentRequisitionService.create).toHaveBeenCalledWith(paymentRequisition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentRequisition>>();
        const paymentRequisition = { id: 123 };
        jest.spyOn(paymentRequisitionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentRequisition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(paymentRequisitionService.update).toHaveBeenCalledWith(paymentRequisition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
