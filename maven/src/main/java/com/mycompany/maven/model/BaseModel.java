package com.mycompany.maven.model;

import java.io.Serializable;

public interface BaseModel extends Serializable {

    public static final String EMAIL_REGEX = ".+@.+\\.[a-z]+";
    public static final String CPF_REGEX = "^(\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2})*$";
    public static final String CNPJ_REGEX = "^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$";
    public static final String PHONE_REGEX = "^\\([0-9]{2}\\) [3-9][0-9]{3}-[0-9]{4,5}$";
    public static final String DATE_REGEX = "^(((0[1-9]|[12][0-9]|3[01])([/])(0[13578]|10|12)([/])(\\d{4}))|" +
                                            "(([0][1-9]|[12][0-9]|30)([/])(0[469]|11)([/])(\\d{4}))|" +
                                            "((0[1-9]|1[0-9]|2[0-8])([/])(02)([/])(\\d{4}))|((29)(\\.|-|\\/)(02)([/])([02468][048]00))|" +
                                            "((29)([/])(02)([/])([13579][26]00))|" +
                                            "((29)([/])(02)([/])([0-9][0-9][0][48]))|" +
                                            "((29)([/])(02)([/])([0-9][0-9][2468][048]))|" +
                                            "((29)([/])(02)([/])([0-9][0-9][13579][26])))*$";

    Integer getId();
}
