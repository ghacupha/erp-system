import {Component, OnDestroy, OnInit} from "@angular/core";
import {IPayment, Payment} from "app/shared/model/payments/payment.model";
import {combineLatest, Observable, Subscription} from "rxjs";
import {ITEMS_PER_PAGE} from "app/shared/constants/pagination.constants";
import {PaymentService} from "app/entities/payments/payment/payment.service";
import {ActivatedRoute} from "@angular/router";
import {HttpResponse} from "@angular/common/http";
import {IPaymentRequisition} from "app/shared/model/payments/payment-requisition.model";
import {ITaxRule} from "app/shared/model/payments/tax-rule.model";
import {IPaymentCategory} from "app/shared/model/payments/payment-category.model";
import {IPaymentCalculation} from "app/shared/model/payments/payment-calculation.model";
import {FormBuilder, Validators} from "@angular/forms";
import {PaymentRequisitionService} from "app/entities/payments/payment-requisition/payment-requisition.service";
import {TaxRuleService} from "app/entities/payments/tax-rule/tax-rule.service";
import {PaymentCategoryService} from "app/entities/payments/payment-category/payment-category.service";
import {PaymentCalculationService} from "app/entities/payments/payment-calculation/payment-calculation.service";
import {map} from "rxjs/operators";
import {PaymentDeleteDialogComponent} from "app/entities/payments/payment/payment-delete-dialog.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {PaymentRequisitionUpdateComponent} from "app/entities/payments/payment-requisition/payment-requisition-update.component";

type SelectableEntity = IPaymentRequisition | ITaxRule | IPaymentCategory | IPaymentCalculation;

@Component({
  selector: 'jhi-payment-details-update',
  templateUrl: './payment-details-update.component.html',
  styleUrls: ['./payment-details-update.component.scss']
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
    protected modalService: NgbModal,
    protected paymentService: PaymentService,
    protected paymentRequisitionService: PaymentRequisitionService,
    protected taxRuleService: TaxRuleService,
    protected paymentCategoryService: PaymentCategoryService,
    protected paymentCalculationService: PaymentCalculationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({payment}) => {
      this.updateForm(payment);

      this.paymentRequisitionService
        .query()
        .subscribe((res: HttpResponse<IPaymentRequisition[]>) => (this.paymentrequisitions = res.body || []));

      this.taxRuleService
        .query({'paymentId.specified': 'false'})
        .pipe(
          map((res: HttpResponse<ITaxRule[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ITaxRule[]) => {
          if (!payment.taxRuleId) {
            this.taxrules = resBody;
          } else {
            this.taxRuleService
              .find(payment.taxRuleId)
              .pipe(
                map((subRes: HttpResponse<ITaxRule>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ITaxRule[]) => (this.taxrules = concatRes));
          }
        });

      this.paymentCategoryService
        .query({'paymentId.specified': 'false'})
        .pipe(
          map((res: HttpResponse<IPaymentCategory[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPaymentCategory[]) => {
          if (!payment.paymentCategoryId) {
            this.paymentcategories = resBody;
          } else {
            this.paymentCategoryService
              .find(payment.paymentCategoryId)
              .pipe(
                map((subRes: HttpResponse<IPaymentCategory>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPaymentCategory[]) => (this.paymentcategories = concatRes));
          }
        });

      this.paymentCalculationService
        .query({'paymentId.specified': 'false'})
        .pipe(
          map((res: HttpResponse<IPaymentCalculation[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPaymentCalculation[]) => {
          if (!payment.paymentCalculationId) {
            this.paymentcalculations = resBody;
          } else {
            this.paymentCalculationService
              .find(payment.paymentCalculationId)
              .pipe(
                map((subRes: HttpResponse<IPaymentCalculation>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
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
      paymentRequisitionId: payment.paymentRequisitionId,
      taxRuleId: payment.taxRuleId,
      paymentCategoryId: payment.paymentCategoryId,
      paymentCalculationId: payment.paymentCalculationId,
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

  private createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      paymentNumber: this.editForm.get(['paymentNumber'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      description: this.editForm.get(['description'])!.value,
      paymentRequisitionId: this.editForm.get(['paymentRequisitionId'])!.value,
      taxRuleId: this.editForm.get(['taxRuleId'])!.value,
      paymentCategoryId: this.editForm.get(['paymentCategoryId'])!.value,
      paymentCalculationId: this.editForm.get(['paymentCalculationId'])!.value,
    };
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  newRequisition() {
    const modalRef = this.modalService.open(PaymentRequisitionUpdateComponent, { size: 'lg', backdrop: 'static' });
    // modalRef.componentInstance.payment = payment;
  }
}
