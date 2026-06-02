import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NbvCompilationBatchDetailComponent } from './nbv-compilation-batch-detail.component';

describe('NbvCompilationBatch Management Detail Component', () => {
  let comp: NbvCompilationBatchDetailComponent;
  let fixture: ComponentFixture<NbvCompilationBatchDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NbvCompilationBatchDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ nbvCompilationBatch: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NbvCompilationBatchDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NbvCompilationBatchDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load nbvCompilationBatch on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.nbvCompilationBatch).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
