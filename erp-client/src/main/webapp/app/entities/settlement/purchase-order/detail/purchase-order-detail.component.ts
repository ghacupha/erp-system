import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseOrder } from '../purchase-order.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-purchase-order-detail',
  templateUrl: './purchase-order-detail.component.html',
})
export class PurchaseOrderDetailComponent implements OnInit {
  purchaseOrder: IPurchaseOrder | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrder }) => {
      this.purchaseOrder = purchaseOrder;
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
