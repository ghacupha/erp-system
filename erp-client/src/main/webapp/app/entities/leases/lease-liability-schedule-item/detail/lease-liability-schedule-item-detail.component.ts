import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseLiabilityScheduleItem } from '../lease-liability-schedule-item.model';

@Component({
  selector: 'jhi-lease-liability-schedule-item-detail',
  templateUrl: './lease-liability-schedule-item-detail.component.html',
})
export class LeaseLiabilityScheduleItemDetailComponent implements OnInit {
  leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiabilityScheduleItem }) => {
      this.leaseLiabilityScheduleItem = leaseLiabilityScheduleItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
