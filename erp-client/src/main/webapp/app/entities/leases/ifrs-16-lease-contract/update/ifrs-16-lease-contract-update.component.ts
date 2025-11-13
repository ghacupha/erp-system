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
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIFRS16LeaseContract, IFRS16LeaseContract } from '../ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from '../service/ifrs-16-lease-contract.service';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ServiceOutletService } from 'app/entities/system/service-outlet/service/service-outlet.service';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { DealerService } from 'app/entities/people/dealer/service/dealer.service';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/system/fiscal-month/service/fiscal-month.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';

@Component({
  selector: 'jhi-ifrs-16-lease-contract-update',
  templateUrl: './ifrs-16-lease-contract-update.component.html',
})
export class IFRS16LeaseContractUpdateComponent implements OnInit {
  isSaving = false;

  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  dealersSharedCollection: IDealer[] = [];
  fiscalMonthsSharedCollection: IFiscalMonth[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];

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
  });

  constructor(
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected serviceOutletService: ServiceOutletService,
    protected dealerService: DealerService,
    protected fiscalMonthService: FiscalMonthService,
    protected businessDocumentService: BusinessDocumentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ iFRS16LeaseContract }) => {
      this.updateForm(iFRS16LeaseContract);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const iFRS16LeaseContract = this.createFromForm();
    if (iFRS16LeaseContract.id !== undefined) {
      this.subscribeToSaveResponse(this.iFRS16LeaseContractService.update(iFRS16LeaseContract));
    } else {
      this.subscribeToSaveResponse(this.iFRS16LeaseContractService.create(iFRS16LeaseContract));
    }
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
    };
  }
}
