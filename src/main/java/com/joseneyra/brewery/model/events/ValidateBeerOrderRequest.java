package com.joseneyra.brewery.model.events;

import com.joseneyra.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateBeerOrderRequest implements Serializable{

    static final long serialVersionUID = 2866091711293999668L;

    private BeerOrderDto beerOrderDto;
}
