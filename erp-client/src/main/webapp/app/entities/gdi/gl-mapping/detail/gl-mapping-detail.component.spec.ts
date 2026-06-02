import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GlMappingDetailComponent } from './gl-mapping-detail.component';

describe('GlMapping Management Detail Component', () => {
  let comp: GlMappingDetailComponent;
  let fixture: ComponentFixture<GlMappingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GlMappingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ glMapping: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GlMappingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GlMappingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load glMapping on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.glMapping).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
