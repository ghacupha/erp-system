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

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDeliveryNote, DeliveryNote } from '../delivery-note.model';
import { DeliveryNoteService } from '../service/delivery-note.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
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

@Component({
  selector: 'jhi-delivery-note-update',
  templateUrl: './delivery-note-update.component.html',
})
export class DeliveryNoteUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];
  dealersSharedCollection: IDealer[] = [];
  businessStampsSharedCollection: IBusinessStamp[] = [];
  purchaseOrdersSharedCollection: IPurchaseOrder[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];

  editForm = this.fb.group({
    id: [],
    deliveryNoteNumber: [null, [Validators.required]],
    documentDate: [null, [Validators.required]],
    description: [],
    serialNumber: [],
    quantity: [],
    remarks: [],
    placeholders: [],
    receivedBy: [null, Validators.required],
    deliveryStamps: [],
    purchaseOrder: [],
    supplier: [null, Validators.required],
    signatories: [],
    otherPurchaseOrders: [],
    businessDocuments: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected deliveryNoteService: DeliveryNoteService,
    protected placeholderService: PlaceholderService,
    protected dealerService: DealerService,
    protected businessStampService: BusinessStampService,
    protected purchaseOrderService: PurchaseOrderService,
    protected businessDocumentService: BusinessDocumentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryNote }) => {
      this.updateForm(deliveryNote);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('erpSystemApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deliveryNote = this.createFromForm();
    if (deliveryNote.id !== undefined) {
      this.subscribeToSaveResponse(this.deliveryNoteService.update(deliveryNote));
    } else {
      this.subscribeToSaveResponse(this.deliveryNoteService.create(deliveryNote));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackBusinessStampById(index: number, item: IBusinessStamp): number {
    return item.id!;
  }

  trackPurchaseOrderById(index: number, item: IPurchaseOrder): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedDealer(option: IDealer, selectedVals?: IDealer[]): IDealer {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedBusinessStamp(option: IBusinessStamp, selectedVals?: IBusinessStamp[]): IBusinessStamp {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedPurchaseOrder(option: IPurchaseOrder, selectedVals?: IPurchaseOrder[]): IPurchaseOrder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedBusinessDocument(option: IBusinessDocument, selectedVals?: IBusinessDocument[]): IBusinessDocument {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliveryNote>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(deliveryNote: IDeliveryNote): void {
    this.editForm.patchValue({
      id: deliveryNote.id,
      deliveryNoteNumber: deliveryNote.deliveryNoteNumber,
      documentDate: deliveryNote.documentDate,
      description: deliveryNote.description,
      serialNumber: deliveryNote.serialNumber,
      quantity: deliveryNote.quantity,
      remarks: deliveryNote.remarks,
      placeholders: deliveryNote.placeholders,
      receivedBy: deliveryNote.receivedBy,
      deliveryStamps: deliveryNote.deliveryStamps,
      purchaseOrder: deliveryNote.purchaseOrder,
      supplier: deliveryNote.supplier,
      signatories: deliveryNote.signatories,
      otherPurchaseOrders: deliveryNote.otherPurchaseOrders,
      businessDocuments: deliveryNote.businessDocuments,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(deliveryNote.placeholders ?? [])
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      deliveryNote.receivedBy,
      deliveryNote.supplier,
      ...(deliveryNote.signatories ?? [])
    );
    this.businessStampsSharedCollection = this.businessStampService.addBusinessStampToCollectionIfMissing(
      this.businessStampsSharedCollection,
      ...(deliveryNote.deliveryStamps ?? [])
    );
    this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
      this.purchaseOrdersSharedCollection,
      deliveryNote.purchaseOrder,
      ...(deliveryNote.otherPurchaseOrders ?? [])
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(deliveryNote.businessDocuments ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) =>
          this.dealerService.addDealerToCollectionIfMissing(
            dealers,
            this.editForm.get('receivedBy')!.value,
            this.editForm.get('supplier')!.value,
            ...(this.editForm.get('signatories')!.value ?? [])
          )
        )
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.businessStampService
      .query()
      .pipe(map((res: HttpResponse<IBusinessStamp[]>) => res.body ?? []))
      .pipe(
        map((businessStamps: IBusinessStamp[]) =>
          this.businessStampService.addBusinessStampToCollectionIfMissing(
            businessStamps,
            ...(this.editForm.get('deliveryStamps')!.value ?? [])
          )
        )
      )
      .subscribe((businessStamps: IBusinessStamp[]) => (this.businessStampsSharedCollection = businessStamps));

    this.purchaseOrderService
      .query()
      .pipe(map((res: HttpResponse<IPurchaseOrder[]>) => res.body ?? []))
      .pipe(
        map((purchaseOrders: IPurchaseOrder[]) =>
          this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
            purchaseOrders,
            this.editForm.get('purchaseOrder')!.value,
            ...(this.editForm.get('otherPurchaseOrders')!.value ?? [])
          )
        )
      )
      .subscribe((purchaseOrders: IPurchaseOrder[]) => (this.purchaseOrdersSharedCollection = purchaseOrders));

    this.businessDocumentService
      .query()
      .pipe(map((res: HttpResponse<IBusinessDocument[]>) => res.body ?? []))
      .pipe(
        map((businessDocuments: IBusinessDocument[]) =>
          this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
            businessDocuments,
            ...(this.editForm.get('businessDocuments')!.value ?? [])
          )
        )
      )
      .subscribe((businessDocuments: IBusinessDocument[]) => (this.businessDocumentsSharedCollection = businessDocuments));
  }

  protected createFromForm(): IDeliveryNote {
    return {
      ...new DeliveryNote(),
      id: this.editForm.get(['id'])!.value,
      deliveryNoteNumber: this.editForm.get(['deliveryNoteNumber'])!.value,
      documentDate: this.editForm.get(['documentDate'])!.value,
      description: this.editForm.get(['description'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      receivedBy: this.editForm.get(['receivedBy'])!.value,
      deliveryStamps: this.editForm.get(['deliveryStamps'])!.value,
      purchaseOrder: this.editForm.get(['purchaseOrder'])!.value,
      supplier: this.editForm.get(['supplier'])!.value,
      signatories: this.editForm.get(['signatories'])!.value,
      otherPurchaseOrders: this.editForm.get(['otherPurchaseOrders'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
    };
  }
}
