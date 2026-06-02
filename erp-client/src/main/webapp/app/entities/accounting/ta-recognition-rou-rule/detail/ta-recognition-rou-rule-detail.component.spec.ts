import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TARecognitionROURuleDetailComponent } from './ta-recognition-rou-rule-detail.component';

describe('TARecognitionROURule Management Detail Component', () => {
  let comp: TARecognitionROURuleDetailComponent;
  let fixture: ComponentFixture<TARecognitionROURuleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TARecognitionROURuleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tARecognitionROURule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TARecognitionROURuleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TARecognitionROURuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tARecognitionROURule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tARecognitionROURule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
