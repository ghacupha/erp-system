import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrepaymentMarshallingDetailComponent } from './prepayment-marshalling-detail.component';

describe('PrepaymentMarshalling Management Detail Component', () => {
  let comp: PrepaymentMarshallingDetailComponent;
  let fixture: ComponentFixture<PrepaymentMarshallingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrepaymentMarshallingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prepaymentMarshalling: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrepaymentMarshallingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrepaymentMarshallingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prepaymentMarshalling on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prepaymentMarshalling).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
