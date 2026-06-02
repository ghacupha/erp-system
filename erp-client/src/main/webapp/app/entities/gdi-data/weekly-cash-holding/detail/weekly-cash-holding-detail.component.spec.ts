import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WeeklyCashHoldingDetailComponent } from './weekly-cash-holding-detail.component';

describe('WeeklyCashHolding Management Detail Component', () => {
  let comp: WeeklyCashHoldingDetailComponent;
  let fixture: ComponentFixture<WeeklyCashHoldingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WeeklyCashHoldingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ weeklyCashHolding: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WeeklyCashHoldingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WeeklyCashHoldingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load weeklyCashHolding on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.weeklyCashHolding).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
