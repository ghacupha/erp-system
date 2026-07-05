import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISettlement } from '../settlement.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-settlement-detail',
  templateUrl: './settlement-detail.component.html',
})
export class SettlementDetailComponent implements OnInit {
  settlement: ISettlement | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ settlement }) => {
      this.settlement = settlement;
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
