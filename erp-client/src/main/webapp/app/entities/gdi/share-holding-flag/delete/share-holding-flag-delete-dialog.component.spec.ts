jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ShareHoldingFlagService } from '../service/share-holding-flag.service';

import { ShareHoldingFlagDeleteDialogComponent } from './share-holding-flag-delete-dialog.component';

describe('ShareHoldingFlag Management Delete Component', () => {
  let comp: ShareHoldingFlagDeleteDialogComponent;
  let fixture: ComponentFixture<ShareHoldingFlagDeleteDialogComponent>;
  let service: ShareHoldingFlagService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ShareHoldingFlagDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ShareHoldingFlagDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ShareHoldingFlagDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ShareHoldingFlagService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
