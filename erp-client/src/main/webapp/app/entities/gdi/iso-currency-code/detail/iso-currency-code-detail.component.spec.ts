import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IsoCurrencyCodeDetailComponent } from './iso-currency-code-detail.component';

describe('IsoCurrencyCode Management Detail Component', () => {
  let comp: IsoCurrencyCodeDetailComponent;
  let fixture: ComponentFixture<IsoCurrencyCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IsoCurrencyCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ isoCurrencyCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IsoCurrencyCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IsoCurrencyCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load isoCurrencyCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.isoCurrencyCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
