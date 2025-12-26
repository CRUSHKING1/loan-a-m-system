package com.loanapp.emi.dto;

import java.math.BigDecimal;


public class EmiPayRequestDto {

    private Long emiId;
    private Long userId;
    private BigDecimal amount;
	public Long getEmiId() {
		return emiId;
	}
	public void setEmiId(Long emiId) {
		this.emiId = emiId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
    
    

}