import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGdiTransactionDataIndex } from '../gdi-transaction-data-index.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-gdi-transaction-data-index-detail',
  templateUrl: './gdi-transaction-data-index-detail.component.html',
})
export class GdiTransactionDataIndexDetailComponent implements OnInit {
  gdiTransactionDataIndex: IGdiTransactionDataIndex | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gdiTransactionDataIndex }) => {
      this.gdiTransactionDataIndex = gdiTransactionDataIndex;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
