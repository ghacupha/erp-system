import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TALeaseRepaymentRuleDetailComponent } from './ta-lease-repayment-rule-detail.component';

describe('TALeaseRepaymentRule Management Detail Component', () => {
  let comp: TALeaseRepaymentRuleDetailComponent;
  let fixture: ComponentFixture<TALeaseRepaymentRuleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TALeaseRepaymentRuleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tALeaseRepaymentRule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TALeaseRepaymentRuleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TALeaseRepaymentRuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tALeaseRepaymentRule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tALeaseRepaymentRule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
