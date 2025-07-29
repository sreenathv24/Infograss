import java.util.*;

public class RemoveDuplicate {
    public static void main(String[] args) {
        String[] f1 = {"aadhar"};
        String[] f2 = {"PAN"};
        String[] f3 = {"Voter ID"};
        String[] f4 = {"Passport"};
        String[] f5 = {"visa", "aadhar", "PAN"}; // duplicates included

        List<String> allElements = new ArrayList<>();
        for (String s : f1) allElements.add(s);
        for (String s : f2) allElements.add(s);
        for (String s : f3) allElements.add(s);
        for (String s : f4) allElements.add(s);
        for (String s : f5) allElements.add(s);

        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();

        for (String item : allElements) {
            if (!seen.add(item)) {
                duplicates.add(item);
            }
        }

        System.out.println("All Elements (with duplicates): " + allElements);
        System.out.println("Duplicate Elements (without using streams): " + duplicates);
    }
}
