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
import { finalize } from 'rxjs/operators';

import { IReasonsForBouncedCheque, ReasonsForBouncedCheque } from '../reasons-for-bounced-cheque.model';
import { ReasonsForBouncedChequeService } from '../service/reasons-for-bounced-cheque.service';

@Component({
  selector: 'jhi-reasons-for-bounced-cheque-update',
  templateUrl: './reasons-for-bounced-cheque-update.component.html',
})
export class ReasonsForBouncedChequeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    bouncedChequeReasonsTypeCode: [null, [Validators.required]],
    bouncedChequeReasonsType: [],
  });

  constructor(
    protected reasonsForBouncedChequeService: ReasonsForBouncedChequeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reasonsForBouncedCheque }) => {
      this.updateForm(reasonsForBouncedCheque);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reasonsForBouncedCheque = this.createFromForm();
    if (reasonsForBouncedCheque.id !== undefined) {
      this.subscribeToSaveResponse(this.reasonsForBouncedChequeService.update(reasonsForBouncedCheque));
    } else {
      this.subscribeToSaveResponse(this.reasonsForBouncedChequeService.create(reasonsForBouncedCheque));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReasonsForBouncedCheque>>): void {
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

  protected updateForm(reasonsForBouncedCheque: IReasonsForBouncedCheque): void {
    this.editForm.patchValue({
      id: reasonsForBouncedCheque.id,
      bouncedChequeReasonsTypeCode: reasonsForBouncedCheque.bouncedChequeReasonsTypeCode,
      bouncedChequeReasonsType: reasonsForBouncedCheque.bouncedChequeReasonsType,
    });
  }

  protected createFromForm(): IReasonsForBouncedCheque {
    return {
      ...new ReasonsForBouncedCheque(),
      id: this.editForm.get(['id'])!.value,
      bouncedChequeReasonsTypeCode: this.editForm.get(['bouncedChequeReasonsTypeCode'])!.value,
      bouncedChequeReasonsType: this.editForm.get(['bouncedChequeReasonsType'])!.value,
    };
  }
}
