package com.joseneyra.beer.order.service.web.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BeerDto extends BaseItem {

    @Builder
    public BeerDto (UUID id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate,
                    String beerName, String beerStyle, BigDecimal price, UUID beerId) {
        super(id,version, createdDate, lastModifiedDate);
        this.beerId = beerId;
        this.beerName = beerName;
        this.beerStyle = beerStyle;
        this.price = price;
    }

    private UUID beerId;
    private String beerName;
    private String beerStyle;
    private BigDecimal price;
}
