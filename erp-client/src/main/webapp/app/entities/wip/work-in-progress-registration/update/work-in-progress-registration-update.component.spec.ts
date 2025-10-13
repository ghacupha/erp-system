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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkInProgressRegistrationService } from '../service/work-in-progress-registration.service';
import { IWorkInProgressRegistration, WorkInProgressRegistration } from '../work-in-progress-registration.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/entities/system/settlement-currency/service/settlement-currency.service';
import { IWorkProjectRegister } from 'app/entities/wip/work-project-register/work-project-register.model';
import { WorkProjectRegisterService } from 'app/entities/wip/work-project-register/service/work-project-register.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';
import { IAssetAccessory } from 'app/entities/assets/asset-accessory/asset-accessory.model';
import { AssetAccessoryService } from 'app/entities/assets/asset-accessory/service/asset-accessory.service';
import { IAssetWarranty } from 'app/entities/assets/asset-warranty/asset-warranty.model';
import { AssetWarrantyService } from 'app/entities/assets/asset-warranty/service/asset-warranty.service';
import { IPaymentInvoice } from 'app/entities/settlement/payment-invoice/payment-invoice.model';
import { PaymentInvoiceService } from 'app/entities/settlement/payment-invoice/service/payment-invoice.service';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ServiceOutletService } from 'app/entities/system/service-outlet/service/service-outlet.service';
import { ISettlement } from 'app/entities/settlement/settlement/settlement.model';
import { SettlementService } from 'app/entities/settlement/settlement/service/settlement.service';
import { IPurchaseOrder } from 'app/entities/settlement/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/settlement/purchase-order/service/purchase-order.service';
import { IDeliveryNote } from 'app/entities/settlement/delivery-note/delivery-note.model';
import { DeliveryNoteService } from 'app/entities/settlement/delivery-note/service/delivery-note.service';
import { IJobSheet } from 'app/entities/settlement/job-sheet/job-sheet.model';
import { JobSheetService } from 'app/entities/settlement/job-sheet/service/job-sheet.service';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { DealerService } from 'app/entities/people/dealer/service/dealer.service';

import { WorkInProgressRegistrationUpdateComponent } from './work-in-progress-registration-update.component';

