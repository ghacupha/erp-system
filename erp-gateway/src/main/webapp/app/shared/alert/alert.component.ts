///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { Component, OnDestroy, OnInit } from '@angular/core';
import { JhiAlertService, JhiAlert } from 'ng-jhipster';

@Component({
  selector: 'jhi-alert',
  template: ` <div class="alerts" role="alert">
    <div *ngFor="let alert of alerts" [ngClass]="setClasses(alert)">
      <ngb-alert *ngIf="alert && alert.type && alert.msg" [type]="alert.type" (close)="close(alert)">
        <pre [innerHTML]="alert.msg"></pre>
      </ngb-alert>
    </div>
  </div>`,
})
export class AlertComponent implements OnInit, OnDestroy {
  alerts: JhiAlert[] = [];

  constructor(private alertService: JhiAlertService) {}

  ngOnInit(): void {
    this.alerts = this.alertService.get();
  }

  setClasses(alert: JhiAlert): { [key: string]: boolean } {
    const classes = { 'jhi-toast': Boolean(alert.toast) };
    if (alert.position) {
      return { ...classes, [alert.position]: true };
    }
    return classes;
  }

  ngOnDestroy(): void {
    this.alertService.clear();
  }

  close(alert: JhiAlert): void {
    // NOSONAR can be removed after https://github.com/SonarSource/SonarJS/issues/1930 is resolved
    alert.close?.(this.alerts); // NOSONAR
  }
}
