import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIsicEconomicActivity } from '../isic-economic-activity.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-isic-economic-activity-detail',
  templateUrl: './isic-economic-activity-detail.component.html',
})
export class IsicEconomicActivityDetailComponent implements OnInit {
  isicEconomicActivity: IIsicEconomicActivity | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isicEconomicActivity }) => {
      this.isicEconomicActivity = isicEconomicActivity;
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
