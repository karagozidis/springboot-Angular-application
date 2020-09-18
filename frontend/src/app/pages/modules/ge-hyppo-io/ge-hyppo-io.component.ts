import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'ge-hyppo-io',
  templateUrl: './ge-hyppo-io.component.html',
  styleUrls: ['./ge-hyppo-io.component.scss']
})
export class GEHyppoIOComponent implements OnInit {
  tabIndex = 0 ;

  changeTab(event){
    this.tabIndex = event.index;
  }

  constructor() { }

  ngOnInit() {
  }

}
