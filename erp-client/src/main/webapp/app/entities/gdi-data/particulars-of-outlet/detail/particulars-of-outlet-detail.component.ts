import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParticularsOfOutlet } from '../particulars-of-outlet.model';

@Component({
  selector: 'jhi-particulars-of-outlet-detail',
  templateUrl: './particulars-of-outlet-detail.component.html',
})
export class ParticularsOfOutletDetailComponent implements OnInit {
  particularsOfOutlet: IParticularsOfOutlet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ particularsOfOutlet }) => {
      this.particularsOfOutlet = particularsOfOutlet;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
