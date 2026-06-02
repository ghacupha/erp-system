import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISystemModule } from '../system-module.model';

@Component({
  selector: 'jhi-system-module-detail',
  templateUrl: './system-module-detail.component.html',
})
export class SystemModuleDetailComponent implements OnInit {
  systemModule: ISystemModule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemModule }) => {
      this.systemModule = systemModule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
