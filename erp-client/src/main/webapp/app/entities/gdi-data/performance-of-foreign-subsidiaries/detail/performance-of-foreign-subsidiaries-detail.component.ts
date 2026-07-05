import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPerformanceOfForeignSubsidiaries } from '../performance-of-foreign-subsidiaries.model';

@Component({
  selector: 'jhi-performance-of-foreign-subsidiaries-detail',
  templateUrl: './performance-of-foreign-subsidiaries-detail.component.html',
})
export class PerformanceOfForeignSubsidiariesDetailComponent implements OnInit {
  performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ performanceOfForeignSubsidiaries }) => {
      this.performanceOfForeignSubsidiaries = performanceOfForeignSubsidiaries;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
