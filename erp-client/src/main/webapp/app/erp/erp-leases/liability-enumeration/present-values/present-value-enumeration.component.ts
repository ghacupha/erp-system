import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { LiabilityEnumerationService } from '../service/liability-enumeration.service';
import { AlertService } from 'app/core/util/alert.service';
import { IPresentValueEnumeration } from '../liability-enumeration.model';

@Component({
  selector: 'jhi-present-value-enumeration',
  templateUrl: './present-value-enumeration.component.html',
})
export class PresentValueEnumerationComponent implements OnInit {
  values: IPresentValueEnumeration[] = [];
  liabilityEnumerationId?: number;
  isLoading = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected liabilityEnumerationService: LiabilityEnumerationService,
    protected alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.liabilityEnumerationId = params['id'];
      this.load();
    });
  }

  load(): void {
    this.isLoading = true;
    this.liabilityEnumerationService.presentValues(this.liabilityEnumerationId).subscribe({
      next: (res: HttpResponse<IPresentValueEnumeration[]>) => {
        this.values = res.body ?? [];
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.isLoading = false;
        this.alertService.addHttpErrorResponse(err);
      },
    });
  }
}
