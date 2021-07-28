import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ErpGatewayTestModule } from '../../../../test.module';
import { MockEventManager } from '../../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../../helpers/mock-active-modal.service';
import { FixedAssetAcquisitionDeleteDialogComponent } from 'app/entities/assets/fixed-asset-acquisition/fixed-asset-acquisition-delete-dialog.component';
import { FixedAssetAcquisitionService } from 'app/entities/assets/fixed-asset-acquisition/fixed-asset-acquisition.service';

describe('Component Tests', () => {
  describe('FixedAssetAcquisition Management Delete Component', () => {
    let comp: FixedAssetAcquisitionDeleteDialogComponent;
    let fixture: ComponentFixture<FixedAssetAcquisitionDeleteDialogComponent>;
    let service: FixedAssetAcquisitionService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [FixedAssetAcquisitionDeleteDialogComponent],
      })
        .overrideTemplate(FixedAssetAcquisitionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FixedAssetAcquisitionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FixedAssetAcquisitionService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
