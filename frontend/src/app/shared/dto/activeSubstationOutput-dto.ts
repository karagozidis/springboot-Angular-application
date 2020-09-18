import { ActiveSubstationInputHeaderDTO } from "./activeSubstationInputHeader-dto";
import { ActiveSubstationInputCommandsDTO } from "./activeSubstationInputCommands-dto";
import { ActiveSubstationInputDataFilesDTO } from "./activeSubstationInputDataFiles-dto";
import { ActiveSubstationInputOperationConstraintsDTO } from "./activeSubstationInputOperationConstraints-dto";
import { ActiveSubstationInputFlexibilityServiceScheduleDTO } from "./activeSubstationInputFlexibilityServiceSchedule-dto";
import {  ActiveSubstationInputMetadataDTO } from "./activeSubstationInputMetadata-dto";
import { ActiveSubstationOutputHeaderDTO } from "./activeSubstationOutputHeader-dto";
import { ActiveSubstationOutputCommandsDTO } from "./activeSubstationOutputCommands-dto";
import { ActiveSubstationOutputScheduleDTO } from "./activeSubstationOutputSchedule-dto";


export class ActiveSubstationInputDTO {
    constructor(
       public header: ActiveSubstationOutputHeaderDTO,
       public commands: ActiveSubstationOutputCommandsDTO,
       public schedule: ActiveSubstationOutputScheduleDTO, 
       public metadata: ActiveSubstationInputMetadataDTO
    ) {}    
  }