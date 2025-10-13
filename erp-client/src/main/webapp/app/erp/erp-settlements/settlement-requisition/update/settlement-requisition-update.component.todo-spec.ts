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

import { SettlementCurrencyService } from '../../settlement-currency/service/settlement-currency.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SettlementRequisitionService } from '../service/settlement-requisition.service';
import { ISettlementRequisition, SettlementRequisition } from '../settlement-requisition.model';

import { SettlementRequisitionUpdateComponent } from './settlement-requisition-update.component';
import { ISettlementCurrency } from '../../settlement-currency/settlement-currency.model';
import { IJobSheet } from '../../job-sheet/job-sheet.model';
import { IDeliveryNote } from '../../delivery-note/delivery-note.model';
import { IPaymentInvoice } from '../../payment-invoice/payment-invoice.model';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { PaymentInvoiceService } from '../../payment-invoice/service/payment-invoice.service';
import { JobSheetService } from '../../job-sheet/service/job-sheet.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { DeliveryNoteService } from '../../delivery-note/service/delivery-note.service';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';

// TODO Someday when we grow up we'll find out why the error says there's a null injector even when clearly
// TODO we are mocking it outright. It could be a problem with one of these libraries we have used on these
// TODO expensive component. And it is expensive, and also pivotal to almost every module that we are using
// TODO or data point that we are tracking

