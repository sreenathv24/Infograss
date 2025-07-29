import java.util.*;
import java.util.stream.*;

public class RemoveDuplicates {
    public static void main(String[] args) {
        String[] f1 = {"aadhar"};
        String[] f2 = {"PAN"};
        String[] f3 = {"Voter ID"};
        String[] f4 = {"Passport"};
        String[] f5 = {"visa", "aadhar", "PAN"}; 

        // Combine all arrays into one list using streams (with duplicates)
        List<String> allElements = Stream.of(f1, f2, f3, f4, f5)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        
        Set<String> seen = new HashSet<>();
        List<String> duplicates = allElements.stream()
                .filter(item -> {
                    if (seen.contains(item)) {
                        return true; 
                    } else {
                        seen.add(item);
                        return false;
                    }
                })
                .collect(Collectors.toList());

        System.out.println("All Elements (with duplicates): " + allElements);
        System.out.println("Duplicate Elements (with full stream logic): " + duplicates);
    }
}
