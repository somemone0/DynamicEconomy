package com.somemone.dynamiceeconomy.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@DatabaseTable(tableName = "marketpositions")
public class MarketPosition { // A structure of a market's sales/price on a certain day in a single item

    public static final String ID_COLUMN_NAME = "id";
    public static final String ITEM_COLUMN_NAME = "item";
    public static final String PRICE_COLUMN_NAME = "price";
    public static final String DATETIME_COLUMN_NAME = "date";
    public static final String AHS_COLUMN_NAME = "sales";
    public static final String DEMAND_SLOPE_COLUMN_NAME = "slope";

    @Setter
    @Getter
    @DatabaseField(generatedId = true, columnName = ID_COLUMN_NAME)
    private int id;

    @Setter
    @Getter
    @DatabaseField(columnName = ITEM_COLUMN_NAME)
    private String item;

    @Setter
    @Getter
    @DatabaseField(columnName = PRICE_COLUMN_NAME)
    private float price;

    @Setter
    @Getter
    @DatabaseField(columnName = DATETIME_COLUMN_NAME)
    private LocalDateTime datetime;

    @Setter
    @Getter
    @DatabaseField(columnName = AHS_COLUMN_NAME)
    private float ahs;

    @Setter
    @Getter
    @DatabaseField(columnName = DEMAND_SLOPE_COLUMN_NAME)
    private float slope;

    MarketPosition() {

    }

    public MarketPosition(String item, float price, float ahs, LocalDateTime datetime, float slope) {
        this.item = item;
        this.price = price;
        this.ahs = ahs;
        this.datetime = datetime;
        this.slope = slope;
    }

}