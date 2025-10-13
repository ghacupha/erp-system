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

import { IParticularsOfOutlet, ParticularsOfOutlet } from '../particulars-of-outlet.model';
import { ParticularsOfOutletService } from '../service/particulars-of-outlet.service';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { CountySubCountyCodeService } from 'app/entities/gdi-data/county-sub-county-code/service/county-sub-county-code.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IOutletType } from 'app/entities/system/outlet-type/outlet-type.model';
import { OutletTypeService } from 'app/entities/system/outlet-type/service/outlet-type.service';
import { IOutletStatus } from 'app/entities/system/outlet-status/outlet-status.model';
import { OutletStatusService } from 'app/entities/system/outlet-status/service/outlet-status.service';

@Component({
  selector: 'jhi-particulars-of-outlet-update',
  templateUrl: './particulars-of-outlet-update.component.html',
})
export class ParticularsOfOutletUpdateComponent implements OnInit {
  isSaving = false;

  countySubCountyCodesSharedCollection: ICountySubCountyCode[] = [];
  institutionCodesSharedCollection: IInstitutionCode[] = [];
  bankBranchCodesSharedCollection: IBankBranchCode[] = [];
  outletTypesSharedCollection: IOutletType[] = [];
  outletStatusesSharedCollection: IOutletStatus[] = [];

  editForm = this.fb.group({
    id: [],
    businessReportingDate: [null, [Validators.required]],
    outletName: [null, [Validators.required]],
    town: [null, [Validators.required]],
    iso6709Latitute: [null, [Validators.required]],
    iso6709Longitude: [null, [Validators.required]],
    cbkApprovalDate: [null, [Validators.required]],
    outletOpeningDate: [null, [Validators.required]],
    outletClosureDate: [],
    licenseFeePayable: [null, [Validators.required]],
    subCountyCode: [null, Validators.required],
    bankCode: [null, Validators.required],
    outletId: [null, Validators.required],
    typeOfOutlet: [null, Validators.required],
    outletStatus: [null, Validators.required],
  });

