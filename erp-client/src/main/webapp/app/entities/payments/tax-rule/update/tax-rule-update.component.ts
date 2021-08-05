import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITaxRule, TaxRule } from '../tax-rule.model';
import { TaxRuleService } from '../service/tax-rule.service';

@Component({
  selector: 'jhi-tax-rule-update',
  templateUrl: './tax-rule-update.component.html',
})
export class TaxRuleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    telcoExciseDuty: [],
    valueAddedTax: [],
    withholdingVAT: [],
    withholdingTaxConsultancy: [],
    withholdingTaxRent: [],
    cateringLevy: [],
    serviceCharge: [],
    withholdingTaxImportedService: [],
  });

  constructor(protected taxRuleService: TaxRuleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxRule }) => {
      this.updateForm(taxRule);
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaxRule>>): void {
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

  protected updateForm(taxRule: ITaxRule): void {
    this.editForm.patchValue({
      id: taxRule.id,
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

  protected createFromForm(): ITaxRule {
    return {
      ...new TaxRule(),
      id: this.editForm.get(['id'])!.value,
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
}
