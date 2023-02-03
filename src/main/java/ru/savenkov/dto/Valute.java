package ru.savenkov.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.jetbrains.annotations.NotNull;
import ru.savenkov.serialization.DoubleDeserializer;

@SuppressWarnings("NotNullFieldNotInitialized")
public class Valute {

    @JacksonXmlProperty(localName = "ID")
    @NotNull
    private String id;

    @JacksonXmlProperty(localName = "NumCode")
    @NotNull
    private Integer numCode;

    @JacksonXmlProperty(localName = "CharCode")
    @NotNull
    private String charCode;

    @JacksonXmlProperty(localName = "Nominal")
    @NotNull
    private Integer nominal;

    @JacksonXmlProperty(localName = "Name")
    @NotNull
    private String name;

    @JacksonXmlProperty(localName = "Value")
    @JsonDeserialize(using = DoubleDeserializer.class) //Cannot deserialize value of type `java.lang.Double` from String "39,5619": not a valid `Double` value
    @NotNull
    private Double value;

    public Valute() {
    }

    public Valute(@NotNull String id,
                  @NotNull Integer numCode,
                  @NotNull String charCode,
                  @NotNull Integer nominal,
                  @NotNull String name,
                  @NotNull Double value) {
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }

    public @NotNull String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public @NotNull Integer getNumCode() {
        return numCode;
    }

    public void setNumCode(@NotNull Integer numCode) {
        this.numCode = numCode;
    }

    public @NotNull String getCharCode() {
        return charCode;
    }

    public void setCharCode(@NotNull String charCode) {
        this.charCode = charCode;
    }

    public @NotNull Integer getNominal() {
        return nominal;
    }

    public void setNominal(@NotNull Integer nominal) {
        this.nominal = nominal;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull Double getValue() {
        return value;
    }

    public void setValue(@NotNull Double value) {
        this.value = value;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Valute{");
        sb.append("id='").append(id).append('\'');
        sb.append(", numCode=").append(numCode);
        sb.append(", charCode='").append(charCode).append('\'');
        sb.append(", nominal=").append(nominal);
        sb.append(", name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