  constructor(
    protected particularsOfOutletService: ParticularsOfOutletService,
    protected countySubCountyCodeService: CountySubCountyCodeService,
    protected institutionCodeService: InstitutionCodeService,
    protected bankBranchCodeService: BankBranchCodeService,
    protected outletTypeService: OutletTypeService,
    protected outletStatusService: OutletStatusService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ particularsOfOutlet }) => {
      this.updateForm(particularsOfOutlet);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const particularsOfOutlet = this.createFromForm();
    if (particularsOfOutlet.id !== undefined) {
      this.subscribeToSaveResponse(this.particularsOfOutletService.update(particularsOfOutlet));
    } else {
      this.subscribeToSaveResponse(this.particularsOfOutletService.create(particularsOfOutlet));
    }
  }

  trackCountySubCountyCodeById(index: number, item: ICountySubCountyCode): number {
    return item.id!;
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParticularsOfOutlet>>): void {
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

  protected updateForm(particularsOfOutlet: IParticularsOfOutlet): void {
    this.editForm.patchValue({
      id: particularsOfOutlet.id,
      businessReportingDate: particularsOfOutlet.businessReportingDate,
      outletName: particularsOfOutlet.outletName,
      town: particularsOfOutlet.town,
      iso6709Latitute: particularsOfOutlet.iso6709Latitute,
      iso6709Longitude: particularsOfOutlet.iso6709Longitude,
      cbkApprovalDate: particularsOfOutlet.cbkApprovalDate,
      outletOpeningDate: particularsOfOutlet.outletOpeningDate,
      outletClosureDate: particularsOfOutlet.outletClosureDate,
      licenseFeePayable: particularsOfOutlet.licenseFeePayable,
      subCountyCode: particularsOfOutlet.subCountyCode,
      bankCode: particularsOfOutlet.bankCode,
      outletId: particularsOfOutlet.outletId,
      typeOfOutlet: particularsOfOutlet.typeOfOutlet,
      outletStatus: particularsOfOutlet.outletStatus,
    });

    this.countySubCountyCodesSharedCollection = this.countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing(
      this.countySubCountyCodesSharedCollection,
      particularsOfOutlet.subCountyCode
    );
    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      particularsOfOutlet.bankCode
    );
    this.bankBranchCodesSharedCollection = this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(
      this.bankBranchCodesSharedCollection,
      particularsOfOutlet.outletId
    );
    this.outletTypesSharedCollection = this.outletTypeService.addOutletTypeToCollectionIfMissing(
      this.outletTypesSharedCollection,
      particularsOfOutlet.typeOfOutlet
    );
    this.outletStatusesSharedCollection = this.outletStatusService.addOutletStatusToCollectionIfMissing(
      this.outletStatusesSharedCollection,
      particularsOfOutlet.outletStatus
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countySubCountyCodeService
      .query()
      .pipe(map((res: HttpResponse<ICountySubCountyCode[]>) => res.body ?? []))
      .pipe(
        map((countySubCountyCodes: ICountySubCountyCode[]) =>
          this.countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing(
            countySubCountyCodes,
            this.editForm.get('subCountyCode')!.value
          )
        )
      )
      .subscribe((countySubCountyCodes: ICountySubCountyCode[]) => (this.countySubCountyCodesSharedCollection = countySubCountyCodes));

    this.institutionCodeService
      .query()
      .pipe(map((res: HttpResponse<IInstitutionCode[]>) => res.body ?? []))
      .pipe(
        map((institutionCodes: IInstitutionCode[]) =>
          this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(institutionCodes, this.editForm.get('bankCode')!.value)
        )
      )
      .subscribe((institutionCodes: IInstitutionCode[]) => (this.institutionCodesSharedCollection = institutionCodes));

    this.bankBranchCodeService
      .query()
      .pipe(map((res: HttpResponse<IBankBranchCode[]>) => res.body ?? []))
      .pipe(
        map((bankBranchCodes: IBankBranchCode[]) =>
          this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(bankBranchCodes, this.editForm.get('outletId')!.value)
        )
      )
      .subscribe((bankBranchCodes: IBankBranchCode[]) => (this.bankBranchCodesSharedCollection = bankBranchCodes));

    this.outletTypeService
      .query()
      .pipe(map((res: HttpResponse<IOutletType[]>) => res.body ?? []))
      .pipe(
        map((outletTypes: IOutletType[]) =>
          this.outletTypeService.addOutletTypeToCollectionIfMissing(outletTypes, this.editForm.get('typeOfOutlet')!.value)
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
  }

  protected createFromForm(): IParticularsOfOutlet {
    return {
      ...new ParticularsOfOutlet(),
      id: this.editForm.get(['id'])!.value,
      businessReportingDate: this.editForm.get(['businessReportingDate'])!.value,
      outletName: this.editForm.get(['outletName'])!.value,
      town: this.editForm.get(['town'])!.value,
      iso6709Latitute: this.editForm.get(['iso6709Latitute'])!.value,
      iso6709Longitude: this.editForm.get(['iso6709Longitude'])!.value,
      cbkApprovalDate: this.editForm.get(['cbkApprovalDate'])!.value,
      outletOpeningDate: this.editForm.get(['outletOpeningDate'])!.value,
      outletClosureDate: this.editForm.get(['outletClosureDate'])!.value,
      licenseFeePayable: this.editForm.get(['licenseFeePayable'])!.value,
      subCountyCode: this.editForm.get(['subCountyCode'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      outletId: this.editForm.get(['outletId'])!.value,
      typeOfOutlet: this.editForm.get(['typeOfOutlet'])!.value,
      outletStatus: this.editForm.get(['outletStatus'])!.value,
    };
  }
}
