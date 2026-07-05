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

import { Component, Inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFRS16LeaseContract, IIFRS16LeaseContract } from '../ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from '../service/ifrs-16-lease-contract.service';
import { uuidv7 } from 'uuidv7';
import { IFiscalMonth } from '../../../erp-pages/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';       
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingIFRS16LeaseContractStatus,
  creatingIFRS16LeaseContractStatus,
  editingIFRS16LeaseContractStatus,
  ifrs16LeaseContractUpdateSelectedInstance
} from '../../../store/selectors/ifrs16-lease-model-workflows-status.selector';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';
import { ILeaseTemplate } from '../../lease-template/lease-template.model';
import { LeaseTemplateService } from '../../lease-template/service/lease-template.service';

@Component({
  selector: 'jhi-ifrs-16-lease-contract-update',
  templateUrl: './ifrs-16-lease-contract-update.component.html',
})
export class IFRS16LeaseContractUpdateComponent implements OnInit {
  isSaving = false;

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = {...new IFRS16LeaseContract()}

  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  dealersSharedCollection: IDealer[] = [];
  fiscalMonthsSharedCollection: IFiscalMonth[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];
  leaseTemplatesSharedCollection: ILeaseTemplate[] = [];

  editForm = this.fb.group({
    id: [],
    bookingId: [null, [Validators.required]],
    leaseTitle: [null, [Validators.required]],
    shortTitle: [null, []],
    description: [],
    inceptionDate: [null, [Validators.required]],
    commencementDate: [null, [Validators.required]],
    serialNumber: [],
    superintendentServiceOutlet: [null, Validators.required],
    mainDealer: [null, Validators.required],
    firstReportingPeriod: [null, Validators.required],
    lastReportingPeriod: [null, Validators.required],
    leaseContractDocument: [],
    leaseContractCalculations: [],
    leaseTemplate: [],
  });

  constructor(
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected serviceOutletService: ServiceOutletService,
    protected dealerService: DealerService,
    protected fiscalMonthService: FiscalMonthService,
    protected businessDocumentService: BusinessDocumentService,
    @Inject(LeaseTemplateService) protected leaseTemplateService: LeaseTemplateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
  ) {
    this.store.pipe(select(copyingIFRS16LeaseContractStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingIFRS16LeaseContractStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingIFRS16LeaseContractStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(ifrs16LeaseContractUpdateSelectedInstance)).subscribe(copied => this.selectedItem = copied);
  }

  ngOnInit(): void {

    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);

      this.editForm.patchValue({
        serialNumber: uuidv7(),
      })
    }

    if (this.weAreCopying) {
      this.updateForm(this.selectedItem);

      this.editForm.patchValue({
        serialNumber: uuidv7(),
      })
    }

    if (this.weAreCreating) {

      this.editForm.patchValue({
        serialNumber: uuidv7(),
      })

      this.loadRelationshipsOptions();
    }

