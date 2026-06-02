import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDealer } from '../dealer.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-dealer-detail',
  templateUrl: './dealer-detail.component.html',
})
export class DealerDetailComponent implements OnInit {
  dealer: IDealer | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dealer }) => {
      this.dealer = dealer;
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
