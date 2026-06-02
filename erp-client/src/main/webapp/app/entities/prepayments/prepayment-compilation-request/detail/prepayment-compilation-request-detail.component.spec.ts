import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrepaymentCompilationRequestDetailComponent } from './prepayment-compilation-request-detail.component';

describe('PrepaymentCompilationRequest Management Detail Component', () => {
  let comp: PrepaymentCompilationRequestDetailComponent;
  let fixture: ComponentFixture<PrepaymentCompilationRequestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrepaymentCompilationRequestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prepaymentCompilationRequest: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrepaymentCompilationRequestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrepaymentCompilationRequestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prepaymentCompilationRequest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prepaymentCompilationRequest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
