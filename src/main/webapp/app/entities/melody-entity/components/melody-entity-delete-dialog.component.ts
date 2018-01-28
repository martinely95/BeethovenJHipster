import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MelodyEntity } from '../models/melody-entity.model';
import { MelodyEntityPopupService } from '../services/melody-entity-popup.service';
import { MelodyEntityService } from '../services/melody-entity.service';

@Component({
    selector: 'jhi-melody-entity-delete-dialog',
    templateUrl: './melody-entity-delete-dialog.component.html'
})
export class MelodyEntityDeleteDialogComponent {

    melodyEntity: MelodyEntity;

    constructor(
        private melodyEntityService: MelodyEntityService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.melodyEntityService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'melodyEntityListModification',
                content: 'Deleted an melodyEntity'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-melody-entity-delete-popup',
    template: ''
})
export class MelodyEntityDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private melodyEntityPopupService: MelodyEntityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.melodyEntityPopupService
                .open(MelodyEntityDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
