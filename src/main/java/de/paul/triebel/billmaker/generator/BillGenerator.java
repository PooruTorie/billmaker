package de.paul.triebel.billmaker.generator;

import com.aspose.words.Document;
import com.aspose.words.NodeType;
import com.aspose.words.Row;
import com.aspose.words.Table;

import java.io.FileInputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;

public class BillGenerator {

    public static Document generate(BillData data, String templateFile) throws Exception {
        Document doc = new Document(new FileInputStream(templateFile));

        NumberFormat priceFormatter = NumberFormat.getCurrencyInstance();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

        doc.getRange().replace("%NAME%", data.getName());
        doc.getRange().replace("%NUMBER%", data.getCustomerNumber());
        doc.getRange().replace("%BILL%", data.getBillNumber());
        doc.getRange().replace("%TAX%", data.getTaxNumber());
        doc.getRange().replace("%STREET%", data.getStreet());
        doc.getRange().replace("%CITY%", data.getCity());
        doc.getRange().replace("%COUNTRY%", data.getCountry());
        doc.getRange().replace("%PLZ%", data.getPlz());
        doc.getRange().replace("%DATE%", data.getDate().format(dateFormatter));
        doc.getRange().replace("%START%", data.getStart().format(dateFormatter));
        doc.getRange().replace("%END%", data.getEnd().format(dateFormatter));
        doc.getRange().replace("%ENDDATE%", data.getBillEnd().format(dateFormatter));
        doc.getRange().replace("%SUM%", priceFormatter.format(data.getSum()));

        for (Object tableObject : doc.getChildNodes(NodeType.TABLE, true)) {
            Table table = (Table) tableObject;
            Row templateRow = table.getRows().get(1);
            table.getRows().removeAt(1);
            ArrayList<BillElementData> reversedElements = (ArrayList<BillElementData>) data.getBillElements().clone();
            Collections.reverse(reversedElements);
            for (BillElementData billElement : reversedElements) {
                Row row = (Row) templateRow.deepClone(true);
                row.getRange().replace("%TITLE%", billElement.getTitle());
                row.getRange().replace("%TEXT%", billElement.getDescription());
                row.getRange().replace("%SINGLEPRICE%", priceFormatter.format(billElement.getPrice()));
                row.getRange().replace("%COUNT%", Integer.toString(billElement.getCount()));
                row.getRange().replace("%PRICE%", priceFormatter.format(billElement.getSum()));
                table.getRows().insert(1, row);
            }
        }
        return doc;
    }

}
