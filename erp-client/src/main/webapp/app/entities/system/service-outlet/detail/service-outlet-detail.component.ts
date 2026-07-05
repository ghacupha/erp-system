import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServiceOutlet } from '../service-outlet.model';

@Component({
  selector: 'jhi-service-outlet-detail',
  templateUrl: './service-outlet-detail.component.html',
})
export class ServiceOutletDetailComponent implements OnInit {
  serviceOutlet: IServiceOutlet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceOutlet }) => {
      this.serviceOutlet = serviceOutlet;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
