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

import { IRelatedPartyRelationship, RelatedPartyRelationship } from '../related-party-relationship.model';
import { RelatedPartyRelationshipService } from '../service/related-party-relationship.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IPartyRelationType } from 'app/entities/gdi/party-relation-type/party-relation-type.model';
import { PartyRelationTypeService } from 'app/entities/gdi/party-relation-type/service/party-relation-type.service';

@Component({
  selector: 'jhi-related-party-relationship-update',
  templateUrl: './related-party-relationship-update.component.html',
})
export class RelatedPartyRelationshipUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  bankBranchCodesSharedCollection: IBankBranchCode[] = [];
  partyRelationTypesSharedCollection: IPartyRelationType[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    customerId: [null, [Validators.required]],
    relatedPartyId: [null, [Validators.required]],
    bankCode: [null, Validators.required],
    branchId: [null, Validators.required],
    relationshipType: [null, Validators.required],
  });

  constructor(
    protected relatedPartyRelationshipService: RelatedPartyRelationshipService,
    protected institutionCodeService: InstitutionCodeService,
    protected bankBranchCodeService: BankBranchCodeService,
    protected partyRelationTypeService: PartyRelationTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relatedPartyRelationship }) => {
      this.updateForm(relatedPartyRelationship);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const relatedPartyRelationship = this.createFromForm();
    if (relatedPartyRelationship.id !== undefined) {
      this.subscribeToSaveResponse(this.relatedPartyRelationshipService.update(relatedPartyRelationship));
    } else {
      this.subscribeToSaveResponse(this.relatedPartyRelationshipService.create(relatedPartyRelationship));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackBankBranchCodeById(index: number, item: IBankBranchCode): number {
    return item.id!;
  }

  trackPartyRelationTypeById(index: number, item: IPartyRelationType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRelatedPartyRelationship>>): void {
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

  protected updateForm(relatedPartyRelationship: IRelatedPartyRelationship): void {
    this.editForm.patchValue({
      id: relatedPartyRelationship.id,
      reportingDate: relatedPartyRelationship.reportingDate,
      customerId: relatedPartyRelationship.customerId,
      relatedPartyId: relatedPartyRelationship.relatedPartyId,
      bankCode: relatedPartyRelationship.bankCode,
      branchId: relatedPartyRelationship.branchId,
      relationshipType: relatedPartyRelationship.relationshipType,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      relatedPartyRelationship.bankCode
    );
    this.bankBranchCodesSharedCollection = this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(
      this.bankBranchCodesSharedCollection,
      relatedPartyRelationship.branchId
    );
    this.partyRelationTypesSharedCollection = this.partyRelationTypeService.addPartyRelationTypeToCollectionIfMissing(
      this.partyRelationTypesSharedCollection,
      relatedPartyRelationship.relationshipType
    );
  }

  protected loadRelationshipsOptions(): void {
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
          this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(bankBranchCodes, this.editForm.get('branchId')!.value)
        )
      )
      .subscribe((bankBranchCodes: IBankBranchCode[]) => (this.bankBranchCodesSharedCollection = bankBranchCodes));

    this.partyRelationTypeService
      .query()
      .pipe(map((res: HttpResponse<IPartyRelationType[]>) => res.body ?? []))
      .pipe(
        map((partyRelationTypes: IPartyRelationType[]) =>
          this.partyRelationTypeService.addPartyRelationTypeToCollectionIfMissing(
            partyRelationTypes,
            this.editForm.get('relationshipType')!.value
          )
        )
      )
      .subscribe((partyRelationTypes: IPartyRelationType[]) => (this.partyRelationTypesSharedCollection = partyRelationTypes));
  }

  protected createFromForm(): IRelatedPartyRelationship {
    return {
      ...new RelatedPartyRelationship(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      customerId: this.editForm.get(['customerId'])!.value,
      relatedPartyId: this.editForm.get(['relatedPartyId'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      branchId: this.editForm.get(['branchId'])!.value,
      relationshipType: this.editForm.get(['relationshipType'])!.value,
    };
  }
}
