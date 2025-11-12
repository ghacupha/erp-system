///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { ErpCommonModule } from '../../../erp-common/erp-common.module';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { JobSheetService } from '../service/job-sheet.service';
import { IJobSheet, JobSheet } from '../job-sheet.model';

import { JobSheetUpdateComponent } from './job-sheet-update.component';
import { BusinessStampService } from '../../business-stamp/service/business-stamp.service';
import { PaymentLabelService } from '../../../erp-pages/payment-label/service/payment-label.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { IBusinessStamp } from '../../business-stamp/business-stamp.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IPaymentLabel } from '../../../erp-pages/payment-label/payment-label.model';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';

describe('JobSheet Management Update Component', () => {
  let comp: JobSheetUpdateComponent;
  let fixture: ComponentFixture<JobSheetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let jobSheetService: JobSheetService;
  let dealerService: DealerService;
  let businessStampService: BusinessStampService;
  let placeholderService: PlaceholderService;
  let paymentLabelService: PaymentLabelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ErpCommonModule, HttpClientTestingModule],
      declarations: [JobSheetUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(JobSheetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JobSheetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    jobSheetService = TestBed.inject(JobSheetService);
    dealerService = TestBed.inject(DealerService);
    businessStampService = TestBed.inject(BusinessStampService);
    placeholderService = TestBed.inject(PlaceholderService);
    paymentLabelService = TestBed.inject(PaymentLabelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dealer query and add missing value', () => {
      const jobSheet: IJobSheet = { id: 456 };
      const biller: IDealer = { id: 78496 };
      jobSheet.biller = biller;
      const signatories: IDealer[] = [{ id: 35673 }];
      jobSheet.signatories = signatories;
      const contactPerson: IDealer = { id: 74999 };
      jobSheet.contactPerson = contactPerson;

      const dealerCollection: IDealer[] = [{ id: 86194 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [biller, ...signatories, contactPerson];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ jobSheet });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessStamp query and add missing value', () => {
      const jobSheet: IJobSheet = { id: 456 };
      const businessStamps: IBusinessStamp[] = [{ id: 10260 }];
      jobSheet.businessStamps = businessStamps;

      const businessStampCollection: IBusinessStamp[] = [{ id: 50045 }];
      jest.spyOn(businessStampService, 'query').mockReturnValue(of(new HttpResponse({ body: businessStampCollection })));
      const additionalBusinessStamps = [...businessStamps];
      const expectedCollection: IBusinessStamp[] = [...additionalBusinessStamps, ...businessStampCollection];
      jest.spyOn(businessStampService, 'addBusinessStampToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ jobSheet });
      comp.ngOnInit();

      expect(businessStampService.query).toHaveBeenCalled();
      expect(businessStampService.addBusinessStampToCollectionIfMissing).toHaveBeenCalledWith(
        businessStampCollection,
        ...additionalBusinessStamps
      );
      expect(comp.businessStampsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const jobSheet: IJobSheet = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 61982 }];
      jobSheet.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 26922 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ jobSheet });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PaymentLabel query and add missing value', () => {
      const jobSheet: IJobSheet = { id: 456 };
      const paymentLabels: IPaymentLabel[] = [{ id: 94561 }];
      jobSheet.paymentLabels = paymentLabels;

      const paymentLabelCollection: IPaymentLabel[] = [{ id: 93546 }];
      jest.spyOn(paymentLabelService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentLabelCollection })));
      const additionalPaymentLabels = [...paymentLabels];
      const expectedCollection: IPaymentLabel[] = [...additionalPaymentLabels, ...paymentLabelCollection];
      jest.spyOn(paymentLabelService, 'addPaymentLabelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ jobSheet });
      comp.ngOnInit();

      expect(paymentLabelService.query).toHaveBeenCalled();
      expect(paymentLabelService.addPaymentLabelToCollectionIfMissing).toHaveBeenCalledWith(
        paymentLabelCollection,
        ...additionalPaymentLabels
      );
      expect(comp.paymentLabelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const jobSheet: IJobSheet = { id: 456 };
      const biller: IDealer = { id: 98308 };
      jobSheet.biller = biller;
      const signatories: IDealer = { id: 34363 };
      jobSheet.signatories = [signatories];
      const contactPerson: IDealer = { id: 87909 };
      jobSheet.contactPerson = contactPerson;
      const businessStamps: IBusinessStamp = { id: 48412 };
      jobSheet.businessStamps = [businessStamps];
      const placeholders: IPlaceholder = { id: 93404 };
      jobSheet.placeholders = [placeholders];
      const paymentLabels: IPaymentLabel = { id: 11910 };
      jobSheet.paymentLabels = [paymentLabels];

      activatedRoute.data = of({ jobSheet });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(jobSheet));
      expect(comp.dealersSharedCollection).toContain(biller);
      expect(comp.dealersSharedCollection).toContain(signatories);
      expect(comp.dealersSharedCollection).toContain(contactPerson);
      expect(comp.businessStampsSharedCollection).toContain(businessStamps);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.paymentLabelsSharedCollection).toContain(paymentLabels);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<JobSheet>>();
      const jobSheet = { id: 123 };
      jest.spyOn(jobSheetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobSheet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jobSheet }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(jobSheetService.update).toHaveBeenCalledWith(jobSheet);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<JobSheet>>();
      const jobSheet = new JobSheet();
      jest.spyOn(jobSheetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobSheet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jobSheet }));
      saveSubject.complete();

      // THEN
      expect(jobSheetService.create).toHaveBeenCalledWith(jobSheet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<JobSheet>>();
      const jobSheet = { id: 123 };
      jest.spyOn(jobSheetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobSheet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(jobSheetService.update).toHaveBeenCalledWith(jobSheet);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDealerById', () => {
      it('Should return tracked Dealer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBusinessStampById', () => {
      it('Should return tracked BusinessStamp primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBusinessStampById(0, entity);
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

    describe('trackPaymentLabelById', () => {
      it('Should return tracked PaymentLabel primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPaymentLabelById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedDealer', () => {
      it('Should return option if no Dealer is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedDealer(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Dealer for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedDealer(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Dealer is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedDealer(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedBusinessStamp', () => {
      it('Should return option if no BusinessStamp is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBusinessStamp(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected BusinessStamp for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBusinessStamp(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this BusinessStamp is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBusinessStamp(option, [selected]);
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
  });
});
