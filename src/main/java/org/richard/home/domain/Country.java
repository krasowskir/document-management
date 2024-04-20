package org.richard.home.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

@Embeddable
public enum Country {
    GERMANY("GERMANY"), AUSTRIA("AUSTRIA"), SWISS("SWISS"), FRANCE("FRANCE"),
    POLAND("POLAND"), ENGLAND("England"), GHANA("GHANA"), SPAIN("SPAIN"), LATVIA("LATVIA"),
    SAUDI_ARABIA("SAUDI_ARABIA"), SLOVAKIA("Slovakia"), SWEDEN("Sweden"), CZECH_REPUBLIC("Czech Republic"),
    NORWAY("NORWAY"), CROATIA("Croatia"), TOGO("Togo"), BOSNIA_AND_HERZEGOVINA("Bosnia and Herzegovina"),
    SERBIA("Serbia"), NETHERLANDS("Netherlands"), DENMARK("Denmark"), ARMENIA("Armenia"), ISRAEL("Israel"),
    MALI("Mali"), UNITED_STATES("United States"), CHILE("Chile"), ARGENTINA("Argentina"), GREECE("Greece"),
    BRAZIL("Brazil"), RUSSIA("Russia"), FINLAND("Finland"), BURKINA_FASO("Burkina Faso"), ECUADOR("Ecuador"),
    SWITZERLAND("Switzerland"), SYRIA("Syria"), BELGIUM("Belgium"), CAMEROON("Cameroon"), ICELAND("Iceland"),
    JAPAN("Japan"), NORTH_MACEDONIA("North Macedonia"), KOSOVO("Kosovo"), GAMBIA("Gambia"), MONTENEGRO("MONTENEGRO"),
    ALGERIA("ALGERIA"), UZBEKISTAN("UZBEKISTAN"), EGYPT("EGYPT"), CONGO_DR("Congo DR"), GUINEA("GUINEA"),
    SIERRA_LEONE("Sierra Leone"), COTE_DE_IVOIRE("Côte d’Ivoire"), ITALY("Italy"), BULGARIA("Bulgaria"), LITHUANIA("Lithuania"),
    UKRAINE("UKRAINE"), HUNGARY("Hungary"), KOREA_REPUBLIC("Korea Republic"), LUXEMBOURG("Luxembourg"), VENEZUELA("Venezuela"),
    IRAN("IRAN "), SLOVENIA("SLOVENIA"), PORTUGAL("PORTUGAL"), SENEGAL("SENEGAL"), AUSTRALIA("AUSTRALIA "),
    NIGERIA("NIGERIA"), CURAÇAO("CURAÇAO"), ANGOLA("ANGOLA"), KAZAKHSTAN("KAZAKHSTAN"), ROMANIA("ROMANIA"),
    MOROCCO("MOROCCO"), ALBANIA("ALBANIA"), ST_LUCIA("ST._LUCIA"), DOMINICAN_REPUBLIC("DOMINICAN_REPUBLIC"),
    COSTA_RICA("Costa Rica"), UGANDA("Uganda"), GEORGIA("GEORGIA"), PANAMA("PANAMA"), TURKEY("TURKEY"),
    MOZAMBIQUE("Mozambique"), NEW_ZEALAND("New Zealand"), CANADA("Canada"), SUDAN("Sudan"), WALES("WALES"),
    NORTHERN_IRELAND("NORTHERN_IRELAND"), COLOMBIA("COLOMBIA"), REPUBLIC_OF_IRELAND("REPUBLIC_OF_IRELAND"), SCOTLAND("Scotland"),
    JAMAICA("JAMAICA"), URUGUAY("URUGUAY"), PARAGUAY("PARAGUAY");

    @Transient
    private final String name;

    Country(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.name;
    }
}
