import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProcessStatus } from '../process-status.model';

@Component({
  selector: 'jhi-process-status-detail',
  templateUrl: './process-status-detail.component.html',
})
export class ProcessStatusDetailComponent implements OnInit {
  processStatus: IProcessStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ processStatus }) => {
      this.processStatus = processStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
