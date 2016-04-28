package me.tomassetti.ofx;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author Federico Tomassetti (http://tomassetti.me)
 */
public class OfxParserTest {

    @Test
    public void testNumberOfTransactions() throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/example.ofx");
        assertEquals(10, new OfxParser().parse(is).size());
    }

    @Test
    public void testTransactionTypes() throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/example.ofx");
        assertEquals(Arrays.asList(
                Transaction.Type.DEPOSIT,
                Transaction.Type.WITHDRAWAL,
                Transaction.Type.WITHDRAWAL,
                Transaction.Type.WITHDRAWAL,
                Transaction.Type.DEPOSIT,
                Transaction.Type.WITHDRAWAL,
                Transaction.Type.WITHDRAWAL,
                Transaction.Type.WITHDRAWAL,
                Transaction.Type.WITHDRAWAL,
                Transaction.Type.WITHDRAWAL),
                new OfxParser().parse(is).stream().map(t -> t.getType()).collect(Collectors.toList()));
    }

    @Test
    public void testTransactionAmounts() throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/example.ofx");
        assertEquals(Arrays.asList(
                new BigDecimal("4000.00"),
                new BigDecimal("31.20"),
                new BigDecimal("4.92"),
                new BigDecimal("20.98"),
                new BigDecimal("7000.00"),
                new BigDecimal("195.00"),
                new BigDecimal("3500.00"),
                new BigDecimal("20.50"),
                new BigDecimal("177.00"),
                new BigDecimal("42.00")),
                new OfxParser().parse(is).stream().map(t -> t.getAmount()).collect(Collectors.toList()));
    }

    @Test
    public void testTransactionDates() throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/example.ofx");
        assertEquals(Arrays.asList(
                LocalDate.of(2016, 4, 13),
                LocalDate.of(2016, 4, 11),
                LocalDate.of(2016, 4, 4),
                LocalDate.of(2016, 4, 4),
                LocalDate.of(2016, 4, 2),
                LocalDate.of(2016, 4, 1),
                LocalDate.of(2016, 3, 25),
                LocalDate.of(2016, 3, 24),
                LocalDate.of(2016, 3, 23),
                LocalDate.of(2016, 3, 23)),
                new OfxParser().parse(is).stream().map(t -> t.getDate()).collect(Collectors.toList()));
    }

    @Test
    public void testTransactionNames() throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/example.ofx");
        assertEquals(Arrays.asList(
                "A client",
                "A Book publisher   10/04",
                "GOOGLE *SVCSAPPS TOMA CCàG 01/04",
                "PRELEVMNT",
                "Some other client",
                "PRELEVMNT",
                "Sending money here and there",
                "Pizzeria",
                "SNCF INTERNET",
                "SNCF INTERNET"),
                new OfxParser().parse(is).stream().map(t -> t.getName()).collect(Collectors.toList()));
    }

    @Test
    public void testTransactionDescriptions() throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/example.ofx");
        assertEquals(Arrays.asList(
                "VIREMENT EN VOTRE FAVEUR",
                "PAIEMENT PAR CARTE",
                "PAIEMENT PAR CARTE",
                "Bouygues Telecom",
                "VIREMENT EN VOTRE FAVEUR",
                "APLRL",
                "Blah blah blah",
                "PAIEMENT PAR CARTE",
                "PAIEMENT PAR CARTE",
                "PAIEMENT PAR CARTE"),
                new OfxParser().parse(is).stream().map(t -> t.getMemo()).collect(Collectors.toList()));
    }

    @Test
    public void testTransactionFitids() throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/example.ofx");
        assertEquals(Arrays.asList(
                "9947030000068",
                "9944290089129",
                "9936170085272",
                "9936230090261",
                "9947030000068",
                "9934320105735",
                "9904660684216",
                "9926100027461",
                "9924570028048",
                "9924570028049"),
                new OfxParser().parse(is).stream().map(t -> t.getFitid()).collect(Collectors.toList()));
    }

}
