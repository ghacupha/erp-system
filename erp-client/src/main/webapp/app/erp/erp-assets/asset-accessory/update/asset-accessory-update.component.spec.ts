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

import { AssetAccessoryService } from '../service/asset-accessory.service';
import { IAssetAccessory, AssetAccessory } from '../asset-accessory.model';

import { AssetAccessoryUpdateComponent } from './asset-accessory-update.component';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { IAssetWarranty } from '../../asset-warranty/asset-warranty.model';
import { AssetWarrantyService } from '../../asset-warranty/service/asset-warranty.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { AssetCategoryService } from '../../asset-category/service/asset-category.service';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { IPurchaseOrder } from '../../../erp-settlements/purchase-order/purchase-order.model';
import { IAssetCategory } from '../../asset-category/asset-category.model';
import { IJobSheet } from '../../../erp-settlements/job-sheet/job-sheet.model';
import { IDeliveryNote } from '../../../erp-settlements/delivery-note/delivery-note.model';
import { PurchaseOrderService } from '../../../erp-settlements/purchase-order/service/purchase-order.service';
import { IPaymentInvoice } from '../../../erp-settlements/payment-invoice/payment-invoice.model';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { PaymentInvoiceService } from '../../../erp-settlements/payment-invoice/service/payment-invoice.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { JobSheetService } from '../../../erp-settlements/job-sheet/service/job-sheet.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { DeliveryNoteService } from '../../../erp-settlements/delivery-note/service/delivery-note.service';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';

