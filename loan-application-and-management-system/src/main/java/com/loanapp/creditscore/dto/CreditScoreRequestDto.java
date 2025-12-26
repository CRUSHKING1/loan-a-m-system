package com.loanapp.creditscore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreditScoreRequestDto {
    
	    @NotNull(message = "PAN Number is required.")
	    @Size(min = 10, max = 10, message = "PAN Number must be exactly 10 digits.")
	    @Pattern(regexp = "^[A-Z0-9]*$", message = "PAN Number must be alphanumeric.")
       private String panNumber;

   
}
