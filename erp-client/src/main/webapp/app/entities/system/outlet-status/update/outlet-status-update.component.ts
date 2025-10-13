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

import { IOutletStatus, OutletStatus } from '../outlet-status.model';
import { OutletStatusService } from '../service/outlet-status.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { BranchStatusType } from 'app/entities/enumerations/branch-status-type.model';

@Component({
  selector: 'jhi-outlet-status-update',
  templateUrl: './outlet-status-update.component.html',
})
export class OutletStatusUpdateComponent implements OnInit {
  isSaving = false;
  branchStatusTypeValues = Object.keys(BranchStatusType);

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    branchStatusTypeCode: [null, [Validators.required]],
    branchStatusType: [null, [Validators.required]],
    branchStatusTypeDescription: [],
    placeholders: [],
  });

  constructor(
    protected outletStatusService: OutletStatusService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ outletStatus }) => {
      this.updateForm(outletStatus);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const outletStatus = this.createFromForm();
    if (outletStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.outletStatusService.update(outletStatus));
    } else {
      this.subscribeToSaveResponse(this.outletStatusService.create(outletStatus));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOutletStatus>>): void {
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

  protected updateForm(outletStatus: IOutletStatus): void {
    this.editForm.patchValue({
      id: outletStatus.id,
      branchStatusTypeCode: outletStatus.branchStatusTypeCode,
      branchStatusType: outletStatus.branchStatusType,
      branchStatusTypeDescription: outletStatus.branchStatusTypeDescription,
      placeholders: outletStatus.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(outletStatus.placeholders ?? [])
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
  }

  protected createFromForm(): IOutletStatus {
    return {
      ...new OutletStatus(),
      id: this.editForm.get(['id'])!.value,
      branchStatusTypeCode: this.editForm.get(['branchStatusTypeCode'])!.value,
      branchStatusType: this.editForm.get(['branchStatusType'])!.value,
      branchStatusTypeDescription: this.editForm.get(['branchStatusTypeDescription'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