    this.editForm.get(['leaseTemplate'])?.valueChanges.subscribe((template: ILeaseTemplate) => {
      if (template) {
        this.updateLeaseTemplate(template);
      }
    });
  }

  updateSuperintendentServiceOutlet(updated: IServiceOutlet): void {
    this.editForm.patchValue({ superintendentServiceOutlet: updated });
  }

  updateMainDealer(updated: IDealer): void {
    this.editForm.patchValue({ mainDealer: updated });
  }

  updateFirstReportingPeriod(updated: IFiscalMonth): void {
    this.editForm.patchValue({ firstReportingPeriod: updated });
  }

  updateLastReportingPeriod(updated: IFiscalMonth): void {
    this.editForm.patchValue({ lastReportingPeriod: updated });
  }

  updateLeaseContractAttachment(updated: IBusinessDocument[]): void {
    this.editForm.patchValue({ leaseContractDocument: [ ...updated] });
  }

  updateLeaseContractCalculations(updated: IBusinessDocument[]): void {
    this.editForm.patchValue({ leaseContractCalculations: [ ...updated] });     
  }

  updateLeaseTemplate(updated: ILeaseTemplate): void {
    if (updated?.serviceOutlet) {
      this.updateSuperintendentServiceOutlet(updated.serviceOutlet);
    }
    if (updated?.mainDealer) {
      this.updateMainDealer(updated.mainDealer);
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.iFRS16LeaseContractService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.iFRS16LeaseContractService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.iFRS16LeaseContractService.create(this.copyFromForm()));
  }

  trackServiceOutletById(index: number, item: IServiceOutlet): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackFiscalMonthById(index: number, item: IFiscalMonth): number {
    return item.id!;
  }

  trackLeaseTemplateById(index: number, item: ILeaseTemplate): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {   
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIFRS16LeaseContract>>): void {
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

  protected updateForm(iFRS16LeaseContract: IIFRS16LeaseContract): void {
    this.editForm.patchValue({
      id: iFRS16LeaseContract.id,
      bookingId: iFRS16LeaseContract.bookingId,
      leaseTitle: iFRS16LeaseContract.leaseTitle,
      shortTitle: iFRS16LeaseContract.shortTitle,
      description: iFRS16LeaseContract.description,
      inceptionDate: iFRS16LeaseContract.inceptionDate,
      commencementDate: iFRS16LeaseContract.commencementDate,
      serialNumber: iFRS16LeaseContract.serialNumber,
      superintendentServiceOutlet: iFRS16LeaseContract.superintendentServiceOutlet,
      mainDealer: iFRS16LeaseContract.mainDealer,
      firstReportingPeriod: iFRS16LeaseContract.firstReportingPeriod,
      lastReportingPeriod: iFRS16LeaseContract.lastReportingPeriod,
      leaseContractDocument: iFRS16LeaseContract.leaseContractDocument,
      leaseContractCalculations: iFRS16LeaseContract.leaseContractCalculations,
      leaseTemplate: iFRS16LeaseContract.leaseTemplate,
    });

    this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
      this.serviceOutletsSharedCollection,
      iFRS16LeaseContract.superintendentServiceOutlet
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      iFRS16LeaseContract.mainDealer
    );
    this.fiscalMonthsSharedCollection = this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(
      this.fiscalMonthsSharedCollection,
      iFRS16LeaseContract.firstReportingPeriod,
      iFRS16LeaseContract.lastReportingPeriod
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      iFRS16LeaseContract.leaseContractDocument,
      iFRS16LeaseContract.leaseContractCalculations
    );
    this.leaseTemplatesSharedCollection = this.leaseTemplateService.addLeaseTemplateToCollectionIfMissing(
      this.leaseTemplatesSharedCollection,
      iFRS16LeaseContract.leaseTemplate
    );
  }

  protected loadRelationshipsOptions(): void {
    this.serviceOutletService
      .query()
      .pipe(map((res: HttpResponse<IServiceOutlet[]>) => res.body ?? []))
      .pipe(
        map((serviceOutlets: IServiceOutlet[]) =>
          this.serviceOutletService.addServiceOutletToCollectionIfMissing(
            serviceOutlets,
            this.editForm.get('superintendentServiceOutlet')!.value
          )
        )
      )
      .subscribe((serviceOutlets: IServiceOutlet[]) => (this.serviceOutletsSharedCollection = serviceOutlets));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('mainDealer')!.value)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.fiscalMonthService
      .query()
      .pipe(map((res: HttpResponse<IFiscalMonth[]>) => res.body ?? []))
      .pipe(
        map((fiscalMonths: IFiscalMonth[]) =>
          this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(
            fiscalMonths,
            this.editForm.get('firstReportingPeriod')!.value,
            this.editForm.get('lastReportingPeriod')!.value
          )
        )
      )
      .subscribe((fiscalMonths: IFiscalMonth[]) => (this.fiscalMonthsSharedCollection = fiscalMonths));

    this.businessDocumentService
      .query()
      .pipe(map((res: HttpResponse<IBusinessDocument[]>) => res.body ?? []))    
      .pipe(
        map((businessDocuments: IBusinessDocument[]) =>
          this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
            businessDocuments,
            this.editForm.get('leaseContractDocument')!.value,
            this.editForm.get('leaseContractCalculations')!.value
          )
        )
      )
      .subscribe((businessDocuments: IBusinessDocument[]) => (this.businessDocumentsSharedCollection = businessDocuments));

    this.leaseTemplateService
      .query()
      .pipe(map((res: HttpResponse<ILeaseTemplate[]>) => res.body ?? []))
      .pipe(
        map((leaseTemplates: ILeaseTemplate[]) =>
          this.leaseTemplateService.addLeaseTemplateToCollectionIfMissing(
            leaseTemplates,
            this.editForm.get('leaseTemplate')!.value
          )
        )
      )
      .subscribe((leaseTemplates: ILeaseTemplate[]) => (this.leaseTemplatesSharedCollection = leaseTemplates));
  }

  protected createFromForm(): IIFRS16LeaseContract {
    return {
      ...new IFRS16LeaseContract(),
      id: this.editForm.get(['id'])!.value,
      bookingId: this.editForm.get(['bookingId'])!.value,
      leaseTitle: this.editForm.get(['leaseTitle'])!.value,
      shortTitle: this.editForm.get(['shortTitle'])!.value,
      description: this.editForm.get(['description'])!.value,
      inceptionDate: this.editForm.get(['inceptionDate'])!.value,
      commencementDate: this.editForm.get(['commencementDate'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      superintendentServiceOutlet: this.editForm.get(['superintendentServiceOutlet'])!.value,
      mainDealer: this.editForm.get(['mainDealer'])!.value,
      firstReportingPeriod: this.editForm.get(['firstReportingPeriod'])!.value, 
      lastReportingPeriod: this.editForm.get(['lastReportingPeriod'])!.value,   
      leaseContractDocument: this.editForm.get(['leaseContractDocument'])!.value,
      leaseContractCalculations: this.editForm.get(['leaseContractCalculations'])!.value,
      leaseTemplate: this.editForm.get(['leaseTemplate'])!.value,
    };
  }

  protected copyFromForm(): IIFRS16LeaseContract {
    return {
      ...new IFRS16LeaseContract(),
      bookingId: this.editForm.get(['bookingId'])!.value,
      leaseTitle: this.editForm.get(['leaseTitle'])!.value,
      shortTitle: this.editForm.get(['shortTitle'])!.value,
      description: this.editForm.get(['description'])!.value,
      inceptionDate: this.editForm.get(['inceptionDate'])!.value,
      commencementDate: this.editForm.get(['commencementDate'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      superintendentServiceOutlet: this.editForm.get(['superintendentServiceOutlet'])!.value,
      mainDealer: this.editForm.get(['mainDealer'])!.value,
      firstReportingPeriod: this.editForm.get(['firstReportingPeriod'])!.value, 
      lastReportingPeriod: this.editForm.get(['lastReportingPeriod'])!.value,   
      leaseContractDocument: this.editForm.get(['leaseContractDocument'])!.value,
      leaseContractCalculations: this.editForm.get(['leaseContractCalculations'])!.value,
      leaseTemplate: this.editForm.get(['leaseTemplate'])!.value,
    };
  }
}
