import countries.Country;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Item {
    String link;
    String baseCreator = "https://www.ikea.com/";
    TreeMap<Double, String> map = new TreeMap<>();
    ArrayList<String> notAvailable = new ArrayList<>();

    public void check(Country item, String name) {
        link = baseCreator + item.id + "/" + item.lang + "/p/-" + name;
        try {
            Document doc = Jsoup.connect(link).execute().parse();

            //Price
            Price local = new Price(doc, item);
            map.put(local.get(), item.countryName + local.toString());

            //Rating
            map.put(local.get(), map.get(local.price) + new Rating(doc));

        } catch (HttpStatusException e) {
            notAvailable.add(item.countryName);
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException " + e.getLocalizedMessage());
        } catch (Exception ignored) {}
    }

    public void writeResult() {
        for (Double price : map.keySet()) {
            System.out.println((int) Math.round(price) + " грн in " + map.get(price));
        }

        if (notAvailable.size() > 0) System.out.println("Product is not available in " + notAvailable.stream().map(Object::toString).collect(Collectors.joining(", ")));

    }
}
