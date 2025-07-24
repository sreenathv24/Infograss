package sdde;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    String college;
    String name;
    int mark1, mark2, mark3;

    public Student(String name, String college, int mark1, int mark2, int mark3) {
        this.college = college;
        this.name = name;
        this.mark1 = mark1;
        this.mark2 = mark2;
        this.mark3 = mark3;
    }

    double getAverage() {
        return (mark1 + mark2 + mark3) / 3.0;
    }

    int getTotal() {
        return mark1 + mark2 + mark3;
    }

    public String toString() {
        return college + " - " + name + " - Average: " + String.format("%.2f", getAverage()) +
               " - Total: " + getTotal();
    }

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>(List.of(
            new Student("Vikram", "REC", 33, 46, 89),
            new Student("Raj", "VIT", 83, 82, 87),
            new Student("Prashu", "ViT", 52, 63, 96),
            new Student("Vimal", "REC", 64, 86, 92),
            new Student("Ram", "VIT", 76, 86, 91),
            new Student("John", "SRM", 92, 91, 86)));

        // Task 1: Students with Average > 60
        System.out.println("Task 1: Students with Average > 60");
        List<Student> avgAbove60 = students.stream()
            .filter(s -> s.getAverage() > 60)
            .collect(Collectors.toList());
        avgAbove60.forEach(System.out::println);

        // Task 2: VIT Students with Total > 160
        System.out.println("\nTask 2: VIT Students with Total > 160");
        List<Student> vitAbove160 = students.stream()
            .filter(s -> s.college.equalsIgnoreCase("VIT") && s.getTotal() > 160)
            .collect(Collectors.toList());
        vitAbove160.forEach(System.out::println);
    }
}
