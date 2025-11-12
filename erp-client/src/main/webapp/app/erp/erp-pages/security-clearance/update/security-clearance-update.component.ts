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

import { ISecurityClearance, SecurityClearance } from '../security-clearance.model';
import { SecurityClearanceService } from '../service/security-clearance.service';
import { IPlaceholder } from '../../placeholder/placeholder.model';
import { PlaceholderService } from '../../placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-security-clearance-update',
  templateUrl: './security-clearance-update.component.html',
})
export class SecurityClearanceUpdateComponent implements OnInit {
  isSaving = false;

  securityClearancesSharedCollection: ISecurityClearance[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    clearanceLevel: [null, [Validators.required]],
    grantedClearances: [],
    placeholders: [],
  });

  constructor(
    protected securityClearanceService: SecurityClearanceService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityClearance }) => {
      this.updateForm(securityClearance);

      this.loadRelationshipsOptions();
    });
  }

  updatePlaceholders(update: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...update]
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const securityClearance = this.createFromForm();
    if (securityClearance.id !== undefined) {
      this.subscribeToSaveResponse(this.securityClearanceService.update(securityClearance));
    } else {
      this.subscribeToSaveResponse(this.securityClearanceService.create(securityClearance));
    }
  }

  trackSecurityClearanceById(index: number, item: ISecurityClearance): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  getSelectedSecurityClearance(option: ISecurityClearance, selectedVals?: ISecurityClearance[]): ISecurityClearance {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISecurityClearance>>): void {
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

  protected updateForm(securityClearance: ISecurityClearance): void {
    this.editForm.patchValue({
      id: securityClearance.id,
      clearanceLevel: securityClearance.clearanceLevel,
      grantedClearances: securityClearance.grantedClearances,
      placeholders: securityClearance.placeholders,
    });

    this.securityClearancesSharedCollection = this.securityClearanceService.addSecurityClearanceToCollectionIfMissing(
      this.securityClearancesSharedCollection,
      ...(securityClearance.grantedClearances ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(securityClearance.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.securityClearanceService
      .query()
      .pipe(map((res: HttpResponse<ISecurityClearance[]>) => res.body ?? []))
      .pipe(
        map((securityClearances: ISecurityClearance[]) =>
          this.securityClearanceService.addSecurityClearanceToCollectionIfMissing(
            securityClearances,
            ...(this.editForm.get('grantedClearances')!.value ?? [])
          )
        )
      )
      .subscribe((securityClearances: ISecurityClearance[]) => (this.securityClearancesSharedCollection = securityClearances));

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

  protected createFromForm(): ISecurityClearance {
    return {
      ...new SecurityClearance(),
      id: this.editForm.get(['id'])!.value,
      clearanceLevel: this.editForm.get(['clearanceLevel'])!.value,
      grantedClearances: this.editForm.get(['grantedClearances'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
