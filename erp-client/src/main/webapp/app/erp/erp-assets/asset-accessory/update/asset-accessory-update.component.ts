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

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAssetAccessory, AssetAccessory } from '../asset-accessory.model';
import { AssetAccessoryService } from '../service/asset-accessory.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { IAssetWarranty } from '../../asset-warranty/asset-warranty.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { AssetWarrantyService } from '../../asset-warranty/service/asset-warranty.service';
import { AssetCategoryService } from '../../asset-category/service/asset-category.service';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { IPurchaseOrder } from '../../../erp-settlements/purchase-order/purchase-order.model';
import { IAssetCategory } from '../../asset-category/asset-category.model';
import { IJobSheet } from '../../../erp-settlements/job-sheet/job-sheet.model';
import { IDeliveryNote } from '../../../erp-settlements/delivery-note/delivery-note.model';
import { IPaymentInvoice } from '../../../erp-settlements/payment-invoice/payment-invoice.model';
import { PurchaseOrderService } from '../../../erp-settlements/purchase-order/service/purchase-order.service';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { PaymentInvoiceService } from '../../../erp-settlements/payment-invoice/service/payment-invoice.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { JobSheetService } from '../../../erp-settlements/job-sheet/service/job-sheet.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { DeliveryNoteService } from '../../../erp-settlements/delivery-note/service/delivery-note.service';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';
import { ISettlement} from '../../../erp-settlements/settlement/settlement.model';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  assetAccessoryUpdateSelectedInstance,
  copyingAssetAccessoryStatus, creatingAssetAccessoryStatus,
  editingAssetAccessoryStatus
} from '../../../store/selectors/asset-accessory-workflow-status.selector';
import { assetAccessoryDataHasMutated } from '../../../store/actions/asset-accessory-update-status.actions';

@Component({
  selector: 'jhi-asset-accessory-update',
  templateUrl: './asset-accessory-update.component.html',
})
export class AssetAccessoryUpdateComponent implements OnInit {
  isSaving = false;

