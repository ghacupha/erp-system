import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TAInterestPaidTransferRuleDetailComponent } from './ta-interest-paid-transfer-rule-detail.component';

describe('TAInterestPaidTransferRule Management Detail Component', () => {
  let comp: TAInterestPaidTransferRuleDetailComponent;
  let fixture: ComponentFixture<TAInterestPaidTransferRuleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TAInterestPaidTransferRuleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tAInterestPaidTransferRule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TAInterestPaidTransferRuleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TAInterestPaidTransferRuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tAInterestPaidTransferRule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tAInterestPaidTransferRule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
