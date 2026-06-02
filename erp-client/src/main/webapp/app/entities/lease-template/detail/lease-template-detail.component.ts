import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseTemplate } from '../lease-template.model';

@Component({
  selector: 'jhi-lease-template-detail',
  templateUrl: './lease-template-detail.component.html',
})
export class LeaseTemplateDetailComponent implements OnInit {
  leaseTemplate: ILeaseTemplate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseTemplate }) => {
      this.leaseTemplate = leaseTemplate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
