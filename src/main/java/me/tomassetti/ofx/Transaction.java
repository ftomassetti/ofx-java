package me.tomassetti.ofx;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Federico Tomassetti (http://tomassetti.me)
 */
public class Transaction {

    enum Type {
        WITHDRAWAL,
        DEPOSIT
    }

    private Type type;
    public BigDecimal amount;
    public String name;
    public String memo;
    public LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;

        Transaction that = (Transaction) o;

        if (type != that.type) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return !(fitid != null ? !fitid.equals(that.fitid) : that.fitid != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (fitid != null ? fitid.hashCode() : 0);
        return result;
    }

    public String fitid;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getFitid() {
        return fitid;
    }

    public void setFitid(String fitid) {
        this.fitid = fitid;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
