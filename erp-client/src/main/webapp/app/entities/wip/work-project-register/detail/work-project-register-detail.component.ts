import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkProjectRegister } from '../work-project-register.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-work-project-register-detail',
  templateUrl: './work-project-register-detail.component.html',
})
export class WorkProjectRegisterDetailComponent implements OnInit {
  workProjectRegister: IWorkProjectRegister | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workProjectRegister }) => {
      this.workProjectRegister = workProjectRegister;
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
