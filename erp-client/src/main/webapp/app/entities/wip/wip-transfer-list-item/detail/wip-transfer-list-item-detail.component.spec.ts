import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WIPTransferListItemDetailComponent } from './wip-transfer-list-item-detail.component';

describe('WIPTransferListItem Management Detail Component', () => {
  let comp: WIPTransferListItemDetailComponent;
  let fixture: ComponentFixture<WIPTransferListItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WIPTransferListItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ wIPTransferListItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WIPTransferListItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WIPTransferListItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load wIPTransferListItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.wIPTransferListItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
