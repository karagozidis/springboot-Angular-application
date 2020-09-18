import {Component, OnInit, ViewChild, ElementRef} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MarketScenarioService} from '../../services/market-scenario.service';
import {MatTableDataSource} from '@angular/material/table';

import {NotificationToastService} from '../../shared/utils/notification-toast-service';
import {MarketScenarioDTO} from '../../shared/dto/market-scenario-dto';
import { MatChipInputEvent, MatAutocompleteSelectedEvent, MatAutocomplete } from '@angular/material';
import { ENTER, COMMA } from '@angular/cdk/keycodes';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, map } from 'rxjs/operators';
import { AppConstants, AppType } from 'app/shared/config/app-constants';
import { AppDataService } from 'app/services/app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';

@Component({
  selector: 'app-market-scenario',
  templateUrl: './market-scenario.component.html',
  styleUrls: ['./market-scenario.component.scss']
})
export class MarketScenarioComponent implements OnInit {

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  dataSource;
  displayedColumns = ['createdOn', 'createdBy', 'scenario'];
  totalElements: any;
  public marketScenarioDTO: MarketScenarioDTO;
  visible = true;
  selectable = true;
  removable = true;
  separatorKeysCodes: number[] = [ENTER, COMMA];
  orderTypesCtrl = new FormControl();
  filteredorderTypes: Observable<string[]>;
  orderTypeNames = AppConstants.Market.BackendEnums.OrderType;
  productsTags = AppConstants.Market.BackendEnums.MarketScenarios;
  orderTypes: string[] = [this.orderTypeNames.LIMIT];
  allorderTypes: string[] = [this.orderTypeNames.LIMIT,this.orderTypeNames.ICEBERG,this.orderTypeNames.IOC,this.orderTypeNames.FOK,this.orderTypeNames.AON,this.orderTypeNames.BASKET, this.productsTags.ONE_HOUR_PRODUCT, this.productsTags.FIFTEEN_MINUTES_PRODUCT];

  @ViewChild('orderTypesInput', null) orderTypesInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto',null) matAutocomplete: MatAutocomplete;

  constructor(private marketScenarioService: MarketScenarioService,
              private appDataService: AppDataService,
              private notificationToastService: NotificationToastService) {

              this.filteredorderTypes = this.orderTypesCtrl.valueChanges.pipe(
                startWith(null),
                map((orderType: string | null) => orderType ? this._filter(orderType) : this.allorderTypes.slice()));
  }

  ngOnInit() {
    this.appDataService.setAppType(AppType.MARKET)
    this.paginator.pageSize = 50;
     this.sort.active = 'createdOn';
     this.sort.direction = 'desc';
    this.refresh();
    this.clearMarketScenarioDTO();
    this.readActiveScenario();

  }

  clearMarketScenarioDTO() {
    this.marketScenarioDTO = new MarketScenarioDTO();
    this.marketScenarioDTO.scenario = '';
  }
  readActiveScenario() {
    this.marketScenarioService.getFirst().subscribe((data: any) => {
    this.marketScenarioDTO = data;
    });
  }


  refresh() {
    let sort = 'createdOn';
    let direction = 'desc';
    if (this.sort.active !== undefined) {
      this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
      sort = this.sort.active;
      direction = this.sort.direction;
    }
    let pageSize = 5;
    if (this.paginator.pageSize !== undefined) {
      pageSize = this.paginator.pageSize;
    }

    this.marketScenarioService.getAllPageable(this.paginator.pageIndex, pageSize, sort,
        direction).subscribe((data: any) => {
      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.sort = this.sort;
      this.dataSource.data = data.content;
      this.totalElements = data.totalElements;
    });
  }




  save(){
    
    
    this.marketScenarioDTO.scenario = this.orderTypes.toString()
    console.log(this.marketScenarioDTO)
    if (this.marketScenarioDTO.scenario === '' ||
        this.marketScenarioDTO.scenario === undefined
    ) {

      this.notificationToastService.showNotification(
          '<h4>Warning</h4>Scenario field must not be empty.',
          'info',
          'top',
          'center',
          'danger');
      return;

    }

    this.marketScenarioService.save(this.marketScenarioDTO).subscribe(data => {
      this.refresh();
      this.notificationToastService.showNotification(
         '<h4>Success</h4>Scenario was saved.',
         'info',
         'top',
         'center',
         'success');

    });
  }
  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    // Add our fruit
    if ((value || '').trim()) {
      this.orderTypes.push(value.trim());
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }

    this.orderTypesCtrl.setValue(null);
  }

  remove(orderType: string): void {
    const index = this.orderTypes.indexOf(orderType);

    if (index >= 0) {
      this.orderTypes.splice(index, 1);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.orderTypes.push(event.option.viewValue);
    this.orderTypesInput.nativeElement.value = '';
    this.orderTypesCtrl.setValue(null);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.allorderTypes.filter(orderType => orderType.toLowerCase().indexOf(filterValue) === 0);
  }
  focusOrderTypesInput(){
    this.orderTypesInput.nativeElement.blur();
    this.orderTypesInput.nativeElement.focus();
  }



}
