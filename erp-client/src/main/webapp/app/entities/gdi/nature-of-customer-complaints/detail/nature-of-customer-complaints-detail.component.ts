import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INatureOfCustomerComplaints } from '../nature-of-customer-complaints.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-nature-of-customer-complaints-detail',
  templateUrl: './nature-of-customer-complaints-detail.component.html',
})
export class NatureOfCustomerComplaintsDetailComponent implements OnInit {
  natureOfCustomerComplaints: INatureOfCustomerComplaints | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ natureOfCustomerComplaints }) => {
      this.natureOfCustomerComplaints = natureOfCustomerComplaints;
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
