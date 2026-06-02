import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILeasePeriod, LeasePeriod } from '../lease-period.model';
import { LeasePeriodService } from '../service/lease-period.service';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/system/fiscal-month/service/fiscal-month.service';

@Component({
  selector: 'jhi-lease-period-update',
  templateUrl: './lease-period-update.component.html',
})
export class LeasePeriodUpdateComponent implements OnInit {
  isSaving = false;

  fiscalMonthsSharedCollection: IFiscalMonth[] = [];

  editForm = this.fb.group({
    id: [],
    sequenceNumber: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    periodCode: [null, [Validators.required]],
    fiscalMonth: [null, Validators.required],
  });

  constructor(
    protected leasePeriodService: LeasePeriodService,
    protected fiscalMonthService: FiscalMonthService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leasePeriod }) => {
      this.updateForm(leasePeriod);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leasePeriod = this.createFromForm();
    if (leasePeriod.id !== undefined) {
      this.subscribeToSaveResponse(this.leasePeriodService.update(leasePeriod));
    } else {
      this.subscribeToSaveResponse(this.leasePeriodService.create(leasePeriod));
    }
  }

  trackFiscalMonthById(index: number, item: IFiscalMonth): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeasePeriod>>): void {
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

  protected updateForm(leasePeriod: ILeasePeriod): void {
    this.editForm.patchValue({
      id: leasePeriod.id,
      sequenceNumber: leasePeriod.sequenceNumber,
      startDate: leasePeriod.startDate,
      endDate: leasePeriod.endDate,
      periodCode: leasePeriod.periodCode,
      fiscalMonth: leasePeriod.fiscalMonth,
    });

    this.fiscalMonthsSharedCollection = this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(
      this.fiscalMonthsSharedCollection,
      leasePeriod.fiscalMonth
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fiscalMonthService
      .query()
      .pipe(map((res: HttpResponse<IFiscalMonth[]>) => res.body ?? []))
      .pipe(
        map((fiscalMonths: IFiscalMonth[]) =>
          this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(fiscalMonths, this.editForm.get('fiscalMonth')!.value)
        )
      )
      .subscribe((fiscalMonths: IFiscalMonth[]) => (this.fiscalMonthsSharedCollection = fiscalMonths));
  }

  protected createFromForm(): ILeasePeriod {
    return {
      ...new LeasePeriod(),
      id: this.editForm.get(['id'])!.value,
      sequenceNumber: this.editForm.get(['sequenceNumber'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      periodCode: this.editForm.get(['periodCode'])!.value,
      fiscalMonth: this.editForm.get(['fiscalMonth'])!.value,
    };
  }
}
