import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DealerDetailComponent } from './dealer-detail.component';

describe('Dealer Management Detail Component', () => {
  let comp: DealerDetailComponent;
  let fixture: ComponentFixture<DealerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DealerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dealer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DealerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DealerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dealer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dealer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
