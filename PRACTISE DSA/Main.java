package javacode;

public class Main {
    public static void main(String[] args) {

        Dept d1 = new Dept();

        // SET student from main
        Student st = new Student(101, "Sreenath");
        d1.setStudent(st);

        // GET student from dept
        Student result = d1.getStudent();
        System.out.println("Student Roll: " + result.getRoll());
        System.out.println("Student Name: " + result.getName());

        // Make objects eligible for GC
        d1 = null;
        st = null;
        result = null;

        System.gc();  // request garbage collection

        System.out.println("End of main");
    }
}

class Dept {

    private Student s1;   // student inside dept

    // setter
    public void setStudent(Student s) {
        this.s1 = s;
    }

    // getter
    public Student getStudent() {
        return this.s1;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Dept finalize() called");
    }
}

class Student {

    private int roll;
    private String name;

    public Student(int roll, String name) {
        this.roll = roll;
        this.name = name;
        System.out.println("Student object created: " + name);
    }

    public int getRoll() {
        return roll;
    }

    public String getName() {
        return name;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Student finalize() called for: " + name);
    }
}
