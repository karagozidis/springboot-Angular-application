export class ConfigurationParametersDTO {
    constructor(
       public  gap_in_days: string,
       public filter_with_grid: boolean,
       public  filter_with_flags: boolean,
       public  check_ranges: boolean,
       public  make_data_charts: boolean,
       public  baseload: string,
       public  primary_reserve: string,
       public  detect_parameters: boolean,
       public  data_time_interval_sec: string,
       public  use_price_data: boolean,
       public  alternative_fixed_price: string,
       public  reserve_MW_price: string,
       public  auto_size_bat: boolean,
       public  battery_power_MW: string,
       public  battery_energy_MWH: string,
       public  gas_price_mwh: string,
       public  gas_LHV: string,
       public  battery_eff: string,
       public  limit_PFRLDCMD_signal: boolean,
       public  search_metadata: boolean,
       public  osm_or_ext_data: string,
       public  rename_output: boolean,
       public  batt_percent_to_pr: string,
       public  batt_power_limit_to_pr: string,
       public  batt_for_black_start: boolean,
       public  batt_price_MWh: string,
       public  batt_price_MW: string,
       public  converter_price_MVA: string,
       public  export_results_tocsv: boolean,
       public limit_power_augm: string,
       public chart_output_listoflist: string,
       public super_fast_fr: boolean,
       public grid_deadband_hz: string,
       public grid_freq_abs_range: string,
       public sffr_power_size_MW: string
    ) {}
  }