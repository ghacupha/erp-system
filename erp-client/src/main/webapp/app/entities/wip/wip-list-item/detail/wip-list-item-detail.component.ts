import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWIPListItem } from '../wip-list-item.model';

@Component({
  selector: 'jhi-wip-list-item-detail',
  templateUrl: './wip-list-item-detail.component.html',
})
export class WIPListItemDetailComponent implements OnInit {
  wIPListItem: IWIPListItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wIPListItem }) => {
      this.wIPListItem = wIPListItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
