import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILeaseTemplate, LeaseTemplate } from '../lease-template.model';
import { LeaseTemplateService } from '../service/lease-template.service';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { AssetCategoryService } from 'app/entities/assets/asset-category/service/asset-category.service';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ServiceOutletService } from 'app/entities/system/service-outlet/service/service-outlet.service';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { DealerService } from 'app/entities/people/dealer/service/dealer.service';

@Component({
  selector: 'jhi-lease-template-update',
  templateUrl: './lease-template-update.component.html',
})
export class LeaseTemplateUpdateComponent implements OnInit {
  isSaving = false;

  transactionAccountsSharedCollection: ITransactionAccount[] = [];
  assetCategoriesSharedCollection: IAssetCategory[] = [];
  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  dealersSharedCollection: IDealer[] = [];

  editForm = this.fb.group({
    id: [],
    templateTitle: [null, [Validators.required]],
    assetAccount: [],
    depreciationAccount: [],
    accruedDepreciationAccount: [],
    interestPaidTransferDebitAccount: [],
    interestPaidTransferCreditAccount: [],
    interestAccruedDebitAccount: [],
    interestAccruedCreditAccount: [],
    leaseRecognitionDebitAccount: [],
    leaseRecognitionCreditAccount: [],
    leaseRepaymentDebitAccount: [],
    leaseRepaymentCreditAccount: [],
    rouRecognitionCreditAccount: [],
    rouRecognitionDebitAccount: [],
    assetCategory: [],
    serviceOutlet: [],
    mainDealer: [],
  });

