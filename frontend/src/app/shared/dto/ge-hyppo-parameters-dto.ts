import { ConfigurationParametersDTO } from "./configuration-parameters-dto";
import { MappingDTO } from "./mappings-dto";

export class GeHyppoParametersDTO {
    constructor(
       public name: string,
       public description: string,
       public configurationParameters: ConfigurationParametersDTO,
       public mappings: MappingDTO,
       public dataFileNames: [],
       public priceFileName: string,
       public usergroup: string
    ) {}
  }
