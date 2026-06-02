import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseRepaymentPeriodDetailComponent } from './lease-repayment-period-detail.component';

describe('LeaseRepaymentPeriod Management Detail Component', () => {
  let comp: LeaseRepaymentPeriodDetailComponent;
  let fixture: ComponentFixture<LeaseRepaymentPeriodDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseRepaymentPeriodDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseRepaymentPeriod: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseRepaymentPeriodDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseRepaymentPeriodDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseRepaymentPeriod on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseRepaymentPeriod).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
