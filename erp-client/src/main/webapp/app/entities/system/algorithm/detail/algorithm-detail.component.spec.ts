import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AlgorithmDetailComponent } from './algorithm-detail.component';

describe('Algorithm Management Detail Component', () => {
  let comp: AlgorithmDetailComponent;
  let fixture: ComponentFixture<AlgorithmDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AlgorithmDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ algorithm: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AlgorithmDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AlgorithmDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load algorithm on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.algorithm).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
