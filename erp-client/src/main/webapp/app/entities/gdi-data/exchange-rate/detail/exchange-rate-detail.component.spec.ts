import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExchangeRateDetailComponent } from './exchange-rate-detail.component';

describe('ExchangeRate Management Detail Component', () => {
  let comp: ExchangeRateDetailComponent;
  let fixture: ComponentFixture<ExchangeRateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExchangeRateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ exchangeRate: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ExchangeRateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExchangeRateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load exchangeRate on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.exchangeRate).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
