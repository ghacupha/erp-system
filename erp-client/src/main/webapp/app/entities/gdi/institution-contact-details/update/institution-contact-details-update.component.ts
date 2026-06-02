import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IInstitutionContactDetails, InstitutionContactDetails } from '../institution-contact-details.model';
import { InstitutionContactDetailsService } from '../service/institution-contact-details.service';

@Component({
  selector: 'jhi-institution-contact-details-update',
  templateUrl: './institution-contact-details-update.component.html',
})
export class InstitutionContactDetailsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    entityId: [null, [Validators.required]],
    entityName: [null, [Validators.required]],
    contactType: [null, [Validators.required]],
    contactLevel: [],
    contactValue: [],
    contactName: [],
    contactDesignation: [],
  });

  constructor(
    protected institutionContactDetailsService: InstitutionContactDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ institutionContactDetails }) => {
      this.updateForm(institutionContactDetails);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const institutionContactDetails = this.createFromForm();
    if (institutionContactDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.institutionContactDetailsService.update(institutionContactDetails));
    } else {
      this.subscribeToSaveResponse(this.institutionContactDetailsService.create(institutionContactDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstitutionContactDetails>>): void {
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

  protected updateForm(institutionContactDetails: IInstitutionContactDetails): void {
    this.editForm.patchValue({
      id: institutionContactDetails.id,
      entityId: institutionContactDetails.entityId,
      entityName: institutionContactDetails.entityName,
      contactType: institutionContactDetails.contactType,
      contactLevel: institutionContactDetails.contactLevel,
      contactValue: institutionContactDetails.contactValue,
      contactName: institutionContactDetails.contactName,
      contactDesignation: institutionContactDetails.contactDesignation,
    });
  }

  protected createFromForm(): IInstitutionContactDetails {
    return {
      ...new InstitutionContactDetails(),
      id: this.editForm.get(['id'])!.value,
      entityId: this.editForm.get(['entityId'])!.value,
      entityName: this.editForm.get(['entityName'])!.value,
      contactType: this.editForm.get(['contactType'])!.value,
      contactLevel: this.editForm.get(['contactLevel'])!.value,
      contactValue: this.editForm.get(['contactValue'])!.value,
      contactName: this.editForm.get(['contactName'])!.value,
      contactDesignation: this.editForm.get(['contactDesignation'])!.value,
    };
  }
}
