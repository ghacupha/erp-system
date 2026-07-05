import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISecurityClearance, SecurityClearance } from '../security-clearance.model';
import { SecurityClearanceService } from '../service/security-clearance.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

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
