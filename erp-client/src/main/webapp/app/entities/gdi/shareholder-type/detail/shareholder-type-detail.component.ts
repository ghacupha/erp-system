import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShareholderType } from '../shareholder-type.model';

@Component({
  selector: 'jhi-shareholder-type-detail',
  templateUrl: './shareholder-type-detail.component.html',
})
export class ShareholderTypeDetailComponent implements OnInit {
  shareholderType: IShareholderType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shareholderType }) => {
      this.shareholderType = shareholderType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