describe('WorkInProgressRegistration Management Update Component', () => {
  let comp: WorkInProgressRegistrationUpdateComponent;
  let fixture: ComponentFixture<WorkInProgressRegistrationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workInProgressRegistrationService: WorkInProgressRegistrationService;
  let placeholderService: PlaceholderService;
  let settlementCurrencyService: SettlementCurrencyService;
  let workProjectRegisterService: WorkProjectRegisterService;
  let businessDocumentService: BusinessDocumentService;
  let assetAccessoryService: AssetAccessoryService;
  let assetWarrantyService: AssetWarrantyService;
  let paymentInvoiceService: PaymentInvoiceService;
  let serviceOutletService: ServiceOutletService;
  let settlementService: SettlementService;
  let purchaseOrderService: PurchaseOrderService;
  let deliveryNoteService: DeliveryNoteService;
  let jobSheetService: JobSheetService;
  let dealerService: DealerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [WorkInProgressRegistrationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(WorkInProgressRegistrationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkInProgressRegistrationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workInProgressRegistrationService = TestBed.inject(WorkInProgressRegistrationService);
    placeholderService = TestBed.inject(PlaceholderService);
    settlementCurrencyService = TestBed.inject(SettlementCurrencyService);
    workProjectRegisterService = TestBed.inject(WorkProjectRegisterService);
    businessDocumentService = TestBed.inject(BusinessDocumentService);
    assetAccessoryService = TestBed.inject(AssetAccessoryService);
    assetWarrantyService = TestBed.inject(AssetWarrantyService);
    paymentInvoiceService = TestBed.inject(PaymentInvoiceService);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    settlementService = TestBed.inject(SettlementService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);
    deliveryNoteService = TestBed.inject(DeliveryNoteService);
    jobSheetService = TestBed.inject(JobSheetService);
    dealerService = TestBed.inject(DealerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 75425 }];
      workInProgressRegistration.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 98879 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call WorkInProgressRegistration query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const workInProgressGroup: IWorkInProgressRegistration = { id: 71550 };
      workInProgressRegistration.workInProgressGroup = workInProgressGroup;

      const workInProgressRegistrationCollection: IWorkInProgressRegistration[] = [{ id: 19737 }];
      jest
        .spyOn(workInProgressRegistrationService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: workInProgressRegistrationCollection })));
      const additionalWorkInProgressRegistrations = [workInProgressGroup];
      const expectedCollection: IWorkInProgressRegistration[] = [
        ...additionalWorkInProgressRegistrations,
        ...workInProgressRegistrationCollection,
      ];
      jest
        .spyOn(workInProgressRegistrationService, 'addWorkInProgressRegistrationToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(workInProgressRegistrationService.query).toHaveBeenCalled();
      expect(workInProgressRegistrationService.addWorkInProgressRegistrationToCollectionIfMissing).toHaveBeenCalledWith(
        workInProgressRegistrationCollection,
        ...additionalWorkInProgressRegistrations
      );
      expect(comp.workInProgressRegistrationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SettlementCurrency query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const settlementCurrency: ISettlementCurrency = { id: 88925 };
      workInProgressRegistration.settlementCurrency = settlementCurrency;

      const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 48153 }];
      jest.spyOn(settlementCurrencyService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCurrencyCollection })));
      const additionalSettlementCurrencies = [settlementCurrency];
      const expectedCollection: ISettlementCurrency[] = [...additionalSettlementCurrencies, ...settlementCurrencyCollection];
      jest.spyOn(settlementCurrencyService, 'addSettlementCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(settlementCurrencyService.query).toHaveBeenCalled();
      expect(settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        settlementCurrencyCollection,
        ...additionalSettlementCurrencies
      );
      expect(comp.settlementCurrenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call WorkProjectRegister query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const workProjectRegister: IWorkProjectRegister = { id: 55547 };
      workInProgressRegistration.workProjectRegister = workProjectRegister;

      const workProjectRegisterCollection: IWorkProjectRegister[] = [{ id: 5036 }];
      jest.spyOn(workProjectRegisterService, 'query').mockReturnValue(of(new HttpResponse({ body: workProjectRegisterCollection })));
      const additionalWorkProjectRegisters = [workProjectRegister];
      const expectedCollection: IWorkProjectRegister[] = [...additionalWorkProjectRegisters, ...workProjectRegisterCollection];
      jest.spyOn(workProjectRegisterService, 'addWorkProjectRegisterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(workProjectRegisterService.query).toHaveBeenCalled();
      expect(workProjectRegisterService.addWorkProjectRegisterToCollectionIfMissing).toHaveBeenCalledWith(
        workProjectRegisterCollection,
        ...additionalWorkProjectRegisters
      );
      expect(comp.workProjectRegistersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessDocument query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const businessDocuments: IBusinessDocument[] = [{ id: 96779 }];
      workInProgressRegistration.businessDocuments = businessDocuments;

      const businessDocumentCollection: IBusinessDocument[] = [{ id: 50202 }];
      jest.spyOn(businessDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: businessDocumentCollection })));
      const additionalBusinessDocuments = [...businessDocuments];
      const expectedCollection: IBusinessDocument[] = [...additionalBusinessDocuments, ...businessDocumentCollection];
      jest.spyOn(businessDocumentService, 'addBusinessDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(businessDocumentService.query).toHaveBeenCalled();
      expect(businessDocumentService.addBusinessDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        businessDocumentCollection,
        ...additionalBusinessDocuments
      );
      expect(comp.businessDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetAccessory query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const assetAccessories: IAssetAccessory[] = [{ id: 36726 }];
      workInProgressRegistration.assetAccessories = assetAccessories;

      const assetAccessoryCollection: IAssetAccessory[] = [{ id: 28409 }];
      jest.spyOn(assetAccessoryService, 'query').mockReturnValue(of(new HttpResponse({ body: assetAccessoryCollection })));
      const additionalAssetAccessories = [...assetAccessories];
      const expectedCollection: IAssetAccessory[] = [...additionalAssetAccessories, ...assetAccessoryCollection];
      jest.spyOn(assetAccessoryService, 'addAssetAccessoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(assetAccessoryService.query).toHaveBeenCalled();
      expect(assetAccessoryService.addAssetAccessoryToCollectionIfMissing).toHaveBeenCalledWith(
        assetAccessoryCollection,
        ...additionalAssetAccessories
      );
      expect(comp.assetAccessoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetWarranty query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const assetWarranties: IAssetWarranty[] = [{ id: 21548 }];
      workInProgressRegistration.assetWarranties = assetWarranties;

      const assetWarrantyCollection: IAssetWarranty[] = [{ id: 35876 }];
      jest.spyOn(assetWarrantyService, 'query').mockReturnValue(of(new HttpResponse({ body: assetWarrantyCollection })));
      const additionalAssetWarranties = [...assetWarranties];
      const expectedCollection: IAssetWarranty[] = [...additionalAssetWarranties, ...assetWarrantyCollection];
      jest.spyOn(assetWarrantyService, 'addAssetWarrantyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(assetWarrantyService.query).toHaveBeenCalled();
      expect(assetWarrantyService.addAssetWarrantyToCollectionIfMissing).toHaveBeenCalledWith(
        assetWarrantyCollection,
        ...additionalAssetWarranties
      );
      expect(comp.assetWarrantiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PaymentInvoice query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const invoice: IPaymentInvoice = { id: 43949 };
      workInProgressRegistration.invoice = invoice;

      const paymentInvoiceCollection: IPaymentInvoice[] = [{ id: 82569 }];
      jest.spyOn(paymentInvoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentInvoiceCollection })));
      const additionalPaymentInvoices = [invoice];
      const expectedCollection: IPaymentInvoice[] = [...additionalPaymentInvoices, ...paymentInvoiceCollection];
      jest.spyOn(paymentInvoiceService, 'addPaymentInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(paymentInvoiceService.query).toHaveBeenCalled();
      expect(paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        paymentInvoiceCollection,
        ...additionalPaymentInvoices
      );
      expect(comp.paymentInvoicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceOutlet query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const outletCode: IServiceOutlet = { id: 67412 };
      workInProgressRegistration.outletCode = outletCode;

      const serviceOutletCollection: IServiceOutlet[] = [{ id: 95747 }];
      jest.spyOn(serviceOutletService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOutletCollection })));
      const additionalServiceOutlets = [outletCode];
      const expectedCollection: IServiceOutlet[] = [...additionalServiceOutlets, ...serviceOutletCollection];
      jest.spyOn(serviceOutletService, 'addServiceOutletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(serviceOutletService.query).toHaveBeenCalled();
      expect(serviceOutletService.addServiceOutletToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOutletCollection,
        ...additionalServiceOutlets
      );
      expect(comp.serviceOutletsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Settlement query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const settlementTransaction: ISettlement = { id: 39775 };
      workInProgressRegistration.settlementTransaction = settlementTransaction;

      const settlementCollection: ISettlement[] = [{ id: 62235 }];
      jest.spyOn(settlementService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCollection })));
      const additionalSettlements = [settlementTransaction];
      const expectedCollection: ISettlement[] = [...additionalSettlements, ...settlementCollection];
      jest.spyOn(settlementService, 'addSettlementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(settlementService.query).toHaveBeenCalled();
      expect(settlementService.addSettlementToCollectionIfMissing).toHaveBeenCalledWith(settlementCollection, ...additionalSettlements);
      expect(comp.settlementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PurchaseOrder query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const purchaseOrder: IPurchaseOrder = { id: 71163 };
      workInProgressRegistration.purchaseOrder = purchaseOrder;

      const purchaseOrderCollection: IPurchaseOrder[] = [{ id: 89194 }];
      jest.spyOn(purchaseOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: purchaseOrderCollection })));
      const additionalPurchaseOrders = [purchaseOrder];
      const expectedCollection: IPurchaseOrder[] = [...additionalPurchaseOrders, ...purchaseOrderCollection];
      jest.spyOn(purchaseOrderService, 'addPurchaseOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(purchaseOrderService.query).toHaveBeenCalled();
      expect(purchaseOrderService.addPurchaseOrderToCollectionIfMissing).toHaveBeenCalledWith(
        purchaseOrderCollection,
        ...additionalPurchaseOrders
      );
      expect(comp.purchaseOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DeliveryNote query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const deliveryNote: IDeliveryNote = { id: 799 };
      workInProgressRegistration.deliveryNote = deliveryNote;

      const deliveryNoteCollection: IDeliveryNote[] = [{ id: 32388 }];
      jest.spyOn(deliveryNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryNoteCollection })));
      const additionalDeliveryNotes = [deliveryNote];
      const expectedCollection: IDeliveryNote[] = [...additionalDeliveryNotes, ...deliveryNoteCollection];
      jest.spyOn(deliveryNoteService, 'addDeliveryNoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(deliveryNoteService.query).toHaveBeenCalled();
      expect(deliveryNoteService.addDeliveryNoteToCollectionIfMissing).toHaveBeenCalledWith(
        deliveryNoteCollection,
        ...additionalDeliveryNotes
      );
      expect(comp.deliveryNotesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call JobSheet query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const jobSheet: IJobSheet = { id: 22913 };
      workInProgressRegistration.jobSheet = jobSheet;

      const jobSheetCollection: IJobSheet[] = [{ id: 73120 }];
      jest.spyOn(jobSheetService, 'query').mockReturnValue(of(new HttpResponse({ body: jobSheetCollection })));
      const additionalJobSheets = [jobSheet];
      const expectedCollection: IJobSheet[] = [...additionalJobSheets, ...jobSheetCollection];
      jest.spyOn(jobSheetService, 'addJobSheetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(jobSheetService.query).toHaveBeenCalled();
      expect(jobSheetService.addJobSheetToCollectionIfMissing).toHaveBeenCalledWith(jobSheetCollection, ...additionalJobSheets);
      expect(comp.jobSheetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const dealer: IDealer = { id: 99615 };
      workInProgressRegistration.dealer = dealer;

      const dealerCollection: IDealer[] = [{ id: 45619 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [dealer];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workInProgressRegistration: IWorkInProgressRegistration = { id: 456 };
      const placeholders: IPlaceholder = { id: 54597 };
      workInProgressRegistration.placeholders = [placeholders];
      const workInProgressGroup: IWorkInProgressRegistration = { id: 39161 };
      workInProgressRegistration.workInProgressGroup = workInProgressGroup;
      const settlementCurrency: ISettlementCurrency = { id: 47782 };
      workInProgressRegistration.settlementCurrency = settlementCurrency;
      const workProjectRegister: IWorkProjectRegister = { id: 74342 };
      workInProgressRegistration.workProjectRegister = workProjectRegister;
      const businessDocuments: IBusinessDocument = { id: 65752 };
      workInProgressRegistration.businessDocuments = [businessDocuments];
      const assetAccessories: IAssetAccessory = { id: 17611 };
      workInProgressRegistration.assetAccessories = [assetAccessories];
      const assetWarranties: IAssetWarranty = { id: 48566 };
      workInProgressRegistration.assetWarranties = [assetWarranties];
      const invoice: IPaymentInvoice = { id: 54139 };
      workInProgressRegistration.invoice = invoice;
      const outletCode: IServiceOutlet = { id: 42315 };
      workInProgressRegistration.outletCode = outletCode;
      const settlementTransaction: ISettlement = { id: 47612 };
      workInProgressRegistration.settlementTransaction = settlementTransaction;
      const purchaseOrder: IPurchaseOrder = { id: 75479 };
      workInProgressRegistration.purchaseOrder = purchaseOrder;
      const deliveryNote: IDeliveryNote = { id: 99737 };
      workInProgressRegistration.deliveryNote = deliveryNote;
      const jobSheet: IJobSheet = { id: 65563 };
      workInProgressRegistration.jobSheet = jobSheet;
      const dealer: IDealer = { id: 30945 };
      workInProgressRegistration.dealer = dealer;

      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(workInProgressRegistration));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.workInProgressRegistrationsSharedCollection).toContain(workInProgressGroup);
      expect(comp.settlementCurrenciesSharedCollection).toContain(settlementCurrency);
      expect(comp.workProjectRegistersSharedCollection).toContain(workProjectRegister);
      expect(comp.businessDocumentsSharedCollection).toContain(businessDocuments);
      expect(comp.assetAccessoriesSharedCollection).toContain(assetAccessories);
      expect(comp.assetWarrantiesSharedCollection).toContain(assetWarranties);
      expect(comp.paymentInvoicesSharedCollection).toContain(invoice);
      expect(comp.serviceOutletsSharedCollection).toContain(outletCode);
      expect(comp.settlementsSharedCollection).toContain(settlementTransaction);
      expect(comp.purchaseOrdersSharedCollection).toContain(purchaseOrder);
      expect(comp.deliveryNotesSharedCollection).toContain(deliveryNote);
      expect(comp.jobSheetsSharedCollection).toContain(jobSheet);
      expect(comp.dealersSharedCollection).toContain(dealer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkInProgressRegistration>>();
      const workInProgressRegistration = { id: 123 };
      jest.spyOn(workInProgressRegistrationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workInProgressRegistration }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(workInProgressRegistrationService.update).toHaveBeenCalledWith(workInProgressRegistration);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkInProgressRegistration>>();
      const workInProgressRegistration = new WorkInProgressRegistration();
      jest.spyOn(workInProgressRegistrationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workInProgressRegistration }));
      saveSubject.complete();

      // THEN
      expect(workInProgressRegistrationService.create).toHaveBeenCalledWith(workInProgressRegistration);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkInProgressRegistration>>();
      const workInProgressRegistration = { id: 123 };
      jest.spyOn(workInProgressRegistrationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workInProgressRegistration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workInProgressRegistrationService.update).toHaveBeenCalledWith(workInProgressRegistration);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackWorkInProgressRegistrationById', () => {
      it('Should return tracked WorkInProgressRegistration primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWorkInProgressRegistrationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSettlementCurrencyById', () => {
      it('Should return tracked SettlementCurrency primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSettlementCurrencyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackWorkProjectRegisterById', () => {
      it('Should return tracked WorkProjectRegister primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWorkProjectRegisterById(0, entity);
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

    describe('trackAssetAccessoryById', () => {
      it('Should return tracked AssetAccessory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssetAccessoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAssetWarrantyById', () => {
      it('Should return tracked AssetWarranty primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssetWarrantyById(0, entity);
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

    describe('trackServiceOutletById', () => {
      it('Should return tracked ServiceOutlet primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackServiceOutletById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSettlementById', () => {
      it('Should return tracked Settlement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSettlementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPurchaseOrderById', () => {
      it('Should return tracked PurchaseOrder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPurchaseOrderById(0, entity);
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

    describe('trackDealerById', () => {
      it('Should return tracked Dealer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealerById(0, entity);
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

    describe('getSelectedAssetAccessory', () => {
      it('Should return option if no AssetAccessory is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedAssetAccessory(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected AssetAccessory for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedAssetAccessory(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this AssetAccessory is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedAssetAccessory(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedAssetWarranty', () => {
      it('Should return option if no AssetWarranty is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedAssetWarranty(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected AssetWarranty for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedAssetWarranty(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this AssetWarranty is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedAssetWarranty(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
