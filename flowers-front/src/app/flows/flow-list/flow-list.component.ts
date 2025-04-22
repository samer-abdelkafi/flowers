import { Component } from '@angular/core';

import {MATERIAL_IMPORTS} from '../../shared/material/material.imports';
import {CdkTableDataSourceInput} from '@angular/cdk/table';
import {DatePipe} from '@angular/common';
import {MatTableDataSource} from '@angular/material/table';
import {PageEvent} from '@angular/material/paginator';






@Component({
  selector: 'app-flow-list',
  imports: [
    MATERIAL_IMPORTS,
    DatePipe
  ],
  templateUrl: './flow-list.component.html',
  styleUrl: './flow-list.component.scss'
})
export class FlowListComponent {
  dataSource: CdkTableDataSourceInput<any> =new MatTableDataSource<any>();
  displayedColumns: Iterable<string> = [];
  totalElements: unknown;
  pageSize: unknown;

  refresh() {

  }

  openMessageDialog(content: string | DocumentFragment |  undefined ) {

  }

  onPageChange($event: PageEvent) {

  }
}
