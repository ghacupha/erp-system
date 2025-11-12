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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DeliveryNoteService } from '../service/delivery-note.service';
import { IDeliveryNote, DeliveryNote } from '../delivery-note.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { DealerService } from 'app/entities/people/dealer/service/dealer.service';
import { IBusinessStamp } from 'app/entities/settlement/business-stamp/business-stamp.model';
import { BusinessStampService } from 'app/entities/settlement/business-stamp/service/business-stamp.service';
import { IPurchaseOrder } from 'app/entities/settlement/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/settlement/purchase-order/service/purchase-order.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';

import { DeliveryNoteUpdateComponent } from './delivery-note-update.component';

describe('DeliveryNote Management Update Component', () => {
  let comp: DeliveryNoteUpdateComponent;
  let fixture: ComponentFixture<DeliveryNoteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deliveryNoteService: DeliveryNoteService;
  let placeholderService: PlaceholderService;
  let dealerService: DealerService;
  let businessStampService: BusinessStampService;
  let purchaseOrderService: PurchaseOrderService;
  let businessDocumentService: BusinessDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DeliveryNoteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DeliveryNoteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeliveryNoteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deliveryNoteService = TestBed.inject(DeliveryNoteService);
    placeholderService = TestBed.inject(PlaceholderService);
    dealerService = TestBed.inject(DealerService);
    businessStampService = TestBed.inject(BusinessStampService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);
    businessDocumentService = TestBed.inject(BusinessDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const deliveryNote: IDeliveryNote = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 91349 }];
      deliveryNote.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 66272 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryNote });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const deliveryNote: IDeliveryNote = { id: 456 };
      const receivedBy: IDealer = { id: 9901 };
      deliveryNote.receivedBy = receivedBy;
      const supplier: IDealer = { id: 67619 };
      deliveryNote.supplier = supplier;
      const signatories: IDealer[] = [{ id: 71897 }];
      deliveryNote.signatories = signatories;

      const dealerCollection: IDealer[] = [{ id: 72860 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [receivedBy, supplier, ...signatories];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryNote });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessStamp query and add missing value', () => {
      const deliveryNote: IDeliveryNote = { id: 456 };
      const deliveryStamps: IBusinessStamp[] = [{ id: 74765 }];
      deliveryNote.deliveryStamps = deliveryStamps;

      const businessStampCollection: IBusinessStamp[] = [{ id: 323 }];
      jest.spyOn(businessStampService, 'query').mockReturnValue(of(new HttpResponse({ body: businessStampCollection })));
      const additionalBusinessStamps = [...deliveryStamps];
      const expectedCollection: IBusinessStamp[] = [...additionalBusinessStamps, ...businessStampCollection];
      jest.spyOn(businessStampService, 'addBusinessStampToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryNote });
      comp.ngOnInit();

      expect(businessStampService.query).toHaveBeenCalled();
      expect(businessStampService.addBusinessStampToCollectionIfMissing).toHaveBeenCalledWith(
        businessStampCollection,
        ...additionalBusinessStamps
      );
      expect(comp.businessStampsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PurchaseOrder query and add missing value', () => {
      const deliveryNote: IDeliveryNote = { id: 456 };
      const purchaseOrder: IPurchaseOrder = { id: 90254 };
      deliveryNote.purchaseOrder = purchaseOrder;
      const otherPurchaseOrders: IPurchaseOrder[] = [{ id: 5273 }];
      deliveryNote.otherPurchaseOrders = otherPurchaseOrders;

      const purchaseOrderCollection: IPurchaseOrder[] = [{ id: 36372 }];
      jest.spyOn(purchaseOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: purchaseOrderCollection })));
      const additionalPurchaseOrders = [purchaseOrder, ...otherPurchaseOrders];
      const expectedCollection: IPurchaseOrder[] = [...additionalPurchaseOrders, ...purchaseOrderCollection];
      jest.spyOn(purchaseOrderService, 'addPurchaseOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryNote });
      comp.ngOnInit();

      expect(purchaseOrderService.query).toHaveBeenCalled();
      expect(purchaseOrderService.addPurchaseOrderToCollectionIfMissing).toHaveBeenCalledWith(
        purchaseOrderCollection,
        ...additionalPurchaseOrders
      );
      expect(comp.purchaseOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessDocument query and add missing value', () => {
      const deliveryNote: IDeliveryNote = { id: 456 };
      const businessDocuments: IBusinessDocument[] = [{ id: 86935 }];
      deliveryNote.businessDocuments = businessDocuments;

      const businessDocumentCollection: IBusinessDocument[] = [{ id: 7176 }];
      jest.spyOn(businessDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: businessDocumentCollection })));
      const additionalBusinessDocuments = [...businessDocuments];
      const expectedCollection: IBusinessDocument[] = [...additionalBusinessDocuments, ...businessDocumentCollection];
      jest.spyOn(businessDocumentService, 'addBusinessDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryNote });
      comp.ngOnInit();

      expect(businessDocumentService.query).toHaveBeenCalled();
      expect(businessDocumentService.addBusinessDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        businessDocumentCollection,
        ...additionalBusinessDocuments
      );
      expect(comp.businessDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deliveryNote: IDeliveryNote = { id: 456 };
      const placeholders: IPlaceholder = { id: 62945 };
      deliveryNote.placeholders = [placeholders];
      const receivedBy: IDealer = { id: 16865 };
      deliveryNote.receivedBy = receivedBy;
      const supplier: IDealer = { id: 80242 };
      deliveryNote.supplier = supplier;
      const signatories: IDealer = { id: 39827 };
      deliveryNote.signatories = [signatories];
      const deliveryStamps: IBusinessStamp = { id: 81629 };
      deliveryNote.deliveryStamps = [deliveryStamps];
      const purchaseOrder: IPurchaseOrder = { id: 84059 };
      deliveryNote.purchaseOrder = purchaseOrder;
      const otherPurchaseOrders: IPurchaseOrder = { id: 64784 };
      deliveryNote.otherPurchaseOrders = [otherPurchaseOrders];
      const businessDocuments: IBusinessDocument = { id: 59570 };
      deliveryNote.businessDocuments = [businessDocuments];

      activatedRoute.data = of({ deliveryNote });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deliveryNote));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.dealersSharedCollection).toContain(receivedBy);
      expect(comp.dealersSharedCollection).toContain(supplier);
      expect(comp.dealersSharedCollection).toContain(signatories);
      expect(comp.businessStampsSharedCollection).toContain(deliveryStamps);
      expect(comp.purchaseOrdersSharedCollection).toContain(purchaseOrder);
      expect(comp.purchaseOrdersSharedCollection).toContain(otherPurchaseOrders);
      expect(comp.businessDocumentsSharedCollection).toContain(businessDocuments);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryNote>>();
      const deliveryNote = { id: 123 };
      jest.spyOn(deliveryNoteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryNote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryNote }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deliveryNoteService.update).toHaveBeenCalledWith(deliveryNote);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryNote>>();
      const deliveryNote = new DeliveryNote();
      jest.spyOn(deliveryNoteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryNote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryNote }));
      saveSubject.complete();

      // THEN
      expect(deliveryNoteService.create).toHaveBeenCalledWith(deliveryNote);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryNote>>();
      const deliveryNote = { id: 123 };
      jest.spyOn(deliveryNoteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryNote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deliveryNoteService.update).toHaveBeenCalledWith(deliveryNote);
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

    describe('trackPurchaseOrderById', () => {
      it('Should return tracked PurchaseOrder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPurchaseOrderById(0, entity);
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
  });
});
