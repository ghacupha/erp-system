import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BouncedChequeCategoriesDetailComponent } from './bounced-cheque-categories-detail.component';

describe('BouncedChequeCategories Management Detail Component', () => {
  let comp: BouncedChequeCategoriesDetailComponent;
  let fixture: ComponentFixture<BouncedChequeCategoriesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BouncedChequeCategoriesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bouncedChequeCategories: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BouncedChequeCategoriesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BouncedChequeCategoriesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bouncedChequeCategories on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bouncedChequeCategories).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
