import { BaseEntity } from '../../../shared/index';

export class MelodyEntity implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public content?: string,
        public createdDateTime?: any,
        public profileId?: number,
    ) {
    }
}
