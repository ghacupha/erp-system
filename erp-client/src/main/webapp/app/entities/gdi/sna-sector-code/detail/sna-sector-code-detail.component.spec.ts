import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SnaSectorCodeDetailComponent } from './sna-sector-code-detail.component';

describe('SnaSectorCode Management Detail Component', () => {
  let comp: SnaSectorCodeDetailComponent;
  let fixture: ComponentFixture<SnaSectorCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SnaSectorCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ snaSectorCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SnaSectorCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SnaSectorCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load snaSectorCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.snaSectorCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
