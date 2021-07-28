import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITaxRule, TaxRule } from 'app/shared/model/payments/tax-rule.model';
import { TaxRuleService } from './tax-rule.service';

@Component({
  selector: 'jhi-tax-rule-update',
  templateUrl: './tax-rule-update.component.html',
})
export class TaxRuleUpdateComponent implements OnInit {
  isSaving = false;
  paymentDateDp: any;

  editForm = this.fb.group({
    id: [],
    paymentNumber: [null, [Validators.required]],
    paymentDate: [null, [Validators.required]],
    telcoExciseDuty: [],
    valueAddedTax: [],
    withholdingVAT: [],
    withholdingTaxConsultancy: [],
    withholdingTaxRent: [],
    cateringLevy: [],
    serviceCharge: [],
    withholdingTaxImportedService: [],
  });

  constructor(protected taxRuleService: TaxRuleService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxRule }) => {
      this.updateForm(taxRule);
    });
  }

  updateForm(taxRule: ITaxRule): void {
    this.editForm.patchValue({
      id: taxRule.id,
      paymentNumber: taxRule.paymentNumber,
      paymentDate: taxRule.paymentDate,
      telcoExciseDuty: taxRule.telcoExciseDuty,
      valueAddedTax: taxRule.valueAddedTax,
      withholdingVAT: taxRule.withholdingVAT,
      withholdingTaxConsultancy: taxRule.withholdingTaxConsultancy,
      withholdingTaxRent: taxRule.withholdingTaxRent,
      cateringLevy: taxRule.cateringLevy,
      serviceCharge: taxRule.serviceCharge,
      withholdingTaxImportedService: taxRule.withholdingTaxImportedService,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const taxRule = this.createFromForm();
    if (taxRule.id !== undefined) {
      this.subscribeToSaveResponse(this.taxRuleService.update(taxRule));
    } else {
      this.subscribeToSaveResponse(this.taxRuleService.create(taxRule));
    }
  }

  private createFromForm(): ITaxRule {
    return {
      ...new TaxRule(),
      id: this.editForm.get(['id'])!.value,
      paymentNumber: this.editForm.get(['paymentNumber'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      telcoExciseDuty: this.editForm.get(['telcoExciseDuty'])!.value,
      valueAddedTax: this.editForm.get(['valueAddedTax'])!.value,
      withholdingVAT: this.editForm.get(['withholdingVAT'])!.value,
      withholdingTaxConsultancy: this.editForm.get(['withholdingTaxConsultancy'])!.value,
      withholdingTaxRent: this.editForm.get(['withholdingTaxRent'])!.value,
      cateringLevy: this.editForm.get(['cateringLevy'])!.value,
      serviceCharge: this.editForm.get(['serviceCharge'])!.value,
      withholdingTaxImportedService: this.editForm.get(['withholdingTaxImportedService'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaxRule>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
