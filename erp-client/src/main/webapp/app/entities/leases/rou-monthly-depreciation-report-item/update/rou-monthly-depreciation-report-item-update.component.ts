import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRouMonthlyDepreciationReportItem, RouMonthlyDepreciationReportItem } from '../rou-monthly-depreciation-report-item.model';
import { RouMonthlyDepreciationReportItemService } from '../service/rou-monthly-depreciation-report-item.service';

@Component({
  selector: 'jhi-rou-monthly-depreciation-report-item-update',
  templateUrl: './rou-monthly-depreciation-report-item-update.component.html',
})
export class RouMonthlyDepreciationReportItemUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fiscalMonthStartDate: [],
    fiscalMonthEndDate: [],
    totalDepreciationAmount: [],
  });

  constructor(
    protected rouMonthlyDepreciationReportItemService: RouMonthlyDepreciationReportItemService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouMonthlyDepreciationReportItem }) => {
      this.updateForm(rouMonthlyDepreciationReportItem);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rouMonthlyDepreciationReportItem = this.createFromForm();
    if (rouMonthlyDepreciationReportItem.id !== undefined) {
      this.subscribeToSaveResponse(this.rouMonthlyDepreciationReportItemService.update(rouMonthlyDepreciationReportItem));
    } else {
      this.subscribeToSaveResponse(this.rouMonthlyDepreciationReportItemService.create(rouMonthlyDepreciationReportItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRouMonthlyDepreciationReportItem>>): void {
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

  protected updateForm(rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem): void {
    this.editForm.patchValue({
      id: rouMonthlyDepreciationReportItem.id,
      fiscalMonthStartDate: rouMonthlyDepreciationReportItem.fiscalMonthStartDate,
      fiscalMonthEndDate: rouMonthlyDepreciationReportItem.fiscalMonthEndDate,
      totalDepreciationAmount: rouMonthlyDepreciationReportItem.totalDepreciationAmount,
    });
  }

  protected createFromForm(): IRouMonthlyDepreciationReportItem {
    return {
      ...new RouMonthlyDepreciationReportItem(),
      id: this.editForm.get(['id'])!.value,
      fiscalMonthStartDate: this.editForm.get(['fiscalMonthStartDate'])!.value,
      fiscalMonthEndDate: this.editForm.get(['fiscalMonthEndDate'])!.value,
      totalDepreciationAmount: this.editForm.get(['totalDepreciationAmount'])!.value,
    };
  }
}
