import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WIPListItemDetailComponent } from './wip-list-item-detail.component';

describe('WIPListItem Management Detail Component', () => {
  let comp: WIPListItemDetailComponent;
  let fixture: ComponentFixture<WIPListItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WIPListItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ wIPListItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WIPListItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WIPListItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load wIPListItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.wIPListItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
