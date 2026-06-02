import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NetBookValueEntryDetailComponent } from './net-book-value-entry-detail.component';

describe('NetBookValueEntry Management Detail Component', () => {
  let comp: NetBookValueEntryDetailComponent;
  let fixture: ComponentFixture<NetBookValueEntryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NetBookValueEntryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ netBookValueEntry: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NetBookValueEntryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NetBookValueEntryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load netBookValueEntry on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.netBookValueEntry).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
