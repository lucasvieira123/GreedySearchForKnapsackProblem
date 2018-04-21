package custom;

import java.util.*;

public class SortHelp {
    boolean isAscendingOrder;
    Map<Integer, Float> relativeCostsMap = new HashMap<>();

    public SortHelp(boolean ascendingOrder, Map<Integer, Float> relativeCostsMap) {
        this.relativeCostsMap = relativeCostsMap;
        this.isAscendingOrder = ascendingOrder;

    }

    public ArrayList<Map.Entry<Integer, Float>> getOrdenedList() {

        if(isAscendingOrder){
            return  (ArrayList<Map.Entry<Integer, Float>>) buildAscendingOrderList();
        }else {
            return   (ArrayList<Map.Entry<Integer, Float>>) buildReverseOrderList();
        }



    }



    private List<Map.Entry<Integer,Float>> buildReverseOrderList() {
        List<Map.Entry<Integer,Float>> sortedEntries = new ArrayList<>(relativeCostsMap.entrySet());

        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        return sortedEntries;

    }

    private  List<Map.Entry<Integer,Float>> buildAscendingOrderList() {
        List<Map.Entry<Integer,Float>> sortedEntries = new ArrayList<>(relativeCostsMap.entrySet());

        sortedEntries.sort(Comparator.comparing(Map.Entry::getValue));

        return  sortedEntries;
    }
}
