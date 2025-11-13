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
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISettlementRequisition, SettlementRequisition } from '../settlement-requisition.model';
import { SettlementRequisitionService } from '../service/settlement-requisition.service';
import { ISettlementCurrency } from '../../settlement-currency/settlement-currency.model';
import { IJobSheet } from '../../job-sheet/job-sheet.model';
import { IDeliveryNote } from '../../delivery-note/delivery-note.model';
import { IPaymentInvoice } from '../../payment-invoice/payment-invoice.model';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { SettlementCurrencyService } from '../../settlement-currency/service/settlement-currency.service';
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
import { PaymentStatus } from '../../../erp-common/enumerations/payment-status.model';
import { v4 as uuidv4 } from 'uuid';
import { SearchWithPagination } from '../../../../core/request/request.model';
import { ISettlement } from '../../settlement/settlement.model';
import { SettlementService } from '../../settlement/service/settlement.service';

@Component({
  selector: 'jhi-settlement-requisition-update',
  templateUrl: './settlement-requisition-update.component.html',
})
export class SettlementRequisitionUpdateComponent implements OnInit {
  isSaving = false;
  paymentStatusValues = Object.keys(PaymentStatus);
  tod = dayjs();

  settlementCurrenciesSharedCollection: ISettlementCurrency[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  dealersSharedCollection: IDealer[] = [];
  paymentInvoicesSharedCollection: IPaymentInvoice[] = [];
  deliveryNotesSharedCollection: IDeliveryNote[] = [];
  jobSheetsSharedCollection: IJobSheet[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  settlementsSharedCollection: ISettlement[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    serialNumber: [null, [Validators.required]],
    timeOfRequisition: [null, [Validators.required]],
    requisitionNumber: [null, [Validators.required]],
    paymentAmount: [null, [Validators.required]],
    paymentStatus: [null, [Validators.required]],
    settlementCurrency: [null, Validators.required],
    currentOwner: [null, Validators.required],
    nativeOwner: [null, Validators.required],
    nativeDepartment: [null, Validators.required],
    biller: [null, Validators.required],
    paymentInvoices: [],
    deliveryNotes: [],
    jobSheets: [],
    signatures: [],
    businessDocuments: [],
    applicationMappings: [],
    placeholders: [],
    transactionId: [],
    transactionDate: [],
    settlements: [],
  });

  constructor(
    protected settlementRequisitionService: SettlementRequisitionService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected applicationUserService: ApplicationUserService,
    protected dealerService: DealerService,
    protected paymentInvoiceService: PaymentInvoiceService,
    protected deliveryNoteService: DeliveryNoteService,
    protected jobSheetService: JobSheetService,
    protected businessDocumentService: BusinessDocumentService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected settlementService: SettlementService,
    protected router: Router,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ settlementRequisition }) => {
      if (settlementRequisition.id === undefined) {
        // settlementRequisition.timeOfRequisition = dayjs();
        settlementRequisition.paymentStatus = PaymentStatus.IN_PROCESS;

        const reqSerial = uuidv4();
        this.editForm.patchValue({
          serialNumber: reqSerial,
          requisitionNumber: reqSerial.substring(0,8),
          timeOfRequisition: this.tod.format(DATE_TIME_FORMAT)
        })
      }

      if (settlementRequisition.id !== undefined) {
        this.copyForm(settlementRequisition);
      }

      this.loadRelationshipsOptions();
    });

    this.updatePreferredSignatories();
    this.updatePreferredCurrency();

  }

  updatePreferredSignatories(): void {
    if (this.editForm.get(['id'])?.value === undefined ) {
      this.universallyUniqueMappingService.findMap("globallyPreferredSettlementRequisitionUpdateSignatoryName")
        .subscribe((mapped) => {
          this.dealerService.search(<SearchWithPagination>{page: 0,size: 0,sort: [],query: mapped.body?.mappedValue})
            .subscribe(({ body: vals }) => {
              if (vals) {
                this.editForm.get(['signatures'])?.setValue(vals)
              }
            });
        });
    }
  }

  updatePreferredCurrency(): void {
    this.universallyUniqueMappingService.findMap("globallyPreferredSettlementIso4217CurrencyCode")
      .subscribe(mapped => {
          this.settlementCurrencyService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: mapped.body?.mappedValue })
            .subscribe(({ body: currencies }) => {
              if (currencies) {
                this.editForm.get(['settlementCurrency'])?.setValue(currencies[0]);
              }
            });
        }
      );
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateBusinessDocument(update: IBusinessDocument[]): void {
    this.editForm.patchValue({
      businessDocuments: [...update],
    });
  }

  updateCurrencies(update: ISettlementCurrency): void {
    this.editForm.patchValue({
      settlementCurrency: update
    });
  }

  updateCurrentOwner(update: IApplicationUser): void {
    this.editForm.patchValue({
      currentOwner: update
    });
  }

  updateNativeOwner(update: IApplicationUser): void {
    this.editForm.patchValue({
      nativeOwner: update
    });
  }

  updateBiller(dealerUpdate: IDealer): void {
    this.editForm.patchValue({
      biller: dealerUpdate,
    });
  }

  updateNativeDepartment(dealerUpdate: IDealer): void {
    this.editForm.patchValue({
      nativeDepartment: dealerUpdate,
    });
  }

  updatePaymentInvoices(update: IPaymentInvoice[]): void {
    this.editForm.patchValue({
      paymentInvoices: update
    });
  }

  updateSignatories(dealerUpdate: IDealer[]): void {
    this.editForm.patchValue({
      signatures: [...dealerUpdate]
    });
  }

  updateSettlements(updates: ISettlement[]): void {
    this.editForm.patchValue({
      settlements: [...updates]
    });
  }

  updatePlaceholders(update: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...update]
    });
  }

  updateApplicationMapping(update: IUniversallyUniqueMapping[]): void {
    this.editForm.patchValue({
      applicationMapping: [...update]
    });
  }

  updateJobSheetItems(update: IJobSheet[]): void {
    this.editForm.patchValue({
      jobSheets: [...update]
    });
  }

  updateDeliveryNotesItems(update: IDeliveryNote[]): void {
    this.editForm.patchValue({
      deliveryNotes: [...update]
    });
  }

  previousState(): void {
    // window.history.back();
    this.router.navigate(['settlement-requisition']).finally();
  }

  save(): void {
    this.isSaving = true;
    const settlementRequisition = this.createFromForm();
    if (settlementRequisition.id !== undefined) {
      this.subscribeToSaveResponse(this.settlementRequisitionService.update(settlementRequisition));
    } else {
      this.subscribeToSaveResponse(this.settlementRequisitionService.create(settlementRequisition));
    }
  }

  trackSettlementById(index: number, item: ISettlement): number {
    return item.id!;
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackPaymentInvoiceById(index: number, item: IPaymentInvoice): number {
    return item.id!;
  }

  trackDeliveryNoteById(index: number, item: IDeliveryNote): number {
    return item.id!;
  }

  trackJobSheetById(index: number, item: IJobSheet): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISettlementRequisition>>): void {
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

  protected updateForm(settlementRequisition: ISettlementRequisition): void {
    this.editForm.patchValue({
      id: settlementRequisition.id,
      description: settlementRequisition.description,
      serialNumber: settlementRequisition.serialNumber,
      timeOfRequisition: settlementRequisition.timeOfRequisition ? settlementRequisition.timeOfRequisition.format(DATE_TIME_FORMAT) : null,
      requisitionNumber: settlementRequisition.requisitionNumber,
      paymentAmount: settlementRequisition.paymentAmount,
      paymentStatus: settlementRequisition.paymentStatus,
      settlementCurrency: settlementRequisition.settlementCurrency,
      currentOwner: settlementRequisition.currentOwner,
      nativeOwner: settlementRequisition.nativeOwner,
      nativeDepartment: settlementRequisition.nativeDepartment,
      biller: settlementRequisition.biller,
      paymentInvoices: settlementRequisition.paymentInvoices,
      deliveryNotes: settlementRequisition.deliveryNotes,
      jobSheets: settlementRequisition.jobSheets,
      signatures: settlementRequisition.signatures,
      businessDocuments: settlementRequisition.businessDocuments,
      applicationMappings: settlementRequisition.applicationMappings,
      placeholders: settlementRequisition.placeholders,
      transactionId: settlementRequisition.transactionId,
      transactionDate: settlementRequisition.transactionDate,
      settlements: settlementRequisition.settlements,
    });

    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      settlementRequisition.settlementCurrency
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      settlementRequisition.currentOwner,
      settlementRequisition.nativeOwner
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      settlementRequisition.nativeDepartment,
      settlementRequisition.biller,
      ...(settlementRequisition.signatures ?? [])
    );
    this.paymentInvoicesSharedCollection = this.paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing(
      this.paymentInvoicesSharedCollection,
      ...(settlementRequisition.paymentInvoices ?? [])
    );
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      ...(settlementRequisition.deliveryNotes ?? [])
    );
    this.jobSheetsSharedCollection = this.jobSheetService.addJobSheetToCollectionIfMissing(
      this.jobSheetsSharedCollection,
      ...(settlementRequisition.jobSheets ?? [])
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(settlementRequisition.businessDocuments ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(settlementRequisition.applicationMappings ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(settlementRequisition.placeholders ?? [])
    );
    this.settlementsSharedCollection = this.settlementService.addSettlementToCollectionIfMissing(
      this.settlementsSharedCollection,
      ...(settlementRequisition.settlements ?? [])
    );
  }

  protected copyForm(settlementRequisition: ISettlementRequisition): void {
    this.editForm.patchValue({
      // id: settlementRequisition.id,
      description: settlementRequisition.description,
      serialNumber: settlementRequisition.serialNumber,
      // timeOfRequisition: settlementRequisition.timeOfRequisition ? settlementRequisition.timeOfRequisition.format(DATE_TIME_FORMAT) : this.tod.format(DATE_TIME_FORMAT),
      timeOfRequisition: this.tod.format(DATE_TIME_FORMAT),
      requisitionNumber: settlementRequisition.requisitionNumber,
      paymentAmount: settlementRequisition.paymentAmount,
      paymentStatus: settlementRequisition.paymentStatus,
      settlementCurrency: settlementRequisition.settlementCurrency,
      currentOwner: settlementRequisition.currentOwner,
      nativeOwner: settlementRequisition.nativeOwner,
      nativeDepartment: settlementRequisition.nativeDepartment,
      biller: settlementRequisition.biller,
      paymentInvoices: settlementRequisition.paymentInvoices,
      deliveryNotes: settlementRequisition.deliveryNotes,
      jobSheets: settlementRequisition.jobSheets,
      signatures: settlementRequisition.signatures,
      businessDocuments: settlementRequisition.businessDocuments,
      applicationMappings: settlementRequisition.applicationMappings,
      placeholders: settlementRequisition.placeholders,
      transactionId: settlementRequisition.transactionId,
      transactionDate: settlementRequisition.transactionDate,
      settlements: settlementRequisition.settlements,
    });

    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      settlementRequisition.settlementCurrency
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      settlementRequisition.currentOwner,
      settlementRequisition.nativeOwner
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      settlementRequisition.nativeDepartment,
      settlementRequisition.biller,
      ...(settlementRequisition.signatures ?? [])
    );
    this.paymentInvoicesSharedCollection = this.paymentInvoiceService.addPaymentInvoiceToCollectionIfMissing(
      this.paymentInvoicesSharedCollection,
      ...(settlementRequisition.paymentInvoices ?? [])
    );
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      ...(settlementRequisition.deliveryNotes ?? [])
    );
    this.jobSheetsSharedCollection = this.jobSheetService.addJobSheetToCollectionIfMissing(
      this.jobSheetsSharedCollection,
      ...(settlementRequisition.jobSheets ?? [])
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(settlementRequisition.businessDocuments ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(settlementRequisition.applicationMappings ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(settlementRequisition.placeholders ?? [])
    );
    this.settlementsSharedCollection = this.settlementService.addSettlementToCollectionIfMissing(
      this.settlementsSharedCollection,
      ...(settlementRequisition.settlements ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.settlementCurrencyService
      .query()
      .pipe(map((res: HttpResponse<ISettlementCurrency[]>) => res.body ?? []))
      .pipe(
        map((settlementCurrencies: ISettlementCurrency[]) =>
          this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
            settlementCurrencies,
            this.editForm.get('settlementCurrency')!.value
          )
        )
      )
      .subscribe((settlementCurrencies: ISettlementCurrency[]) => (this.settlementCurrenciesSharedCollection = settlementCurrencies));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(
            applicationUsers,
            this.editForm.get('currentOwner')!.value,
            this.editForm.get('nativeOwner')!.value
          )
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) =>
          this.dealerService.addDealerToCollectionIfMissing(
            dealers,
            this.editForm.get('nativeDepartment')!.value,
            this.editForm.get('biller')!.value,
            ...(this.editForm.get('signatures')!.value ?? [])
          )
        )
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

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
            ...(this.editForm.get('applicationMappings')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.settlementService
      .query()
      .pipe(map((res: HttpResponse<ISettlement[]>) => res.body ?? []))
      .pipe(
        map((settlements: ISettlement[]) =>
          this.settlementService.addSettlementToCollectionIfMissing(settlements, ...(this.editForm.get('settlements')!.value ?? []))
        )
      )
      .subscribe((settlements: ISettlement[]) => (this.settlementsSharedCollection = settlements));
  }

  protected createFromForm(): ISettlementRequisition {
    return {
      ...new SettlementRequisition(),
      // id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      timeOfRequisition: this.editForm.get(['timeOfRequisition'])!.value
        ? dayjs(this.editForm.get(['timeOfRequisition'])!.value, DATE_TIME_FORMAT)
        : undefined,
      requisitionNumber: this.editForm.get(['requisitionNumber'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      paymentStatus: this.editForm.get(['paymentStatus'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      currentOwner: this.editForm.get(['currentOwner'])!.value,
      nativeOwner: this.editForm.get(['nativeOwner'])!.value,
      nativeDepartment: this.editForm.get(['nativeDepartment'])!.value,
      biller: this.editForm.get(['biller'])!.value,
      paymentInvoices: this.editForm.get(['paymentInvoices'])!.value,
      deliveryNotes: this.editForm.get(['deliveryNotes'])!.value,
      jobSheets: this.editForm.get(['jobSheets'])!.value,
      signatures: this.editForm.get(['signatures'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
      applicationMappings: this.editForm.get(['applicationMappings'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      transactionId: this.editForm.get(['transactionId'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      settlements: this.editForm.get(['settlements'])!.value,
    };
  }
}
