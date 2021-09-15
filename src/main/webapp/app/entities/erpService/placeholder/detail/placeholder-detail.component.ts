import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlaceholder } from '../placeholder.model';

@Component({
  selector: 'jhi-placeholder-detail',
  templateUrl: './placeholder-detail.component.html',
})
export class PlaceholderDetailComponent implements OnInit {
  placeholder: IPlaceholder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ placeholder }) => {
      this.placeholder = placeholder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
