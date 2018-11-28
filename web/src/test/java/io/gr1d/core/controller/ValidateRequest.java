package io.gr1d.core.controller;

import io.gr1d.core.validation.CPFCNPJ;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ValidateRequest {

    @NotEmpty
    private String name;

    @CPFCNPJ
    private String documentNumber;

}
