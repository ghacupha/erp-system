import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlaceholderDetailComponent } from './placeholder-detail.component';

describe('Placeholder Management Detail Component', () => {
  let comp: PlaceholderDetailComponent;
  let fixture: ComponentFixture<PlaceholderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlaceholderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ placeholder: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlaceholderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlaceholderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load placeholder on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.placeholder).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
