package me.tomassetti.ofx;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Federico Tomassetti (http://tomassetti.me)
 */
public class OfxParser {

    public List<Transaction> parse(File file) throws IOException {
        return parse(new FileInputStream(file));
    }

    private class Collector {
        private List<String> openTags = new LinkedList<>();
        private List<Transaction> transactions = new LinkedList<>();

        public void end() {

        }

        public void openTag(String tag) {
            openTags.add(tag);
            if (tag.equals("STMTTRN")) {
                transactions.add(new Transaction());
            }
        }

        public void closeTag(String tag) {
            // when we close a tag we could implicitly close a bunch of
            // tags in between.
            // For example consider: <a><b><c><d></a>
            // when I encounter "</a>" it tells me that all the tags inside "a" should be considered
            // to be closed
            while(!lastOpenTag().equals(tag)){
                closeTag(lastOpenTag());
            }
            // remove the last one
            openTags.remove(openTags.size() - 1);
        }

        public void text(String text) {
            if (lastOpenTag().equals("TRNAMT")) {
                if (text.startsWith("-")) {
                    lastTransaction().setType(Transaction.Type.WITHDRAWAL);
                    lastTransaction().setAmount(new BigDecimal(text.substring(1)));
                } else if (text.startsWith("+")){
                    lastTransaction().setType(Transaction.Type.DEPOSIT);
                    lastTransaction().setAmount(new BigDecimal(text.substring(1)));
                } else {
                    throw new UnsupportedOperationException();
                }
            } else if (lastOpenTag().equals("NAME")) {
                lastTransaction().setName(text);
            } else if (lastOpenTag().equals("MEMO")) {
                lastTransaction().setMemo(text);
            } else if (lastOpenTag().equals("DTPOSTED")) {
                lastTransaction().setDate(LocalDate.parse(text, DateTimeFormatter.BASIC_ISO_DATE));
            } else if (lastOpenTag().equals("FITID")) {
                lastTransaction().setFitid(text);
            }
        }

        private String lastOpenTag() {
            if (openTags.size() == 0) {
                return "";
            } else {
                return openTags.get(openTags.size() - 1);
            }
        }

        private Transaction lastTransaction() {
            if (transactions.size() == 0) {
                return null;
            } else {
                return transactions.get(transactions.size() - 1);
            }
        }
    }

    private boolean startsWith(String content, int position, String s) {
        return content.length() >= position + s.length() && content.substring(position, position + s.length()).equals(s);
    }

    private void processInput(String content, int position, Collector collector) {
        if (content.length() == position) {
            collector.end();
        } else if (startsWith(content, position, "</")) {
            int close = content.indexOf(">", position);
            collector.closeTag(content.substring(position + 2, close));
            processInput(content, close + 1, collector);
        } else if (startsWith(content, position, "<")) {
            int close = content.indexOf(">", position);
            collector.openTag(content.substring(position + 1, close));
            processInput(content, close + 1, collector);
        } else {
            int next = content.indexOf("<",position);
            if(next==-1){
                next=content.length();
            }
            String text=content.substring(position,next).trim();
            if (!text.isEmpty()){
                collector.text(text);
            }
            processInput(content, next, collector);
        }
    }

    public List<Transaction> parse(InputStream is) throws IOException {
        String content = readAll(is);
        Collector collector = new Collector();
        processInput(content, 0, collector);
        return collector.transactions;
    }

    private String readAll(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        try{
            while((line=reader.readLine())!=null){
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            return stringBuilder.toString();
        }finally{
            reader.close();
        }
    }

}
