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

import { IWorkProjectRegister, WorkProjectRegister } from '../work-project-register.model';
import { WorkProjectRegisterService } from '../service/work-project-register.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { ISettlementCurrency } from '../../../erp-settlements/settlement-currency/settlement-currency.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { SettlementCurrencyService } from '../../../erp-settlements/settlement-currency/service/settlement-currency.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';

@Component({
  selector: 'jhi-work-project-register-update',
  templateUrl: './work-project-register-update.component.html',
})
export class WorkProjectRegisterUpdateComponent implements OnInit {
  isSaving = false;

  dealersSharedCollection: IDealer[] = [];
  settlementCurrenciesSharedCollection: ISettlementCurrency[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];

  editForm = this.fb.group({
    id: [],
    catalogueNumber: [null, [Validators.required]],
    projectTitle: [null, [Validators.required]],
    description: [],
    details: [],
    detailsContentType: [],
    totalProjectCost: [],
    additionalNotes: [],
    additionalNotesContentType: [],
    dealers: [null, Validators.required],
    settlementCurrency: [],
    placeholders: [],
    businessDocuments: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected workProjectRegisterService: WorkProjectRegisterService,
    protected dealerService: DealerService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected placeholderService: PlaceholderService,
    protected businessDocumentService: BusinessDocumentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workProjectRegister }) => {
      this.updateForm(workProjectRegister);

      if (workProjectRegister.id === undefined) {
        // Update catalogue number
        this.workProjectRegisterService.getNextCatalogueNumber().subscribe(nextValue => {
          if (nextValue.body) {
            this.editForm.patchValue({
              catalogueNumber: nextValue.body,
            })
          }
        });
      }

      this.loadRelationshipsOptions();
    });
  }

  updateSettlementCurrency(update: ISettlementCurrency): void {
    this.editForm.patchValue({
      settlementCurrency: update
    });
  }

  updateDealers(update: IDealer[]): void {
    this.editForm.patchValue({
      dealers: [ ...update]
    });
  }

  updatePlaceholders(update: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [ ...update]
    });
  }

  updateBusinessDocument(update: IBusinessDocument[]): void {
    this.editForm.patchValue({
      businessDocuments: [ ...update]
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
    const workProjectRegister = this.createFromForm();
    if (workProjectRegister.id !== undefined) {
      this.subscribeToSaveResponse(this.workProjectRegisterService.update(workProjectRegister));
    } else {
      this.subscribeToSaveResponse(this.workProjectRegisterService.create(workProjectRegister));
    }
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkProjectRegister>>): void {
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

  protected updateForm(workProjectRegister: IWorkProjectRegister): void {
    this.editForm.patchValue({
      id: workProjectRegister.id,
      catalogueNumber: workProjectRegister.catalogueNumber,
      projectTitle: workProjectRegister.projectTitle,
      description: workProjectRegister.description,
      details: workProjectRegister.details,
      detailsContentType: workProjectRegister.detailsContentType,
      totalProjectCost: workProjectRegister.totalProjectCost,
      additionalNotes: workProjectRegister.additionalNotes,
      additionalNotesContentType: workProjectRegister.additionalNotesContentType,
      dealers: workProjectRegister.dealers,
      settlementCurrency: workProjectRegister.settlementCurrency,
      placeholders: workProjectRegister.placeholders,
      businessDocuments: workProjectRegister.businessDocuments,
    });

    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      ...(workProjectRegister.dealers ?? [])
    );
    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      workProjectRegister.settlementCurrency
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(workProjectRegister.placeholders ?? [])
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(workProjectRegister.businessDocuments ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) =>
          this.dealerService.addDealerToCollectionIfMissing(dealers, ...(this.editForm.get('dealers')!.value ?? []))
        )
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

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

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

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

  protected createFromForm(): IWorkProjectRegister {
    return {
      ...new WorkProjectRegister(),
      id: this.editForm.get(['id'])!.value,
      catalogueNumber: this.editForm.get(['catalogueNumber'])!.value,
      projectTitle: this.editForm.get(['projectTitle'])!.value,
      description: this.editForm.get(['description'])!.value,
      detailsContentType: this.editForm.get(['detailsContentType'])!.value,
      details: this.editForm.get(['details'])!.value,
      totalProjectCost: this.editForm.get(['totalProjectCost'])!.value,
      additionalNotesContentType: this.editForm.get(['additionalNotesContentType'])!.value,
      additionalNotes: this.editForm.get(['additionalNotes'])!.value,
      dealers: this.editForm.get(['dealers'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
    };
  }
}
