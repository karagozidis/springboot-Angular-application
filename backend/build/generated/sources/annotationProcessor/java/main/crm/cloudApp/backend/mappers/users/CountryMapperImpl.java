package crm.cloudApp.backend.mappers.users;

import crm.cloudApp.backend.dto.users.CountryDTO;
import crm.cloudApp.backend.models.users.Country;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:13+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class CountryMapperImpl extends CountryMapper {

    @Override
    public Country map(CountryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Country country = new Country();

        if ( dto.getCreatedOn() != null ) {
            country.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            country.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            country.setName( dto.getName() );
        }

        return country;
    }

    @Override
    public CountryDTO map(Country entity) {
        if ( entity == null ) {
            return null;
        }

        CountryDTO countryDTO = new CountryDTO();

        if ( entity.getId() != null ) {
            countryDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            countryDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            countryDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getName() != null ) {
            countryDTO.setName( entity.getName() );
        }

        return countryDTO;
    }

    @Override
    public void map(CountryDTO dto, Country entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreatedOn() != null ) {
            entity.setCreatedOn( dto.getCreatedOn() );
        }
        else {
            entity.setCreatedOn( null );
        }
        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        else {
            entity.setId( null );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        else {
            entity.setName( null );
        }
    }
}
