import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseLiabilityScheduleReportItem } from '../lease-liability-schedule-report-item.model';

@Component({
  selector: 'jhi-lease-liability-schedule-report-item-detail',
  templateUrl: './lease-liability-schedule-report-item-detail.component.html',
})
export class LeaseLiabilityScheduleReportItemDetailComponent implements OnInit {
  leaseLiabilityScheduleReportItem: ILeaseLiabilityScheduleReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiabilityScheduleReportItem }) => {
      this.leaseLiabilityScheduleReportItem = leaseLiabilityScheduleReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
