import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TALeaseInterestAccrualRuleDetailComponent } from './ta-lease-interest-accrual-rule-detail.component';

describe('TALeaseInterestAccrualRule Management Detail Component', () => {
  let comp: TALeaseInterestAccrualRuleDetailComponent;
  let fixture: ComponentFixture<TALeaseInterestAccrualRuleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TALeaseInterestAccrualRuleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tALeaseInterestAccrualRule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TALeaseInterestAccrualRuleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TALeaseInterestAccrualRuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tALeaseInterestAccrualRule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tALeaseInterestAccrualRule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
