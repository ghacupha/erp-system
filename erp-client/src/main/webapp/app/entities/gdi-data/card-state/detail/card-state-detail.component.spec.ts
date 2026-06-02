import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CardStateDetailComponent } from './card-state-detail.component';

describe('CardState Management Detail Component', () => {
  let comp: CardStateDetailComponent;
  let fixture: ComponentFixture<CardStateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardStateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cardState: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CardStateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CardStateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cardState on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cardState).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
