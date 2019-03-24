package ee.moog.mugloar.model;

import java.util.ArrayList;
import java.util.HashSet;

public class AdList extends ArrayList<AdMessage> {
    private static HashSet<String> collection = new HashSet<>();

    public boolean allEncrypted() {
        for(AdMessage ad : this) {
            if(!ad.encrypted) {
                return false;
            }
        }
        return true;
    }

    public void collectProbabilities() {
        for(AdMessage ad : this) {
            collection.add(ad.probability);
        }
    }

    public static HashSet<String> getCollection() {
        return collection;
    }
}
