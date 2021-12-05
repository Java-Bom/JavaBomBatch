package com.javabom.definitiveguide.chap8;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Customer {

    @Size(max = 10)
    private String firstName;

    @NotBlank
    private String lastName;
}
