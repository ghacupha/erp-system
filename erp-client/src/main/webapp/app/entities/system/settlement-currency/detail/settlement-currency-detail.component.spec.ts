import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SettlementCurrencyDetailComponent } from './settlement-currency-detail.component';

describe('SettlementCurrency Management Detail Component', () => {
  let comp: SettlementCurrencyDetailComponent;
  let fixture: ComponentFixture<SettlementCurrencyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SettlementCurrencyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ settlementCurrency: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SettlementCurrencyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SettlementCurrencyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load settlementCurrency on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.settlementCurrency).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
