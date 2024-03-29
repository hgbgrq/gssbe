package com.hgb.gssbe.common.constance;

public enum Excel {

    ORGANIZATION(7 - 1, 3 - 1), ORDERING_DATE(9 - 1, 3 - 1), DEAD_LINE_DATE(10 - 1, 3 - 1), STYLE_NO(1 - 1, 1 - 1), ITEM(1 - 1, 3 - 1), SIZE(1 - 1, 7 - 1), COLOR(1 - 1, 11 - 1), QTY(1 - 1, 12 - 1), ETC(1 - 1, 13 - 1), PRD_START_ROW(12 - 1, 1 - 1), PRD_END_ROW(23 - 1, 1 - 1), QTY_SUM(1 - 1, 12 - 1);

    private final int row;
    private final int cell;

    Excel(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    public int getRow() {
        return row;
    }

    public int getCell() {
        return cell;
    }
}
