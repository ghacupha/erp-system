import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReasonsForBouncedChequeDetailComponent } from './reasons-for-bounced-cheque-detail.component';

describe('ReasonsForBouncedCheque Management Detail Component', () => {
  let comp: ReasonsForBouncedChequeDetailComponent;
  let fixture: ComponentFixture<ReasonsForBouncedChequeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReasonsForBouncedChequeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ reasonsForBouncedCheque: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReasonsForBouncedChequeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReasonsForBouncedChequeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reasonsForBouncedCheque on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.reasonsForBouncedCheque).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
