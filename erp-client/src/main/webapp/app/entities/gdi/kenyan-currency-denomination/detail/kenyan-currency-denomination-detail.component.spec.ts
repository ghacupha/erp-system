import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KenyanCurrencyDenominationDetailComponent } from './kenyan-currency-denomination-detail.component';

describe('KenyanCurrencyDenomination Management Detail Component', () => {
  let comp: KenyanCurrencyDenominationDetailComponent;
  let fixture: ComponentFixture<KenyanCurrencyDenominationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KenyanCurrencyDenominationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ kenyanCurrencyDenomination: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(KenyanCurrencyDenominationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(KenyanCurrencyDenominationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load kenyanCurrencyDenomination on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.kenyanCurrencyDenomination).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
