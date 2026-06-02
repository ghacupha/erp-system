import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWIPTransferListItem } from '../wip-transfer-list-item.model';

@Component({
  selector: 'jhi-wip-transfer-list-item-detail',
  templateUrl: './wip-transfer-list-item-detail.component.html',
})
export class WIPTransferListItemDetailComponent implements OnInit {
  wIPTransferListItem: IWIPTransferListItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wIPTransferListItem }) => {
      this.wIPTransferListItem = wIPTransferListItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