  constructor(
    protected leaseTemplateService: LeaseTemplateService,
    protected transactionAccountService: TransactionAccountService,
    protected assetCategoryService: AssetCategoryService,
    protected serviceOutletService: ServiceOutletService,
    protected dealerService: DealerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseTemplate }) => {
      this.updateForm(leaseTemplate);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaseTemplate = this.createFromForm();
    if (leaseTemplate.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseTemplateService.update(leaseTemplate));
    } else {
      this.subscribeToSaveResponse(this.leaseTemplateService.create(leaseTemplate));
    }
  }

  trackTransactionAccountById(index: number, item: ITransactionAccount): number {
    return item.id!;
  }

  trackAssetCategoryById(index: number, item: IAssetCategory): number {
    return item.id!;
  }

  trackServiceOutletById(index: number, item: IServiceOutlet): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseTemplate>>): void {
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

  protected updateForm(leaseTemplate: ILeaseTemplate): void {
    this.editForm.patchValue({
      id: leaseTemplate.id,
      templateTitle: leaseTemplate.templateTitle,
      assetAccount: leaseTemplate.assetAccount,
      depreciationAccount: leaseTemplate.depreciationAccount,
      accruedDepreciationAccount: leaseTemplate.accruedDepreciationAccount,
      interestPaidTransferDebitAccount: leaseTemplate.interestPaidTransferDebitAccount,
      interestPaidTransferCreditAccount: leaseTemplate.interestPaidTransferCreditAccount,
      interestAccruedDebitAccount: leaseTemplate.interestAccruedDebitAccount,
      interestAccruedCreditAccount: leaseTemplate.interestAccruedCreditAccount,
      leaseRecognitionDebitAccount: leaseTemplate.leaseRecognitionDebitAccount,
      leaseRecognitionCreditAccount: leaseTemplate.leaseRecognitionCreditAccount,
      leaseRepaymentDebitAccount: leaseTemplate.leaseRepaymentDebitAccount,
      leaseRepaymentCreditAccount: leaseTemplate.leaseRepaymentCreditAccount,
      rouRecognitionCreditAccount: leaseTemplate.rouRecognitionCreditAccount,
      rouRecognitionDebitAccount: leaseTemplate.rouRecognitionDebitAccount,
      assetCategory: leaseTemplate.assetCategory,
      serviceOutlet: leaseTemplate.serviceOutlet,
      mainDealer: leaseTemplate.mainDealer,
    });

    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      leaseTemplate.assetAccount,
      leaseTemplate.depreciationAccount,
      leaseTemplate.accruedDepreciationAccount,
      leaseTemplate.interestPaidTransferDebitAccount,
      leaseTemplate.interestPaidTransferCreditAccount,
      leaseTemplate.interestAccruedDebitAccount,
      leaseTemplate.interestAccruedCreditAccount,
      leaseTemplate.leaseRecognitionDebitAccount,
      leaseTemplate.leaseRecognitionCreditAccount,
      leaseTemplate.leaseRepaymentDebitAccount,
      leaseTemplate.leaseRepaymentCreditAccount,
      leaseTemplate.rouRecognitionCreditAccount,
      leaseTemplate.rouRecognitionDebitAccount
    );
    this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing(
      this.assetCategoriesSharedCollection,
      leaseTemplate.assetCategory
    );
    this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
      this.serviceOutletsSharedCollection,
      leaseTemplate.serviceOutlet
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      leaseTemplate.mainDealer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transactionAccountService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccount[]>) => res.body ?? []))
      .pipe(
        map((transactionAccounts: ITransactionAccount[]) =>
          this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
            transactionAccounts,
            this.editForm.get('assetAccount')!.value,
            this.editForm.get('depreciationAccount')!.value,
            this.editForm.get('accruedDepreciationAccount')!.value,
            this.editForm.get('interestPaidTransferDebitAccount')!.value,
            this.editForm.get('interestPaidTransferCreditAccount')!.value,
            this.editForm.get('interestAccruedDebitAccount')!.value,
            this.editForm.get('interestAccruedCreditAccount')!.value,
            this.editForm.get('leaseRecognitionDebitAccount')!.value,
            this.editForm.get('leaseRecognitionCreditAccount')!.value,
            this.editForm.get('leaseRepaymentDebitAccount')!.value,
            this.editForm.get('leaseRepaymentCreditAccount')!.value,
            this.editForm.get('rouRecognitionCreditAccount')!.value,
            this.editForm.get('rouRecognitionDebitAccount')!.value
          )
        )
      )
      .subscribe((transactionAccounts: ITransactionAccount[]) => (this.transactionAccountsSharedCollection = transactionAccounts));

    this.assetCategoryService
      .query()
      .pipe(map((res: HttpResponse<IAssetCategory[]>) => res.body ?? []))
      .pipe(
        map((assetCategories: IAssetCategory[]) =>
          this.assetCategoryService.addAssetCategoryToCollectionIfMissing(assetCategories, this.editForm.get('assetCategory')!.value)
        )
      )
      .subscribe((assetCategories: IAssetCategory[]) => (this.assetCategoriesSharedCollection = assetCategories));

    this.serviceOutletService
      .query()
      .pipe(map((res: HttpResponse<IServiceOutlet[]>) => res.body ?? []))
      .pipe(
        map((serviceOutlets: IServiceOutlet[]) =>
          this.serviceOutletService.addServiceOutletToCollectionIfMissing(serviceOutlets, this.editForm.get('serviceOutlet')!.value)
        )
      )
      .subscribe((serviceOutlets: IServiceOutlet[]) => (this.serviceOutletsSharedCollection = serviceOutlets));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('mainDealer')!.value)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));
  }

  protected createFromForm(): ILeaseTemplate {
    return {
      ...new LeaseTemplate(),
      id: this.editForm.get(['id'])!.value,
      templateTitle: this.editForm.get(['templateTitle'])!.value,
      assetAccount: this.editForm.get(['assetAccount'])!.value,
      depreciationAccount: this.editForm.get(['depreciationAccount'])!.value,
      accruedDepreciationAccount: this.editForm.get(['accruedDepreciationAccount'])!.value,
      interestPaidTransferDebitAccount: this.editForm.get(['interestPaidTransferDebitAccount'])!.value,
      interestPaidTransferCreditAccount: this.editForm.get(['interestPaidTransferCreditAccount'])!.value,
      interestAccruedDebitAccount: this.editForm.get(['interestAccruedDebitAccount'])!.value,
      interestAccruedCreditAccount: this.editForm.get(['interestAccruedCreditAccount'])!.value,
      leaseRecognitionDebitAccount: this.editForm.get(['leaseRecognitionDebitAccount'])!.value,
      leaseRecognitionCreditAccount: this.editForm.get(['leaseRecognitionCreditAccount'])!.value,
      leaseRepaymentDebitAccount: this.editForm.get(['leaseRepaymentDebitAccount'])!.value,
      leaseRepaymentCreditAccount: this.editForm.get(['leaseRepaymentCreditAccount'])!.value,
      rouRecognitionCreditAccount: this.editForm.get(['rouRecognitionCreditAccount'])!.value,
      rouRecognitionDebitAccount: this.editForm.get(['rouRecognitionDebitAccount'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      mainDealer: this.editForm.get(['mainDealer'])!.value,
    };
  }
}
