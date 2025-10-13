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

import { ITerminalsAndPOS, TerminalsAndPOS } from '../terminals-and-pos.model';
import { TerminalsAndPOSService } from '../service/terminals-and-pos.service';
import { ITerminalTypes } from 'app/entities/gdi/terminal-types/terminal-types.model';
import { TerminalTypesService } from 'app/entities/gdi/terminal-types/service/terminal-types.service';
import { ITerminalFunctions } from 'app/entities/gdi/terminal-functions/terminal-functions.model';
import { TerminalFunctionsService } from 'app/entities/gdi/terminal-functions/service/terminal-functions.service';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { CountySubCountyCodeService } from 'app/entities/gdi-data/county-sub-county-code/service/county-sub-county-code.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';

@Component({
  selector: 'jhi-terminals-and-pos-update',
  templateUrl: './terminals-and-pos-update.component.html',
})
export class TerminalsAndPOSUpdateComponent implements OnInit {
  isSaving = false;

  terminalTypesSharedCollection: ITerminalTypes[] = [];
  terminalFunctionsSharedCollection: ITerminalFunctions[] = [];
  countySubCountyCodesSharedCollection: ICountySubCountyCode[] = [];
  institutionCodesSharedCollection: IInstitutionCode[] = [];
  bankBranchCodesSharedCollection: IBankBranchCode[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    terminalId: [null, [Validators.required]],
    merchantId: [null, [Validators.required]],
    terminalName: [null, [Validators.required]],
    terminalLocation: [null, [Validators.required]],
    iso6709Latitute: [null, [Validators.required]],
    iso6709Longitude: [null, [Validators.required]],
    terminalOpeningDate: [null, [Validators.required]],
    terminalClosureDate: [],
    terminalType: [null, Validators.required],
    terminalFunctionality: [null, Validators.required],
    physicalLocation: [null, Validators.required],
    bankId: [null, Validators.required],
    branchId: [null, Validators.required],
  });

  constructor(
    protected terminalsAndPOSService: TerminalsAndPOSService,
    protected terminalTypesService: TerminalTypesService,
    protected terminalFunctionsService: TerminalFunctionsService,
    protected countySubCountyCodeService: CountySubCountyCodeService,
    protected institutionCodeService: InstitutionCodeService,
    protected bankBranchCodeService: BankBranchCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ terminalsAndPOS }) => {
      this.updateForm(terminalsAndPOS);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const terminalsAndPOS = this.createFromForm();
    if (terminalsAndPOS.id !== undefined) {
      this.subscribeToSaveResponse(this.terminalsAndPOSService.update(terminalsAndPOS));
    } else {
      this.subscribeToSaveResponse(this.terminalsAndPOSService.create(terminalsAndPOS));
    }
  }

  trackTerminalTypesById(index: number, item: ITerminalTypes): number {
    return item.id!;
  }

  trackTerminalFunctionsById(index: number, item: ITerminalFunctions): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITerminalsAndPOS>>): void {
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

  protected updateForm(terminalsAndPOS: ITerminalsAndPOS): void {
    this.editForm.patchValue({
      id: terminalsAndPOS.id,
      reportingDate: terminalsAndPOS.reportingDate,
      terminalId: terminalsAndPOS.terminalId,
      merchantId: terminalsAndPOS.merchantId,
      terminalName: terminalsAndPOS.terminalName,
      terminalLocation: terminalsAndPOS.terminalLocation,
      iso6709Latitute: terminalsAndPOS.iso6709Latitute,
      iso6709Longitude: terminalsAndPOS.iso6709Longitude,
      terminalOpeningDate: terminalsAndPOS.terminalOpeningDate,
      terminalClosureDate: terminalsAndPOS.terminalClosureDate,
      terminalType: terminalsAndPOS.terminalType,
      terminalFunctionality: terminalsAndPOS.terminalFunctionality,
      physicalLocation: terminalsAndPOS.physicalLocation,
      bankId: terminalsAndPOS.bankId,
      branchId: terminalsAndPOS.branchId,
    });

    this.terminalTypesSharedCollection = this.terminalTypesService.addTerminalTypesToCollectionIfMissing(
      this.terminalTypesSharedCollection,
      terminalsAndPOS.terminalType
    );
    this.terminalFunctionsSharedCollection = this.terminalFunctionsService.addTerminalFunctionsToCollectionIfMissing(
      this.terminalFunctionsSharedCollection,
      terminalsAndPOS.terminalFunctionality
    );
    this.countySubCountyCodesSharedCollection = this.countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing(
      this.countySubCountyCodesSharedCollection,
      terminalsAndPOS.physicalLocation
    );
    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      terminalsAndPOS.bankId
    );
    this.bankBranchCodesSharedCollection = this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(
      this.bankBranchCodesSharedCollection,
      terminalsAndPOS.branchId
    );
  }

  protected loadRelationshipsOptions(): void {
    this.terminalTypesService
      .query()
      .pipe(map((res: HttpResponse<ITerminalTypes[]>) => res.body ?? []))
      .pipe(
        map((terminalTypes: ITerminalTypes[]) =>
          this.terminalTypesService.addTerminalTypesToCollectionIfMissing(terminalTypes, this.editForm.get('terminalType')!.value)
        )
      )
      .subscribe((terminalTypes: ITerminalTypes[]) => (this.terminalTypesSharedCollection = terminalTypes));

    this.terminalFunctionsService
      .query()
      .pipe(map((res: HttpResponse<ITerminalFunctions[]>) => res.body ?? []))
      .pipe(
        map((terminalFunctions: ITerminalFunctions[]) =>
          this.terminalFunctionsService.addTerminalFunctionsToCollectionIfMissing(
            terminalFunctions,
            this.editForm.get('terminalFunctionality')!.value
          )
        )
      )
      .subscribe((terminalFunctions: ITerminalFunctions[]) => (this.terminalFunctionsSharedCollection = terminalFunctions));

    this.countySubCountyCodeService
      .query()
      .pipe(map((res: HttpResponse<ICountySubCountyCode[]>) => res.body ?? []))
      .pipe(
        map((countySubCountyCodes: ICountySubCountyCode[]) =>
          this.countySubCountyCodeService.addCountySubCountyCodeToCollectionIfMissing(
            countySubCountyCodes,
            this.editForm.get('physicalLocation')!.value
          )
        )
      )
      .subscribe((countySubCountyCodes: ICountySubCountyCode[]) => (this.countySubCountyCodesSharedCollection = countySubCountyCodes));

    this.institutionCodeService
      .query()
      .pipe(map((res: HttpResponse<IInstitutionCode[]>) => res.body ?? []))
      .pipe(
        map((institutionCodes: IInstitutionCode[]) =>
          this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(institutionCodes, this.editForm.get('bankId')!.value)
        )
      )
      .subscribe((institutionCodes: IInstitutionCode[]) => (this.institutionCodesSharedCollection = institutionCodes));

    this.bankBranchCodeService
      .query()
      .pipe(map((res: HttpResponse<IBankBranchCode[]>) => res.body ?? []))
      .pipe(
        map((bankBranchCodes: IBankBranchCode[]) =>
          this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(bankBranchCodes, this.editForm.get('branchId')!.value)
        )
      )
      .subscribe((bankBranchCodes: IBankBranchCode[]) => (this.bankBranchCodesSharedCollection = bankBranchCodes));
  }

  protected createFromForm(): ITerminalsAndPOS {
    return {
      ...new TerminalsAndPOS(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      terminalId: this.editForm.get(['terminalId'])!.value,
      merchantId: this.editForm.get(['merchantId'])!.value,
      terminalName: this.editForm.get(['terminalName'])!.value,
      terminalLocation: this.editForm.get(['terminalLocation'])!.value,
      iso6709Latitute: this.editForm.get(['iso6709Latitute'])!.value,
      iso6709Longitude: this.editForm.get(['iso6709Longitude'])!.value,
      terminalOpeningDate: this.editForm.get(['terminalOpeningDate'])!.value,
      terminalClosureDate: this.editForm.get(['terminalClosureDate'])!.value,
      terminalType: this.editForm.get(['terminalType'])!.value,
      terminalFunctionality: this.editForm.get(['terminalFunctionality'])!.value,
      physicalLocation: this.editForm.get(['physicalLocation'])!.value,
      bankId: this.editForm.get(['bankId'])!.value,
      branchId: this.editForm.get(['branchId'])!.value,
    };
  }
}
