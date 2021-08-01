import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaxReferenceDetailComponent } from './tax-reference-detail.component';

describe('Component Tests', () => {
  describe('TaxReference Management Detail Component', () => {
    let comp: TaxReferenceDetailComponent;
    let fixture: ComponentFixture<TaxReferenceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TaxReferenceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ taxReference: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TaxReferenceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TaxReferenceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load taxReference on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.taxReference).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
