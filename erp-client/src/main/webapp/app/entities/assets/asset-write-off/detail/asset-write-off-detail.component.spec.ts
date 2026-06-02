import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssetWriteOffDetailComponent } from './asset-write-off-detail.component';

describe('AssetWriteOff Management Detail Component', () => {
  let comp: AssetWriteOffDetailComponent;
  let fixture: ComponentFixture<AssetWriteOffDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetWriteOffDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assetWriteOff: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetWriteOffDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetWriteOffDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assetWriteOff on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assetWriteOff).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
