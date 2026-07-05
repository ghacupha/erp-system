import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISecurityClearance } from '../security-clearance.model';

@Component({
  selector: 'jhi-security-clearance-detail',
  templateUrl: './security-clearance-detail.component.html',
})
export class SecurityClearanceDetailComponent implements OnInit {
  securityClearance: ISecurityClearance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityClearance }) => {
      this.securityClearance = securityClearance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
