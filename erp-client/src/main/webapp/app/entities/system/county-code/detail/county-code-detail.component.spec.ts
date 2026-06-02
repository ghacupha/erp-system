import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountyCodeDetailComponent } from './county-code-detail.component';

describe('CountyCode Management Detail Component', () => {
  let comp: CountyCodeDetailComponent;
  let fixture: ComponentFixture<CountyCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CountyCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ countyCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CountyCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CountyCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load countyCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.countyCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
