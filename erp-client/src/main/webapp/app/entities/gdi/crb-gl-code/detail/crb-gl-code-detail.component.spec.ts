import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CrbGlCodeDetailComponent } from './crb-gl-code-detail.component';

describe('CrbGlCode Management Detail Component', () => {
  let comp: CrbGlCodeDetailComponent;
  let fixture: ComponentFixture<CrbGlCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CrbGlCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ crbGlCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CrbGlCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CrbGlCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load crbGlCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.crbGlCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
