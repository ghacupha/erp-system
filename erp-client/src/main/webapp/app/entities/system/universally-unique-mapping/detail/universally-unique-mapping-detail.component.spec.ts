import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UniversallyUniqueMappingDetailComponent } from './universally-unique-mapping-detail.component';

describe('UniversallyUniqueMapping Management Detail Component', () => {
  let comp: UniversallyUniqueMappingDetailComponent;
  let fixture: ComponentFixture<UniversallyUniqueMappingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UniversallyUniqueMappingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ universallyUniqueMapping: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UniversallyUniqueMappingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UniversallyUniqueMappingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load universallyUniqueMapping on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.universallyUniqueMapping).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
