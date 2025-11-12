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

import { IServiceOutlet, ServiceOutlet } from '../service-outlet.model';
import { ServiceOutletService } from '../service/service-outlet.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IOutletType } from 'app/entities/system/outlet-type/outlet-type.model';
import { OutletTypeService } from 'app/entities/system/outlet-type/service/outlet-type.service';
import { IOutletStatus } from 'app/entities/system/outlet-status/outlet-status.model';
import { OutletStatusService } from 'app/entities/system/outlet-status/service/outlet-status.service';
import { ICountyCode } from 'app/entities/system/county-code/county-code.model';
import { CountyCodeService } from 'app/entities/system/county-code/service/county-code.service';

@Component({
  selector: 'jhi-service-outlet-update',
  templateUrl: './service-outlet-update.component.html',
})
export class ServiceOutletUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];
  bankBranchCodesSharedCollection: IBankBranchCode[] = [];
  outletTypesSharedCollection: IOutletType[] = [];
  outletStatusesSharedCollection: IOutletStatus[] = [];
  countyCodesSharedCollection: ICountyCode[] = [];

  editForm = this.fb.group({
    id: [],
    outletCode: [null, [Validators.required]],
    outletName: [null, [Validators.required]],
    town: [],
    parliamentaryConstituency: [],
    gpsCoordinates: [],
    outletOpeningDate: [],
    regulatorApprovalDate: [],
    outletClosureDate: [],
    dateLastModified: [],
    licenseFeePayable: [],
    placeholders: [],
    bankCode: [],
    outletType: [],
    outletStatus: [],
    countyName: [],
    subCountyName: [],
  });

  constructor(
    protected serviceOutletService: ServiceOutletService,
    protected placeholderService: PlaceholderService,
    protected bankBranchCodeService: BankBranchCodeService,
    protected outletTypeService: OutletTypeService,
    protected outletStatusService: OutletStatusService,
    protected countyCodeService: CountyCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceOutlet }) => {
      this.updateForm(serviceOutlet);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceOutlet = this.createFromForm();
    if (serviceOutlet.id !== undefined) {
      this.subscribeToSaveResponse(this.serviceOutletService.update(serviceOutlet));
    } else {
      this.subscribeToSaveResponse(this.serviceOutletService.create(serviceOutlet));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackBankBranchCodeById(index: number, item: IBankBranchCode): number {
    return item.id!;
  }

  trackOutletTypeById(index: number, item: IOutletType): number {
    return item.id!;
  }

  trackOutletStatusById(index: number, item: IOutletStatus): number {
    return item.id!;
  }

  trackCountyCodeById(index: number, item: ICountyCode): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceOutlet>>): void {
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

  protected updateForm(serviceOutlet: IServiceOutlet): void {
    this.editForm.patchValue({
      id: serviceOutlet.id,
      outletCode: serviceOutlet.outletCode,
      outletName: serviceOutlet.outletName,
      town: serviceOutlet.town,
      parliamentaryConstituency: serviceOutlet.parliamentaryConstituency,
      gpsCoordinates: serviceOutlet.gpsCoordinates,
      outletOpeningDate: serviceOutlet.outletOpeningDate,
      regulatorApprovalDate: serviceOutlet.regulatorApprovalDate,
      outletClosureDate: serviceOutlet.outletClosureDate,
      dateLastModified: serviceOutlet.dateLastModified,
      licenseFeePayable: serviceOutlet.licenseFeePayable,
      placeholders: serviceOutlet.placeholders,
      bankCode: serviceOutlet.bankCode,
      outletType: serviceOutlet.outletType,
      outletStatus: serviceOutlet.outletStatus,
      countyName: serviceOutlet.countyName,
      subCountyName: serviceOutlet.subCountyName,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(serviceOutlet.placeholders ?? [])
    );
    this.bankBranchCodesSharedCollection = this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(
      this.bankBranchCodesSharedCollection,
      serviceOutlet.bankCode
    );
    this.outletTypesSharedCollection = this.outletTypeService.addOutletTypeToCollectionIfMissing(
      this.outletTypesSharedCollection,
      serviceOutlet.outletType
    );
    this.outletStatusesSharedCollection = this.outletStatusService.addOutletStatusToCollectionIfMissing(
      this.outletStatusesSharedCollection,
      serviceOutlet.outletStatus
    );
    this.countyCodesSharedCollection = this.countyCodeService.addCountyCodeToCollectionIfMissing(
      this.countyCodesSharedCollection,
      serviceOutlet.countyName,
      serviceOutlet.subCountyName
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

    this.bankBranchCodeService
      .query()
      .pipe(map((res: HttpResponse<IBankBranchCode[]>) => res.body ?? []))
      .pipe(
        map((bankBranchCodes: IBankBranchCode[]) =>
          this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(bankBranchCodes, this.editForm.get('bankCode')!.value)
        )
      )
      .subscribe((bankBranchCodes: IBankBranchCode[]) => (this.bankBranchCodesSharedCollection = bankBranchCodes));

    this.outletTypeService
      .query()
      .pipe(map((res: HttpResponse<IOutletType[]>) => res.body ?? []))
      .pipe(
        map((outletTypes: IOutletType[]) =>
          this.outletTypeService.addOutletTypeToCollectionIfMissing(outletTypes, this.editForm.get('outletType')!.value)
        )
      )
      .subscribe((outletTypes: IOutletType[]) => (this.outletTypesSharedCollection = outletTypes));

    this.outletStatusService
      .query()
      .pipe(map((res: HttpResponse<IOutletStatus[]>) => res.body ?? []))
      .pipe(
        map((outletStatuses: IOutletStatus[]) =>
          this.outletStatusService.addOutletStatusToCollectionIfMissing(outletStatuses, this.editForm.get('outletStatus')!.value)
        )
      )
      .subscribe((outletStatuses: IOutletStatus[]) => (this.outletStatusesSharedCollection = outletStatuses));

    this.countyCodeService
      .query()
      .pipe(map((res: HttpResponse<ICountyCode[]>) => res.body ?? []))
      .pipe(
        map((countyCodes: ICountyCode[]) =>
          this.countyCodeService.addCountyCodeToCollectionIfMissing(
            countyCodes,
            this.editForm.get('countyName')!.value,
            this.editForm.get('subCountyName')!.value
          )
        )
      )
      .subscribe((countyCodes: ICountyCode[]) => (this.countyCodesSharedCollection = countyCodes));
  }

  protected createFromForm(): IServiceOutlet {
    return {
      ...new ServiceOutlet(),
      id: this.editForm.get(['id'])!.value,
      outletCode: this.editForm.get(['outletCode'])!.value,
      outletName: this.editForm.get(['outletName'])!.value,
      town: this.editForm.get(['town'])!.value,
      parliamentaryConstituency: this.editForm.get(['parliamentaryConstituency'])!.value,
      gpsCoordinates: this.editForm.get(['gpsCoordinates'])!.value,
      outletOpeningDate: this.editForm.get(['outletOpeningDate'])!.value,
      regulatorApprovalDate: this.editForm.get(['regulatorApprovalDate'])!.value,
      outletClosureDate: this.editForm.get(['outletClosureDate'])!.value,
      dateLastModified: this.editForm.get(['dateLastModified'])!.value,
      licenseFeePayable: this.editForm.get(['licenseFeePayable'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      outletType: this.editForm.get(['outletType'])!.value,
      outletStatus: this.editForm.get(['outletStatus'])!.value,
      countyName: this.editForm.get(['countyName'])!.value,
      subCountyName: this.editForm.get(['subCountyName'])!.value,
    };
  }
}