  assetWarrantiesSharedCollection: IAssetWarranty[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  paymentInvoicesSharedCollection: IPaymentInvoice[] = [];
  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  settlementsSharedCollection: ISettlement[] = [];
  assetCategoriesSharedCollection: IAssetCategory[] = [];
  purchaseOrdersSharedCollection: IPurchaseOrder[] = [];
  deliveryNotesSharedCollection: IDeliveryNote[] = [];
  jobSheetsSharedCollection: IJobSheet[] = [];
  dealersSharedCollection: IDealer[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = {...new AssetAccessory()}

  editForm = this.fb.group({
    id: [],
    assetTag: [],
    assetDetails: [],
    comments: [],
    commentsContentType: [],
    modelNumber: [],
    serialNumber: [],
    assetWarranties: [],
    placeholders: [],
    paymentInvoices: [],
    serviceOutlet: [null, Validators.required],
    settlements: [null, Validators.required],
    assetCategory: [null, Validators.required],
    purchaseOrders: [],
    deliveryNotes: [],
    jobSheets: [],
    dealer: [null, Validators.required],
    designatedUsers: [],
    businessDocuments: [],
    universallyUniqueMappings: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected assetAccessoryService: AssetAccessoryService,
    protected assetWarrantyService: AssetWarrantyService,
    protected placeholderService: PlaceholderService,
    protected paymentInvoiceService: PaymentInvoiceService,
    protected serviceOutletService: ServiceOutletService,
    protected settlementService: SettlementService,
    protected assetCategoryService: AssetCategoryService,
    protected purchaseOrderService: PurchaseOrderService,
    protected deliveryNoteService: DeliveryNoteService,
    protected jobSheetService: JobSheetService,
    protected dealerService: DealerService,
    protected businessDocumentService: BusinessDocumentService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
  ) {
    this.store.pipe(select(copyingAssetAccessoryStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingAssetAccessoryStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingAssetAccessoryStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(assetAccessoryUpdateSelectedInstance)).subscribe(copied => this.selectedItem = copied);
  }

  ngOnInit(): void {

    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);
    }

    if (this.weAreCopying) {
      this.copyForm(this.selectedItem)
    }

    if (this.weAreCreating) {
      this.loadRelationshipsOptions();
    }
  }

  updateDealer(dealerUpdate: IDealer): void {
    this.editForm.patchValue({
      dealer: dealerUpdate,
    });
  }

  updateAssetCategory(update: IAssetCategory): void {
    this.editForm.patchValue({
      assetCategory: update
    });
  }

  updateServiceOutlet(update: IServiceOutlet): void {
    this.editForm.patchValue({
      serviceOutlet: update
    });
  }

  updateDesignatedUsers(update: IDealer[]): void {
    this.editForm.patchValue({
      designatedUsers: [...update]
    });
  }

  updateSettlements(update: ISettlement[]): void {
    this.editForm.patchValue({
      settlements: [...update]
    });
  }

  updatePurchaseOrders(update: IPurchaseOrder[]): void {
    this.editForm.patchValue({
      purchaseOrders: [...update]
    });
  }

  updateDeliveryNotes(update: IDeliveryNote[]): void {
    this.editForm.patchValue({
      deliveryNotes: [...update]
    });
  }

  updateJobSheets(update: IJobSheet[]): void {
    this.editForm.patchValue({
      jobSheets: [...update]
    });
  }

  updateBusinessDocuments(update: IBusinessDocument[]): void {
    this.editForm.patchValue({
      businessDocuments: [...update]
    });
  }

  updateUniversallyUniqueMappings(update: IUniversallyUniqueMapping[]): void {
    this.editForm.patchValue({
      universallyUniqueMappings: [...update]
    });
  }

  updateAssetWarranties(update: IAssetWarranty[]): void {
    this.editForm.patchValue({
      assetWarranties: [...update]
    });
  }

  updatePlaceholders(update: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...update]
    });
  }

  updatePaymentInvoices(update: IPaymentInvoice[]): void {
    this.editForm.patchValue({
      paymentInvoices: update
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
    this.store.dispatch(assetAccessoryDataHasMutated());

    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.assetAccessoryService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.assetAccessoryService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.assetAccessoryService.create(this.copyFromForm()));
  }

  trackAssetWarrantyById(index: number, item: IAssetWarranty): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackPaymentInvoiceById(index: number, item: IPaymentInvoice): number {
    return item.id!;
  }

  trackServiceOutletById(index: number, item: IServiceOutlet): number {
    return item.id!;
  }

  trackSettlementById(index: number, item: ISettlement): number {
    return item.id!;
  }

  trackAssetCategoryById(index: number, item: IAssetCategory): number {
    return item.id!;
  }

  trackPurchaseOrderById(index: number, item: IPurchaseOrder): number {
    return item.id!;
  }

  trackDeliveryNoteById(index: number, item: IDeliveryNote): number {
    return item.id!;
  }

  trackJobSheetById(index: number, item: IJobSheet): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  getSelectedAssetWarranty(option: IAssetWarranty, selectedVals?: IAssetWarranty[]): IAssetWarranty {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
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

  getSelectedPaymentInvoice(option: IPaymentInvoice, selectedVals?: IPaymentInvoice[]): IPaymentInvoice {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedSettlement(option: ISettlement, selectedVals?: ISettlement[]): ISettlement {
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

  getSelectedDeliveryNote(option: IDeliveryNote, selectedVals?: IDeliveryNote[]): IDeliveryNote {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedJobSheet(option: IJobSheet, selectedVals?: IJobSheet[]): IJobSheet {
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

  getSelectedUniversallyUniqueMapping(
    option: IUniversallyUniqueMapping,
    selectedVals?: IUniversallyUniqueMapping[]
  ): IUniversallyUniqueMapping {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetAccessory>>): void {
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

  protected updateForm(assetAccessory: IAssetAccessory): void {
    this.editForm.patchValue({
      id: assetAccessory.id,
      assetTag: assetAccessory.assetTag,
      assetDetails: assetAccessory.assetDetails,
      comments: assetAccessory.comments,
      commentsContentType: assetAccessory.commentsContentType,
      modelNumber: assetAccessory.modelNumber,
      serialNumber: assetAccessory.serialNumber,
      assetWarranties: assetAccessory.assetWarranties,
      placeholders: assetAccessory.placeholders,
      paymentInvoices: assetAccessory.paymentInvoices,
      serviceOutlet: assetAccessory.serviceOutlet,
      settlements: assetAccessory.settlements,
      assetCategory: assetAccessory.assetCategory,
      purchaseOrders: assetAccessory.purchaseOrders,
      deliveryNotes: assetAccessory.deliveryNotes,
      jobSheets: assetAccessory.jobSheets,
      dealer: assetAccessory.dealer,
      designatedUsers: assetAccessory.designatedUsers,
      businessDocuments: assetAccessory.businessDocuments,
      universallyUniqueMappings: assetAccessory.universallyUniqueMappings,
    });

    this.assetWarrantiesSharedCollection = this.assetWarrantyService.addAssetWarrantyToCollectionIfMissing(
      this.assetWarrantiesSharedCollection,
      ...(assetAccessory.assetWarranties ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(assetAccessory.placeholders ?? [])
    );
    this.paymentInvoicesSharedCollection = this.paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing(
      this.paymentInvoicesSharedCollection,
      ...(assetAccessory.paymentInvoices ?? [])
    );
    this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
      this.serviceOutletsSharedCollection,
      assetAccessory.serviceOutlet
    );
    this.settlementsSharedCollection = this.settlementService.addSettlementToCollectionIfMissing(
      this.settlementsSharedCollection,
      ...(assetAccessory.settlements ?? [])
    );
    this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing(
      this.assetCategoriesSharedCollection,
      assetAccessory.assetCategory
    );
    this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
      this.purchaseOrdersSharedCollection,
      ...(assetAccessory.purchaseOrders ?? [])
    );
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      ...(assetAccessory.deliveryNotes ?? [])
    );
    this.jobSheetsSharedCollection = this.jobSheetService.addJobSheetToCollectionIfMissing(
      this.jobSheetsSharedCollection,
      ...(assetAccessory.jobSheets ?? [])
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      assetAccessory.dealer,
      ...(assetAccessory.designatedUsers ?? [])
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(assetAccessory.businessDocuments ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(assetAccessory.universallyUniqueMappings ?? [])
    );
  }

  protected copyForm(assetAccessory: IAssetAccessory): void {
    this.editForm.patchValue({
      id: assetAccessory.id,
      assetTag: assetAccessory.assetTag,
      assetDetails: assetAccessory.assetDetails,
      comments: assetAccessory.comments,
      commentsContentType: assetAccessory.commentsContentType,
      modelNumber: assetAccessory.modelNumber,
      serialNumber: assetAccessory.serialNumber,
      assetWarranties: assetAccessory.assetWarranties,
      placeholders: assetAccessory.placeholders,
      paymentInvoices: assetAccessory.paymentInvoices,
      serviceOutlet: assetAccessory.serviceOutlet,
      settlements: assetAccessory.settlements,
      assetCategory: assetAccessory.assetCategory,
      purchaseOrders: assetAccessory.purchaseOrders,
      deliveryNotes: assetAccessory.deliveryNotes,
      jobSheets: assetAccessory.jobSheets,
      dealer: assetAccessory.dealer,
      designatedUsers: assetAccessory.designatedUsers,
      businessDocuments: assetAccessory.businessDocuments,
      universallyUniqueMappings: assetAccessory.universallyUniqueMappings,
    });

    this.assetWarrantiesSharedCollection = this.assetWarrantyService.addAssetWarrantyToCollectionIfMissing(
      this.assetWarrantiesSharedCollection,
      ...(assetAccessory.assetWarranties ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(assetAccessory.placeholders ?? [])
    );
    this.paymentInvoicesSharedCollection = this.paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing(
      this.paymentInvoicesSharedCollection,
      ...(assetAccessory.paymentInvoices ?? [])
    );
    this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
      this.serviceOutletsSharedCollection,
      assetAccessory.serviceOutlet
    );
    this.settlementsSharedCollection = this.settlementService.addSettlementToCollectionIfMissing(
      this.settlementsSharedCollection,
      ...(assetAccessory.settlements ?? [])
    );
    this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing(
      this.assetCategoriesSharedCollection,
      assetAccessory.assetCategory
    );
    this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
      this.purchaseOrdersSharedCollection,
      ...(assetAccessory.purchaseOrders ?? [])
    );
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      ...(assetAccessory.deliveryNotes ?? [])
    );
    this.jobSheetsSharedCollection = this.jobSheetService.addJobSheetToCollectionIfMissing(
      this.jobSheetsSharedCollection,
      ...(assetAccessory.jobSheets ?? [])
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      assetAccessory.dealer,
      ...(assetAccessory.designatedUsers ?? [])
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(assetAccessory.businessDocuments ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(assetAccessory.universallyUniqueMappings ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assetWarrantyService
      .query()
      .pipe(map((res: HttpResponse<IAssetWarranty[]>) => res.body ?? []))
      .pipe(
        map((assetWarranties: IAssetWarranty[]) =>
          this.assetWarrantyService.addAssetWarrantyToCollectionIfMissing(
            assetWarranties,
            ...(this.editForm.get('assetWarranties')!.value ?? [])
          )
        )
      )
      .subscribe((assetWarranties: IAssetWarranty[]) => (this.assetWarrantiesSharedCollection = assetWarranties));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.paymentInvoiceService
      .query()
      .pipe(map((res: HttpResponse<IPaymentInvoice[]>) => res.body ?? []))
      .pipe(
        map((paymentInvoices: IPaymentInvoice[]) =>
          this.paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing(
            paymentInvoices,
            ...(this.editForm.get('paymentInvoices')!.value ?? [])
          )
        )
      )
      .subscribe((paymentInvoices: IPaymentInvoice[]) => (this.paymentInvoicesSharedCollection = paymentInvoices));

    this.serviceOutletService
      .query()
      .pipe(map((res: HttpResponse<IServiceOutlet[]>) => res.body ?? []))
      .pipe(
        map((serviceOutlets: IServiceOutlet[]) =>
          this.serviceOutletService.addServiceOutletToCollectionIfMissing(serviceOutlets, this.editForm.get('serviceOutlet')!.value)
        )
      )
      .subscribe((serviceOutlets: IServiceOutlet[]) => (this.serviceOutletsSharedCollection = serviceOutlets));

    this.settlementService
      .query()
      .pipe(map((res: HttpResponse<ISettlement[]>) => res.body ?? []))
      .pipe(
        map((settlements: ISettlement[]) =>
          this.settlementService.addSettlementToCollectionIfMissing(settlements, ...(this.editForm.get('settlements')!.value ?? []))
        )
      )
      .subscribe((settlements: ISettlement[]) => (this.settlementsSharedCollection = settlements));

    this.assetCategoryService
      .query()
      .pipe(map((res: HttpResponse<IAssetCategory[]>) => res.body ?? []))
      .pipe(
        map((assetCategories: IAssetCategory[]) =>
          this.assetCategoryService.addAssetCategoryToCollectionIfMissing(assetCategories, this.editForm.get('assetCategory')!.value)
        )
      )
      .subscribe((assetCategories: IAssetCategory[]) => (this.assetCategoriesSharedCollection = assetCategories));

    this.purchaseOrderService
      .query()
      .pipe(map((res: HttpResponse<IPurchaseOrder[]>) => res.body ?? []))
      .pipe(
        map((purchaseOrders: IPurchaseOrder[]) =>
          this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
            purchaseOrders,
            ...(this.editForm.get('purchaseOrders')!.value ?? [])
          )
        )
      )
      .subscribe((purchaseOrders: IPurchaseOrder[]) => (this.purchaseOrdersSharedCollection = purchaseOrders));

    this.deliveryNoteService
      .query()
      .pipe(map((res: HttpResponse<IDeliveryNote[]>) => res.body ?? []))
      .pipe(
        map((deliveryNotes: IDeliveryNote[]) =>
          this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(deliveryNotes, ...(this.editForm.get('deliveryNotes')!.value ?? []))
        )
      )
      .subscribe((deliveryNotes: IDeliveryNote[]) => (this.deliveryNotesSharedCollection = deliveryNotes));

    this.jobSheetService
      .query()
      .pipe(map((res: HttpResponse<IJobSheet[]>) => res.body ?? []))
      .pipe(
        map((jobSheets: IJobSheet[]) =>
          this.jobSheetService.addJobSheetToCollectionIfMissing(jobSheets, ...(this.editForm.get('jobSheets')!.value ?? []))
        )
      )
      .subscribe((jobSheets: IJobSheet[]) => (this.jobSheetsSharedCollection = jobSheets));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) =>
          this.dealerService.addDealerToCollectionIfMissing(
            dealers,
            this.editForm.get('dealer')!.value,
            ...(this.editForm.get('designatedUsers')!.value ?? [])
          )
        )
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

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

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('universallyUniqueMappings')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );
  }

  protected createFromForm(): IAssetAccessory {
    return {
      ...new AssetAccessory(),
      id: this.editForm.get(['id'])!.value,
      assetTag: this.editForm.get(['assetTag'])!.value,
      assetDetails: this.editForm.get(['assetDetails'])!.value,
      commentsContentType: this.editForm.get(['commentsContentType'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      modelNumber: this.editForm.get(['modelNumber'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      assetWarranties: this.editForm.get(['assetWarranties'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      paymentInvoices: this.editForm.get(['paymentInvoices'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      settlements: this.editForm.get(['settlements'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      purchaseOrders: this.editForm.get(['purchaseOrders'])!.value,
      deliveryNotes: this.editForm.get(['deliveryNotes'])!.value,
      jobSheets: this.editForm.get(['jobSheets'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
      designatedUsers: this.editForm.get(['designatedUsers'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
      universallyUniqueMappings: this.editForm.get(['universallyUniqueMappings'])!.value,
    };
  }

  protected copyFromForm(): IAssetAccessory {
    return {
      ...new AssetAccessory(),
      assetTag: this.editForm.get(['assetTag'])!.value,
      assetDetails: this.editForm.get(['assetDetails'])!.value,
      commentsContentType: this.editForm.get(['commentsContentType'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      modelNumber: this.editForm.get(['modelNumber'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      assetWarranties: this.editForm.get(['assetWarranties'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      paymentInvoices: this.editForm.get(['paymentInvoices'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      settlements: this.editForm.get(['settlements'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      purchaseOrders: this.editForm.get(['purchaseOrders'])!.value,
      deliveryNotes: this.editForm.get(['deliveryNotes'])!.value,
      jobSheets: this.editForm.get(['jobSheets'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
      designatedUsers: this.editForm.get(['designatedUsers'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
      universallyUniqueMappings: this.editForm.get(['universallyUniqueMappings'])!.value,
    };
  }
}
