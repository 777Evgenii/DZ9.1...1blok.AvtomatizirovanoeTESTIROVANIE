package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    public static Faker faker = new Faker(new Locale("ru"));

    public static UserInfo generateValidUser() {
        return new UserInfo(generateCity(), generateName(), generatePhone());
    }

    public static String generateDate(int plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private static String generateCity() {
        String[] cities = new String[]{"Москва", "Пенза", "Волгоград", "Саратов", "Казань"};
        return cities[(new Random()).nextInt(cities.length)];
    }

    private static String generateName() {
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    private static String generatePhone() {
        return faker.phoneNumber().phoneNumber();
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}