import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouModelMetadataDetailComponent } from './rou-model-metadata-detail.component';

describe('RouModelMetadata Management Detail Component', () => {
  let comp: RouModelMetadataDetailComponent;
  let fixture: ComponentFixture<RouModelMetadataDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouModelMetadataDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouModelMetadata: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouModelMetadataDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouModelMetadataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouModelMetadata on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouModelMetadata).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
