import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CardStatusFlagDetailComponent } from './card-status-flag-detail.component';

describe('CardStatusFlag Management Detail Component', () => {
  let comp: CardStatusFlagDetailComponent;
  let fixture: ComponentFixture<CardStatusFlagDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardStatusFlagDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cardStatusFlag: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CardStatusFlagDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CardStatusFlagDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cardStatusFlag on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cardStatusFlag).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
