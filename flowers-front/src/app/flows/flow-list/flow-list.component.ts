import {Component, OnInit} from '@angular/core';

import {MATERIAL_IMPORTS} from '../../shared/material/material.imports';
import {MatTableDataSource} from '@angular/material/table';
import {PageEvent} from '@angular/material/paginator';
import {FlowService} from '../services/flow.service';
import {Flow} from '../model/flow';
import {Router} from '@angular/router';


@Component({
  selector: 'app-flow-list',
  imports: [
    MATERIAL_IMPORTS
  ],
  templateUrl: './flow-list.component.html',
  styleUrl: './flow-list.component.scss'
})
export class FlowListComponent implements OnInit {

  dataSource: MatTableDataSource<Flow> = new MatTableDataSource<Flow>();
  displayedColumns: Iterable<string> = ['id', 'name', 'publishers', 'subscribers', 'status'];
  totalElements: number = 0;
  pageSize: number = 10;
  currentPage: number = 0;


  constructor(private flowService: FlowService, private router: Router) {
  }


  ngOnInit(): void {
    this.load(0, this.pageSize, 'id');
  }


  load(page: number, size: number, sortBy: string): void {
    this.flowService.get().subscribe(
      data => {
        this.dataSource.data = data;
        this.totalElements = data.length;
      }
    );
  }

  refresh() {
    this.load(this.currentPage, this.pageSize, 'id');
  }

  openMessageDialog(content: string | DocumentFragment |  undefined ) {

  }

  onPageChange(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.load(this.currentPage, this.pageSize, 'id');
  }

  editFlow(id: string): void {
    this.router.navigate(['/flows/edit', id]);
  }



}
