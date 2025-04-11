package com.example.ai_breath_mobile_app.beans;

public class User {
    private static int nextId = 1; // Static variable to generate unique IDs
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int age;
    private double height; // in meters
    private double weight; // in kilograms
    private double imc;    // Body Mass Index (BMI)
    private String gender; // Added gender

    // Constructor
    public User(String username, String password, String firstName, String lastName, int age, double height, double weight, String gender) {
        this.id = nextId++; // Assign the current ID and increment for the next user
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.imc = calculateIMC(height, weight); // Calculate IMC during initialization
        this.gender = gender;
    }

    // Default constructor
    public User() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getImc() { return imc; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    // Method to calculate IMC (BMI)
    private double calculateIMC(double height, double weight) {
        if (height <= 0) return 0; // Avoid division by zero
        return weight / (height * height);
    }
}