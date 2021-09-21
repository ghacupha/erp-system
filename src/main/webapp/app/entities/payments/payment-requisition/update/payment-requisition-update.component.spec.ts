jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentRequisitionService } from '../service/payment-requisition.service';
import { IPaymentRequisition, PaymentRequisition } from '../payment-requisition.model';
import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { PaymentLabelService } from 'app/entities/payment-label/service/payment-label.service';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { DealerService } from 'app/entities/dealers/dealer/service/dealer.service';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/erpService/placeholder/service/placeholder.service';

import { PaymentRequisitionUpdateComponent } from './payment-requisition-update.component';

describe('Component Tests', () => {
  describe('PaymentRequisition Management Update Component', () => {
    let comp: PaymentRequisitionUpdateComponent;
    let fixture: ComponentFixture<PaymentRequisitionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentRequisitionService: PaymentRequisitionService;
    let paymentLabelService: PaymentLabelService;
    let dealerService: DealerService;
    let placeholderService: PlaceholderService;

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
      paymentLabelService = TestBed.inject(PaymentLabelService);
      dealerService = TestBed.inject(DealerService);
      placeholderService = TestBed.inject(PlaceholderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PaymentLabel query and add missing value', () => {
        const paymentRequisition: IPaymentRequisition = { id: 456 };
        const paymentLabels: IPaymentLabel[] = [{ id: 32074 }];
        paymentRequisition.paymentLabels = paymentLabels;

        const paymentLabelCollection: IPaymentLabel[] = [{ id: 24191 }];
        jest.spyOn(paymentLabelService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentLabelCollection })));
        const additionalPaymentLabels = [...paymentLabels];
        const expectedCollection: IPaymentLabel[] = [...additionalPaymentLabels, ...paymentLabelCollection];
        jest.spyOn(paymentLabelService, 'addPaymentLabelToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ paymentRequisition });
        comp.ngOnInit();

        expect(paymentLabelService.query).toHaveBeenCalled();
        expect(paymentLabelService.addPaymentLabelToCollectionIfMissing).toHaveBeenCalledWith(
          paymentLabelCollection,
          ...additionalPaymentLabels
        );
        expect(comp.paymentLabelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Dealer query and add missing value', () => {
        const paymentRequisition: IPaymentRequisition = { id: 456 };
        const dealer: IDealer = { id: 49433 };
        paymentRequisition.dealer = dealer;

        const dealerCollection: IDealer[] = [{ id: 8092 }];
        jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
        const additionalDealers = [dealer];
        const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
        jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ paymentRequisition });
        comp.ngOnInit();

        expect(dealerService.query).toHaveBeenCalled();
        expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
        expect(comp.dealersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Placeholder query and add missing value', () => {
        const paymentRequisition: IPaymentRequisition = { id: 456 };
        const placeholders: IPlaceholder[] = [{ id: 52864 }];
        paymentRequisition.placeholders = placeholders;

        const placeholderCollection: IPlaceholder[] = [{ id: 38640 }];
        jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
        const additionalPlaceholders = [...placeholders];
        const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
        jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ paymentRequisition });
        comp.ngOnInit();

        expect(placeholderService.query).toHaveBeenCalled();
        expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
          placeholderCollection,
          ...additionalPlaceholders
        );
        expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const paymentRequisition: IPaymentRequisition = { id: 456 };
        const paymentLabels: IPaymentLabel = { id: 7525 };
        paymentRequisition.paymentLabels = [paymentLabels];
        const dealer: IDealer = { id: 84854 };
        paymentRequisition.dealer = dealer;
        const placeholders: IPlaceholder = { id: 85634 };
        paymentRequisition.placeholders = [placeholders];

        activatedRoute.data = of({ paymentRequisition });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paymentRequisition));
        expect(comp.paymentLabelsSharedCollection).toContain(paymentLabels);
        expect(comp.dealersSharedCollection).toContain(dealer);
        expect(comp.placeholdersSharedCollection).toContain(placeholders);
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

    describe('Tracking relationships identifiers', () => {
      describe('trackPaymentLabelById', () => {
        it('Should return tracked PaymentLabel primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentLabelById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDealerById', () => {
        it('Should return tracked Dealer primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDealerById(0, entity);
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