describe('SettlementRequisition Management Update Component', () => {
  let comp: SettlementRequisitionUpdateComponent;
  let fixture: ComponentFixture<SettlementRequisitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let settlementRequisitionService: SettlementRequisitionService;
  let settlementCurrencyService: SettlementCurrencyService;
  let applicationUserService: ApplicationUserService;
  let dealerService: DealerService;
  let paymentInvoiceService: PaymentInvoiceService;
  let deliveryNoteService: DeliveryNoteService;
  let jobSheetService: JobSheetService;
  let businessDocumentService: BusinessDocumentService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SettlementRequisitionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SettlementRequisitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SettlementRequisitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    settlementRequisitionService = TestBed.inject(SettlementRequisitionService);
    settlementCurrencyService = TestBed.inject(SettlementCurrencyService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    dealerService = TestBed.inject(DealerService);
    paymentInvoiceService = TestBed.inject(PaymentInvoiceService);
    deliveryNoteService = TestBed.inject(DeliveryNoteService);
    jobSheetService = TestBed.inject(JobSheetService);
    businessDocumentService = TestBed.inject(BusinessDocumentService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SettlementCurrency query and add missing value', () => {
      const settlementRequisition: ISettlementRequisition = { id: 456 };
      const settlementCurrency: ISettlementCurrency = { id: 49575 };
      settlementRequisition.settlementCurrency = settlementCurrency;

      const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 2417 }];
      jest.spyOn(settlementCurrencyService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCurrencyCollection })));
      const additionalSettlementCurrencies = [settlementCurrency];
      const expectedCollection: ISettlementCurrency[] = [...additionalSettlementCurrencies, ...settlementCurrencyCollection];
      jest.spyOn(settlementCurrencyService, 'addSettlementCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      expect(settlementCurrencyService.query).toHaveBeenCalled();
      expect(settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        settlementCurrencyCollection,
        ...additionalSettlementCurrencies
      );
      expect(comp.settlementCurrenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const settlementRequisition: ISettlementRequisition = { id: 456 };
      const currentOwner: IApplicationUser = { id: 81078 };
      settlementRequisition.currentOwner = currentOwner;
      const nativeOwner: IApplicationUser = { id: 11852 };
      settlementRequisition.nativeOwner = nativeOwner;

      const applicationUserCollection: IApplicationUser[] = [{ id: 29141 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [currentOwner, nativeOwner];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const settlementRequisition: ISettlementRequisition = { id: 456 };
      const nativeDepartment: IDealer = { id: 82155 };
      settlementRequisition.nativeDepartment = nativeDepartment;
      const biller: IDealer = { id: 6309 };
      settlementRequisition.biller = biller;
      const signatures: IDealer[] = [{ id: 37940 }];
      settlementRequisition.signatures = signatures;

      const dealerCollection: IDealer[] = [{ id: 20961 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [nativeDepartment, biller, ...signatures];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PaymentInvoice query and add missing value', () => {
      const settlementRequisition: ISettlementRequisition = { id: 456 };
      const paymentInvoices: IPaymentInvoice[] = [{ id: 61205 }];
      settlementRequisition.paymentInvoices = paymentInvoices;

      const paymentInvoiceCollection: IPaymentInvoice[] = [{ id: 74609 }];
      jest.spyOn(paymentInvoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentInvoiceCollection })));
      const additionalPaymentInvoices = [...paymentInvoices];
      const expectedCollection: IPaymentInvoice[] = [...additionalPaymentInvoices, ...paymentInvoiceCollection];
      jest.spyOn(paymentInvoiceService, 'addPaymentInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      expect(paymentInvoiceService.query).toHaveBeenCalled();
      expect(paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        paymentInvoiceCollection,
        ...additionalPaymentInvoices
      );
      expect(comp.paymentInvoicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DeliveryNote query and add missing value', () => {
      const settlementRequisition: ISettlementRequisition = { id: 456 };
      const deliveryNotes: IDeliveryNote[] = [{ id: 81720 }];
      settlementRequisition.deliveryNotes = deliveryNotes;

      const deliveryNoteCollection: IDeliveryNote[] = [{ id: 65137 }];
      jest.spyOn(deliveryNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryNoteCollection })));
      const additionalDeliveryNotes = [...deliveryNotes];
      const expectedCollection: IDeliveryNote[] = [...additionalDeliveryNotes, ...deliveryNoteCollection];
      jest.spyOn(deliveryNoteService, 'addDeliveryNoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      expect(deliveryNoteService.query).toHaveBeenCalled();
      expect(deliveryNoteService.addDeliveryNoteToCollectionIfMissing).toHaveBeenCalledWith(
        deliveryNoteCollection,
        ...additionalDeliveryNotes
      );
      expect(comp.deliveryNotesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call JobSheet query and add missing value', () => {
      const settlementRequisition: ISettlementRequisition = { id: 456 };
      const jobSheets: IJobSheet[] = [{ id: 29156 }];
      settlementRequisition.jobSheets = jobSheets;

      const jobSheetCollection: IJobSheet[] = [{ id: 42851 }];
      jest.spyOn(jobSheetService, 'query').mockReturnValue(of(new HttpResponse({ body: jobSheetCollection })));
      const additionalJobSheets = [...jobSheets];
      const expectedCollection: IJobSheet[] = [...additionalJobSheets, ...jobSheetCollection];
      jest.spyOn(jobSheetService, 'addJobSheetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      expect(jobSheetService.query).toHaveBeenCalled();
      expect(jobSheetService.addJobSheetToCollectionIfMissing).toHaveBeenCalledWith(jobSheetCollection, ...additionalJobSheets);
      expect(comp.jobSheetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessDocument query and add missing value', () => {
      const settlementRequisition: ISettlementRequisition = { id: 456 };
      const businessDocuments: IBusinessDocument[] = [{ id: 39417 }];
      settlementRequisition.businessDocuments = businessDocuments;

      const businessDocumentCollection: IBusinessDocument[] = [{ id: 13780 }];
      jest.spyOn(businessDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: businessDocumentCollection })));
      const additionalBusinessDocuments = [...businessDocuments];
      const expectedCollection: IBusinessDocument[] = [...additionalBusinessDocuments, ...businessDocumentCollection];
      jest.spyOn(businessDocumentService, 'addBusinessDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      expect(businessDocumentService.query).toHaveBeenCalled();
      expect(businessDocumentService.addBusinessDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        businessDocumentCollection,
        ...additionalBusinessDocuments
      );
      expect(comp.businessDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const settlementRequisition: ISettlementRequisition = { id: 456 };
      const applicationMappings: IUniversallyUniqueMapping[] = [{ id: 87011 }];
      settlementRequisition.applicationMappings = applicationMappings;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 37714 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...applicationMappings];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const settlementRequisition: ISettlementRequisition = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 38490 }];
      settlementRequisition.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 83159 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const settlementRequisition: ISettlementRequisition = { id: 456 };
      const settlementCurrency: ISettlementCurrency = { id: 18727 };
      settlementRequisition.settlementCurrency = settlementCurrency;
      const currentOwner: IApplicationUser = { id: 87953 };
      settlementRequisition.currentOwner = currentOwner;
      const nativeOwner: IApplicationUser = { id: 30908 };
      settlementRequisition.nativeOwner = nativeOwner;
      const nativeDepartment: IDealer = { id: 2651 };
      settlementRequisition.nativeDepartment = nativeDepartment;
      const biller: IDealer = { id: 86745 };
      settlementRequisition.biller = biller;
      const signatures: IDealer = { id: 51317 };
      settlementRequisition.signatures = [signatures];
      const paymentInvoices: IPaymentInvoice = { id: 8640 };
      settlementRequisition.paymentInvoices = [paymentInvoices];
      const deliveryNotes: IDeliveryNote = { id: 2187 };
      settlementRequisition.deliveryNotes = [deliveryNotes];
      const jobSheets: IJobSheet = { id: 11047 };
      settlementRequisition.jobSheets = [jobSheets];
      const businessDocuments: IBusinessDocument = { id: 68418 };
      settlementRequisition.businessDocuments = [businessDocuments];
      const applicationMappings: IUniversallyUniqueMapping = { id: 34613 };
      settlementRequisition.applicationMappings = [applicationMappings];
      const placeholders: IPlaceholder = { id: 9021 };
      settlementRequisition.placeholders = [placeholders];

      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(settlementRequisition));
      expect(comp.settlementCurrenciesSharedCollection).toContain(settlementCurrency);
      expect(comp.applicationUsersSharedCollection).toContain(currentOwner);
      expect(comp.applicationUsersSharedCollection).toContain(nativeOwner);
      expect(comp.dealersSharedCollection).toContain(nativeDepartment);
      expect(comp.dealersSharedCollection).toContain(biller);
      expect(comp.dealersSharedCollection).toContain(signatures);
      expect(comp.paymentInvoicesSharedCollection).toContain(paymentInvoices);
      expect(comp.deliveryNotesSharedCollection).toContain(deliveryNotes);
      expect(comp.jobSheetsSharedCollection).toContain(jobSheets);
      expect(comp.businessDocumentsSharedCollection).toContain(businessDocuments);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(applicationMappings);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SettlementRequisition>>();
      const settlementRequisition = { id: 123 };
      jest.spyOn(settlementRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: settlementRequisition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(settlementRequisitionService.update).toHaveBeenCalledWith(settlementRequisition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SettlementRequisition>>();
      const settlementRequisition = new SettlementRequisition();
      jest.spyOn(settlementRequisitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: settlementRequisition }));
      saveSubject.complete();

      // THEN
      expect(settlementRequisitionService.create).toHaveBeenCalledWith(settlementRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SettlementRequisition>>();
      const settlementRequisition = { id: 123 };
      jest.spyOn(settlementRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ settlementRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(settlementRequisitionService.update).toHaveBeenCalledWith(settlementRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSettlementCurrencyById', () => {
      it('Should return tracked SettlementCurrency primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSettlementCurrencyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
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

    describe('trackPaymentInvoiceById', () => {
      it('Should return tracked PaymentInvoice primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPaymentInvoiceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDeliveryNoteById', () => {
      it('Should return tracked DeliveryNote primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDeliveryNoteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackJobSheetById', () => {
      it('Should return tracked JobSheet primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackJobSheetById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBusinessDocumentById', () => {
      it('Should return tracked BusinessDocument primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBusinessDocumentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUniversallyUniqueMappingById', () => {
      it('Should return tracked UniversallyUniqueMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUniversallyUniqueMappingById(0, entity);
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

    describe('getSelectedPaymentInvoice', () => {
      it('Should return option if no PaymentInvoice is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPaymentInvoice(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected PaymentInvoice for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPaymentInvoice(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this PaymentInvoice is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPaymentInvoice(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedDeliveryNote', () => {
      it('Should return option if no DeliveryNote is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedDeliveryNote(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected DeliveryNote for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedDeliveryNote(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this DeliveryNote is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedDeliveryNote(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedJobSheet', () => {
      it('Should return option if no JobSheet is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedJobSheet(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected JobSheet for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedJobSheet(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this JobSheet is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedJobSheet(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedBusinessDocument', () => {
      it('Should return option if no BusinessDocument is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBusinessDocument(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected BusinessDocument for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBusinessDocument(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this BusinessDocument is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBusinessDocument(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedUniversallyUniqueMapping', () => {
      it('Should return option if no UniversallyUniqueMapping is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUniversallyUniqueMapping(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected UniversallyUniqueMapping for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this UniversallyUniqueMapping is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected]);
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
