import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IIsoCountryCode, IsoCountryCode } from '../iso-country-code.model';
import { IsoCountryCodeService } from '../service/iso-country-code.service';

@Component({
  selector: 'jhi-iso-country-code-update',
  templateUrl: './iso-country-code-update.component.html',
})
export class IsoCountryCodeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    countryCode: [],
    countryDescription: [],
    continentCode: [],
    continentName: [],
    subRegion: [],
  });

  constructor(
    protected isoCountryCodeService: IsoCountryCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isoCountryCode }) => {
      this.updateForm(isoCountryCode);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const isoCountryCode = this.createFromForm();
    if (isoCountryCode.id !== undefined) {
      this.subscribeToSaveResponse(this.isoCountryCodeService.update(isoCountryCode));
    } else {
      this.subscribeToSaveResponse(this.isoCountryCodeService.create(isoCountryCode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIsoCountryCode>>): void {
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

  protected updateForm(isoCountryCode: IIsoCountryCode): void {
    this.editForm.patchValue({
      id: isoCountryCode.id,
      countryCode: isoCountryCode.countryCode,
      countryDescription: isoCountryCode.countryDescription,
      continentCode: isoCountryCode.continentCode,
      continentName: isoCountryCode.continentName,
      subRegion: isoCountryCode.subRegion,
    });
  }

  protected createFromForm(): IIsoCountryCode {
    return {
      ...new IsoCountryCode(),
      id: this.editForm.get(['id'])!.value,
      countryCode: this.editForm.get(['countryCode'])!.value,
      countryDescription: this.editForm.get(['countryDescription'])!.value,
      continentCode: this.editForm.get(['continentCode'])!.value,
      continentName: this.editForm.get(['continentName'])!.value,
      subRegion: this.editForm.get(['subRegion'])!.value,
    };
  }
}
