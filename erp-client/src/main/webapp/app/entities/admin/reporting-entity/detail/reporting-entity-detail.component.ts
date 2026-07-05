import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReportingEntity } from '../reporting-entity.model';

@Component({
  selector: 'jhi-reporting-entity-detail',
  templateUrl: './reporting-entity-detail.component.html',
})
export class ReportingEntityDetailComponent implements OnInit {
  reportingEntity: IReportingEntity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportingEntity }) => {
      this.reportingEntity = reportingEntity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
