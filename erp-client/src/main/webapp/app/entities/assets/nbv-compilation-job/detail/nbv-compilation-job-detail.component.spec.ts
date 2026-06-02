import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NbvCompilationJobDetailComponent } from './nbv-compilation-job-detail.component';

describe('NbvCompilationJob Management Detail Component', () => {
  let comp: NbvCompilationJobDetailComponent;
  let fixture: ComponentFixture<NbvCompilationJobDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NbvCompilationJobDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ nbvCompilationJob: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NbvCompilationJobDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NbvCompilationJobDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load nbvCompilationJob on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.nbvCompilationJob).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
