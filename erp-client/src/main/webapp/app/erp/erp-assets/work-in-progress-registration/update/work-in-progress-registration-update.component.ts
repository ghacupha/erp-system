///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { Observable, of, Subject } from 'rxjs';
import { finalize, map} from 'rxjs/operators';

import { IWorkInProgressRegistration, WorkInProgressRegistration } from '../work-in-progress-registration.model';
import { WorkInProgressRegistrationService } from '../service/work-in-progress-registration.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IJobSheet } from '../../../erp-settlements/job-sheet/job-sheet.model';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { IDeliveryNote } from '../../../erp-settlements/delivery-note/delivery-note.model';
import { PurchaseOrderService } from '../../../erp-settlements/purchase-order/service/purchase-order.service';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { JobSheetService } from '../../../erp-settlements/job-sheet/service/job-sheet.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { DeliveryNoteService } from '../../../erp-settlements/delivery-note/service/delivery-note.service';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { IPurchaseOrder } from '../../../erp-settlements/purchase-order/purchase-order.model';
import { IPaymentInvoice } from '../../../erp-settlements/payment-invoice/payment-invoice.model';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';
import { PaymentInvoiceService } from '../../../erp-settlements/payment-invoice/service/payment-invoice.service';
import { IPaymentLabel } from '../../../erp-pages/payment-label/payment-label.model';
import { IAssetWarranty } from '../../asset-warranty/asset-warranty.model';
import { IAssetAccessory } from '../../asset-accessory/asset-accessory.model';
import { AssetAccessoryService } from '../../asset-accessory/service/asset-accessory.service';
import { AssetWarrantyService } from '../../asset-warranty/service/asset-warranty.service';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingWIPRegistrationStatus,
  creatingWIPRegistrationStatus,
  editingWIPRegistrationStatus,
  wipRegistrationUpdateSelectedInstance
} from '../../../store/selectors/wip-registration-workflow-status.selector';
import { wipRegistrationDataHasMutated } from '../../../store/actions/wip-registration-update-status.actions';
import { ISettlementCurrency } from '../../../erp-settlements/settlement-currency/settlement-currency.model';
import { IWorkProjectRegister } from '../../work-project-register/work-project-register.model';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { SettlementCurrencyService } from '../../../erp-settlements/settlement-currency/service/settlement-currency.service';
import { WorkProjectRegisterService } from '../../work-project-register/service/work-project-register.service';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';

