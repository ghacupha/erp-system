import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ILiabilityEnumeration } from '../liability-enumeration.model';
import { LiabilityEnumerationService } from '../service/liability-enumeration.service';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-liability-enumeration',
  templateUrl: './liability-enumeration.component.html',
})
export class LiabilityEnumerationComponent implements OnInit {
  liabilityEnumerations: ILiabilityEnumeration[] = [];
  isLoading = false;

  constructor(
    protected liabilityEnumerationService: LiabilityEnumerationService,
    protected alertService: AlertService,
    protected router: Router
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading = true;
    this.liabilityEnumerationService.query({ size: 20 }).subscribe({
      next: (res: HttpResponse<ILiabilityEnumeration[]>) => {
        this.isLoading = false;
        this.liabilityEnumerations = res.body ?? [];
      },
      error: err => {
        this.isLoading = false;
        this.alertService.addHttpErrorResponse(err);
      },
    });
  }

  trackId(index: number, item: ILiabilityEnumeration): number {
    return item.id!;
  }

  viewPresentValues(item: ILiabilityEnumeration): void {
    if (item.id) {
      this.router.navigate(['erp', 'leases', 'liability-enumeration', item.id, 'present-values']);
    }
  }
}
