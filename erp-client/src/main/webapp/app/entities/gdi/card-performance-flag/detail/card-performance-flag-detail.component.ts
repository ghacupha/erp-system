import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICardPerformanceFlag } from '../card-performance-flag.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-card-performance-flag-detail',
  templateUrl: './card-performance-flag-detail.component.html',
})
export class CardPerformanceFlagDetailComponent implements OnInit {
  cardPerformanceFlag: ICardPerformanceFlag | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardPerformanceFlag }) => {
      this.cardPerformanceFlag = cardPerformanceFlag;
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
