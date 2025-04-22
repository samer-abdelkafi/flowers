import { Routes } from '@angular/router';
import {FlowListComponent} from './flows/flow-list/flow-list.component';
import {MessageListComponent} from './messages/message-list/message-list.component';

export const routes: Routes = [
  { path: 'flows', component: FlowListComponent },
  { path: 'messages', component: MessageListComponent },
  { path: '**', redirectTo: 'flows' }
];