@Component({
  selector: 'jhi-work-in-progress-registration-update',
  templateUrl: './work-in-progress-registration-update.component.html',
})
export class WorkInProgressRegistrationUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];
  workInProgressRegistrationsSharedCollection: IWorkInProgressRegistration[] = [];
  settlementCurrenciesSharedCollection: ISettlementCurrency[] = [];
  workProjectRegistersSharedCollection: IWorkProjectRegister[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];
  assetAccessoriesSharedCollection: IAssetAccessory[] = [];
  assetWarrantiesSharedCollection: IAssetWarranty[] = [];
  paymentInvoicesSharedCollection: IPaymentInvoice[] = [];
  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  settlementsSharedCollection: ISettlement[] = [];
  purchaseOrdersSharedCollection: IPurchaseOrder[] = [];
  deliveryNotesSharedCollection: IDeliveryNote[] = [];
  jobSheetsSharedCollection: IJobSheet[] = [];
  dealersSharedCollection: IDealer[] = [];

  editForm = this.fb.group({
    id: [],
    sequenceNumber: [null, [Validators.required]],
    particulars: [],
    instalmentDate: [],
    instalmentAmount: [],
    comments: [],
    commentsContentType: [],
    levelOfCompletion: [],
    completed: [],
    placeholders: [],
    workInProgressGroup: [],
    settlementCurrency: [],
    workProjectRegister: [],
    businessDocuments: [],
    assetAccessories: [],
    assetWarranties: [],
    invoice: [],
    outletCode: [],
    settlementTransaction: [],
    purchaseOrder: [],
    deliveryNote: [],
    jobSheet: [],
    dealer: [],
  });

  minAccountLengthTerm = 3;

  placeholdersLoading = false;
  placeholderControlInput$ = new Subject<string>();
  placeholderLookups$: Observable<IPlaceholder[]> = of([]);

  paymentInvoicesLoading = false;

  settlementsLoading = false;

  purchaseOrdersLoading = false;

  serviceOutletsLoading = false;

  assetCategoriesLoading = false;

  deliveryNotesLoading = false;

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = {...new WorkInProgressRegistration()}

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected workInProgressRegistrationService: WorkInProgressRegistrationService,
    protected placeholderService: PlaceholderService,
    protected paymentInvoiceService: PaymentInvoiceService,
    protected serviceOutletService: ServiceOutletService,
    protected settlementService: SettlementService,
    protected purchaseOrderService: PurchaseOrderService,
    protected deliveryNoteService: DeliveryNoteService,
    protected jobSheetService: JobSheetService,
    protected dealerService: DealerService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected workProjectRegisterService: WorkProjectRegisterService,
    protected businessDocumentService: BusinessDocumentService,
    protected assetAccessoryService: AssetAccessoryService,
    protected assetWarrantyService: AssetWarrantyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
  ) {
    this.store.pipe(select(copyingWIPRegistrationStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingWIPRegistrationStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingWIPRegistrationStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(wipRegistrationUpdateSelectedInstance)).subscribe(copied => this.selectedItem = copied);
  }

  ngOnInit(): void {
    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);
    }

    if (this.weAreCopying) {

      this.copyForm(this.selectedItem);

      this.workInProgressRegistrationService.getNextAssetNumber().subscribe(nextValue => {
        if (nextValue.body) {
          this.editForm.patchValue({
            sequenceNumber: nextValue.body,
          })
        }
      });
    }

    if (this.weAreCreating) {

      this.workInProgressRegistrationService.getNextAssetNumber().subscribe(nextValue => {
        if (nextValue.body) {
          this.editForm.patchValue({
            sequenceNumber: nextValue.body,
          })
        }
      });

    }

    this.updateDetailsGivenTransferSettlement();
  }

  updateDetailsGivenTransferSettlement(): void {
    this.editForm.get(['settlementTransaction'])?.valueChanges.subscribe((trfSettlement) => {
      this.settlementService.find(trfSettlement.id).subscribe((settlementTransactionResponse) => {
        if (settlementTransactionResponse.body) {
          const transferredSettlement = settlementTransactionResponse.body;

          this.editForm.patchValue({
            instalmentDate: transferredSettlement.paymentDate,
            instalmentAmount: transferredSettlement.paymentAmount,
            particulars: transferredSettlement.description,
            businessDocuments: transferredSettlement.businessDocuments,
            dealer: transferredSettlement.biller,
            placeholder: transferredSettlement.placeholders,
            settlementCurrency: transferredSettlement.settlementCurrency

          });
        }
      });
    });
  }

  updatePlaceholders(updated: IPlaceholder[]): void {
    this.editForm.patchValue({ placeholders: [...updated] });
  }
  updateDealer(updated: IDealer): void {
    this.editForm.patchValue({ dealer: updated });
  }
  updateJobSheet(updated: IJobSheet): void {
    this.editForm.patchValue({ jobSheet: updated });
  }
  updateDeliveryNote(updated: IDeliveryNote): void {
    this.editForm.patchValue({ deliveryNote: updated });
  }
  updatePurchaseOrder(updated: IPurchaseOrder): void {
    this.editForm.patchValue({ purchaseOrder: updated });
  }
  updateSettlementTransaction(updated: ISettlement): void {
    this.editForm.patchValue({ settlementTransaction: updated });
  }
  updateOutletCode(updated: IServiceOutlet): void {
    this.editForm.patchValue({ outletCode: updated });
  }
  updateInvoice(updated: IPaymentInvoice): void {
    this.editForm.patchValue({ invoice: updated });
  }
  updateAssetWarranties(updated: IAssetWarranty[]): void {
    this.editForm.patchValue({ assetWarranties: [...updated] });
  }

  updateAssetAccessories(updated: IAssetAccessory[]): void  {
    this.editForm.patchValue({ assetAccessories: [...updated]});
  }

  updateBusinessDocuments(updated: IBusinessDocument[]): void  {
    this.editForm.patchValue({ businessDocuments: [...updated]});
  }

  updateCurrency(updated: ISettlementCurrency): void  {
    this.editForm.patchValue({ settlementCurrency: updated});
  }

  updateWorkInProgressGroup(updated: IWorkInProgressRegistration): void  {
    this.editForm.patchValue({ workInProgressGroup: updated});
  }

  updateWorkProjectRegister(updated: IWorkProjectRegister): void  {
    this.editForm.patchValue({ workProjectRegister: updated});
  }

  trackWorkInProgressRegistrationById(index: number, item: IWorkInProgressRegistration): number {
    return item.id!;
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackWorkProjectRegisterById(index: number, item: IWorkProjectRegister): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
  }

  trackAssetAccessoryById(index: number, item: IAssetAccessory): number {
    return item.id!;
  }

  trackAssetWarrantyById(index: number, item: IAssetWarranty): number {
    return item.id!;
  }

  trackPaymentInvoiceByFn(item: IPaymentInvoice): number {
    return item.id!;
  }

  trackPlaceholdersByFn(item: IPaymentLabel): number {
    return item.id!;
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
    this.store.dispatch(wipRegistrationDataHasMutated());
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.workInProgressRegistrationService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.workInProgressRegistrationService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.workInProgressRegistrationService.create(this.copyFromForm()));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkInProgressRegistration>>): void {
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

  protected updateForm(workInProgressRegistration: IWorkInProgressRegistration): void {
    this.editForm.patchValue({
      id: workInProgressRegistration.id,
      sequenceNumber: workInProgressRegistration.sequenceNumber,
      particulars: workInProgressRegistration.particulars,
      instalmentDate: workInProgressRegistration.instalmentDate,
      instalmentAmount: workInProgressRegistration.instalmentAmount,
      comments: workInProgressRegistration.comments,
      commentsContentType: workInProgressRegistration.commentsContentType,
      levelOfCompletion: workInProgressRegistration.levelOfCompletion,
      completed: workInProgressRegistration.completed,
      placeholders: workInProgressRegistration.placeholders,
      workInProgressGroup: workInProgressRegistration.workInProgressGroup,
      settlementCurrency: workInProgressRegistration.settlementCurrency,
      workProjectRegister: workInProgressRegistration.workProjectRegister,
      businessDocuments: workInProgressRegistration.businessDocuments,
      assetAccessories: workInProgressRegistration.assetAccessories,
      assetWarranties: workInProgressRegistration.assetWarranties,
      invoice: workInProgressRegistration.invoice,
      outletCode: workInProgressRegistration.outletCode,
      settlementTransaction: workInProgressRegistration.settlementTransaction,
      purchaseOrder: workInProgressRegistration.purchaseOrder,
      deliveryNote: workInProgressRegistration.deliveryNote,
      jobSheet: workInProgressRegistration.jobSheet,
      dealer: workInProgressRegistration.dealer,
    });

  }

  protected copyForm(workInProgressRegistration: IWorkInProgressRegistration): void {
    this.editForm.patchValue({
      id: workInProgressRegistration.id,
      sequenceNumber: workInProgressRegistration.sequenceNumber,
      particulars: workInProgressRegistration.particulars,
      instalmentDate: workInProgressRegistration.instalmentDate,
      instalmentAmount: workInProgressRegistration.instalmentAmount,
      comments: workInProgressRegistration.comments,
      commentsContentType: workInProgressRegistration.commentsContentType,
      levelOfCompletion: workInProgressRegistration.levelOfCompletion,
      completed: workInProgressRegistration.completed,
      placeholders: workInProgressRegistration.placeholders,
      workInProgressGroup: workInProgressRegistration.workInProgressGroup,
      settlementCurrency: workInProgressRegistration.settlementCurrency,
      workProjectRegister: workInProgressRegistration.workProjectRegister,
      businessDocuments: workInProgressRegistration.businessDocuments,
      assetAccessories: workInProgressRegistration.assetAccessories,
      assetWarranties: workInProgressRegistration.assetWarranties,
      invoice: workInProgressRegistration.invoice,
      outletCode: workInProgressRegistration.outletCode,
      settlementTransaction: workInProgressRegistration.settlementTransaction,
      purchaseOrder: workInProgressRegistration.purchaseOrder,
      deliveryNote: workInProgressRegistration.deliveryNote,
      jobSheet: workInProgressRegistration.jobSheet,
      dealer: workInProgressRegistration.dealer,
    });

  }

  protected createFromForm(): IWorkInProgressRegistration {
    return {
      ...new WorkInProgressRegistration(),
      id: this.editForm.get(['id'])!.value,
      sequenceNumber: this.editForm.get(['sequenceNumber'])!.value,
      particulars: this.editForm.get(['particulars'])!.value,
      instalmentDate: this.editForm.get(['instalmentDate'])!.value,
      instalmentAmount: this.editForm.get(['instalmentAmount'])!.value,
      commentsContentType: this.editForm.get(['commentsContentType'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      levelOfCompletion: this.editForm.get(['levelOfCompletion'])!.value,
      completed: this.editForm.get(['completed'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      workInProgressGroup: this.editForm.get(['workInProgressGroup'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      workProjectRegister: this.editForm.get(['workProjectRegister'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
      assetAccessories: this.editForm.get(['assetAccessories'])!.value,
      assetWarranties: this.editForm.get(['assetWarranties'])!.value,
      invoice: this.editForm.get(['invoice'])!.value,
      outletCode: this.editForm.get(['outletCode'])!.value,
      settlementTransaction: this.editForm.get(['settlementTransaction'])!.value,
      purchaseOrder: this.editForm.get(['purchaseOrder'])!.value,
      deliveryNote: this.editForm.get(['deliveryNote'])!.value,
      jobSheet: this.editForm.get(['jobSheet'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
    };
  }

  protected copyFromForm(): IWorkInProgressRegistration {
    return {
      ...new WorkInProgressRegistration(),
      // id: this.editForm.get(['id'])!.value,
      sequenceNumber: this.editForm.get(['sequenceNumber'])!.value,
      particulars: this.editForm.get(['particulars'])!.value,
      instalmentDate: this.editForm.get(['instalmentDate'])!.value,
      instalmentAmount: this.editForm.get(['instalmentAmount'])!.value,
      commentsContentType: this.editForm.get(['commentsContentType'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      levelOfCompletion: this.editForm.get(['levelOfCompletion'])!.value,
      completed: this.editForm.get(['completed'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      workInProgressGroup: this.editForm.get(['workInProgressGroup'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      workProjectRegister: this.editForm.get(['workProjectRegister'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
      assetAccessories: this.editForm.get(['assetAccessories'])!.value,
      assetWarranties: this.editForm.get(['assetWarranties'])!.value,
      invoice: this.editForm.get(['invoice'])!.value,
      outletCode: this.editForm.get(['outletCode'])!.value,
      settlementTransaction: this.editForm.get(['settlementTransaction'])!.value,
      purchaseOrder: this.editForm.get(['purchaseOrder'])!.value,
      deliveryNote: this.editForm.get(['deliveryNote'])!.value,
      jobSheet: this.editForm.get(['jobSheet'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
    };
  }
}
