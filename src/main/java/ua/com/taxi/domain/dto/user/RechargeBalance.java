package ua.com.taxi.domain.dto.user;

import javax.validation.constraints.Min;

public class RechargeBalance {

    @Min(1)
    private long amount;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RechargeBalance{" +
                "amount=" + amount +
                '}';
    }
}
