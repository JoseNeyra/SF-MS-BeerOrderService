package com.joseneyra.beer.order.service.web.mappers;


import com.joseneyra.beer.order.service.domain.Customer;
import com.joseneyra.brewery.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface CustomerMapper {
    CustomerDto customerToDto(Customer customer);

    Customer dtoToCustomer(CustomerDto customerDto);
}
