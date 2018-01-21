import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BeethovenSharedModule } from '../../shared';
import {
    MelodyEntityService,
    MelodyEntityPopupService,
    MelodyEntityComponent,
    MelodyEntityDetailComponent,
    MelodyEntityDialogComponent,
    MelodyEntityPopupComponent,
    MelodyEntityDeletePopupComponent,
    MelodyEntityDeleteDialogComponent,
    melodyEntityRoute,
    melodyEntityPopupRoute,
    MelodyEntityResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...melodyEntityRoute,
    ...melodyEntityPopupRoute,
];

@NgModule({
    imports: [
        BeethovenSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MelodyEntityComponent,
        MelodyEntityDetailComponent,
        MelodyEntityDialogComponent,
        MelodyEntityDeleteDialogComponent,
        MelodyEntityPopupComponent,
        MelodyEntityDeletePopupComponent,
    ],
    entryComponents: [
        MelodyEntityComponent,
        MelodyEntityDialogComponent,
        MelodyEntityPopupComponent,
        MelodyEntityDeleteDialogComponent,
        MelodyEntityDeletePopupComponent,
    ],
    providers: [
        MelodyEntityService,
        MelodyEntityPopupService,
        MelodyEntityResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BeethovenMelodyEntityModule {}
