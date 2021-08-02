import {Component, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {HttpResponse} from "@angular/common/http";
import {FormBuilder, Validators} from "@angular/forms";
import {map} from "rxjs/operators";
import { IPaymentRequisition } from '../../../entities/payments/payment-requisition/payment-requisition.model';
import { ITaxRule } from '../../../entities/payments/tax-rule/tax-rule.model';
import { IPaymentCategory } from '../../../entities/payments/payment-category/payment-category.model';
import { IPaymentCalculation } from '../../../entities/payments/payment-calculation/payment-calculation.model';
import { PaymentService } from '../../../entities/payments/payment/service/payment.service';
import { PaymentRequisitionService } from '../../../entities/payments/payment-requisition/service/payment-requisition.service';
import { TaxRuleService } from '../../../entities/payments/tax-rule/service/tax-rule.service';
import { PaymentCategoryService } from '../../../entities/payments/payment-category/service/payment-category.service';
import { PaymentCalculationService } from '../../../entities/payments/payment-calculation/service/payment-calculation.service';
import { IPayment, Payment } from '../../../entities/payments/payment/payment.model';

type SelectableEntity = IPaymentRequisition | ITaxRule | IPaymentCategory| IPaymentCalculation;

@Component({
  selector: 'jhi-payment-details-update',
  templateUrl: './payment-details-update.component.html',
})
export class PaymentDetailsUpdateComponent implements OnInit {
  isSaving = false;
  paymentrequisitions: IPaymentRequisition[] = [];
  taxrules: ITaxRule[] = [];
  paymentcategories: IPaymentCategory[] = [];
  paymentcalculations: IPaymentCalculation[] = [];
  paymentDateDp: any;

  editForm = this.fb.group({
    id: [],
    paymentNumber: [],
    paymentDate: [],
    paymentAmount: [],
    description: [],
    paymentRequisitionId: [],
    taxRuleId: [],
    paymentCategoryId: [null, Validators.required],
    paymentCalculationId: [null, Validators.required],
  });

  constructor(
    protected paymentService: PaymentService,
    protected paymentRequisitionService: PaymentRequisitionService,
    protected taxRuleService: TaxRuleService,
    protected paymentCategoryService: PaymentCategoryService,
    protected paymentCalculationService: PaymentCalculationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.updateForm(payment);

      this.paymentRequisitionService
        .query()
        .subscribe((res: HttpResponse<IPaymentRequisition[]>) => (this.paymentrequisitions = res.body ?? []));

      this.taxRuleService
        .query({ 'paymentId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<ITaxRule[]>) => res.body ?? [])
        )
        .subscribe((resBody: ITaxRule[]) => {
          if (!payment.taxRuleId) {
            this.taxrules = resBody;
          } else {
            this.taxRuleService
              .find(payment.taxRuleId)
              .pipe(
                map((subRes: HttpResponse<ITaxRule>) => subRes.body ? [subRes.body].concat(resBody) : resBody
                )
              )
              .subscribe((concatRes: ITaxRule[]) => (this.taxrules = concatRes));
          }
        });

      this.paymentCategoryService
        .query({ 'paymentId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPaymentCategory[]>) => res.body ?? []
          )
        )
        .subscribe((resBody: IPaymentCategory[]) => {
          if (!payment.paymentCategoryId) {
            this.paymentcategories = resBody;
          } else {
            this.paymentCategoryService
              .find(payment.paymentCategoryId)
              .pipe(
                map((subRes: HttpResponse<IPaymentCategory>) =>  subRes.body ? [subRes.body].concat(resBody) : resBody
                )
              )
              .subscribe((concatRes: IPaymentCategory[]) => (this.paymentcategories = concatRes));
          }
        });

      this.paymentCalculationService
        .query({ 'paymentId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPaymentCalculation[]>) => res.body ?? []
          )
        )
        .subscribe((resBody: IPaymentCalculation[]) => {
          if (!payment.paymentCalculationId) {
            this.paymentcalculations = resBody;
          } else {
            this.paymentCalculationService
              .find(payment.paymentCalculationId)
              .pipe(
                map((subRes: HttpResponse<IPaymentCalculation>) => subRes.body ? [subRes.body].concat(resBody) : resBody
                )
              )
              .subscribe((concatRes: IPaymentCalculation[]) => (this.paymentcalculations = concatRes));
          }
        });
    });
  }

  updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      paymentNumber: payment.paymentNumber,
      paymentDate: payment.paymentDate,
      paymentAmount: payment.paymentAmount,
      description: payment.description,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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

  private createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      paymentNumber: this.editForm.get(['paymentNumber'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }


}
