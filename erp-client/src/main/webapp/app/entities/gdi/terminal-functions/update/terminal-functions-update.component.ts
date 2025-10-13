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
import { finalize } from 'rxjs/operators';

import { ITerminalFunctions, TerminalFunctions } from '../terminal-functions.model';
import { TerminalFunctionsService } from '../service/terminal-functions.service';

@Component({
  selector: 'jhi-terminal-functions-update',
  templateUrl: './terminal-functions-update.component.html',
})
export class TerminalFunctionsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    functionCode: [null, [Validators.required]],
    terminalFunctionality: [null, [Validators.required]],
  });

  constructor(
    protected terminalFunctionsService: TerminalFunctionsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ terminalFunctions }) => {
      this.updateForm(terminalFunctions);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const terminalFunctions = this.createFromForm();
    if (terminalFunctions.id !== undefined) {
      this.subscribeToSaveResponse(this.terminalFunctionsService.update(terminalFunctions));
    } else {
      this.subscribeToSaveResponse(this.terminalFunctionsService.create(terminalFunctions));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITerminalFunctions>>): void {
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

  protected updateForm(terminalFunctions: ITerminalFunctions): void {
    this.editForm.patchValue({
      id: terminalFunctions.id,
      functionCode: terminalFunctions.functionCode,
      terminalFunctionality: terminalFunctions.terminalFunctionality,
    });
  }

  protected createFromForm(): ITerminalFunctions {
    return {
      ...new TerminalFunctions(),
      id: this.editForm.get(['id'])!.value,
      functionCode: this.editForm.get(['functionCode'])!.value,
      terminalFunctionality: this.editForm.get(['terminalFunctionality'])!.value,
    };
  }
}
