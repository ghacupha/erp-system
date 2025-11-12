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

import { AssetRegistration, IAssetRegistration } from '../asset-registration.model';
import { AssetRegistrationService } from '../service/asset-registration.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISettlementCurrency } from '../../../erp-settlements/settlement-currency/settlement-currency.model';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { IAssetAccessory } from '../../asset-accessory/asset-accessory.model';
import { SettlementCurrencyService } from '../../../erp-settlements/settlement-currency/service/settlement-currency.service';
import { AssetAccessoryService } from '../../asset-accessory/service/asset-accessory.service';
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
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import {
  assetRegistrationUpdateUpdateSelectedInstance,
  copyingAssetRegistrationStatus,
  creatingAssetRegistrationStatus,
  editingAssetRegistrationStatus
} from '../../../store/selectors/asset-registration-workflow-status.selector';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import { assetRegistrationDataHasMutated } from '../../../store/actions/fixed-assets-register-update-status.actions';

@Component({
  selector: 'jhi-asset-registration-update',
  templateUrl: './asset-registration-update.component.html',
})
export class AssetRegistrationUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];
  paymentInvoicesSharedCollection: IPaymentInvoice[] = [];
  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  settlementsSharedCollection: ISettlement[] = [];
  assetCategoriesSharedCollection: IAssetCategory[] = [];
  purchaseOrdersSharedCollection: IPurchaseOrder[] = [];
  deliveryNotesSharedCollection: IDeliveryNote[] = [];
  jobSheetsSharedCollection: IJobSheet[] = [];
  dealersSharedCollection: IDealer[] = [];
  settlementCurrenciesSharedCollection: ISettlementCurrency[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];
  assetWarrantiesSharedCollection: IAssetWarranty[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  assetAccessoriesSharedCollection: IAssetAccessory[] = [];

  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = {...new AssetRegistration()}

  editForm = this.fb.group({
    id: [],
    assetNumber: [null, [Validators.required]],
    assetTag: [null, [Validators.required]],
    assetDetails: [],
    assetCost: [null, [Validators.required]],
    comments: [],
    commentsContentType: [],
    modelNumber: [],
    serialNumber: [],
    remarks: [],
    capitalizationDate: [null, [Validators.required]],
    historicalCost: [null, [Validators.required]],
    registrationDate: [null, [Validators.required]],
    placeholders: [],
    paymentInvoices: [],
    otherRelatedServiceOutlets: [],
    otherRelatedSettlements: [],
    assetCategory: [null, Validators.required],
    purchaseOrders: [],
    deliveryNotes: [],
    jobSheets: [],
    dealer: [null, Validators.required],
    designatedUsers: [],
    settlementCurrency: [],
    businessDocuments: [],
    assetWarranties: [],
    universallyUniqueMappings: [],
    assetAccessories: [],
    mainServiceOutlet: [],
    acquiringTransaction: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected assetRegistrationService: AssetRegistrationService,
    protected placeholderService: PlaceholderService,
    protected paymentInvoiceService: PaymentInvoiceService,
    protected serviceOutletService: ServiceOutletService,
    protected settlementService: SettlementService,
    protected assetCategoryService: AssetCategoryService,
    protected purchaseOrderService: PurchaseOrderService,
    protected deliveryNoteService: DeliveryNoteService,
    protected jobSheetService: JobSheetService,
    protected dealerService: DealerService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected businessDocumentService: BusinessDocumentService,
    protected assetWarrantyService: AssetWarrantyService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected assetAccessoryService: AssetAccessoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
  ) {
    this.store.pipe(select(copyingAssetRegistrationStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingAssetRegistrationStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingAssetRegistrationStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(assetRegistrationUpdateUpdateSelectedInstance)).subscribe(copied => this.selectedItem = copied);
  }

  ngOnInit(): void {

    if (this.weAreEditing) {

      this.updateForm(this.selectedItem);
    }

    if (this.weAreCopying) {

      this.copyForm(this.selectedItem);

      this.assetRegistrationService.getNextAssetNumber().subscribe(nextValue => {
        if (nextValue.body) {
          this.editForm.patchValue({
            assetNumber: nextValue.body,
          })
        }
      });
    }

    if (this.weAreCreating) {

      this.assetRegistrationService.getNextAssetNumber().subscribe(nextValue => {
        if (nextValue.body) {
          this.editForm.patchValue({
            assetNumber: nextValue.body,
          })
        }
      });
    }

  }

  updateAssetAccessories(updated: IAssetAccessory[]): void  {
    this.editForm.patchValue({ assetAccessories: [...updated]});
  }

  updateOtherServiceOutlets(updated: IServiceOutlet[]): void {
    this.editForm.patchValue({ otherRelatedServiceOutlets: [...updated] });
  }

  updateUniversallyUniqueMappings(updated: IUniversallyUniqueMapping[]): void {
    this.editForm.patchValue({ universallyUniqueMappings: [...updated]});
  }

  updateAssetWarranties(updated: IAssetWarranty[]): void {
    this.editForm.patchValue({ assetWarranties: [...updated] });
  }

  updateBusinessDocuments(updated: IBusinessDocument[]): void {
    this.editForm.patchValue({ businessDocuments: [...updated] });
  }

  updateSettlementCurrency(updated: ISettlementCurrency): void {
    this.editForm.patchValue({ settlementCurrency: updated });
  }

  updateAcquiringTransaction(updated: ISettlement): void {
    this.editForm.patchValue({ acquiringTransaction: updated });
  }

  updateDesignatedUsers(updated: IDealer[]): void {
    this.editForm.patchValue({ designatedUsers: [...updated] });
  }

  updateDealer(updated: IDealer): void {
    this.editForm.patchValue( { dealer: updated });
  }

  updatePaymentInvoices(updated: IPaymentInvoice[]): void {
    this.editForm.patchValue({ paymentInvoices: [...updated] });
  }

  updateMainServiceOutlet(updated: IServiceOutlet): void {
    this.editForm.patchValue({ mainServiceOutlet: updated });
  }

  updateSettlements(updated: ISettlement[]): void {
    this.editForm.patchValue({ otherRelatedSettlements: [...updated] });
  }

  updateAssetCategory(updated: IAssetCategory): void {
    this.editForm.patchValue({ assetCategory: updated });
  }

  updatePurchaseOrders(updated: IPurchaseOrder[]): void {
    this.editForm.patchValue({ purchaseOrders: [...updated] });
  }

  updateDeliveryNotes(updated: IDeliveryNote[]): void {
    this.editForm.patchValue({ deliveryNotes: [...updated] });
  }

  updateJobSheets(updated: IJobSheet[]): void {
    this.editForm.patchValue({ jobSheets: [...updated] });
  }

  updatePlaceholders(updated: IPlaceholder[]): void {
    this.editForm.patchValue({ placeholders: [...updated] });
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
    this.store.dispatch(assetRegistrationDataHasMutated());

    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.assetRegistrationService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.assetRegistrationService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.assetRegistrationService.create(this.copyFromForm()));
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

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
  }

  trackAssetWarrantyById(index: number, item: IAssetWarranty): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackAssetAccessoryById(index: number, item: IAssetAccessory): number {
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

  getSelectedServiceOutlet(option: IServiceOutlet, selectedVals?: IServiceOutlet[]): IServiceOutlet {
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

  getSelectedAssetAccessory(option: IAssetAccessory, selectedVals?: IAssetAccessory[]): IAssetAccessory {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetRegistration>>): void {
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

  protected updateForm(assetRegistration: IAssetRegistration): void {
    this.editForm.patchValue({
      id: assetRegistration.id,
      assetNumber: assetRegistration.assetNumber,
      assetTag: assetRegistration.assetTag,
      assetDetails: assetRegistration.assetDetails,
      assetCost: assetRegistration.assetCost,
      comments: assetRegistration.comments,
      commentsContentType: assetRegistration.commentsContentType,
      modelNumber: assetRegistration.modelNumber,
      serialNumber: assetRegistration.serialNumber,
      remarks: assetRegistration.remarks,
      capitalizationDate: assetRegistration.capitalizationDate,
      historicalCost: assetRegistration.historicalCost,
      registrationDate: assetRegistration.registrationDate,
      placeholders: assetRegistration.placeholders,
      paymentInvoices: assetRegistration.paymentInvoices,
      otherRelatedServiceOutlets: assetRegistration.otherRelatedServiceOutlets,
      otherRelatedSettlements: assetRegistration.otherRelatedSettlements,
      assetCategory: assetRegistration.assetCategory,
      purchaseOrders: assetRegistration.purchaseOrders,
      deliveryNotes: assetRegistration.deliveryNotes,
      jobSheets: assetRegistration.jobSheets,
      dealer: assetRegistration.dealer,
      designatedUsers: assetRegistration.designatedUsers,
      settlementCurrency: assetRegistration.settlementCurrency,
      businessDocuments: assetRegistration.businessDocuments,
      assetWarranties: assetRegistration.assetWarranties,
      universallyUniqueMappings: assetRegistration.universallyUniqueMappings,
      assetAccessories: assetRegistration.assetAccessories,
      mainServiceOutlet: assetRegistration.mainServiceOutlet,
      acquiringTransaction: assetRegistration.acquiringTransaction,
    });

    // this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
    //   this.placeholdersSharedCollection,
    //   ...(assetRegistration.placeholders ?? [])
    // );
    // this.paymentInvoicesSharedCollection = this.paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing(
    //   this.paymentInvoicesSharedCollection,
    //   ...(assetRegistration.paymentInvoices ?? [])
    // );
    // this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
    //   this.serviceOutletsSharedCollection,
    //   ...(assetRegistration.otherRelatedServiceOutlets ?? []),
    //   assetRegistration.mainServiceOutlet
    // );
    // this.settlementsSharedCollection = this.settlementService.addSettlementToCollectionIfMissing(
    //   this.settlementsSharedCollection,
    //   ...(assetRegistration.otherRelatedSettlements ?? [])
    // );
    // this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing(
    //   this.assetCategoriesSharedCollection,
    //   assetRegistration.assetCategory
    // );
    // this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
    //   this.purchaseOrdersSharedCollection,
    //   ...(assetRegistration.purchaseOrders ?? [])
    // );
    // this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
    //   this.deliveryNotesSharedCollection,
    //   ...(assetRegistration.deliveryNotes ?? [])
    // );
    // this.jobSheetsSharedCollection = this.jobSheetService.addJobSheetToCollectionIfMissing(
    //   this.jobSheetsSharedCollection,
    //   ...(assetRegistration.jobSheets ?? [])
    // );
    // this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
    //   this.dealersSharedCollection,
    //   assetRegistration.dealer,
    //   ...(assetRegistration.designatedUsers ?? [])
    // );
    // this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
    //   this.settlementCurrenciesSharedCollection,
    //   assetRegistration.settlementCurrency
    // );
    // this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
    //   this.businessDocumentsSharedCollection,
    //   ...(assetRegistration.businessDocuments ?? [])
    // );
    // this.assetWarrantiesSharedCollection = this.assetWarrantyService.addAssetWarrantyToCollectionIfMissing(
    //   this.assetWarrantiesSharedCollection,
    //   ...(assetRegistration.assetWarranties ?? [])
    // );
    // this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
    //   this.universallyUniqueMappingsSharedCollection,
    //   ...(assetRegistration.universallyUniqueMappings ?? [])
    // );
    // this.assetAccessoriesSharedCollection = this.assetAccessoryService.addAssetAccessoryToCollectionIfMissing(
    //   this.assetAccessoriesSharedCollection,
    //   ...(assetRegistration.assetAccessories ?? [])
    // );
  }

  protected copyForm(assetRegistration: IAssetRegistration): void {
    this.editForm.patchValue({
      assetTag: assetRegistration.assetTag,
      assetDetails: assetRegistration.assetDetails,
      assetCost: assetRegistration.assetCost,
      comments: assetRegistration.comments,
      commentsContentType: assetRegistration.commentsContentType,
      modelNumber: assetRegistration.modelNumber,
      serialNumber: assetRegistration.serialNumber,
      remarks: assetRegistration.remarks,
      capitalizationDate: assetRegistration.capitalizationDate,
      historicalCost: assetRegistration.historicalCost,
      registrationDate: assetRegistration.registrationDate,
      placeholders: assetRegistration.placeholders,
      paymentInvoices: assetRegistration.paymentInvoices,
      otherRelatedServiceOutlets: assetRegistration.otherRelatedServiceOutlets,
      otherRelatedSettlements: assetRegistration.otherRelatedSettlements,
      assetCategory: assetRegistration.assetCategory,
      purchaseOrders: assetRegistration.purchaseOrders,
      deliveryNotes: assetRegistration.deliveryNotes,
      jobSheets: assetRegistration.jobSheets,
      dealer: assetRegistration.dealer,
      designatedUsers: assetRegistration.designatedUsers,
      settlementCurrency: assetRegistration.settlementCurrency,
      businessDocuments: assetRegistration.businessDocuments,
      assetWarranties: assetRegistration.assetWarranties,
      universallyUniqueMappings: assetRegistration.universallyUniqueMappings,
      assetAccessories: assetRegistration.assetAccessories,
      mainServiceOutlet: assetRegistration.mainServiceOutlet,
      acquiringTransaction: assetRegistration.acquiringTransaction,
    });

    // this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
    //   this.placeholdersSharedCollection,
    //   ...(assetRegistration.placeholders ?? [])
    // );
    // this.paymentInvoicesSharedCollection = this.paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing(
    //   this.paymentInvoicesSharedCollection,
    //   ...(assetRegistration.paymentInvoices ?? [])
    // );
    // this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
    //   this.serviceOutletsSharedCollection,
    //   ...(assetRegistration.otherRelatedServiceOutlets ?? []),
    //   assetRegistration.mainServiceOutlet
    // );
    // this.settlementsSharedCollection = this.settlementService.addSettlementToCollectionIfMissing(
    //   this.settlementsSharedCollection,
    //   ...(assetRegistration.otherRelatedSettlements ?? [])
    // );
    // this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing(
    //   this.assetCategoriesSharedCollection,
    //   assetRegistration.assetCategory
    // );
    // this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
    //   this.purchaseOrdersSharedCollection,
    //   ...(assetRegistration.purchaseOrders ?? [])
    // );
    // this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
    //   this.deliveryNotesSharedCollection,
    //   ...(assetRegistration.deliveryNotes ?? [])
    // );
    // this.jobSheetsSharedCollection = this.jobSheetService.addJobSheetToCollectionIfMissing(
    //   this.jobSheetsSharedCollection,
    //   ...(assetRegistration.jobSheets ?? [])
    // );
    // this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
    //   this.dealersSharedCollection,
    //   assetRegistration.dealer,
    //   ...(assetRegistration.designatedUsers ?? [])
    // );
    // this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
    //   this.settlementCurrenciesSharedCollection,
    //   assetRegistration.settlementCurrency
    // );
    // this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
    //   this.businessDocumentsSharedCollection,
    //   ...(assetRegistration.businessDocuments ?? [])
    // );
    // this.assetWarrantiesSharedCollection = this.assetWarrantyService.addAssetWarrantyToCollectionIfMissing(
    //   this.assetWarrantiesSharedCollection,
    //   ...(assetRegistration.assetWarranties ?? [])
    // );
    // this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
    //   this.universallyUniqueMappingsSharedCollection,
    //   ...(assetRegistration.universallyUniqueMappings ?? [])
    // );
    // this.assetAccessoriesSharedCollection = this.assetAccessoryService.addAssetAccessoryToCollectionIfMissing(
    //   this.assetAccessoriesSharedCollection,
    //   ...(assetRegistration.assetAccessories ?? [])
    // );
  }

  // protected loadRelationshipsOptions(): void {
  //   this.placeholderService
  //     .query()
  //     .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
  //     .pipe(
  //       map((placeholders: IPlaceholder[]) =>
  //         this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
  //       )
  //     )
  //     .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  //
  //   this.paymentInvoiceService
  //     .query()
  //     .pipe(map((res: HttpResponse<IPaymentInvoice[]>) => res.body ?? []))
  //     .pipe(
  //       map((paymentInvoices: IPaymentInvoice[]) =>
  //         this.paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing(
  //           paymentInvoices,
  //           ...(this.editForm.get('paymentInvoices')!.value ?? [])
  //         )
  //       )
  //     )
  //     .subscribe((paymentInvoices: IPaymentInvoice[]) => (this.paymentInvoicesSharedCollection = paymentInvoices));
  //
  //   this.serviceOutletService
  //     .query()
  //     .pipe(map((res: HttpResponse<IServiceOutlet[]>) => res.body ?? []))
  //     .pipe(
  //       map((serviceOutlets: IServiceOutlet[]) =>
  //         this.serviceOutletService.addServiceOutletToCollectionIfMissing(
  //           serviceOutlets,
  //           ...(this.editForm.get('serviceOutlets')!.value ?? []),
  //           this.editForm.get('mainServiceOutlet')!.value
  //         )
  //       )
  //     )
  //     .subscribe((serviceOutlets: IServiceOutlet[]) => (this.serviceOutletsSharedCollection = serviceOutlets));
  //
  //   this.settlementService
  //     .query()
  //     .pipe(map((res: HttpResponse<ISettlement[]>) => res.body ?? []))
  //     .pipe(
  //       map((settlements: ISettlement[]) =>
  //         this.settlementService.addSettlementToCollectionIfMissing(settlements, ...(this.editForm.get('settlements')!.value ?? []))
  //       )
  //     )
  //     .subscribe((settlements: ISettlement[]) => (this.settlementsSharedCollection = settlements));
  //
  //   this.assetCategoryService
  //     .query()
  //     .pipe(map((res: HttpResponse<IAssetCategory[]>) => res.body ?? []))
  //     .pipe(
  //       map((assetCategories: IAssetCategory[]) =>
  //         this.assetCategoryService.addAssetCategoryToCollectionIfMissing(assetCategories, this.editForm.get('assetCategory')!.value)
  //       )
  //     )
  //     .subscribe((assetCategories: IAssetCategory[]) => (this.assetCategoriesSharedCollection = assetCategories));
  //
  //   this.purchaseOrderService
  //     .query()
  //     .pipe(map((res: HttpResponse<IPurchaseOrder[]>) => res.body ?? []))
  //     .pipe(
  //       map((purchaseOrders: IPurchaseOrder[]) =>
  //         this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
  //           purchaseOrders,
  //           ...(this.editForm.get('purchaseOrders')!.value ?? [])
  //         )
  //       )
  //     )
  //     .subscribe((purchaseOrders: IPurchaseOrder[]) => (this.purchaseOrdersSharedCollection = purchaseOrders));
  //
  //   this.deliveryNoteService
  //     .query()
  //     .pipe(map((res: HttpResponse<IDeliveryNote[]>) => res.body ?? []))
  //     .pipe(
  //       map((deliveryNotes: IDeliveryNote[]) =>
  //         this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(deliveryNotes, ...(this.editForm.get('deliveryNotes')!.value ?? []))
  //       )
  //     )
  //     .subscribe((deliveryNotes: IDeliveryNote[]) => (this.deliveryNotesSharedCollection = deliveryNotes));
  //
  //   this.jobSheetService
  //     .query()
  //     .pipe(map((res: HttpResponse<IJobSheet[]>) => res.body ?? []))
  //     .pipe(
  //       map((jobSheets: IJobSheet[]) =>
  //         this.jobSheetService.addJobSheetToCollectionIfMissing(jobSheets, ...(this.editForm.get('jobSheets')!.value ?? []))
  //       )
  //     )
  //     .subscribe((jobSheets: IJobSheet[]) => (this.jobSheetsSharedCollection = jobSheets));
  //
  //   this.dealerService
  //     .query()
  //     .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
  //     .pipe(
  //       map((dealers: IDealer[]) =>
  //         this.dealerService.addDealerToCollectionIfMissing(
  //           dealers,
  //           this.editForm.get('dealer')!.value,
  //           ...(this.editForm.get('designatedUsers')!.value ?? [])
  //         )
  //       )
  //     )
  //     .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));
  //
  //   this.settlementCurrencyService
  //     .query()
  //     .pipe(map((res: HttpResponse<ISettlementCurrency[]>) => res.body ?? []))
  //     .pipe(
  //       map((settlementCurrencies: ISettlementCurrency[]) =>
  //         this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
  //           settlementCurrencies,
  //           this.editForm.get('settlementCurrency')!.value
  //         )
  //       )
  //     )
  //     .subscribe((settlementCurrencies: ISettlementCurrency[]) => (this.settlementCurrenciesSharedCollection = settlementCurrencies));
  //
  //   this.businessDocumentService
  //     .query()
  //     .pipe(map((res: HttpResponse<IBusinessDocument[]>) => res.body ?? []))
  //     .pipe(
  //       map((businessDocuments: IBusinessDocument[]) =>
  //         this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
  //           businessDocuments,
  //           ...(this.editForm.get('businessDocuments')!.value ?? [])
  //         )
  //       )
  //     )
  //     .subscribe((businessDocuments: IBusinessDocument[]) => (this.businessDocumentsSharedCollection = businessDocuments));
  //
  //   this.assetWarrantyService
  //     .query()
  //     .pipe(map((res: HttpResponse<IAssetWarranty[]>) => res.body ?? []))
  //     .pipe(
  //       map((assetWarranties: IAssetWarranty[]) =>
  //         this.assetWarrantyService.addAssetWarrantyToCollectionIfMissing(
  //           assetWarranties,
  //           ...(this.editForm.get('assetWarranties')!.value ?? [])
  //         )
  //       )
  //     )
  //     .subscribe((assetWarranties: IAssetWarranty[]) => (this.assetWarrantiesSharedCollection = assetWarranties));
  //
  //   this.universallyUniqueMappingService
  //     .query()
  //     .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
  //     .pipe(
  //       map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
  //         this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
  //           universallyUniqueMappings,
  //           ...(this.editForm.get('universallyUniqueMappings')!.value ?? [])
  //         )
  //       )
  //     )
  //     .subscribe(
  //       (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
  //         (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
  //     );
  //
  //   this.assetAccessoryService
  //     .query()
  //     .pipe(map((res: HttpResponse<IAssetAccessory[]>) => res.body ?? []))
  //     .pipe(
  //       map((assetAccessories: IAssetAccessory[]) =>
  //         this.assetAccessoryService.addAssetAccessoryToCollectionIfMissing(
  //           assetAccessories,
  //           ...(this.editForm.get('assetAccessories')!.value ?? [])
  //         )
  //       )
  //     )
  //     .subscribe((assetAccessories: IAssetAccessory[]) => (this.assetAccessoriesSharedCollection = assetAccessories));
  // }

  protected createFromForm(): IAssetRegistration {
    return {
      ...new AssetRegistration(),
      id: this.editForm.get(['id'])!.value,
      assetNumber: this.editForm.get(['assetNumber'])!.value,
      assetTag: this.editForm.get(['assetTag'])!.value,
      assetDetails: this.editForm.get(['assetDetails'])!.value,
      assetCost: this.editForm.get(['assetCost'])!.value,
      commentsContentType: this.editForm.get(['commentsContentType'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      modelNumber: this.editForm.get(['modelNumber'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      capitalizationDate: this.editForm.get(['capitalizationDate'])!.value,
      historicalCost: this.editForm.get(['historicalCost'])!.value,
      registrationDate: this.editForm.get(['registrationDate'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      paymentInvoices: this.editForm.get(['paymentInvoices'])!.value,
      otherRelatedServiceOutlets: this.editForm.get(['otherRelatedServiceOutlets'])!.value,
      otherRelatedSettlements: this.editForm.get(['otherRelatedSettlements'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      purchaseOrders: this.editForm.get(['purchaseOrders'])!.value,
      deliveryNotes: this.editForm.get(['deliveryNotes'])!.value,
      jobSheets: this.editForm.get(['jobSheets'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
      designatedUsers: this.editForm.get(['designatedUsers'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
      assetWarranties: this.editForm.get(['assetWarranties'])!.value,
      universallyUniqueMappings: this.editForm.get(['universallyUniqueMappings'])!.value,
      assetAccessories: this.editForm.get(['assetAccessories'])!.value,
      mainServiceOutlet: this.editForm.get(['mainServiceOutlet'])!.value,
      acquiringTransaction: this.editForm.get(['acquiringTransaction'])!.value,
    };
  }

  protected copyFromForm(): IAssetRegistration {
    return {
      ...new AssetRegistration(),
      assetNumber: this.editForm.get(['assetNumber'])!.value,
      assetTag: this.editForm.get(['assetTag'])!.value,
      assetDetails: this.editForm.get(['assetDetails'])!.value,
      assetCost: this.editForm.get(['assetCost'])!.value,
      commentsContentType: this.editForm.get(['commentsContentType'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      modelNumber: this.editForm.get(['modelNumber'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      capitalizationDate: this.editForm.get(['capitalizationDate'])!.value,
      historicalCost: this.editForm.get(['historicalCost'])!.value,
      registrationDate: this.editForm.get(['registrationDate'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      paymentInvoices: this.editForm.get(['paymentInvoices'])!.value,
      otherRelatedServiceOutlets: this.editForm.get(['otherRelatedServiceOutlets'])!.value,
      otherRelatedSettlements: this.editForm.get(['otherRelatedSettlements'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      purchaseOrders: this.editForm.get(['purchaseOrders'])!.value,
      deliveryNotes: this.editForm.get(['deliveryNotes'])!.value,
      jobSheets: this.editForm.get(['jobSheets'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
      designatedUsers: this.editForm.get(['designatedUsers'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
      assetWarranties: this.editForm.get(['assetWarranties'])!.value,
      universallyUniqueMappings: this.editForm.get(['universallyUniqueMappings'])!.value,
      assetAccessories: this.editForm.get(['assetAccessories'])!.value,
      mainServiceOutlet: this.editForm.get(['mainServiceOutlet'])!.value,
      acquiringTransaction: this.editForm.get(['acquiringTransaction'])!.value,
    };
  }
}
