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

import { PlaceholderService } from '../../placeholder/service/placeholder.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentLabelUpdateComponent } from './payment-label-update.component';
import { PaymentLabelService } from 'app/erp/erp-pages/payment-label/service/payment-label.service';
import { IPaymentLabel, PaymentLabel } from 'app/erp/erp-pages/payment-label/payment-label.model';
import { ErpCommonModule } from 'app/erp/erp-common/erp-common.module';
import { IPlaceholder } from '../../placeholder/placeholder.model';

describe('Component Tests', () => {
  describe('PaymentLabel Management Update Component', () => {
    let comp: PaymentLabelUpdateComponent;
    let fixture: ComponentFixture<PaymentLabelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentLabelService: PaymentLabelService;
    let placeholderService: PlaceholderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpCommonModule, HttpClientTestingModule],
        declarations: [PaymentLabelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PaymentLabelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentLabelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentLabelService = TestBed.inject(PaymentLabelService);
      placeholderService = TestBed.inject(PlaceholderService);

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

      it('Should call Placeholder query and add missing value', () => {
        const paymentLabel: IPaymentLabel = { id: 456 };
        const placeholders: IPlaceholder[] = [{ id: 14078 }];
        paymentLabel.placeholders = placeholders;

        const placeholderCollection: IPlaceholder[] = [{ id: 52254 }];
        jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
        const additionalPlaceholders = [...placeholders];
        const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
        jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ paymentLabel });
        comp.ngOnInit();

        expect(placeholderService.query).toHaveBeenCalled();
        expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(
          placeholderCollection,
          ...additionalPlaceholders
        );
        expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const paymentLabel: IPaymentLabel = { id: 456 };
        const containingPaymentLabel: IPaymentLabel = { id: 4866 };
        paymentLabel.containingPaymentLabel = containingPaymentLabel;
        const placeholders: IPlaceholder = { id: 88585 };
        paymentLabel.placeholders = [placeholders];

        activatedRoute.data = of({ paymentLabel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paymentLabel));
        expect(comp.paymentLabelsSharedCollection).toContain(containingPaymentLabel);
        expect(comp.placeholdersSharedCollection).toContain(placeholders);
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

      describe('trackPlaceholderById', () => {
        it('Should return tracked Placeholder primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPlaceholderById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
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
