jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentLabelService } from '../service/payment-label.service';
import { IPaymentLabel, PaymentLabel } from '../payment-label.model';

import { PaymentLabelUpdateComponent } from './payment-label-update.component';

describe('Component Tests', () => {
  describe('PaymentLabel Management Update Component', () => {
    let comp: PaymentLabelUpdateComponent;
    let fixture: ComponentFixture<PaymentLabelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentLabelService: PaymentLabelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentLabelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PaymentLabelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentLabelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentLabelService = TestBed.inject(PaymentLabelService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PaymentLabel query and add missing value', () => {
        const paymentLabel: IPaymentLabel = { id: 456 };
        const containingPaymentLabel: IPaymentLabel = { id: 9251 };
        paymentLabel.containingPaymentLabel = containingPaymentLabel;

        const paymentLabelCollection: IPaymentLabel[] = [{ id: 89587 }];
        jest.spyOn(paymentLabelService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentLabelCollection })));
        const additionalPaymentLabels = [containingPaymentLabel];
        const expectedCollection: IPaymentLabel[] = [...additionalPaymentLabels, ...paymentLabelCollection];
        jest.spyOn(paymentLabelService, 'addPaymentLabelToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ paymentLabel });
        comp.ngOnInit();

        expect(paymentLabelService.query).toHaveBeenCalled();
        expect(paymentLabelService.addPaymentLabelToCollectionIfMissing).toHaveBeenCalledWith(
          paymentLabelCollection,
          ...additionalPaymentLabels
        );
        expect(comp.paymentLabelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const paymentLabel: IPaymentLabel = { id: 456 };
        const containingPaymentLabel: IPaymentLabel = { id: 4866 };
        paymentLabel.containingPaymentLabel = containingPaymentLabel;

        activatedRoute.data = of({ paymentLabel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paymentLabel));
        expect(comp.paymentLabelsSharedCollection).toContain(containingPaymentLabel);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentLabel>>();
        const paymentLabel = { id: 123 };
        jest.spyOn(paymentLabelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentLabel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentLabel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(paymentLabelService.update).toHaveBeenCalledWith(paymentLabel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentLabel>>();
        const paymentLabel = new PaymentLabel();
        jest.spyOn(paymentLabelService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentLabel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentLabel }));
        saveSubject.complete();

        // THEN
        expect(paymentLabelService.create).toHaveBeenCalledWith(paymentLabel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PaymentLabel>>();
        const paymentLabel = { id: 123 };
        jest.spyOn(paymentLabelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentLabel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(paymentLabelService.update).toHaveBeenCalledWith(paymentLabel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPaymentLabelById', () => {
        it('Should return tracked PaymentLabel primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentLabelById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
