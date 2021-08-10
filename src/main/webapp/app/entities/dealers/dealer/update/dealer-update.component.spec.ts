jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DealerService } from '../service/dealer.service';
import { IDealer, Dealer } from '../dealer.model';
import { IPayment } from 'app/entities/payments/payment/payment.model';
import { PaymentService } from 'app/entities/payments/payment/service/payment.service';

import { DealerUpdateComponent } from './dealer-update.component';

describe('Component Tests', () => {
  describe('Dealer Management Update Component', () => {
    let comp: DealerUpdateComponent;
    let fixture: ComponentFixture<DealerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dealerService: DealerService;
    let paymentService: PaymentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DealerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DealerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DealerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dealerService = TestBed.inject(DealerService);
      paymentService = TestBed.inject(PaymentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Payment query and add missing value', () => {
        const dealer: IDealer = { id: 456 };
        const payments: IPayment[] = [{ id: 76966 }];
        dealer.payments = payments;

        const paymentCollection: IPayment[] = [{ id: 93722 }];
        jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
        const additionalPayments = [...payments];
        const expectedCollection: IPayment[] = [...additionalPayments, ...paymentCollection];
        jest.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dealer });
        comp.ngOnInit();

        expect(paymentService.query).toHaveBeenCalled();
        expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(paymentCollection, ...additionalPayments);
        expect(comp.paymentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dealer: IDealer = { id: 456 };
        const payments: IPayment = { id: 11178 };
        dealer.payments = [payments];

        activatedRoute.data = of({ dealer });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dealer));
        expect(comp.paymentsSharedCollection).toContain(payments);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Dealer>>();
        const dealer = { id: 123 };
        jest.spyOn(dealerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dealer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dealer }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dealerService.update).toHaveBeenCalledWith(dealer);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Dealer>>();
        const dealer = new Dealer();
        jest.spyOn(dealerService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dealer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dealer }));
        saveSubject.complete();

        // THEN
        expect(dealerService.create).toHaveBeenCalledWith(dealer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Dealer>>();
        const dealer = { id: 123 };
        jest.spyOn(dealerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dealer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dealerService.update).toHaveBeenCalledWith(dealer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPaymentById', () => {
        it('Should return tracked Payment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedPayment', () => {
        it('Should return option if no Payment is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPayment(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Payment for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPayment(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Payment is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPayment(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