describe('AssetAccessory Management Update Component', () => {
  let comp: AssetAccessoryUpdateComponent;
  let fixture: ComponentFixture<AssetAccessoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetAccessoryService: AssetAccessoryService;
  let assetWarrantyService: AssetWarrantyService;
  let placeholderService: PlaceholderService;
  let paymentInvoiceService: PaymentInvoiceService;
  let serviceOutletService: ServiceOutletService;
  let settlementService: SettlementService;
  let assetCategoryService: AssetCategoryService;
  let purchaseOrderService: PurchaseOrderService;
  let deliveryNoteService: DeliveryNoteService;
  let jobSheetService: JobSheetService;
  let dealerService: DealerService;
  let businessDocumentService: BusinessDocumentService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AssetAccessoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AssetAccessoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetAccessoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetAccessoryService = TestBed.inject(AssetAccessoryService);
    assetWarrantyService = TestBed.inject(AssetWarrantyService);
    placeholderService = TestBed.inject(PlaceholderService);
    paymentInvoiceService = TestBed.inject(PaymentInvoiceService);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    settlementService = TestBed.inject(SettlementService);
    assetCategoryService = TestBed.inject(AssetCategoryService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);
    deliveryNoteService = TestBed.inject(DeliveryNoteService);
    jobSheetService = TestBed.inject(JobSheetService);
    dealerService = TestBed.inject(DealerService);
    businessDocumentService = TestBed.inject(BusinessDocumentService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AssetWarranty query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const assetWarranties: IAssetWarranty[] = [{ id: 53648 }];
      assetAccessory.assetWarranties = assetWarranties;

      const assetWarrantyCollection: IAssetWarranty[] = [{ id: 52634 }];
      jest.spyOn(assetWarrantyService, 'query').mockReturnValue(of(new HttpResponse({ body: assetWarrantyCollection })));
      const additionalAssetWarranties = [...assetWarranties];
      const expectedCollection: IAssetWarranty[] = [...additionalAssetWarranties, ...assetWarrantyCollection];
      jest.spyOn(assetWarrantyService, 'addAssetWarrantyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(assetWarrantyService.query).toHaveBeenCalled();
      expect(assetWarrantyService.addAssetWarrantyToCollectionIfMissing).toHaveBeenCalledWith(
        assetWarrantyCollection,
        ...additionalAssetWarranties
      );
      expect(comp.assetWarrantiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 25316 }];
      assetAccessory.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 27607 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PaymentInvoice query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const paymentInvoices: IPaymentInvoice[] = [{ id: 27409 }];
      assetAccessory.paymentInvoices = paymentInvoices;

      const paymentInvoiceCollection: IPaymentInvoice[] = [{ id: 80605 }];
      jest.spyOn(paymentInvoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentInvoiceCollection })));
      const additionalPaymentInvoices = [...paymentInvoices];
      const expectedCollection: IPaymentInvoice[] = [...additionalPaymentInvoices, ...paymentInvoiceCollection];
      jest.spyOn(paymentInvoiceService, 'addPaymentInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(paymentInvoiceService.query).toHaveBeenCalled();
      expect(paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        paymentInvoiceCollection,
        ...additionalPaymentInvoices
      );
      expect(comp.paymentInvoicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceOutlet query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const serviceOutlet: IServiceOutlet = { id: 9184 };
      assetAccessory.serviceOutlet = serviceOutlet;

      const serviceOutletCollection: IServiceOutlet[] = [{ id: 42194 }];
      jest.spyOn(serviceOutletService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOutletCollection })));
      const additionalServiceOutlets = [serviceOutlet];
      const expectedCollection: IServiceOutlet[] = [...additionalServiceOutlets, ...serviceOutletCollection];
      jest.spyOn(serviceOutletService, 'addServiceOutletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(serviceOutletService.query).toHaveBeenCalled();
      expect(serviceOutletService.addServiceOutletToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOutletCollection,
        ...additionalServiceOutlets
      );
      expect(comp.serviceOutletsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Settlement query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const settlements: ISettlement[] = [{ id: 56784 }];
      assetAccessory.settlements = settlements;

      const settlementCollection: ISettlement[] = [{ id: 7068 }];
      jest.spyOn(settlementService, 'query').mockReturnValue(of(new HttpResponse({ body: settlementCollection })));
      const additionalSettlements = [...settlements];
      const expectedCollection: ISettlement[] = [...additionalSettlements, ...settlementCollection];
      jest.spyOn(settlementService, 'addSettlementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(settlementService.query).toHaveBeenCalled();
      expect(settlementService.addSettlementToCollectionIfMissing).toHaveBeenCalledWith(settlementCollection, ...additionalSettlements);
      expect(comp.settlementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AssetCategory query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const assetCategory: IAssetCategory = { id: 86963 };
      assetAccessory.assetCategory = assetCategory;

      const assetCategoryCollection: IAssetCategory[] = [{ id: 14687 }];
      jest.spyOn(assetCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCategoryCollection })));
      const additionalAssetCategories = [assetCategory];
      const expectedCollection: IAssetCategory[] = [...additionalAssetCategories, ...assetCategoryCollection];
      jest.spyOn(assetCategoryService, 'addAssetCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(assetCategoryService.query).toHaveBeenCalled();
      expect(assetCategoryService.addAssetCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        assetCategoryCollection,
        ...additionalAssetCategories
      );
      expect(comp.assetCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PurchaseOrder query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const purchaseOrders: IPurchaseOrder[] = [{ id: 11201 }];
      assetAccessory.purchaseOrders = purchaseOrders;

      const purchaseOrderCollection: IPurchaseOrder[] = [{ id: 89287 }];
      jest.spyOn(purchaseOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: purchaseOrderCollection })));
      const additionalPurchaseOrders = [...purchaseOrders];
      const expectedCollection: IPurchaseOrder[] = [...additionalPurchaseOrders, ...purchaseOrderCollection];
      jest.spyOn(purchaseOrderService, 'addPurchaseOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(purchaseOrderService.query).toHaveBeenCalled();
      expect(purchaseOrderService.addPurchaseOrderToCollectionIfMissing).toHaveBeenCalledWith(
        purchaseOrderCollection,
        ...additionalPurchaseOrders
      );
      expect(comp.purchaseOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DeliveryNote query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const deliveryNotes: IDeliveryNote[] = [{ id: 4899 }];
      assetAccessory.deliveryNotes = deliveryNotes;

      const deliveryNoteCollection: IDeliveryNote[] = [{ id: 32019 }];
      jest.spyOn(deliveryNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryNoteCollection })));
      const additionalDeliveryNotes = [...deliveryNotes];
      const expectedCollection: IDeliveryNote[] = [...additionalDeliveryNotes, ...deliveryNoteCollection];
      jest.spyOn(deliveryNoteService, 'addDeliveryNoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(deliveryNoteService.query).toHaveBeenCalled();
      expect(deliveryNoteService.addDeliveryNoteToCollectionIfMissing).toHaveBeenCalledWith(
        deliveryNoteCollection,
        ...additionalDeliveryNotes
      );
      expect(comp.deliveryNotesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call JobSheet query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const jobSheets: IJobSheet[] = [{ id: 3572 }];
      assetAccessory.jobSheets = jobSheets;

      const jobSheetCollection: IJobSheet[] = [{ id: 75594 }];
      jest.spyOn(jobSheetService, 'query').mockReturnValue(of(new HttpResponse({ body: jobSheetCollection })));
      const additionalJobSheets = [...jobSheets];
      const expectedCollection: IJobSheet[] = [...additionalJobSheets, ...jobSheetCollection];
      jest.spyOn(jobSheetService, 'addJobSheetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(jobSheetService.query).toHaveBeenCalled();
      expect(jobSheetService.addJobSheetToCollectionIfMissing).toHaveBeenCalledWith(jobSheetCollection, ...additionalJobSheets);
      expect(comp.jobSheetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const dealer: IDealer = { id: 6339 };
      assetAccessory.dealer = dealer;
      const designatedUsers: IDealer[] = [{ id: 21385 }];
      assetAccessory.designatedUsers = designatedUsers;

      const dealerCollection: IDealer[] = [{ id: 67207 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [dealer, ...designatedUsers];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessDocument query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const businessDocuments: IBusinessDocument[] = [{ id: 96268 }];
      assetAccessory.businessDocuments = businessDocuments;

      const businessDocumentCollection: IBusinessDocument[] = [{ id: 64327 }];
      jest.spyOn(businessDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: businessDocumentCollection })));
      const additionalBusinessDocuments = [...businessDocuments];
      const expectedCollection: IBusinessDocument[] = [...additionalBusinessDocuments, ...businessDocumentCollection];
      jest.spyOn(businessDocumentService, 'addBusinessDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(businessDocumentService.query).toHaveBeenCalled();
      expect(businessDocumentService.addBusinessDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        businessDocumentCollection,
        ...additionalBusinessDocuments
      );
      expect(comp.businessDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const universallyUniqueMappings: IUniversallyUniqueMapping[] = [{ id: 38781 }];
      assetAccessory.universallyUniqueMappings = universallyUniqueMappings;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 70096 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...universallyUniqueMappings];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assetAccessory: IAssetAccessory = { id: 456 };
      const assetWarranties: IAssetWarranty = { id: 76781 };
      assetAccessory.assetWarranties = [assetWarranties];
      const placeholders: IPlaceholder = { id: 71731 };
      assetAccessory.placeholders = [placeholders];
      const paymentInvoices: IPaymentInvoice = { id: 15323 };
      assetAccessory.paymentInvoices = [paymentInvoices];
      const serviceOutlet: IServiceOutlet = { id: 18389 };
      assetAccessory.serviceOutlet = serviceOutlet;
      const settlements: ISettlement = { id: 7422 };
      assetAccessory.settlements = [settlements];
      const assetCategory: IAssetCategory = { id: 53139 };
      assetAccessory.assetCategory = assetCategory;
      const purchaseOrders: IPurchaseOrder = { id: 12965 };
      assetAccessory.purchaseOrders = [purchaseOrders];
      const deliveryNotes: IDeliveryNote = { id: 36290 };
      assetAccessory.deliveryNotes = [deliveryNotes];
      const jobSheets: IJobSheet = { id: 63356 };
      assetAccessory.jobSheets = [jobSheets];
      const dealer: IDealer = { id: 40159 };
      assetAccessory.dealer = dealer;
      const designatedUsers: IDealer = { id: 98263 };
      assetAccessory.designatedUsers = [designatedUsers];
      const businessDocuments: IBusinessDocument = { id: 82834 };
      assetAccessory.businessDocuments = [businessDocuments];
      const universallyUniqueMappings: IUniversallyUniqueMapping = { id: 64758 };
      assetAccessory.universallyUniqueMappings = [universallyUniqueMappings];

      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetAccessory));
      expect(comp.assetWarrantiesSharedCollection).toContain(assetWarranties);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.paymentInvoicesSharedCollection).toContain(paymentInvoices);
      expect(comp.serviceOutletsSharedCollection).toContain(serviceOutlet);
      expect(comp.settlementsSharedCollection).toContain(settlements);
      expect(comp.assetCategoriesSharedCollection).toContain(assetCategory);
      expect(comp.purchaseOrdersSharedCollection).toContain(purchaseOrders);
      expect(comp.deliveryNotesSharedCollection).toContain(deliveryNotes);
      expect(comp.jobSheetsSharedCollection).toContain(jobSheets);
      expect(comp.dealersSharedCollection).toContain(dealer);
      expect(comp.dealersSharedCollection).toContain(designatedUsers);
      expect(comp.businessDocumentsSharedCollection).toContain(businessDocuments);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(universallyUniqueMappings);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetAccessory>>();
      const assetAccessory = { id: 123 };
      jest.spyOn(assetAccessoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetAccessory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetAccessoryService.update).toHaveBeenCalledWith(assetAccessory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetAccessory>>();
      const assetAccessory = new AssetAccessory();
      jest.spyOn(assetAccessoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetAccessory }));
      saveSubject.complete();

      // THEN
      expect(assetAccessoryService.create).toHaveBeenCalledWith(assetAccessory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetAccessory>>();
      const assetAccessory = { id: 123 };
      jest.spyOn(assetAccessoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetAccessory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetAccessoryService.update).toHaveBeenCalledWith(assetAccessory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAssetWarrantyById', () => {
      it('Should return tracked AssetWarranty primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssetWarrantyById(0, entity);
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

    describe('trackAssetCategoryById', () => {
      it('Should return tracked AssetCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAssetCategoryById(0, entity);
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
  });

  describe('Getting selected relationships', () => {
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

    describe('getSelectedSettlement', () => {
      it('Should return option if no Settlement is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedSettlement(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Settlement for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedSettlement(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Settlement is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedSettlement(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedPurchaseOrder', () => {
      it('Should return option if no PurchaseOrder is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPurchaseOrder(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected PurchaseOrder for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPurchaseOrder(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this PurchaseOrder is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPurchaseOrder(option, [selected]);
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
  });
});

