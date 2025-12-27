package com.loanapp.kyc.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

public class KycNotFoundException extends Exception {

	public KycNotFoundException() {
		super();
		
	}

	public KycNotFoundException(String message) {
		super(message);
		
	}

}
