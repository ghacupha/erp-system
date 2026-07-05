///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPrepaymentAccount } from '../prepayment-account.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  prepaymentAccountCopyWorkflowInitiatedFromView,
  prepaymentAccountEditWorkflowInitiatedFromView
} from '../../../store/actions/prepayment-account-update-status.actions';
import { BusinessDocumentService, EntityResponseType as BusinessDocumentResponseType } from '../../../erp-pages/business-document/service/business-document.service';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { SettlementService, EntityResponseType as SettlementResponseType } from '../../../erp-settlements/settlement/service/settlement.service';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';

@Component({
  selector: 'jhi-prepayment-account-detail',
  templateUrl: './prepayment-account-detail.component.html',
  styleUrls: ['./prepayment-account-detail.component.scss'],
})
export class PrepaymentAccountDetailComponent implements OnInit, OnDestroy {
  prepaymentAccount: IPrepaymentAccount | null = null;
  relatedBusinessDocuments: IBusinessDocument[] = [];
  selectedBusinessDocument: IBusinessDocument | null = null;
  documentPreviewUrl: SafeResourceUrl | null = null;
  documentPreviewTitle = '';
  loadingDocuments = false;

  private previewObjectUrl: string | null = null;
  private readonly subscriptions = new Subscription();

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    protected store: Store<State>,
    protected businessDocumentService: BusinessDocumentService,
    protected settlementService: SettlementService,
    protected sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.data.subscribe(({ prepaymentAccount }) => {
        this.prepaymentAccount = prepaymentAccount;
        this.loadRelatedBusinessDocuments();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.revokePreviewUrl();
  }

  editButtonEvent(instance: IPrepaymentAccount): void {
    this.store.dispatch(prepaymentAccountEditWorkflowInitiatedFromView({ editedInstance: instance }));
  }

  copyButtonEvent(instance: IPrepaymentAccount): void {
    this.store.dispatch(prepaymentAccountCopyWorkflowInitiatedFromView({ copiedInstance: instance }));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  selectBusinessDocument(document: IBusinessDocument): void {
    if (!document?.id) {
      return;
    }
    this.selectedBusinessDocument = document;
    this.loadingDocuments = true;
    this.businessDocumentService
      .find(document.id)
      .pipe(finalize(() => (this.loadingDocuments = false)))
      .subscribe({
        next: (response: BusinessDocumentResponseType) => {
          const body = response.body;
          if (!body?.documentFile || !body.documentFileContentType) {
            this.clearPreview();
            return;
          }
          this.setPreview(body);
        },
        error: () => this.clearPreview(),
      });
  }

  clearPreview(): void {
    this.selectedBusinessDocument = null;
    this.documentPreviewTitle = '';
    this.revokePreviewUrl();
    this.documentPreviewUrl = null;
  }

  trackBusinessDocument(index: number, document: IBusinessDocument): number {
    return document.id ?? index;
  }

  previousState(): void {
    window.history.back();
  }

  private loadRelatedBusinessDocuments(): void {
    this.relatedBusinessDocuments = [];
    this.clearPreview();

    if (!this.prepaymentAccount?.id) {
      return;
    }

    this.loadingDocuments = true;
    const accountDocuments = this.prepaymentAccount.businessDocuments ?? [];
    const settlement = this.prepaymentAccount.prepaymentTransaction;
    const settlementDocuments = settlement?.businessDocuments ?? [];

    const settlementRequest: ReturnType<SettlementService['find']> | null = settlement?.id ? this.settlementService.find(settlement.id) : null;
    if (!settlementRequest) {
      this.relatedBusinessDocuments = this.mergeBusinessDocuments(accountDocuments, settlementDocuments);
      this.loadingDocuments = false;
      return;
    }

    this.subscriptions.add(
      settlementRequest.pipe(finalize(() => (this.loadingDocuments = false))).subscribe({
        next: (response: SettlementResponseType) => {
          const body: ISettlement | undefined = response.body ?? undefined;
          this.relatedBusinessDocuments = this.mergeBusinessDocuments(accountDocuments, body?.businessDocuments ?? settlementDocuments);
        },
        error: () => {
          this.relatedBusinessDocuments = this.mergeBusinessDocuments(accountDocuments, settlementDocuments);
        },
      })
    );
  }

  private mergeBusinessDocuments(...groups: Array<IBusinessDocument[] | null | undefined>): IBusinessDocument[] {
    const seen = new Set<number>();
    const documents: IBusinessDocument[] = [];
    for (const group of groups) {
      for (const document of group ?? []) {
        if (!document?.id || seen.has(document.id)) {
          continue;
        }
        seen.add(document.id);
        documents.push(document);
      }
    }
    return documents;
  }

  private setPreview(document: IBusinessDocument): void {
    this.revokePreviewUrl();
    const blob = this.base64ToBlob(document.documentFile ?? '', document.documentFileContentType ?? 'application/octet-stream');
    this.previewObjectUrl = URL.createObjectURL(blob);
    this.documentPreviewTitle = document.documentTitle ?? 'Business document';
    this.documentPreviewUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.previewObjectUrl);
  }

  private revokePreviewUrl(): void {
    if (this.previewObjectUrl) {
      URL.revokeObjectURL(this.previewObjectUrl);
      this.previewObjectUrl = null;
    }
  }

  private base64ToBlob(base64: string, contentType: string): Blob {
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);
    for (let index = 0; index < byteCharacters.length; index++) {
      byteNumbers[index] = byteCharacters.charCodeAt(index);
    }
    return new Blob([new Uint8Array(byteNumbers)], { type: contentType });
  }
}
