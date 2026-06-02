import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWeeklyCounterfeitHolding } from '../weekly-counterfeit-holding.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-weekly-counterfeit-holding-detail',
  templateUrl: './weekly-counterfeit-holding-detail.component.html',
})
export class WeeklyCounterfeitHoldingDetailComponent implements OnInit {
  weeklyCounterfeitHolding: IWeeklyCounterfeitHolding | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weeklyCounterfeitHolding }) => {
      this.weeklyCounterfeitHolding = weeklyCounterfeitHolding;
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
