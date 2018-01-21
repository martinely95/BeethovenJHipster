import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MelodyEntity } from './melody-entity.model';
import { MelodyEntityPopupService } from './melody-entity-popup.service';
import { MelodyEntityService } from './melody-entity.service';
import { Profile, ProfileService } from '../profile';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-melody-entity-dialog',
    templateUrl: './melody-entity-dialog.component.html'
})
export class MelodyEntityDialogComponent implements OnInit {

    melodyEntity: MelodyEntity;
    isSaving: boolean;

    profiles: Profile[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private melodyEntityService: MelodyEntityService,
        private profileService: ProfileService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.profileService.query()
            .subscribe((res: ResponseWrapper) => { this.profiles = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.melodyEntity.id !== undefined) {
            this.subscribeToSaveResponse(
                this.melodyEntityService.update(this.melodyEntity));
        } else {
            this.subscribeToSaveResponse(
                this.melodyEntityService.create(this.melodyEntity));
        }
    }

    private subscribeToSaveResponse(result: Observable<MelodyEntity>) {
        result.subscribe((res: MelodyEntity) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: MelodyEntity) {
        this.eventManager.broadcast({ name: 'melodyEntityListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProfileById(index: number, item: Profile) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-melody-entity-popup',
    template: ''
})
export class MelodyEntityPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private melodyEntityPopupService: MelodyEntityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.melodyEntityPopupService
                    .open(MelodyEntityDialogComponent as Component, params['id']);
            } else {
                this.melodyEntityPopupService
                    .open(MelodyEntityDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
