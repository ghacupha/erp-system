import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BankBranchCodeDetailComponent } from './bank-branch-code-detail.component';

describe('BankBranchCode Management Detail Component', () => {
  let comp: BankBranchCodeDetailComponent;
  let fixture: ComponentFixture<BankBranchCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BankBranchCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bankBranchCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BankBranchCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BankBranchCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bankBranchCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bankBranchCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
