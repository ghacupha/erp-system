jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SignedPaymentService } from '../service/signed-payment.service';
import { ISignedPayment, SignedPayment } from '../signed-payment.model';
import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { PaymentLabelService } from 'app/entities/payment-label/service/payment-label.service';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/erpService/placeholder/service/placeholder.service';

import { SignedPaymentUpdateComponent } from './signed-payment-update.component';

describe('Component Tests', () => {
  describe('SignedPayment Management Update Component', () => {
    let comp: SignedPaymentUpdateComponent;
    let fixture: ComponentFixture<SignedPaymentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let signedPaymentService: SignedPaymentService;
    let paymentLabelService: PaymentLabelService;
    let placeholderService: PlaceholderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SignedPaymentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SignedPaymentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SignedPaymentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      signedPaymentService = TestBed.inject(SignedPaymentService);
      paymentLabelService = TestBed.inject(PaymentLabelService);
      placeholderService = TestBed.inject(PlaceholderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PaymentLabel query and add missing value', () => {
        const signedPayment: ISignedPayment = { id: 456 };
        const paymentLabels: IPaymentLabel[] = [{ id: 60214 }];
        signedPayment.paymentLabels = paymentLabels;

        const paymentLabelCollection: IPaymentLabel[] = [{ id: 40943 }];
        jest.spyOn(paymentLabelService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentLabelCollection })));
        const additionalPaymentLabels = [...paymentLabels];
        const expectedCollection: IPaymentLabel[] = [...additionalPaymentLabels, ...paymentLabelCollection];
        jest.spyOn(paymentLabelService, 'addPaymentLabelToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ signedPayment });
        comp.ngOnInit();

        expect(paymentLabelService.query).toHaveBeenCalled();
        expect(paymentLabelService.addPaymentLabelToCollectionIfMissing).toHaveBeenCalledWith(
          paymentLabelCollection,
          ...additionalPaymentLabels
        );
        expect(comp.paymentLabelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Placeholder query and add missing value', () => {
        const signedPayment: ISignedPayment = { id: 456 };
        const placeholders: IPlaceholder[] = [{ id: 75417 }];
        signedPayment.placeholders = placeholders;

        const placeholderCollection: IPlaceholder[] = [{ id: 2425 }];
        jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
        const additionalPlaceholders = [...placeholders];
        const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
        jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ signedPayment });
        comp.ngOnInit();

        expect(placeholderService.query).toHaveBeenCalled();
        expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
          placeholderCollection,
          ...additionalPlaceholders
        );
        expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const signedPayment: ISignedPayment = { id: 456 };
        const paymentLabels: IPaymentLabel = { id: 61040 };
        signedPayment.paymentLabels = [paymentLabels];
        const placeholders: IPlaceholder = { id: 24907 };
        signedPayment.placeholders = [placeholders];

        activatedRoute.data = of({ signedPayment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(signedPayment));
        expect(comp.paymentLabelsSharedCollection).toContain(paymentLabels);
        expect(comp.placeholdersSharedCollection).toContain(placeholders);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SignedPayment>>();
        const signedPayment = { id: 123 };
        jest.spyOn(signedPaymentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ signedPayment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: signedPayment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(signedPaymentService.update).toHaveBeenCalledWith(signedPayment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SignedPayment>>();
        const signedPayment = new SignedPayment();
        jest.spyOn(signedPaymentService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ signedPayment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: signedPayment }));
        saveSubject.complete();

        // THEN
        expect(signedPaymentService.create).toHaveBeenCalledWith(signedPayment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SignedPayment>>();
        const signedPayment = { id: 123 };
        jest.spyOn(signedPaymentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ signedPayment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(signedPaymentService.update).toHaveBeenCalledWith(signedPayment);
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

      describe('trackPlaceholderById', () => {
        it('Should return tracked Placeholder primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPlaceholderById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedPaymentLabel', () => {
        it('Should return option if no PaymentLabel is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPaymentLabel(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected PaymentLabel for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPaymentLabel(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this PaymentLabel is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPaymentLabel(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedPlaceholder', () => {
        it('Should return option if no Placeholder is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPlaceholder(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Placeholder for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPlaceholder(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Placeholder is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPlaceholder(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
