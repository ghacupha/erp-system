import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TACompilationRequestDetailComponent } from './ta-compilation-request-detail.component';

describe('TACompilationRequest Management Detail Component', () => {
  let comp: TACompilationRequestDetailComponent;
  let fixture: ComponentFixture<TACompilationRequestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TACompilationRequestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tACompilationRequest: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TACompilationRequestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TACompilationRequestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tACompilationRequest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tACompilationRequest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
